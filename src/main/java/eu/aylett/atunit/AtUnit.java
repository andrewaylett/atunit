/**
 * Copyright (C) 2007 Logan Johnson
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.aylett.atunit;

import com.google.common.collect.Sets;
import eu.aylett.atunit.core.Container;
import eu.aylett.atunit.core.*;
import eu.aylett.atunit.core.MockFramework;
import eu.aylett.atunit.easymock.EasyMockFramework;
import eu.aylett.atunit.guice.GuiceContainer;
import eu.aylett.atunit.jmock.JMockFramework;
import eu.aylett.atunit.mockito.MockitoFramework;
import eu.aylett.atunit.spring.SpringContainer;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/**
 * This is the JUnit test runner used for AtUnit tests. It delegates to the
 * configured {@link MockFramework} for mock and stub object
 * creation and to the configured {@link Container} for dependency
 * injection.
 *
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 *
 * @see <a href="example/ExampleAtUnitTest.java.xhtml"/>ExampleAtUnitTest.java</a>
 */
public class AtUnit extends BlockJUnit4ClassRunner {

    public AtUnit(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object createTest() throws Exception {
        Class<?> c = getTestClass().getJavaClass();
        Set<Field> testFields = getFields(c);

        Container container = getContainerFor(c);
        MockFramework mockFramework = getMockFrameworkFor(c);

        // make sure we have one (and only one) @Unit field
        Field unitField = getUnitField(testFields);
        if (unitField.getAnnotation(Mock.class) != null) {
            throw new IncompatibleAnnotationException(Unit.class, Mock.class);
        }

        final Map<Field, Object> fieldValues = mockFramework.getValues(testFields.toArray(new Field[testFields.size()]));
        if (fieldValues.containsKey(unitField)) {
            throw new IncompatibleAnnotationException(Unit.class, unitField.getType());
        }

        Object test = container.createTest(c, fieldValues);

        // any field values created by AtUnit but not injected by the container are injected here.
        for (Field field : fieldValues.keySet()) {
            field.setAccessible(true);
            if (field.get(test) == null) {
                field.set(test, fieldValues.get(field));
            }
        }

        return test;
    }


    /**
     * Gets all declared fields and all inherited fields.
     */
    protected Set<Field> getFields(Class<?> c) {
        Set<Field> fields = Sets.newHashSet(c.getDeclaredFields());
        while ((c = c.getSuperclass()) != null) {
            for (Field f : c.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())
                        && !Modifier.isPrivate(f.getModifiers())
                        ) {
                    fields.add(f);
                }
            }
        }
        return fields;
    }

    protected Field getUnitField(Set<Field> fields) throws NoUnitException, TooManyUnitsException {
        Field unitField = null;
        for (Field field : fields) {
            for (Annotation anno : field.getAnnotations()) {
                if (Unit.class.isAssignableFrom(anno.annotationType())) {
                    if (unitField != null)
                        throw new TooManyUnitsException("Already had field " + unitField + " when I found field " + field);
                    unitField = field;
                }
            }
        }
        if (unitField == null) throw new NoUnitException();
        return unitField;
    }


    protected Container getContainerFor(Class<?> testClass) throws Exception {
        Class<? extends Container> containerClass = NoContainer.class;

        eu.aylett.atunit.Container containerAnno = testClass.getAnnotation(eu.aylett.atunit.Container.class);
        ContainerClass containerClassAnno = testClass.getAnnotation(ContainerClass.class);

        if (containerAnno != null && containerClassAnno != null)
            throw new IncompatibleAnnotationException(eu.aylett.atunit.Container.class, ContainerClass.class);

        if (containerAnno != null) {
            switch (containerAnno.value()) {
                case GUICE:
                    containerClass = GuiceContainer.class;
                    break;
                case SPRING:
                    containerClass = SpringContainer.class;
                    break;
            }
        }

        if (containerClassAnno != null) {
            containerClass = containerClassAnno.value();
        }

        return containerClass.newInstance();
    }

    protected MockFramework getMockFrameworkFor(Class<?> testClass) throws Exception {
        Class<? extends MockFramework> mockFrameworkClass = NoMockFramework.class;

        eu.aylett.atunit.MockFramework mockFrameworkAnno = testClass.getAnnotation(eu.aylett.atunit.MockFramework.class);
        MockFrameworkClass mockFrameworkClassAnno = testClass.getAnnotation(MockFrameworkClass.class);

        if (mockFrameworkAnno != null && mockFrameworkClassAnno != null)
            throw new IncompatibleAnnotationException(eu.aylett.atunit.MockFramework.class, MockFrameworkClass.class);

        if (mockFrameworkAnno != null) {
            switch (mockFrameworkAnno.value()) {
                case EASYMOCK:
                    mockFrameworkClass = EasyMockFramework.class;
                    break;
                case JMOCK:
                    mockFrameworkClass = JMockFramework.class;
                    break;
                case MOCKITO:
                    mockFrameworkClass = MockitoFramework.class;
                    break;
                default:
                    throw new IllegalStateException("Expected switch block to be exhaustive: " + mockFrameworkAnno.value());
            }
        }

        if (mockFrameworkClassAnno != null) {
            mockFrameworkClass = mockFrameworkClassAnno.value();
        }

        return mockFrameworkClass.newInstance();
    }

}

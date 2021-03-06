package eu.aylett.atunit.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import eu.aylett.atunit.*;
import eu.aylett.atunit.easymock.EasyMockFramework;
import eu.aylett.atunit.jmock.JMockFramework;
import eu.aylett.atunit.mockito.MockitoFramework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

final public class TestClassUtils {

    /**
     * Gets the old-style MockFramework class for the given test class by checking for a MockFramework or MockFrameworkClass annotation.
     *
     * @throws IncompatibleAnnotationException if both MockFramework and MockFrameworkClass are present.
     */
    public static Class<? extends MockFramework> getMockFrameworkClass(Class<?> testClass) throws IncompatibleAnnotationException {
        Class<? extends MockFramework> mockFrameworkClass = NoMockFramework.class;

        eu.aylett.atunit.MockFramework mockFrameworkAnno = testClass.getAnnotation(eu.aylett.atunit.MockFramework.class);
        MockFrameworkClass mockFrameworkClassAnno = testClass.getAnnotation(MockFrameworkClass.class);

        if (mockFrameworkAnno != null && mockFrameworkClassAnno != null)
            throw new IncompatibleAnnotationException(eu.aylett.atunit.MockFramework.class, MockFrameworkClass.class);

        if (mockFrameworkAnno != null) {
            return mockFrameworkAnno.value().mockClass;
        }

        if (mockFrameworkClassAnno != null) {
            mockFrameworkClass = mockFrameworkClassAnno.value();
        }

        return mockFrameworkClass;
    }

    /**
     * Gets all fields (declared and inherited) on the given class.
     */
    public static Set<Field> getFields(Class<?> testClass) {
        Set<Field> fields = Sets.newHashSet(testClass.getDeclaredFields());
        Class<?> c = testClass;
        while ((c = c.getSuperclass()) != null) {
            for (Field f : c.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())
                        && !Modifier.isPrivate(f.getModifiers())
                        ) {
                    fields.add(f);
                }
            }
        }
        return ImmutableSet.copyOf(fields);
    }

    /**
     * Gets the one and only field in the set annotated with Unit.
     *
     * @throws NoUnitException       if no field is annotated with Unit.
     * @throws TooManyUnitsException if more than one field is annotated with Unit.
     */
    public static Field getUnitField(Set<Field> fields) throws NoUnitException, TooManyUnitsException {
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

    /**
     * Gets the old-style Container class for the given test class by looking for a Container or ContainerClass annotation.
     *
     * @throws IncompatibleAnnotationException if both Container and ContainerClass annotations are present.
     */
    public static Class<? extends Container> getContainerClass(Class<?> testClass) throws IncompatibleAnnotationException {

        eu.aylett.atunit.Container containerAnno = testClass.getAnnotation(eu.aylett.atunit.Container.class);
        ContainerClass containerClassAnno = testClass.getAnnotation(ContainerClass.class);

        if (containerAnno != null && containerClassAnno != null)
            throw new IncompatibleAnnotationException(eu.aylett.atunit.Container.class, ContainerClass.class);

        if (containerAnno != null) {
            return containerAnno.value().containerClass;
        }

        if (containerClassAnno != null) {
            return containerClassAnno.value();
        }

        return NoContainer.class;
    }


    /**
     * Gets all fields annotated with Mock.
     */
    public static Set<Field> getMockFields(Set<Field> fields) {
        return fields.stream()
                .filter(field -> field.getAnnotation(Mock.class) != null)
                .collect(Collectors.toSet());
    }

    /**
     * Gets all fields annotated with Stub
     */
    public static Set<Field> getStubFields(Set<Field> fields) {
        return fields.stream()
                .filter(field -> field.getAnnotation(Stub.class) != null)
                .collect(Collectors.toSet());
    }

}

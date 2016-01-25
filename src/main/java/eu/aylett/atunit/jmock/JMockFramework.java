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

package eu.aylett.atunit.jmock;

import eu.aylett.atunit.Mock;
import eu.aylett.atunit.Stub;
import eu.aylett.atunit.core.IncompatibleAnnotationException;
import eu.aylett.atunit.core.MockFramework;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JMockFramework implements MockFramework {

    public Map<Field, Object> getValues(Field[] fields) throws Exception {
        final Map<Field, Object> jmockFields = new HashMap<>();

        Mockery mockery = null;
        final Set<Object> ignored = new HashSet<>();

        for (Field field : fields) {
            if (Mockery.class.isAssignableFrom(field.getType())) {
                if (field.getAnnotation(Mock.class) != null)
                    throw new IncompatibleAnnotationException(Mock.class, field.getType());

                if (Mockery.class.equals(field.getType())) {
                    mockery = new JUnit4Mockery();
                } else {
                    mockery = (Mockery) field.getType().newInstance();
                }
                jmockFields.put(field, mockery);
                break;
            }
        }

        for (Field field : fields) {
            boolean isMock = (field.getAnnotation(Mock.class) != null);
            boolean isStub = (field.getAnnotation(Stub.class) != null);
            if (!isMock && !isStub) continue;
            if (isMock && (mockery == null)) throw new NoMockeryException();
            if (isStub && (mockery == null)) mockery = new JUnit4Mockery();

            Class<?> fieldType = field.getType();
            if (fieldType.isArray()) {
                Object[] array = (Object[]) Array.newInstance(fieldType.getComponentType(), 3);
                for (int i = 0; i < array.length; i++) {
                    array[i] = mockery.mock(fieldType.getComponentType());
                    if (isStub) {
                        ignored.add(array[i]);
                    }
                }
                jmockFields.put(field, array);

            } else {
                Object mock = mockery.mock(field.getType());
                if (isStub) {
                    ignored.add(mock);
                }

                jmockFields.put(field, mock);
            }

        }

        if (!ignored.isEmpty()) {
            Expectations expectations = new Expectations() {{
                ignored.forEach(this::ignoring);
            }};
            mockery.checking(expectations);
        }

        return jmockFields;
    }

}

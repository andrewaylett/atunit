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

package eu.aylett.atunit.easymock;

import eu.aylett.atunit.Mock;
import eu.aylett.atunit.Stub;
import eu.aylett.atunit.core.MockFramework;
import org.easymock.EasyMock;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EasyMockFramework implements MockFramework {

    public Map<Field, Object> getValues(Field[] fields) throws Exception {
        Map<Field, Object> mocksAndStubs = new HashMap<>();

        for (Field field : fields) {
            if (field.getAnnotation(Mock.class) != null) {
                mocksAndStubs.put(field, EasyMock.createStrictMock(field.getType()));
            } else if (field.getAnnotation(Stub.class) != null) {
                mocksAndStubs.put(field, EasyMock.createNiceMock(field.getType()));
            }
        }

        return mocksAndStubs;
    }

}

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

import eu.aylett.atunit.easymock.EasyMockFramework;
import eu.aylett.atunit.jmock.JMockFramework;
import eu.aylett.atunit.mockito.MockitoFramework;

import java.lang.annotation.*;


/**
 * Tells AtUnit to use a supported mock objects framework. The specified
 * framework will be used to populate fields in the test annotated with
 * {@link Mock} or {@link Stub}.
 *
 * A parameter of type {@link Option} is required.
 *
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 *
 * @see Mock
 * @see Stub
 * @see <a href="example/ExampleJMockTest.java.xhtml">ExampleJMockTest.java</a>
 * @see <a href="example/ExampleJMockTest.java.xhtml">ExampleEasyMockTest.java</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface MockFramework {

    enum Option {
        JMOCK(JMockFramework.class),
        EASYMOCK(EasyMockFramework.class),
        MOCKITO(MockitoFramework.class),
        ;

        public final Class<? extends eu.aylett.atunit.core.MockFramework> mockClass;

        Option(Class<? extends eu.aylett.atunit.core.MockFramework> mockClass) {
            this.mockClass = mockClass;
        }
    }

    Option value();

}

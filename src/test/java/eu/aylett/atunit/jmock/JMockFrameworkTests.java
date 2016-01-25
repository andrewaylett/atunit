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

import eu.aylett.atunit.AtUnit;
import eu.aylett.atunit.Mock;
import eu.aylett.atunit.MockFramework;
import eu.aylett.atunit.Unit;
import eu.aylett.atunit.core.IncompatibleAnnotationException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class JMockFrameworkTests {

    JUnitCore junit;

    @Before
    public void setUp() {
        junit = new JUnitCore();
    }

    @Test
    public void tWithMockery() {
        Result result = junit.run(TestClasses.WithMockery.class);
        assertTrue(result.wasSuccessful());
        assertEquals(2, result.getRunCount());
    }

    @Test
    public void tInheritance() {
        Result result = junit.run(TestClasses.Inheritance.class);
        assertTrue(result.wasSuccessful());
        assertEquals(2, result.getRunCount());
    }

    @Test
    public void tMockWithoutMockery() {
        Result result = junit.run(TestClasses.MockWithoutMockery.class);
        assertFalse(result.wasSuccessful());
        //noinspection ThrowableResultOfMethodCallIgnored
        assertTrue(result.getFailures().get(0).getException() instanceof NoMockeryException);
    }

    @Test
    public void tIncompatibleAnnotations() {
        for (Class<?> testClass : new Class[]{TestClasses.MockUnit.class,
                TestClasses.MockeryUnit.class,
                TestClasses.MockeryMock.class}) {

            Result result = junit.run(testClass);
            assertFalse(result.wasSuccessful());
            //noinspection ThrowableResultOfMethodCallIgnored
            assertTrue(result.getFailures().get(0).getException() instanceof IncompatibleAnnotationException);
        }
    }


    protected static class TestClasses {

        @RunWith(AtUnit.class)
        public static abstract class AbstractAtUnitTest {
            @Test
            public void tPass() {
                assertTrue(true);
            }
        }

        @MockFramework(MockFramework.Option.JMOCK)
        public static abstract class AbstractJMockTest extends AbstractAtUnitTest {

        }

        public static class HappyTest extends AbstractJMockTest {
            @SuppressWarnings("unused")
            @Unit
            String unit;
        }

        public static class WithMockery extends AbstractJMockTest {
            Mockery mockery;
            @SuppressWarnings("unused")
            @Unit
            String unit;
            @Mock
            ExampleInterface myMock;

            @Test
            public void tMockery() {
                mockery.checking(new Expectations() {{
                    oneOf(myMock).isAwesome();
                    will(returnValue(true));
                }});

                assertTrue(myMock.isAwesome());
            }
        }

        public static class Inheritance extends WithMockery {

        }

        @SuppressWarnings("unused")
        public static class MockWithoutMockery extends AbstractJMockTest {
            @Unit
            String unit;
            @Mock
            ExampleInterface mock;
        }

        public static class MockUnit extends AbstractJMockTest {
            @SuppressWarnings("unused")
            @Mock
            @Unit
            ExampleInterface mock;

            public MockUnit() {
            }
        }

        public static class MockeryUnit extends AbstractJMockTest {
            @SuppressWarnings("unused")
            @Unit
            Mockery mockery;

            public MockeryUnit() {
            }
        }

        @SuppressWarnings("unused")
        public static class MockeryMock extends AbstractJMockTest {
            @Mock
            Mockery mockery;
            @Unit
            String unit;

            public MockeryMock() {
            }
        }

    }

    public interface ExampleInterface {
        boolean isAwesome();
    }


}

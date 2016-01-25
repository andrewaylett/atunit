/**
 * Copyright (C) 2007 Logan Johnson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.aylett.atunit.mockito;

import eu.aylett.atunit.AtUnit;
import eu.aylett.atunit.Mock;
import eu.aylett.atunit.MockFramework;
import eu.aylett.atunit.Unit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockitoFrameworkTests {
	
	JUnitCore junit;
	
	@Before
	public void setUp() {
		junit = new JUnitCore();
	}
	
	@Test
	public void tOptionMocks() {
		Result result = junit.run(TestClasses.OptionMocks.class);
		assertTrue(result.wasSuccessful());
		assertEquals(1, result.getRunCount());
	}
	
	@Test
	public void tOptionMockFrameworkClass() {
		Result result = junit.run(TestClasses.OptionMockFrameworkClass.class);
		assertTrue(result.wasSuccessful());
		assertEquals(1, result.getRunCount());
	}
	
	
	@Test
	public void tInheritance() {
		Result result = junit.run(TestClasses.Inheritance.class);
		assertTrue(result.wasSuccessful());
		assertEquals(1, result.getRunCount());
	}
	
	protected static class TestClasses {
		
		@RunWith(AtUnit.class)
		@MockFramework(MockFramework.Option.MOCKITO)
		public static class OptionMocks {
			@Unit protected String unit;
			@Mock protected StringFactory stringFactory;
			
			@Test
			public void tGetString() {
				when(stringFactory.getString()).thenReturn("my string");
				
				assertEquals("my string", stringFactory.getString());
				
				verify(stringFactory).getString();
			}
			
			public interface StringFactory {
				String getString();
			}
		}
		
		public static class Inheritance extends OptionMocks {
			
		}

		@RunWith(AtUnit.class)
		@MockFramework(MockFramework.Option.MOCKITO)
		public static class OptionMockFrameworkClass {
			@Unit protected String unit;
			@Mock protected StringFactory stringFactory;
			
			@Test
			public void tGetString() {
				when(stringFactory.getString()).thenReturn("my string");

				assertEquals("my string", stringFactory.getString());
				
				verify(stringFactory).getString();
			}
			
			public interface StringFactory {
				String getString();
			}
		}
		
	}

}

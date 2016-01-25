package eu.aylett.atunit.core;

import com.google.common.collect.Sets;
import eu.aylett.atunit.Mock;
import eu.aylett.atunit.Plugins;
import eu.aylett.atunit.Stub;
import eu.aylett.atunit.Unit;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CoreTestFixtureTests {
	
	@Test
	public void tConstructor() throws Exception {
		Class<?> testClass = TestClasses.Good.class;
		CoreTestFixture fixture = new CoreTestFixture(testClass);

		assertEquals(testClass, fixture.getTestClass());
		assertEquals(PluginUtilsTests.DummyContainerPlugin.class, fixture.getContainerPluginClass());
		assertEquals(testClass.getDeclaredField("unit"), fixture.getUnitField());
		
		Set<Field> expectedMocks = Sets.newHashSet(testClass.getDeclaredField("mock1"), 
				                                   testClass.getDeclaredField("mock2"));
		assertEquals(expectedMocks, fixture.getMockFields());
		
		Set<Field> expectedStubs = Sets.newHashSet(testClass.getDeclaredField("stub1"), 
                                                   testClass.getDeclaredField("stub2"));
		assertEquals(expectedStubs, fixture.getStubFields());

		Set<Field> expectedFields = Sets.newHashSet(testClass.getDeclaredFields());
		assertEquals(expectedFields, fixture.getFields());
	}
	
	protected static class TestClasses {
		
		@Plugins({PluginUtilsTests.DummyPlugin1.class, PluginUtilsTests.DummyPlugin2.class, PluginUtilsTests.DummyContainerPlugin.class})
		public static class Good {
			@Unit String unit;
			@Mock String mock1;
			@Mock Integer mock2;
			@Stub String stub1;
			@Stub Integer stub2;
		}
	}
	
}

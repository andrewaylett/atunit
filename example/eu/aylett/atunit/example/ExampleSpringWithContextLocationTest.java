
package eu.aylett.atunit.example;

import static org.junit.Assert.*;

import eu.aylett.atunit.AtUnit;
import eu.aylett.atunit.Container;
import eu.aylett.atunit.Unit;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.*;
import eu.aylett.atunit.example.subjects.User;
import eu.aylett.atunit.spring.Bean;
import eu.aylett.atunit.spring.Context;

/**
 * This example is exactly the same as ExampleSpringTest, except that it
 * specifies the location of a context XML file to use.
 * 
 */
@RunWith(AtUnit.class)
@Container(Container.Option.SPRING)
/*
 * The @Context annotation tells AtUnit to use the named context XML file,
 * instead of looking for a file named after the test class.
 */
@Context("ExampleSpringTest.xml")
public class ExampleSpringWithContextLocationTest {
	
	@Bean @Unit
	User user;
	@Bean("username") String username;
	
	@Test
	public void testGetId() {
		assertEquals(500, user.getId().intValue());
	}
	
	@Test
	public void testGetUsername() {
		assertEquals("fred", user.getUsername());
	}
}

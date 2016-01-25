package eu.aylett.atunit.example;

import eu.aylett.atunit.AtUnit;
import eu.aylett.atunit.Container;
import eu.aylett.atunit.Unit;
import eu.aylett.atunit.example.subjects.User;
import eu.aylett.atunit.spring.Bean;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * This example demonstrates AtUnit's Spring integration.
 * <p/>
 * Fields can be populated from the Spring context. By default, AtUnit will look for
 * a configuration file named after the test class plus a ".xml" suffix,
 * parallel to the test class in the classpath. In this example, AtUnit looks in
 * the classpath for atunit/spring/ExampleSpringTest.xml.
 */
@RunWith(AtUnit.class)
@Container(Container.Option.SPRING)
public class ExampleSpringTest {

    /*
     * Any fields annotated with Bean will be injected from the context.  By default, annotated fields are
     * autowired by type.
     */
    @Bean
    @Unit
    User user;

    /*
     * If autowiring by type is not sufficient, you can request a bean by name.
     */
    @Bean("username")
    String username;

    @Test
    public void testGetId() {
        assertEquals(500, user.getId().intValue());
    }

    @Test
    public void testGetUsername() {
        assertEquals("fred", user.getUsername());
    }
}

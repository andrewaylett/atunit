package eu.aylett.atunit.example;

import com.google.inject.Inject;
import eu.aylett.atunit.*;
import eu.aylett.atunit.example.subjects.GuiceUserManager;
import eu.aylett.atunit.example.subjects.Logger;
import eu.aylett.atunit.example.subjects.User;
import eu.aylett.atunit.example.subjects.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;


/**
 * This example demonstrates the combined integration of Guice and JMock. The
 * combined integration of a mock framework and a dependency injection container
 * is where AtUnit really shines, because it allows you to simply declare some
 * fields and start testing.
 * <p/>
 * See ExampleGuiceTest and ExampleJMockTest for introductions to AtUnit's Guice
 * and JMock support.
 * <p/>
 * Please note also that any Container and MockFramework can be used together.
 */
@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
@MockFramework(MockFramework.Option.MOCKITO)
public class ExampleGuiceAndMockitoTest {

    @Inject
    @Unit
    GuiceUserManager manager;
    @Inject
    User emptyUser;

    /*
     * Just as it does when no Container is used, AtUnit creates any @Mock or
     * @Stub fields. However, once they're created, AtUnit binds them into the
     * Guice injector so that Guice can inject them anywhere they're needed.
     *
     * Since AtUnit creates these fields, you have two choices: You can mark
     * them with @Inject, in which case they will be directly injected by Guice
     * like any other field; or you can forego @Inject and AtUnit will set them
     * after Guice is finished. Since you very rarely need the full power of
     * Guice when actually setting these kinds of fields, you can usually do
     * without @Inject on them.
     *
     */
    @Mock
    UserDao dao;
    @Stub
    Logger ignored;


    @Test
    public void testGetUser() {
        when(dao.load(500)).thenReturn(emptyUser);

        assertSame(emptyUser, manager.getUser(500));
    }

}

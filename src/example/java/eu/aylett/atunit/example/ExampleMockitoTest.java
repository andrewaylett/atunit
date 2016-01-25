package eu.aylett.atunit.example;

import eu.aylett.atunit.*;
import eu.aylett.atunit.example.subjects.Logger;
import eu.aylett.atunit.example.subjects.User;
import eu.aylett.atunit.example.subjects.UserDao;
import eu.aylett.atunit.example.subjects.UserManagerImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This example demonstrates AtUnit's EasyMock integration.
 * 
 */
@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.MOCKITO) // tells AtUnit to use EasyMock
public class ExampleMockitoTest {

	@Unit
	UserManagerImpl manager;
	User fred;
	
	/*
	 * Any field annotated with @Mock or @Stub will be populated automatically
	 * by AtUnit with a mock object provided by Mockito.
	 */
	@Mock
	UserDao dao;
	@Stub
	Logger log;
	
	@Before
	public void setUp() {
		manager = new UserManagerImpl(dao, log);
		fred = new User(1, "Fred");
	}
	
	@Test
	public void testGetStringForSomeReason() {
		when(dao.load(1)).thenReturn(fred);
		assertSame(fred, manager.getUser(1));
		verify(dao).load(1);
	}
	
}

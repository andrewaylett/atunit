package eu.aylett.atunit.example;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import eu.aylett.atunit.*;
import eu.aylett.atunit.example.subjects.Logger;
import eu.aylett.atunit.example.subjects.User;
import eu.aylett.atunit.example.subjects.UserDao;
import eu.aylett.atunit.example.subjects.UserManagerImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.*;

/**
 * This example demonstrates AtUnit's EasyMock integration.
 * 
 */
@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.EASYMOCK) // tells AtUnit to use EasyMock
public class ExampleEasyMockTest {

	@Unit
	UserManagerImpl manager;
	User fred;
	
	/*
	 * Any field annotated with @Mock or @Stub will be populated automatically
	 * by AtUnit with a mock object provided by EasyMock.
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
		expect(dao.load(1)).andReturn(fred);
		replay(dao);
		assertSame(fred, manager.getUser(1));
		verify(dao);
	}
	
}

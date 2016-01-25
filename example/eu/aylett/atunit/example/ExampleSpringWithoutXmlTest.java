package eu.aylett.atunit.example;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import eu.aylett.atunit.*;
import eu.aylett.atunit.example.subjects.Logger;
import eu.aylett.atunit.example.subjects.User;
import eu.aylett.atunit.example.subjects.UserDao;
import eu.aylett.atunit.example.subjects.UserManagerImpl;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.*;
import eu.aylett.atunit.spring.Bean;

/**
 * This example shows Spring integration without an XML file.
 * 
 * This test is the same as ExampleSpringEasyMockTest, except that there is no XML file.
 * 
 */
@RunWith(AtUnit.class)
@Container(Container.Option.SPRING)
@MockFramework(MockFramework.Option.EASYMOCK)
public class ExampleSpringWithoutXmlTest {

	@Bean @Unit
	UserManagerImpl manager;
	@Bean("userDao") @Mock
	UserDao dao;
	@Bean("log") @Stub
	Logger log;
	@Bean("fred")
	User fred;
	
	@Test
	public void testGetUser() {
		expect(dao.load(1)).andReturn(fred);
		replay(dao);
		assertSame(fred, manager.getUser(1));
		verify(dao);
	}
	
}

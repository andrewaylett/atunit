# Anatomy of an AtUnit test #

AtUnit is driven by annotations (hence the name).  The example below shows the AtUnit annotations in context.

The specifics of Guice and JMock integration will be covered elsewhere.  The general semantics of the AtUnit annotations are the same for all containers and mock object frameworks.

```

import atunit.*;
import atunit.example.subjects.*;
// ...
import com.google.inject.Inject;
import org.junit.runner.RunWith;

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
@MockFramework(MockFramework.Option.JMOCK)
public class ExampleGuiceAndJMockTest {

	@Inject @Unit GuiceUserManager manager;
	@Inject User emptyUser;
	Mockery mockery;
	@Mock UserDao dao;
	@Stub Logger ignoredLogger;

	@Test
	public void testGetUser() {
		mockery.checking(new Expectations() {{ 
			one (dao).load(with(equal(500)));
				will(returnValue(emptyUser));
		}});
		assertSame(emptyUser, manager.getUser(500));
	}
}

```

## `@RunWith(AtUnit.class)` ##
This is a JUnit annotation-- it tells JUnit to use AtUnit to run the test.  Most of AtUnit's smarts live in its test runner, so this is required for the other annotations to work.

## `@Container` and `@ContainerClass` ##
These annotations tell AtUnit which Inversion of Control container to use.  `@Container` is for specifying a supported container; `@ContainerClass` takes a class name as a parameter so that you can plug in your own container.

The container is used to inject dependencies into your test's fields, and into each other.  Container-specific annotations are needed in order to request injection of a field, except where noted in this document.

See the special notes on [supported containers](SupportedContainers.md) for more details on using your container of choice.

## `@MockFramework` and `@MockFrameworkClass` ##
These annotations are analagous to the container annotations above, but for mock frameworks.  Again, `@MockFramework` specifies supported framework; `@MockFrameworkClass` allows you to plug in your own.

For details on using a specific mock objects framework, see the page on [supported frameworks](SupportedMockFrameworks.md).

## `@Unit` ##
A unit test should have one and only one subject, to keep your tests clear and focused.  An AtUnit test _must_ have exactly one subject, and it must be annotated with `@Unit`.

## `@Mock` and `@Stub` ##
An AtUnit test can obtain two kinds of mock object from the framework of your choice:
  * _Mocks_ have strict expectations and can be used to validate interactions.
  * _Stubs_ are ignored by the framework and are used to supply dependencies that are irrelevant to the test.

AtUnit examines fields that carry the `@Mock` and `@Stub` annotations and uses the configured framework to create mock objects to fill those fields.  If you're using a container, these field values will be bound appropriately within the container so that they can be injected by the container into other objects.  If you are not using a container, or if these fields are not injected by the container, AtUnit will inject the fields itself.  This means that in most cases, you need not bother with container-specific annotations on `@Mock` or `@Stub` fields.
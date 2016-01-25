# Supported Mock Object Frameworks #

This document contains framework-specific notes.  For general information about using mock objects with AtUnit, see the [test anatomy page](AtUnitTest.md).

## EasyMock ##

To use EasyMock in your test, annotate your test class with **`@MockFramework(MockFramework.Option.EASYMOCK)`**:

```
// ...

@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.EASYMOCK) // tells AtUnit to use EasyMock
public class ExampleEasyMockTest {

	@Mock UserDao dao;
	@Stub Logger log;
	
	// ...
}

```

When using EasyMock, fields annotated with **`@Mock`** will be populated with strict mocks, and fields annotated with **`@Stub`** will be populated with nice mocks.  See [the EasyMock documentation](http://easymock.org/EasyMock2_3_Documentation.html) for an explanation of the difference.

## JMock ##

To use JMock, annotate your test class with **`@MockFramework(MockFramework.Option.JMOCK)`**:

```
// ...

@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.JMOCK) // use JMock for mock objects
public class ExampleJMockTest {

	Mockery mockery;
	@Mock UserDao dao;
	@Stub Logger log;
	
	// ...
}
```

If you have a field of type **`Mockery`**, AtUnit will populate it with the same `Mockery` it uses to create your mocks and stubs.

Fields annotated with **`@Mock`** are populated with normal JMock mock objects.  Since mocks are useless without the `Mockery`, if you have `@Mock` fields you must also have a `Mockery` field or AtUnit will throw an exception.

Fields annotated with **`@Stub`** are also populated with mock objects, but the `Mockery` is instructed to ignore them.  The presence of `@Stub` fields does not require the presence of a `Mockery` field.

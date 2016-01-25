# Supported Containers #

This document consists of container-specific notes.  For general information about using dependency injection containers with AtUnit, see the [test anatomy page](AtUnitTest.md).

## Guice ##

To use Guice for dependency injection, annotate your test class with **`@Container(Container.Option.GUICE)`**:

```
// ...

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class ExampleGuiceTest {

	@Inject @Unit GuiceUser user;
	
	// ...
}
```

Any test that uses Guice will be instantiated using the Guice `Injector`.  If you are using a [mock framework](SupportedMockFrameworks.md), any mocks you declare will be bound to their interfaces as singletons and injected into objects that request those interfaces.

If a test requires more flexibility in your bindings than is provided via field annotation-- for instance, if it requires the injection of some configuration parameters into the test subject-- the test class may optionally implement `Module`.  Any bindings defined in the `configure` method will be merged with the field bindings and used when instantiating the test:

```
// ...

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class ExampleGuiceTest implements Module {

	@Inject @Unit GuiceUser user;
	
	public void configure(Binder b) {
		b.bind(String.class).annotatedWith(Names.named("user.name")).toInstance("fred");
		b.bind(Integer.class).annotatedWith(Names.named("user.id")).toInstance(500);
	}
	
	// ...
}
```

## Spring ##

To use Spring for dependency injection, annotate your test with **`@Container(Container.Option.SPRING)`**:

```
package atunit.example;
// ...

@RunWith(AtUnit.class)
@Container(Container.Option.SPRING)
public class ExampleSpringTest {

	@Bean @Unit User user;
	@Bean("username") String username;
	
	// ...
}	
```

By default, AtUnit will look in the classpath for a Spring XML context configuration file named after the test class and in the same package.  In the example above, AtUnit will look in the classpath `/atunit/example/ExampleSpringTest.xml`.  No exception will be thrown if the XML file is not found, because an XML file is often not needed.

An alternate location for the XML file may be specified by annotating the test class with **`@Context("/path/to/filename.xml")`**.

Fields annotated with **`@Bean`** will be populated from the application context.  By default, they are autowired by type.  The `@Bean` annotation also takes a bean name parameter-- if present, the named bean will be used to populate the field.

If the test uses a [mock object framework](SupportedMockFrameworks.md), and has any mock object fields declared, those fields will be populated by AtUnit if they are not populated by Spring.  If they carry the `@Bean` annotation, they will be placed into the Spring context for injection into other objects (using autowiring by type by default).  If no bean name is specified in the annotation, one will be generated.
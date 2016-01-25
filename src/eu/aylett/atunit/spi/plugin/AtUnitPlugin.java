package eu.aylett.atunit.spi.plugin;

import eu.aylett.atunit.spi.exception.AtUnitPluginException;
import eu.aylett.atunit.spi.exception.InvalidTestException;
import eu.aylett.atunit.spi.model.TestClass;
import eu.aylett.atunit.spi.model.TestFixtureEvent;
import eu.aylett.atunit.spi.model.TestInstanceEvent;

public interface AtUnitPlugin {
	void validateTestClass(TestClass testClass) throws InvalidTestException, AtUnitPluginException;
	void handleTestFixtureEvent(TestFixtureEvent event) throws InvalidTestException, AtUnitPluginException;
	void handleTestInstanceEvent(TestInstanceEvent event) throws InvalidTestException, AtUnitPluginException;
}

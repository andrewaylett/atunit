package eu.aylett.atunit.spi.plugin;

import eu.aylett.atunit.spi.exception.AtUnitPluginException;
import eu.aylett.atunit.spi.exception.InvalidTestException;
import eu.aylett.atunit.spi.model.TestClass;
import eu.aylett.atunit.spi.model.TestFixtureEvent;
import eu.aylett.atunit.spi.model.TestInstanceEvent;

public abstract class AtUnitPluginAdapter implements AtUnitPlugin {

    public void validateTestClass(TestClass testClass) throws InvalidTestException, AtUnitPluginException {
    }

    public void handleTestFixtureEvent(TestFixtureEvent event) throws InvalidTestException, AtUnitPluginException {
    }

    public void handleTestInstanceEvent(TestInstanceEvent event) throws InvalidTestException, AtUnitPluginException {
    }

}

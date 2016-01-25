package eu.aylett.atunit.spi.plugin;

import eu.aylett.atunit.spi.model.TestFixture;

public interface ContainerPlugin extends AtUnitPlugin {
	Object instantiate(TestFixture testClass) throws Exception;
}

package eu.aylett.atunit.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import eu.aylett.atunit.Plugins;
import eu.aylett.atunit.spi.exception.IncompatiblePluginsException;
import eu.aylett.atunit.spi.plugin.AtUnitPlugin;
import eu.aylett.atunit.spi.plugin.ContainerPlugin;

import java.util.List;
import java.util.Set;

public class PluginUtils {

	
	/**
	 * Gets the single ContainerPlugin specified in the Plugins annotation's list.
	 * @return the ContainerPlugin's class, or null if none is specified.
	 * @throws IncompatiblePluginsException if multiple ContainerPlugins are specified in the annotation.
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends ContainerPlugin> getContainerPluginClass(Class<?> testClass) throws IncompatiblePluginsException {
		Set<Class<? extends ContainerPlugin>> containers = Sets.newHashSet();
		
		for ( Class<?> plugin : getPluginClasses(testClass)) {
			if ( ContainerPlugin.class.isAssignableFrom(plugin) ) {
				Class<? extends ContainerPlugin> container = (Class<? extends ContainerPlugin>)plugin;
				containers.add(container);
			}
		}
		
		Class<? extends ContainerPlugin>[] containersArray = new Class[0];
		
		switch(containers.size()) {
			case 0: return null;
			case 1: return Iterables.getOnlyElement(containers);
			default: throw new IncompatiblePluginsException(containers.toArray(containersArray));
		}
	}

	/**
	 * Gets all plugins specified by the Plugins annotation.  Any duplicates are consolidated.
	 */
	public static List<Class<? extends AtUnitPlugin>> getPluginClasses(Class<?> testClass) {
		Plugins anno = testClass.getAnnotation(Plugins.class);
		if ( anno != null ) {
			// whack any duplicates
			Set<Class<? extends AtUnitPlugin>> plugins = ImmutableSet.copyOf(anno.value());
			return ImmutableList.copyOf(plugins);
		} else {
			return ImmutableList.of();
		}
		
	}

	
}

package de.felix_klauke.fallout.spigot.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.felix_klauke.fallout.core.module.FalloutCoreModule;
import de.felix_klauke.fallout.spigot.FalloutSpigotApplication;
import de.felix_klauke.fallout.spigot.FalloutSpigotApplicationImpl;
import de.felix_klauke.fallout.spigot.FalloutSpigotPlugin;
import org.bukkit.plugin.Plugin;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class FalloutSpigotModule extends AbstractModule {

    private final FalloutSpigotPlugin falloutSpigotPlugin;

    public FalloutSpigotModule(FalloutSpigotPlugin falloutSpigotPlugin) {
        this.falloutSpigotPlugin = falloutSpigotPlugin;
    }

    @Override
    protected void configure() {
        install(new FalloutCoreModule(falloutCoreConfig));

        bind(Plugin.class).annotatedWith(Names.named("falloutPlugin")).toInstance(falloutSpigotPlugin);

        bind(FalloutSpigotApplication.class).to(FalloutSpigotApplicationImpl.class).asEagerSingleton();
    }
}

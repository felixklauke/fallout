package de.felix_klauke.fallout.spigot.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.felix_klauke.fallout.core.config.FalloutCoreConfig;
import de.felix_klauke.fallout.core.module.FalloutCoreModule;
import de.felix_klauke.fallout.spigot.FalloutSpigotApplication;
import de.felix_klauke.fallout.spigot.FalloutSpigotApplicationImpl;
import de.felix_klauke.fallout.spigot.FalloutSpigotPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class FalloutSpigotModule extends AbstractModule {

    private final FalloutSpigotPlugin falloutSpigotPlugin;
    private final FalloutCoreConfig coreConfig;

    public FalloutSpigotModule(FalloutSpigotPlugin falloutSpigotPlugin) {
        this.falloutSpigotPlugin = falloutSpigotPlugin;

        FileConfiguration config = falloutSpigotPlugin.getConfig();
        coreConfig = new FalloutCoreConfig(config.getString("database.url"), config.getString("database.user"), config.getString("database.password"));
    }

    @Override
    protected void configure() {
        install(new FalloutCoreModule(coreConfig));

        bind(Plugin.class).annotatedWith(Names.named("falloutPlugin")).toInstance(falloutSpigotPlugin);

        bind(FalloutSpigotApplication.class).to(FalloutSpigotApplicationImpl.class).asEagerSingleton();
    }
}

package de.felix_klauke.fallout.spigot;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.felix_klauke.fallout.spigot.module.FalloutSpigotModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class FalloutSpigotPlugin extends JavaPlugin {

    private FalloutSpigotApplication falloutSpigotApplication;

    @Override
    public void onDisable() {
        falloutSpigotApplication.destroy();
    }

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new FalloutSpigotModule(this));
        falloutSpigotApplication = injector.getInstance(FalloutSpigotApplication.class);
        falloutSpigotApplication.initialize();
    }

    public FalloutSpigotApplication getFalloutApplication() {
        return falloutSpigotApplication;
    }
}

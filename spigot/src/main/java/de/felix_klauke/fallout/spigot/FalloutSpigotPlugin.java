package de.felix_klauke.fallout.spigot;

import com.google.inject.Guice;
import de.felix_klauke.fallout.spigot.module.FalloutSpigotModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class FalloutSpigotPlugin extends JavaPlugin {

    private FalloutSpigotApplication falloutSpigotApplication;

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        Guice.createInjector(new FalloutSpigotModule(this));
    }

    public FalloutSpigotApplication getFalloutApplication() {
        return falloutSpigotApplication;
    }
}

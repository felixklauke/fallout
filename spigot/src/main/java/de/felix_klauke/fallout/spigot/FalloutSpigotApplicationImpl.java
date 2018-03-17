package de.felix_klauke.fallout.spigot;

import de.felix_klauke.fallout.core.kingdom.KingdomController;
import de.felix_klauke.fallout.spigot.command.CommandKingdom;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class FalloutSpigotApplicationImpl implements FalloutSpigotApplication {

    private final JavaPlugin plugin;
    private final KingdomController kingdomController;
    private final CommandKingdom commandKingdom;

    @Inject
    public FalloutSpigotApplicationImpl(@Named("falloutPlugin") Plugin plugin, KingdomController kingdomController,
                                        CommandKingdom commandKingdom) {
        this.plugin = (JavaPlugin) plugin;
        this.kingdomController = kingdomController;
        this.commandKingdom = commandKingdom;
    }

    @Override
    public void initialize() {
        plugin.getCommand("kingdom").setExecutor(commandKingdom);
    }

    @Override
    public void destroy() {

    }
}

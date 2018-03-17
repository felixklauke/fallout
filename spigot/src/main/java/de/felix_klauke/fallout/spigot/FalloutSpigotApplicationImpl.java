package de.felix_klauke.fallout.spigot;

import de.felix_klauke.fallout.core.kingdom.Kingdom;
import de.felix_klauke.fallout.core.kingdom.KingdomController;
import de.felix_klauke.fallout.spigot.command.CommandKingdom;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;
import java.util.function.Consumer;

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

    @Override
    public void handleKingdomInfoPerformed(Player player) {
        Location location = player.getLocation();
        kingdomController.getKingdom(location.getWorld().getName(), location.getChunk().getX(), location.getChunk().getZ(), new Consumer<Kingdom>() {
            @Override
            public void accept(Kingdom kingdom) {
                if (kingdom == null) {
                    player.sendMessage("Du bist in keinem Königreich. (" + location.getChunk().getX() + " | " + location.getChunk().getZ() + ")");
                    return;
                }

                player.sendMessage("Du befindest dich im Königreich " + kingdom.getName());
            }
        });
    }

    @Override
    public void handleKingdomCreatePerformed(Player player, String kingdomName) {
        Location location = player.getLocation();
        kingdomController.createKingdom(UUID.randomUUID(), player.getUniqueId(), kingdomName, "Just another random kingdom.", location.getWorld().getName(), location.getChunk().getX(), location.getChunk().getZ(), success -> {
            if (success) {
                player.sendMessage("Ein Fehler ist aufgetreten.");
                return;
            }

            player.sendMessage("Königreich erstellt.");
        });
    }
}

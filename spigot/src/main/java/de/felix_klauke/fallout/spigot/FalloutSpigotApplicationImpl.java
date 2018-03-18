package de.felix_klauke.fallout.spigot;

import de.felix_klauke.fallout.core.kingdom.Kingdom;
import de.felix_klauke.fallout.core.kingdom.KingdomController;
import de.felix_klauke.fallout.spigot.command.CommandKingdom;
import org.bukkit.Chunk;
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
    public void handleKingdomHerePerformed(Player player) {
        Location location = player.getLocation();
        kingdomController.getKingdom(location.getWorld().getName(), location.getChunk().getX(), location.getChunk().getZ(), new Consumer<Kingdom>() {
            @Override
            public void accept(Kingdom kingdom) {
                if (kingdom == null) {
                    player.sendMessage("Du befindest dich in keinem Königreich Königreich. (" + location.getChunk().getX() + " | " + location.getChunk().getZ() + ")");
                    return;
                }

                player.sendMessage("Du befindest dich im Königreich " + kingdom.getName());
            }
        });
    }

    @Override
    public void handleKingdomCreatePerformed(Player player, String kingdomName) {
        kingdomController.getKingdom(kingdomName, kingdom -> {
            if (kingdom != null) {
                player.sendMessage("En Königreich mit diesem Namen existiert bereits.");
                return;
            }

            Chunk chunk = player.getLocation().getChunk();
            kingdomController.getKingdom(chunk.getWorld().getName(), chunk.getX(), chunk.getZ(), existingKingdom -> {
                if (existingKingdom != null) {
                    player.sendMessage("Hier ist bereits ein Königreich.");
                    return;
                }

                kingdomController.createKingdom(UUID.randomUUID(), player.getUniqueId(), kingdomName, 0, "Just another random kingdom.", chunk.getWorld().getName(), chunk.getX(), chunk.getZ(), success -> {
                    if (!success) {
                        player.sendMessage("Ein Fehler ist aufgetreten.");
                        return;
                    }

                    player.sendMessage("Königreich erstellt.");
                });
            });
        });
    }

    @Override
    public void handleKingdomInfoPerformed(Player player) {
        kingdomController.getKingdom(player.getUniqueId(), kingdom -> {
            if (kingdom == null) {
                player.sendMessage("Du bist in keinem Königreich.");
                return;
            }

            printKingdomInfo(player, kingdom);
        });
    }

    @Override
    public void handleKingdomLeavePerformed(Player player) {
        kingdomController.getKingdom(player.getUniqueId(), kingdom -> {
            if (kingdom == null) {
                player.sendMessage("Du bist in keinem Königreich.");
                return;
            }

            kingdomController.removeMemberFromKingdom(kingdom.getUniqueId(), player.getUniqueId(), success -> {
                if (!success) {
                    player.sendMessage("Es ist ein Fehler aufgetreten.");
                    return;
                }

                player.sendMessage("Du wurdest aus dem Königreich entfernt.");
            });
        });
    }

    @Override
    public void handleKingdomClaimPerformed(Player player) {
        kingdomController.getKingdom(player.getUniqueId(), kingdom -> {
            if (kingdom == null) {
                player.sendMessage("Du bist in keinem Königreich.");
                return;
            }

            Chunk chunk = player.getLocation().getChunk();
            kingdomController.getKingdom(chunk.getWorld().getName(), chunk.getX(), chunk.getZ(), existingKingdom -> {
                if (existingKingdom != null) {
                    player.sendMessage("Dieser Bereich liegt bereits im Königreich " + kingdom.getName() + ".");
                    return;
                }

                if (kingdom.getBalance() < 500) {
                    player.sendMessage("Das Königreich hat nicht genügend Geld in der Kasse.");
                    return;
                }

                kingdomController.addKingdomHolding(kingdom.getUniqueId(), chunk.getWorld().getName(), chunk.getX(), chunk.getZ(), success -> {
                    if (!success) {
                        player.sendMessage("Es ist ein Fehler aufgetreten.");
                        return;
                    }

                    player.sendMessage("Das Land wurde deinem Königreich hinzugefügt.");

                    kingdomController.manipulateKingdomBalance(kingdom.getUniqueId(), -500, balanceManipulationSuccessful -> {
                        if (!balanceManipulationSuccessful) {
                            player.sendMessage("Beim Bezahlen der Kosten für das Land ist ein Fehler aufgetreten.");
                            return;
                        }

                        player.sendMessage("Deinem Königreich wurde entsprechend Geld aus der Staatskasse entfernt.");
                    });
                });
            });
        });
    }

    private void printKingdomInfo(Player player, Kingdom kingdom) {
        player.sendMessage("Königreich: " + kingdom.getName());
        player.sendMessage("Beschreibung; " + kingdom.getDescription());
        player.sendMessage("Reichtum: " + kingdom.getBalance());
    }
}

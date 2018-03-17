package de.felix_klauke.fallout.spigot;

import org.bukkit.entity.Player;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface FalloutSpigotApplication {

    void initialize();

    void destroy();

    void handleKingdomInfoPerformed(Player player);

    void handleKingdomCreatePerformed(Player player, String arg);
}

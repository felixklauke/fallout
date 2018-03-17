package de.felix_klauke.fallout.spigot.command;

import de.felix_klauke.fallout.spigot.FalloutSpigotApplication;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class CommandKingdom implements CommandExecutor {

    private Provider<FalloutSpigotApplication> falloutApplication;

    @Inject
    public CommandKingdom(Provider<FalloutSpigotApplication> falloutApplication) {
        this.falloutApplication = falloutApplication;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Nope.");
            return false;
        }

        return true;
    }
}
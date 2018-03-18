package de.felix_klauke.fallout.spigot.task;

import de.felix_klauke.fallout.spigot.FalloutSpigotApplication;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class TaxCollectingTask implements Runnable {

    private final Provider<FalloutSpigotApplication> falloutApplicationProvider;

    @Inject
    public TaxCollectingTask(Provider<FalloutSpigotApplication> falloutApplicationProvider) {
        this.falloutApplicationProvider = falloutApplicationProvider;
    }

    @Override
    public void run() {
        Bukkit.broadcastMessage("Starting tax collection...");

        falloutApplicationProvider.get().processTaxCollection();

        Bukkit.broadcastMessage("Tax collection completed.");
    }
}

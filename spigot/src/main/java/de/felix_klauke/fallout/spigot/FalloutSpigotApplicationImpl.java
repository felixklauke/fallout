package de.felix_klauke.fallout.spigot;

import de.felix_klauke.fallout.core.kingdom.KingdomController;

import javax.inject.Inject;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class FalloutSpigotApplicationImpl implements FalloutSpigotApplication {

    private final KingdomController kingdomController;

    @Inject
    public FalloutSpigotApplicationImpl(KingdomController kingdomController) {
        this.kingdomController = kingdomController;
    }
}

package de.felix_klauke.fallout.core.kingdom;

import java.util.UUID;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface KingdomMember {

    UUID getUniqueId();

    UUID getKingdomUniqueId();

    int getRankId();
}

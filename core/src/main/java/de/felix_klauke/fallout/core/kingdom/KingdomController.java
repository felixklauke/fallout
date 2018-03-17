package de.felix_klauke.fallout.core.kingdom;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface KingdomController {

    void getKingdom(String name, Consumer<Kingdom> kingdomConsumer);

    void getKingdomHoldings(UUID kingdomUniqueId, Consumer<Set<KingdomLandHolding>> consumer);

    void createKingdom(UUID uniqueId, String name, String description, Consumer<Boolean> resultConsumer);

    void removeKingdom(UUID uniqueId, Consumer<Boolean> result);

    void getKingdom(UUID playerUniqueId, Consumer<Kingdom> kingdomConsumer);

    void addMemberToKingdom(UUID kingdomUniqueId, UUID playerUniqueId);

    void removeMemberFromKingdom(UUID kingdomUniqueId, UUID playerUniqueId);

    void isKingdomMember(UUID kingdomUniqueId, UUID playerUniqueId);
}

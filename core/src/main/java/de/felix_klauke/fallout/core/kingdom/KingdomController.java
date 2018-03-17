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

    void addKingdomHolding(UUID kingdomUniqueId, String worldName, int x, int z, Consumer<Boolean> consumer);

    void removeKingdomHolding(UUID kingdomUniqueId, String worldName, int x, int z, Consumer<Boolean> consumer);

    void createKingdom(UUID uniqueId, UUID ownerUniqueId, String name, String description, String worldName, int x, int z, Consumer<Boolean> resultConsumer);

    void removeKingdom(UUID uniqueId, Consumer<Boolean> result);

    void getKingdom(UUID playerUniqueId, Consumer<Kingdom> kingdomConsumer);

    void addMemberToKingdom(UUID kingdomUniqueId, UUID playerUniqueId, int rankId, Consumer<Boolean> consumer);

    void getKingdomMembers(UUID kingdomUniqueId, Consumer<Set<KingdomMember>> consumer);

    void removeMemberFromKingdom(UUID kingdomUniqueId, UUID playerUniqueId, Consumer<Boolean> consumer);

    void isKingdomMember(UUID kingdomUniqueId, UUID playerUniqueId, Consumer<Boolean> result);

    void updateMemberRank(UUID playerUniqueId, int rankId, Consumer<Boolean> consumer);

    void getKingdom(String worldName, int x, int z, Consumer<Kingdom> kingdomConsumer);
}

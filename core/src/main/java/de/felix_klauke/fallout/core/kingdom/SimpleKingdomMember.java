package de.felix_klauke.fallout.core.kingdom;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class SimpleKingdomMember implements KingdomMember {

    private final UUID uniqueId;
    private final UUID kingdomUniqueId;
    private final int rankId;

    SimpleKingdomMember(UUID uniqueId, UUID kingdomUniqueId, int rankId) {
        this.uniqueId = uniqueId;
        this.kingdomUniqueId = kingdomUniqueId;
        this.rankId = rankId;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public UUID getKingdomUniqueId() {
        return kingdomUniqueId;
    }

    @Override
    public int getRankId() {
        return rankId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId, kingdomUniqueId, rankId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleKingdomMember)) {
            return false;
        }
        SimpleKingdomMember that = (SimpleKingdomMember) o;
        return rankId == that.rankId &&
                Objects.equals(uniqueId, that.uniqueId) &&
                Objects.equals(kingdomUniqueId, that.kingdomUniqueId);
    }
}

package de.felix_klauke.fallout.core.kingdom;

import java.util.Objects;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class SimpleKingdomLandHolding implements KingdomLandHolding {

    private final int x;
    private final int z;

    SimpleKingdomLandHolding(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleKingdomLandHolding)) {
            return false;
        }
        SimpleKingdomLandHolding that = (SimpleKingdomLandHolding) o;
        return x == that.x &&
                z == that.z;
    }
}

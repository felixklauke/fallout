package de.felix_klauke.fallout.core.kingdom;

import java.util.UUID;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class SimpleKingdom implements Kingdom {

    private final UUID uniqueId;
    private final String name;
    private final double balance;
    private String description = "";

    SimpleKingdom(UUID uniqueId, String name, double balance, String description) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.balance = balance;
        this.description = description;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getBalance() {
        return balance;
    }
}

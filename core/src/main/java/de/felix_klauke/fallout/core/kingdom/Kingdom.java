package de.felix_klauke.fallout.core.kingdom;

import java.util.UUID;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface Kingdom {

    UUID getUniqueId();

    String getName();

    String getDescription();

    double getBalance();
}

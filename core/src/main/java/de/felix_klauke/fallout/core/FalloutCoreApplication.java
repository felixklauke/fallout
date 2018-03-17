package de.felix_klauke.fallout.core;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface FalloutCoreApplication {

    /**
     * Setup the application.
     */
    void initialize();

    /**
     * Destroy the application and clean up all resources.
     */
    void destroy();
}

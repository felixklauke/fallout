package de.felix_klauke.fallout.core.module;

import com.google.inject.AbstractModule;
import de.felix_klauke.fallout.core.config.FalloutCoreConfig;
import de.felix_klauke.fallout.core.kingdom.KingdomController;
import de.felix_klauke.fallout.core.kingdom.KingdomControllerImpl;
import de.felix_klauke.fallout.core.provider.DataSourceProvider;

import javax.sql.DataSource;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class FalloutCoreModule extends AbstractModule {

    private final FalloutCoreConfig falloutCoreConfig;

    public FalloutCoreModule(FalloutCoreConfig falloutCoreConfig) {
        this.falloutCoreConfig = falloutCoreConfig;
    }

    @Override
    protected void configure() {
        bind(FalloutCoreConfig.class).toInstance(falloutCoreConfig);

        bind(DataSource.class).toProvider(DataSourceProvider.class).asEagerSingleton();
        bind(KingdomController.class).to(KingdomControllerImpl.class).asEagerSingleton();
    }
}

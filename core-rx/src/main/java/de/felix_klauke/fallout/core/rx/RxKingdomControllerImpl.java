package de.felix_klauke.fallout.core.rx;

import de.felix_klauke.fallout.core.kingdom.KingdomControllerImpl;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class RxKingdomControllerImpl extends KingdomControllerImpl {

    @Inject
    public RxKingdomControllerImpl(DataSource dataSource) {
        super(dataSource);
    }
}

package de.felix_klauke.fallout.core.kingdom;

import com.google.common.collect.Sets;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class KingdomControllerImpl implements KingdomController {

    private static final String QUERY_GET_KINGDOM_BY_NAME = "SELECT `uniqueId`, `description` FROM fallout_kingdoms WHERE fallout_kingdoms.`name` = ?";
    private static final String QUERY_CREATE_KINGDOM = "INSERT INTO fallout_kingdoms (`uniqueId`, `name`, `description`) VALUES (?, ?, ?)";
    private static final String QUERY_DELETE_KINGDOM_BY_UUID = "DELETE FROM fallout_kingdoms WHERE `uniqueId` = ?";
    private static final String QUERY_GET_LAND_HOLDINGS_BY_KINGDOM_UUID = "SELECT `posX`, `posZ` FROM fallout_land_holdings WHERE `kindomUniqueId` = ?";

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final DataSource dataSource;

    @Inject
    public KingdomControllerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void getKingdom(String name, Consumer<Kingdom> kingdomConsumer) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(QUERY_GET_KINGDOM_BY_NAME)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    kingdomConsumer.accept(null);
                    resultSet.close();
                    return;
                }

                Kingdom kingdom = new SimpleKingdom(UUID.fromString(resultSet.getString("uniqueId")),
                        name, resultSet.getString("description"));

                resultSet.close();

                kingdomConsumer.accept(kingdom);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void getKingdomHoldings(UUID kingdomUniqueId, Consumer<Set<KingdomLandHolding>> consumer) {
        executorService.submit(() -> {
            Set<KingdomLandHolding> holdings = Sets.newHashSet();

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(QUERY_GET_LAND_HOLDINGS_BY_KINGDOM_UUID)) {
                preparedStatement.setString(1, kingdomUniqueId.toString());

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    KingdomLandHolding holding = new SimpleKingdomLandHolding(resultSet.getInt("posX"), resultSet.getInt("posZ"));
                    holdings.add(holding);
                }

                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            consumer.accept(holdings);
        });
    }

    @Override
    public void createKingdom(UUID uniqueId, String name, String description, Consumer<Boolean> resultConsumer) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_CREATE_KINGDOM)) {
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, description);

                boolean result = preparedStatement.execute();
                resultConsumer.accept(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void removeKingdom(UUID uniqueId, Consumer<Boolean> result) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_DELETE_KINGDOM_BY_UUID)) {
                preparedStatement.setString(1, uniqueId.toString());

                int changedRows = preparedStatement.executeUpdate();

                result.accept(changedRows > 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void getKingdom(UUID playerUniqueId, Consumer<Kingdom> kingdomConsumer) {

    }

    @Override
    public void getKingdom(int x, int z, Consumer<Kingdom> kingdomConsumer) {

    }

    @Override
    public void addMemberToKingdom(UUID kingdomUniqueId, UUID playerUniqueId) {

    }

    @Override
    public void removeMemberFromKingdom(UUID kingdomUniqueId, UUID playerUniqueId) {

    }

    @Override
    public void isKingdomMember(UUID kingdomUniqueId, UUID playerUniqueId) {

    }
}

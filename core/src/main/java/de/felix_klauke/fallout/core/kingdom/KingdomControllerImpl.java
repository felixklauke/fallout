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

    private static final String QUERY_GET_KINGDOM_BY_NAME = "SELECT `uniqueId`, `balance`, `description` FROM fallout_kingdoms WHERE fallout_kingdoms.`name` = ?";
    private static final String QUERY_CREATE_KINGDOM = "INSERT INTO fallout_kingdoms (`uniqueId`, `name`, `balance`, `description`) VALUES (?, ?, ?, ?)";
    private static final String QUERY_DELETE_KINGDOM_BY_UUID = "DELETE FROM fallout_kingdoms WHERE `uniqueId` = ?";
    private static final String QUERY_GET_LAND_HOLDINGS_BY_KINGDOM_UUID = "SELECT `world`, `posX`, `posZ` FROM fallout_land_holdings WHERE `kindomUniqueId` = ?";
    private static final String QUERY_GET_KINGDOM_BY_LOCATION = "SELECT fallout_kingdoms.`uniqueId`, fallout_kingdoms.`balance`,fallout_kingdoms.`description`, fallout_kingdoms.`name` FROM fallout_land_holdings INNER JOIN fallout_kingdoms ON fallout_land_holdings.kindomUniqueId = fallout_kingdoms.uniqueId WHERE fallout_land_holdings.world = ? AND fallout_land_holdings.posX = ? AND fallout_land_holdings.posZ = ?";
    private static final String QUERY_ADD_MEMBER_TO_KINGDOM = "INSERT INTO fallout_kingdom_members (`uniqueId`, `kingdomUniqueId`, `rankId`) VALUES (?, ?, ?)";
    private static final String QUERY_REMOVE_MEMBER_FROM_KINGDOM = "DELETE FROM fallout_kingdom_members WHERE `uniqueId` = ? AND `kingdomUniqueId` = ?";
    private static final String QUERY_GET_KINGDOM_MEMBER = "SELECT `rankId` FROM fallout_kingdom_members WHERE `uniqueId` = ? AND `kingdomUniqueId` = ?";
    private static final String QUERY_GET_KINGDOM_MEMBERS = "SELECT `uniqueId`, `rankId` FROM fallout_kingdom_members WHERE `kingdomUniqueId` = ?";
    private static final String QUERY_GET_KINGDOM_BY_MEMBER_UUID = "SELECT fallout_kingdoms.`uniqueId`, fallout_kingdoms.`name`, fallout_kingdoms.`balance`, fallout_kingdoms.`description` FROM fallout_kingdom_members INNER JOIN fallout_kingdoms ON fallout_kingdoms.`uniqueId` = fallout_kingdom_members.`kingdomUniqueId` WHERE fallout_kingdom_members.`uniqueId` = ?";
    private static final String QUERY_UPDATE_MEMBER_RANK = "UPDATE fallout_kingdom_members SET rankId = ? WHERE uniqueId = ?";
    private static final String QUERY_REMOVE_HOLDING_FROM_KINGDOM = "DELETE FROM fallout_land_holdings WHERE `world` = ? AND `posX` = ? AND `posZ` = ? AND `kindomUniqueId` = ?";
    private static final String QUERY_ADD_HOLDING_TO_KINGDOM = "INSERT INTO fallout_land_holdings (`world`, `posX`, `posZ`, `kindomUniqueId`) VALUES (?, ?, ?, ?)";
    private static final String QUERY_UPDATE_KINGDOM_BALANCE = "UPDATE fallout_kingdoms SET `balance` = `balance` + ? WHERE `uniqueId` = ?";

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
                        name, resultSet.getDouble("balance"), resultSet.getString("description"));

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
                    KingdomLandHolding holding = new SimpleKingdomLandHolding(resultSet.getString("world"),
                            resultSet.getInt("posX"), resultSet.getInt("posZ"));
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
    public void addKingdomHolding(UUID kingdomUniqueId, String worldName, int x, int z, Consumer<Boolean> consumer) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_HOLDING_TO_KINGDOM)) {
                preparedStatement.setString(1, worldName);
                preparedStatement.setInt(2, x);
                preparedStatement.setInt(3, z);
                preparedStatement.setString(4, kingdomUniqueId.toString());

                int changedRows = preparedStatement.executeUpdate();

                consumer.accept(changedRows > 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void removeKingdomHolding(UUID kingdomUniqueId, String worldName, int x, int z, Consumer<Boolean> consumer) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_REMOVE_HOLDING_FROM_KINGDOM)) {
                preparedStatement.setString(1, worldName);
                preparedStatement.setInt(2, x);
                preparedStatement.setInt(3, z);
                preparedStatement.setString(4, kingdomUniqueId.toString());

                int changedRows = preparedStatement.executeUpdate();

                consumer.accept(changedRows > 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void createKingdom(UUID uniqueId, UUID ownerUniqueId, String name, double balance, String description, String worldName, int x, int z, Consumer<Boolean> resultConsumer) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_CREATE_KINGDOM)) {
                preparedStatement.setString(1, uniqueId.toString());
                preparedStatement.setString(2, name);
                preparedStatement.setDouble(3, balance);
                preparedStatement.setString(4, description);

                int result = preparedStatement.executeUpdate();
                resultConsumer.accept(result > 0);

                if (result < 0) {
                    return;
                }

                addMemberToKingdom(uniqueId, ownerUniqueId, 0, aBoolean -> {

                });

                addKingdomHolding(uniqueId, worldName, x, z, aBoolean -> {

                });
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
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_GET_KINGDOM_BY_MEMBER_UUID)) {
                preparedStatement.setString(1, playerUniqueId.toString());

                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    kingdomConsumer.accept(null);
                    return;
                }

                Kingdom kingdom = new SimpleKingdom(UUID.fromString(resultSet.getString("uniqueId")), resultSet.getString("name"),
                        resultSet.getDouble("balance"), resultSet.getString("description"));
                kingdomConsumer.accept(kingdom);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void addMemberToKingdom(UUID kingdomUniqueId, UUID playerUniqueId, int rankId, Consumer<Boolean> consumer) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_MEMBER_TO_KINGDOM)) {
                preparedStatement.setString(1, playerUniqueId.toString());
                preparedStatement.setString(2, kingdomUniqueId.toString());
                preparedStatement.setInt(3, rankId);

                int rows = preparedStatement.executeUpdate();
                consumer.accept(rows > 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void getKingdomMembers(UUID kingdomUniqueId, Consumer<Set<KingdomMember>> consumer) {
        executorService.submit(() -> {
            Set<KingdomMember> kingdomMembers = Sets.newHashSet();

            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_GET_KINGDOM_MEMBERS)) {
                preparedStatement.setString(1, kingdomUniqueId.toString());

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    KingdomMember kingdomMember = new SimpleKingdomMember(UUID.fromString(resultSet.getString("uniqueId")), kingdomUniqueId, resultSet.getInt("rankId"));
                    kingdomMembers.add(kingdomMember);
                }

                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            consumer.accept(kingdomMembers);
        });
    }

    @Override
    public void removeMemberFromKingdom(UUID kingdomUniqueId, UUID playerUniqueId, Consumer<Boolean> consumer) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_REMOVE_MEMBER_FROM_KINGDOM)) {
                preparedStatement.setString(1, playerUniqueId.toString());
                preparedStatement.setString(2, kingdomUniqueId.toString());

                int rows = preparedStatement.executeUpdate();
                consumer.accept(rows > 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void isKingdomMember(UUID kingdomUniqueId, UUID playerUniqueId, Consumer<Boolean> result) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_GET_KINGDOM_MEMBER)) {
                preparedStatement.setString(1, playerUniqueId.toString());
                preparedStatement.setString(2, kingdomUniqueId.toString());

                ResultSet resultSet = preparedStatement.executeQuery();
                result.accept(resultSet.next());
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void updateMemberRank(UUID playerUniqueId, int rankId, Consumer<Boolean> consumer) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_UPDATE_MEMBER_RANK)) {
                preparedStatement.setInt(1, rankId);
                preparedStatement.setString(2, playerUniqueId.toString());

                int rows = preparedStatement.executeUpdate();
                consumer.accept(rows > 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void getKingdom(String worldName, int x, int z, Consumer<Kingdom> kingdomConsumer) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_GET_KINGDOM_BY_LOCATION)) {
                preparedStatement.setString(1, worldName);
                preparedStatement.setInt(2, x);
                preparedStatement.setInt(3, z);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    kingdomConsumer.accept(null);
                    return;
                }

                Kingdom kingdom = new SimpleKingdom(UUID.fromString(resultSet.getString("uniqueId")), resultSet.getString("name"),
                        resultSet.getDouble("balance"), resultSet.getString("description"));

                resultSet.close();

                kingdomConsumer.accept(kingdom);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void manipulateKingdomBalance(UUID uniqueId, double delta, Consumer<Boolean> consumer) {
        executorService.submit(() -> {
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY_UPDATE_KINGDOM_BALANCE)) {
                preparedStatement.setDouble(1, delta);
                preparedStatement.setString(2, uniqueId.toString());

                int rows = preparedStatement.executeUpdate();
                consumer.accept(rows > 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}

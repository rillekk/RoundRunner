package net.rillekk.roundrunner.mysql;


import net.rillekk.roundrunner.RoundRunner;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/***************************************************************
 *                                                             *
 *   @author Rillekk                                           *
 *   @Instagram: rillekk                                       *
 *   @Discord: Rillekk#8642                                    *
 *                                                             *
 *                                                             *
 *   Jede Art der Vervielfältigung, Verbreitung, Verleihung,   *
 *   öffentlich Zugänglichmachung oder andere Nutzung bedarf   *
 *   der ausdrücklichen, schriftlichen Zustimmung von Rillekk. *
 *                                                             *
 ***************************************************************/


public class MySQLRoundRunner {
    private final RoundRunner plugin;

    public MySQLRoundRunner(RoundRunner plugin) {
        this.plugin = plugin;
        plugin.getMySQL().update("CREATE TABLE IF NOT EXISTS RoundRunnerRounds (UUID VARCHAR(100), PlayerName VARCHAR(16), Rounds INT)");

    }

    public CompletableFuture<Boolean> userExists(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResultSet rs = plugin.getMySQL().query("SELECT UUID FROM RoundRunnerRounds WHERE UUID = '" + player.getUniqueId().toString() + "'");
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public CompletableFuture<Integer> getRounds(Player player) {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        ResultSet rs = plugin.getMySQL().query("SELECT Rounds FROM RoundRunnerRounds WHERE UUID = '" + player.getUniqueId().toString() + "'");
                        if (rs.next())
                            return rs.getInt("Rounds");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
        );
    }


    public void setRounds(Player player, int rounds) {
        plugin.getCachedThreadExecutor().execute(() -> userExists(player).thenAccept(check -> {
            if (check) {
                plugin.getMySQL().update("UPDATE RoundRunnerRounds SET PlayerName = '" + player.getName() + "', Rounds = '" + rounds + "' WHERE UUID = '" + player.getUniqueId().toString() + "'");
            } else {
                plugin.getMySQL().update("INSERT INTO RoundRunnerRounds (UUID, PlayerName, Rounds) VALUES ('" + player.getUniqueId().toString() + "', '" + player.getName() + "', '" + rounds + "')");
            }
        }));
    }


    public CompletableFuture<Integer> getRank(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            int rank = -1;
            try {
                ResultSet rs = plugin.getMySQL().query("SELECT UUID FROM RoundRunnerRounds ORDER BY Rounds DESC");
                while (rs.next()) {
                    if (rank == -1)
                        rank = 1;
                    else
                        rank++;
                    if (rs.getString("UUID").equals(player.getUniqueId().toString()))
                        return rank;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        });
    }

    public CompletableFuture<String[][]> getFirstRanks(int positions) {
        return CompletableFuture.supplyAsync(() -> {
            ResultSet rs = plugin.getMySQL().query("SELECT * FROM RoundRunnerRounds ORDER BY Rounds DESC");
            String[][] players = new String[positions][2];
            try {
                int i = 0;
                while (rs.next()) {
                    if (i == positions)
                        break;
                    players[i][0] = rs.getString("PlayerName");
                    players[i][1] = String.valueOf(rs.getInt("Rounds"));
                    i++;
                }
                return players;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /*
        methods with a OfflinePlayer as Parameter
     */

    public CompletableFuture<Integer> getRank(OfflinePlayer offlinePlayer) {
        return CompletableFuture.supplyAsync(() -> {
            int rank = -1;
            try {
                ResultSet rs = plugin.getMySQL().query("SELECT UUID FROM RoundRunnerRounds ORDER BY Rounds DESC");
                while (rs.next()) {
                    if (rank == -1)
                        rank = 1;
                    else
                        rank++;
                    if (rs.getString("UUID").equals(offlinePlayer.getUniqueId().toString()))
                        return rank;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        });
    }

    public CompletableFuture<Boolean> userExists(OfflinePlayer offlinePlayer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResultSet rs = plugin.getMySQL().query("SELECT UUID FROM RoundRunnerRounds WHERE UUID = '" + offlinePlayer.getUniqueId().toString() + "'");
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public CompletableFuture<Integer> getRounds(OfflinePlayer offlinePlayer) {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        ResultSet rs = plugin.getMySQL().query("SELECT Rounds FROM RoundRunnerRounds WHERE UUID = '" + offlinePlayer.getUniqueId().toString() + "'");
                        if (rs.next())
                            return rs.getInt("Rounds");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
        );
    }

    public void setRounds(OfflinePlayer offlinePlayer, int rounds) {
        plugin.getCachedThreadExecutor().execute(() -> userExists(offlinePlayer).thenAccept(check -> {
            if (check) {
                plugin.getMySQL().update("UPDATE RoundRunnerRounds SET PlayerName = '" + offlinePlayer.getName() + "', Rounds = '" + rounds + "' WHERE UUID = '" + offlinePlayer.getUniqueId().toString() + "'");
            } else {
                plugin.getMySQL().update("INSERT INTO RoundRunnerRounds (UUID, PlayerName, Rounds) VALUES ('" + offlinePlayer.getUniqueId().toString() + "', '" + offlinePlayer.getName() + "', '" + rounds + "')");
            }
        }));
    }
}

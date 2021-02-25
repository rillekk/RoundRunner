package net.rillekk.roundrunner.listener;


import lombok.Getter;
import lombok.Setter;

import de.blyrex.coins.api.CoinsManagerProvider;

import net.rillekk.roundrunner.RoundRunner;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

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

@Getter
@Setter
public class PlayerMoveListener implements Listener {
    private final RoundRunner plugin;
    private String[] controlVariable;
    private HashMap<Player, Integer> controlCoins = new HashMap<>();
    private double configx;
    private double configy;
    private double configz;

    public PlayerMoveListener(RoundRunner plugin) {
        this.plugin = plugin;
        if (plugin.getLocationFileConfiguration().contains("Location.")) {
            String arrayString = "" + plugin.getLocationFileConfiguration().getConfigurationSection("Location.").getKeys(false);
            this.controlVariable = arrayString.replaceAll("" + "\\[", "").replaceAll("]", "").split(",");
        } else {
            plugin.getLocationFileConfiguration().set("Location.", null);
        }
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getY() == plugin.getLocationFileConfiguration().getDouble("Location." + 1 + ".y")) {
            double x = player.getLocation().getX();
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ();
            for (int i = 1; i < controlVariable.length + 1; i++) {
                if (plugin.getRoundChecker().containsKey(player)) {
                    if (plugin.getRoundChecker().get(player) == i) {
                        this.configx = plugin.getLocationFileConfiguration().getDouble("Location." + i + ".x");
                        this.configy = plugin.getLocationFileConfiguration().getDouble("Location." + i + ".y");
                        this.configz = plugin.getLocationFileConfiguration().getDouble("Location." + i + ".z");
                        if (almostEqual(x, configx)) {
                            if (almostEqual(y, configy)) {
                                if (almostEqual(z, configz)) {
                                    if (i == 1) {
                                        plugin.getRoundChecker().put(player, plugin.getRoundChecker().get(player) + 1);
                                    } else if (i >= controlVariable.length) {
                                        plugin.getMySQLRoundRunner().getRounds(player).thenAccept(oldRounds -> {
                                            plugin.getMySQLRoundRunner().setRounds(player, oldRounds + 1);
                                                if(controlCoins.get(player) == 1) {
                                                    CoinsManagerProvider.get().getCoinsUser(player.getUniqueId()).thenAccept(coinsUser -> {
                                                        coinsUser.addCoins(1L);
                                                        CoinsManagerProvider.get().saveCoinsUser(coinsUser);
                                                        player.sendTitle("§6Runde", "§6" + (oldRounds + 1));
                                                        player.playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
                                                        plugin.getRoundChecker().put(player, 1);
                                                        controlCoins.put(player, 5);
                                                    });
                                                } else {
                                                    player.sendTitle("§6Runde", "§6" + (oldRounds + 1));
                                                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 3);
                                                    plugin.getRoundChecker().put(player, 1);
                                                    controlCoins.put(player, (controlCoins.get(player) - 1));
                                                }
                                            });
                                    } else {
                                        plugin.getRoundChecker().put(player, plugin.getRoundChecker().get(player) + 1);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    plugin.getRoundChecker().put(player, 1);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.controlCoins.put(player, 5);
    }


    private boolean almostEqual(double a, double b) {
        return Math.abs(a - b) < 2;
    }
}
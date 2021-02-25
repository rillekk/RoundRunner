package net.rillekk.roundrunner.commands;


import lombok.SneakyThrows;

import net.rillekk.roundrunner.RoundRunner;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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


public class RundenCommand implements CommandExecutor {
    private final RoundRunner plugin;

    public RundenCommand(RoundRunner plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!plugin.getTimeout().containsKey(player)) {
                if (args.length == 0) {
                    plugin.getMySQLRoundRunner().getRounds(player).thenAccept(rounds -> plugin.getMySQLRoundRunner().getRank(player).thenAccept(rank -> {
                        player.sendMessage(plugin.getPrefix() + "✬ §m-------------------§f§7 ✬");
                        player.sendMessage(plugin.getPrefix() + "                          ");
                        player.sendMessage(plugin.getPrefix() + "➥ RoundRunner §8▏ §7Runden");
                        player.sendMessage(plugin.getPrefix() + "                           ");
                        player.sendMessage(plugin.getPrefix() + "Runden §8➸ §a" + rounds);
                        player.sendMessage(plugin.getPrefix() + "Position im Ranking §8➸ §a" + rank);
                        player.sendMessage(plugin.getPrefix() + "                          ");
                        player.sendMessage(plugin.getPrefix() + "✬ §m-------------------§f§7 ✬");
                        plugin.getTimeout().put(player, 8);
                    }));

                } else if (args.length == 1) {
                    OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[0]);
                    plugin.getMySQLRoundRunner().userExists(offlinePlayer).thenAccept(check -> {
                        if (check) {
                            plugin.getMySQLRoundRunner().getRounds(offlinePlayer).thenAccept(rounds -> plugin.getMySQLRoundRunner().getRank(offlinePlayer).thenAccept(rank -> {
                                player.sendMessage(plugin.getPrefix() + "✬ §m-------------------§f§7 ✬");
                                player.sendMessage(plugin.getPrefix() + "                          ");
                                player.sendMessage(plugin.getPrefix() + "➥ RoundRunner §8▏ §7Runden von §a" + offlinePlayer.getName());
                                player.sendMessage(plugin.getPrefix() + "                          ");
                                player.sendMessage(plugin.getPrefix() + "Runden §8➸ §a" + rounds);
                                player.sendMessage(plugin.getPrefix() + offlinePlayer.getName() + "'s Position im Ranking §8➸ §a" + rank);
                                player.sendMessage(plugin.getPrefix() + "                          ");
                                player.sendMessage(plugin.getPrefix() + "✬ §m-------------------§f§7 ✬");
                                plugin.getTimeout().put(player, 8);
                            }));
                        } else {
                            plugin.getTimeout().put(player, 8);
                            player.sendMessage(plugin.getPrefix() + "Dieser Spieler ist noch nie eine Runde gelaufen.");
                        }
                    });
                }
            } else
                player.sendMessage(plugin.getPrefix() + "Bitte warte einen Moment.");
        }
        return false;
    }
}
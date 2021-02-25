package net.rillekk.roundrunner.commands;


import lombok.SneakyThrows;

import net.rillekk.roundrunner.RoundRunner;

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


public class RundenRankingCommand implements CommandExecutor {

    private final RoundRunner plugin;

    public RundenRankingCommand(RoundRunner plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!plugin.getTimeout().containsKey(player)) {
                player.sendMessage(plugin.getPrefix() + "✬ §m-----------------------------§f§7 ✬");
                player.sendMessage(plugin.getPrefix() + "                          ");
                player.sendMessage(plugin.getPrefix() + "➥ RoundRunner §8▏ §7Top Runden Läufer");
                player.sendMessage(plugin.getPrefix() + "                          ");

                plugin.getMySQLRoundRunner().getFirstRanks(10).thenAccept(players -> {
                    for (int i = 0; i < players.length; i++) {
                        if (players[i] != null) {
                            player.sendMessage(plugin.getPrefix() + "§aTop " + (i + 1) + " §8➸ §a" + players[i][0] + " §8(§a" + players[i][1] + "§8)");
                        }
                    }
                    player.sendMessage(plugin.getPrefix() + "                          ");

                    player.sendMessage(plugin.getPrefix() + "✬ §m-----------------------------§f§7 ✬");
                    plugin.getTimeout().put(player, 8);
                });
            } else
                player.sendMessage(plugin.getPrefix() + "Bitte warte einen Moment.");
        }
        return false;
    }
}
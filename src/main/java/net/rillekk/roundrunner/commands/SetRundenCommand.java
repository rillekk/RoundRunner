package net.rillekk.roundrunner.commands;


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


public class SetRundenCommand implements CommandExecutor {
    private final RoundRunner plugin;
    public SetRundenCommand(RoundRunner plugin) {
        this.plugin = plugin;
    }
    private int rounds;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("roundrunner.setrunden")) {
                if (args.length == 2) {
                    OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
                    plugin.getMySQLRoundRunner().userExists(target).thenAccept(check -> {
                        if(check) {
                            this.rounds = Integer.parseInt(args[1]);
                            plugin.getMySQLRoundRunner().setRounds(target, rounds);
                            player.sendMessage(plugin.getPrefix() + target.getName() + "'s Runden wurden nun auf " + rounds + " gesetzt.");
                        } else
                            player.sendMessage(plugin.getPrefix() + "Dieser Spieler ist noch nie eine Runde gelaufen.");
                    });
                } else
                    player.sendMessage(plugin.getPrefix() + "§c/setrunden <Spieler> <Runden>");
            } else
                player.sendMessage(plugin.getPrefix() + "§cDazu hast du keine Rechte!");
        }
            return false;
    }
}
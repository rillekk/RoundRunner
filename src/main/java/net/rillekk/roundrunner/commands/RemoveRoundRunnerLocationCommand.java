package net.rillekk.roundrunner.commands;


import lombok.SneakyThrows;

import net.rillekk.roundrunner.RoundRunner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.yaml.snakeyaml.error.YAMLException;

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


public class RemoveRoundRunnerLocationCommand implements CommandExecutor {
    private final RoundRunner plugin;
    public RemoveRoundRunnerLocationCommand(RoundRunner plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("roundrunner.removeroundrunnerlocation")) {
                if(args.length == 0) {
                    for(String key : plugin.getLocationFileConfiguration().getKeys(false)) {
                        plugin.getLocationFileConfiguration().set(key, null);
                    }
                    plugin.getLocationFileConfiguration().save(plugin.getLocationFile());
                    player.sendMessage(plugin.getPrefix() + "Alle Locations wurden gelöscht.");
                } else if (args.length == 1) {
                    String path = "Location." + args[0];
                    try {
                        plugin.getLocationFileConfiguration().set(path, null);
                        plugin.getLocationFileConfiguration().save(plugin.getLocationFile());
                        player.sendMessage(plugin.getPrefix() + "Die Location " + args[0] + " wurde gelöscht.");
                        plugin.getTimeout().put(player, 8);
                    } catch (YAMLException e) {
                        System.err.println(e);
                        player.sendMessage(plugin.getPrefix() + "§cEs gab einen Fehler.");
                    }
                } else
                    player.sendMessage(plugin.getPrefix() + "§c/removeroundrunnerlocation <Location>");
            } else
                player.sendMessage(plugin.getPrefix() + "§cDazu hast du keine Rechte!");
        }
        return false;
    }
}
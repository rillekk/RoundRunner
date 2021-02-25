package net.rillekk.roundrunner.commands;


import lombok.SneakyThrows;

import net.rillekk.roundrunner.RoundRunner;

import org.bukkit.Location;
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


public class SetRoundRunnerLocation implements CommandExecutor {
    private final RoundRunner plugin;
    public SetRoundRunnerLocation(RoundRunner plugin) {
        this.plugin = plugin;
    }
    private String[] controlVariable;


    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location location = player.getLocation();

            if (plugin.getLocationFile().exists() && plugin.getLocationFileConfiguration().contains("Location.1")) {
                if (plugin.getLocationFileConfiguration().contains("Location.")) {
                    String arrayString = "" + plugin.getLocationFileConfiguration().getConfigurationSection("Location.").getKeys(false);
                    this.controlVariable = arrayString.replaceAll("" + "\\[", "").replaceAll("]", "").split(",");
                }
            }

            if (plugin.getLocationFileConfiguration().contains("Location.1")) {
                plugin.getLocationFileConfiguration().set("Location." + (controlVariable.length + 1) + ".world", location.getWorld().getName());
                plugin.getLocationFileConfiguration().set("Location." + (controlVariable.length + 1) + ".x", location.getBlockX());
                plugin.getLocationFileConfiguration().set("Location." + (controlVariable.length + 1) + ".y", location.getBlockY());
                plugin.getLocationFileConfiguration().set("Location." + (controlVariable.length + 1) + ".z", location.getZ());
                plugin.getLocationFileConfiguration().save(plugin.getLocationFile());
                player.sendMessage(plugin.getPrefix() + "Die Location wurde erfolgreich gesetzt.");
                plugin.getTimeout().put(player, 8);
            } else {
                plugin.getLocationFileConfiguration().set("Location." + 1 + ".world", location.getWorld().getName());
                plugin.getLocationFileConfiguration().set("Location." + 1 + ".x", location.getBlockX());
                plugin.getLocationFileConfiguration().set("Location." + 1 + ".y", location.getBlockY());
                plugin.getLocationFileConfiguration().set("Location." + 1 + ".z", location.getZ());
                plugin.getLocationFileConfiguration().save(plugin.getLocationFile());
                player.sendMessage(plugin.getPrefix() + "Die Location wurde erfolgreich gesetzt.");
                plugin.getTimeout().put(player, 8);
            }
        }
        return false;
    }
}
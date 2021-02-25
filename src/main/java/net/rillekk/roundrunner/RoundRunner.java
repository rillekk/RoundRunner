package net.rillekk.roundrunner;


import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import net.rillekk.roundrunner.commands.*;
import net.rillekk.roundrunner.listener.PlayerMoveListener;
import net.rillekk.roundrunner.mysql.MySQL;
import net.rillekk.roundrunner.mysql.MySQLRoundRunner;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class RoundRunner extends JavaPlugin
{
    private static RoundRunner INSTANCE;

    private File mysqlFile;
    private File locationFile;
    private FileConfiguration mysqlFileConfiguration;
    private FileConfiguration locationFileConfiguration;
    private String prefix;
    private HashMap<Player, Integer> roundChecker;
    private MySQL mySQL;
    private MySQLRoundRunner mySQLRoundRunner;
    private HashMap<Player, Integer> timeout;
    private ExecutorService cachedThreadExecutor;


    @SneakyThrows
    @Override
    public void onEnable()
    {
        INSTANCE = this;

        this.prefix = "§8» §6OhneLimit §8× §7";
        this.mysqlFile = new File(getDataFolder(), "mysql.yml");
        this.locationFile = new File(getDataFolder(), "location.yml");
        this.mysqlFileConfiguration = YamlConfiguration.loadConfiguration(mysqlFile);
        this.locationFileConfiguration = YamlConfiguration.loadConfiguration(locationFile);
        this.roundChecker = new HashMap<>();
        this.setMySQL();
        this.mySQL = new MySQL( getMysqlFileConfiguration().getString("Host"),
                                getMysqlFileConfiguration().getString("Port"),
                                getMysqlFileConfiguration().getString("Database"),
                                getMysqlFileConfiguration().getString("Username"),
                                getMysqlFileConfiguration().getString("Password"),
                                this );
        this.mySQLRoundRunner = new MySQLRoundRunner(this);
        this.timeout = new HashMap<>();
        this.cachedThreadExecutor = Executors.newCachedThreadPool();


        super.getCommand("setroundrunnerlocation").setExecutor(new SetRoundRunnerLocation(this));
        super.getCommand("runden").setExecutor(new RundenCommand(this));
        super.getCommand("rundenranking").setExecutor(new RundenRankingCommand(this));
        super.getCommand(("setrunden")).setExecutor((new SetRundenCommand(this)));
        super.getCommand("removeroundrunnerlocation").setExecutor(new RemoveRoundRunnerLocationCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        this.startTimeoutScheduler();
    }

    public void setMySQL() throws IOException
    {
        if (!getMysqlFileConfiguration().contains("Host"))
            getMysqlFileConfiguration().set("Host", "localhost");

        if (!getMysqlFileConfiguration().contains("Port"))
            getMysqlFileConfiguration().set("Port", 3306);

        if (!getMysqlFileConfiguration().contains("Database"))
            getMysqlFileConfiguration().set("Database", "database");

        if (!getMysqlFileConfiguration().contains("Username"))
            getMysqlFileConfiguration().set("Username", "username");

        if (!getMysqlFileConfiguration().contains("Password"))
            getMysqlFileConfiguration().set("Password", "password");

        getMysqlFileConfiguration().save(mysqlFile);
    }

    private void startTimeoutScheduler()
    {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () ->
        {
            for(Map.Entry<Player, Integer> entry : timeout.entrySet())
            {
                if(entry.getValue() == 0)
                    timeout.remove(entry.getKey());
                else
                    timeout.put(entry.getKey(), entry.getValue() - 1);
            }
        },0, 5);
    }
}

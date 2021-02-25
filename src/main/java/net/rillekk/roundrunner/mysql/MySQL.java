package net.rillekk.roundrunner.mysql;


import lombok.Getter;

import net.rillekk.roundrunner.RoundRunner;

import java.sql.*;

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
public class MySQL {
    private final RoundRunner plugin;

    private final String HOST;
    private final String PORT;
    private final String DATABASE;
    private final String USER;
    private final String PASSWORD;

    private Connection con;

    public MySQL(String host, String port, String database, String user, String password, RoundRunner plugin) {
        this.HOST = host;
        this.PORT = port;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;

        this.plugin = plugin;

        connect();
    }

    private void connect() {
        try {
            this.con = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":" + this.PORT + "/" + this.DATABASE + "?autoReconnect=true", this.USER, this.PASSWORD);
            plugin.getLogger().info("Die Verbindung zur MySQL wurde hergestellt!");
        } catch (SQLException e) {
            plugin.getLogger().info("Die Verbindung zur MySQL ist fehlgeschlagen! Fehler: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (this.con != null) {
                this.con.close();
                plugin.getLogger().info(plugin.getPrefix() + "Die Verbindung zur MySQL wurde Erfolgreich beendet!");
            }
        } catch (SQLException e) {
            plugin.getLogger().info(plugin.getPrefix() + "Fehler beim beenden der Verbindung zur MySQL! Fehler: " + e.getMessage());
        }
    }

    public void update(String qry) {
        try (Statement st = this.con.createStatement()) {
            st.executeUpdate(qry);
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public ResultSet query(String qry) {
        ResultSet rs = null;
        try {
            Statement st = this.con.createStatement();
            rs = st.executeQuery(qry);
        } catch (SQLException e) {
            System.err.println(e);
        }
        return rs;
    }


    public Connection getCon() {
        return con;
    }
}
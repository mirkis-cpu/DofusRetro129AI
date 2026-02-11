// 

// 

package org.aestia.database;

import java.sql.Connection;
import org.aestia.kernel.Main;
import com.zaxxer.hikari.HikariConfig;
import org.aestia.database.data.ServerData;
import org.aestia.database.data.PlayerData;
import org.aestia.database.data.AccountData;
import com.zaxxer.hikari.HikariDataSource;

public class Database
{
    private HikariDataSource dataSource;
    private AccountData accountData;
    private PlayerData playerData;
    private ServerData serverData;
    
    public void initializeData() {
        this.accountData = new AccountData(this.dataSource);
        this.playerData = new PlayerData(this.dataSource);
        this.serverData = new ServerData(this.dataSource);
    }
    
    public boolean initializeConnection() {
        final HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.mariadb.jdbc.MySQLDataSource");
        config.addDataSourceProperty("serverName", Main.config.getHost());
        config.addDataSourceProperty("port", Main.config.getPort());
        config.addDataSourceProperty("databaseName", Main.config.getDatabaseName());
        config.addDataSourceProperty("user", Main.config.getUser());
        config.addDataSourceProperty("password", Main.config.getPass());
        this.dataSource = new HikariDataSource(config);
        if (!this.testConnection(this.dataSource)) {
            System.out.println("Pleaz check your username and password and database connection");
            System.exit(0);
        }
        this.initializeData();
        return true;
    }
    
    public HikariDataSource getDataSource() {
        return this.dataSource;
    }
    
    public AccountData getAccountData() {
        return this.accountData;
    }
    
    public PlayerData getPlayerData() {
        return this.playerData;
    }
    
    public ServerData getServerData() {
        return this.serverData;
    }
    
    private boolean testConnection(final HikariDataSource dataSource) {
        try {
            final Connection connection = dataSource.getConnection();
            connection.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

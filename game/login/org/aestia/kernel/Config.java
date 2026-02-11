// 

// 

package org.aestia.kernel;

import com.typesafe.config.ConfigFactory;
import java.io.File;
import org.aestia.exchange.ExchangeServer;
import org.aestia.login.LoginServer;

public class Config
{
    private static final com.typesafe.config.Config configFile;
    public final long startTime;
    private boolean isRunning;
    private LoginServer loginServer;
    private ExchangeServer exchangeServer;
    private String host;
    private String user;
    private String pass;
    private int port;
    private String databaseName;
    private String loginIp;
    private String exchangeIp;
    private String version;
    private int loginPort;
    private int exchangePort;
    
    static {
        configFile = ConfigFactory.parseFile(new File("config.conf"));
    }
    
    public Config() {
        this.startTime = System.currentTimeMillis();
    }
    
    public void initialize() {
        try {
            this.host = Config.configFile.getString("database.host");
            this.user = Config.configFile.getString("database.user");
            this.pass = Config.configFile.getString("database.password");
            this.databaseName = Config.configFile.getString("database.loginName");
            this.port = Config.configFile.getInt("database.port");
            this.loginIp = Config.configFile.getString("network.loginIp");
            this.exchangeIp = Config.configFile.getString("network.exchangeIp");
            this.version = Config.configFile.getString("network.version");
            this.loginPort = Config.configFile.getInt("network.loginPort");
            this.exchangePort = Config.configFile.getInt("network.exchangePort");
        }
        catch (Exception e) {
            System.out.println(" <> Config illisible ou champs manquants: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public com.typesafe.config.Config getConfigFile() {
        return Config.configFile;
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
    
    public void setRunning(final boolean isRunning) {
        this.isRunning = isRunning;
    }
    
    public LoginServer getLoginServer() {
        return this.loginServer;
    }
    
    public void setLoginServer(final LoginServer loginServer) {
        this.loginServer = loginServer;
    }
    
    public ExchangeServer getExchangeServer() {
        return this.exchangeServer;
    }
    
    public void setExchangeServer(final ExchangeServer exchangeServer) {
        this.exchangeServer = exchangeServer;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public String getUser() {
        return this.user;
    }
    
    public String getPass() {
        return this.pass;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public String getDatabaseName() {
        return this.databaseName;
    }
    
    public String getLoginIp() {
        return this.loginIp;
    }
    
    public String getExchangeIp() {
        return this.exchangeIp;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public int getLoginPort() {
        return this.loginPort;
    }
    
    public int getExchangePort() {
        return this.exchangePort;
    }
}

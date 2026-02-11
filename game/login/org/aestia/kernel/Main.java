package org.aestia.kernel;

import org.aestia.login.LoginServer;
import org.aestia.exchange.ExchangeServer;

import java.util.HashMap;
import java.util.Map;

import org.aestia.database.Database;

public class Main
{
    public static Database database = new Database();
    public static Config config = new Config();
    
    
    public static void main(final String[] arg) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Main.exit();
                System.out.println("Le login est a present ferme!");
            }
        });
        start();
    }
    
    public static void start() {
        final Console console = new Console();
        Main.config.initialize();
        System.out.println(EmulatorInfos.HARD_NAME.toString());
        if (!Main.database.initializeConnection()) {
            System.out.println("> Identifiants de connexion invalides");
            System.out.println("> Redemarrage...");
            exit();
            System.exit(0);
        }
        System.err.println("TEST2");
        Main.database.getServerData().load(null);
        Main.config.setExchangeServer(new ExchangeServer());
        Main.config.getExchangeServer().start();
        Main.config.setLoginServer(new LoginServer());
        Main.config.getLoginServer().start();
        System.out.println(" > Lancement du serveur termine : " + (System.currentTimeMillis() - Main.config.startTime) + " ms");
        Main.config.setRunning(true);
        console.initialize();
    }
    
    public static void exit() {
        System.out.println(" <> Fermeture du jeu <>");
        if (Main.config.isRunning()) {
            Main.config.setRunning(false);
            Main.config.getLoginServer().stop();
            Main.config.getExchangeServer().stop();
        }
        System.out.println(" <> Redemmarage <>");
    }
}

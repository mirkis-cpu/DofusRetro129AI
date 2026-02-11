/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ch.qos.logback.classic.Level
 *  ch.qos.logback.classic.Logger
 *  org.slf4j.LoggerFactory
 */
package org.aestia.kernel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.aestia.db.Database;
import org.aestia.exchange.ExchangeClient;
import org.aestia.game.GameServer;
import org.aestia.game.scheduler.WorldSave;
import org.aestia.game.world.World;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class Main {
	public static boolean isRunning = false;
	public static boolean isSaving = false;
	public static boolean modDebug = true;
	public static boolean allowMulePvp = false;
	public static boolean useSubscribe = false;
	public static int startLevel = 1;
	public static int startKamas = 0;
	public static boolean mapAsBlocked = false;
	public static boolean fightAsBlocked = false;
	public static boolean tradeAsBlocked = false;
	public static String key = "jivaa";
	public static int serverId = 1;
	public static int exchangePort = 666;
	public static String exchangeIp = "127.0.0.1";
	public static String loginHostDB = "127.0.0.1";
	public static String loginNameDB = "local";
	public static String loginUserDB = "root";
	public static String loginPassDB = "";
	public static String loginPortDB = "3306";
	public static int gamePort = 5555;
	public static String hostDB = "127.0.0.1";
	public static String nameDB = "";
	public static String userDB = "root";
	public static String passDB = "";
	public static String portDB = "3306";
	public static String Ip = "";
	public static GameServer gameServer;
	public static ExchangeClient exchangeClient;

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				if (Main.isRunning) {
					GameServer.setState(0);
					Main.gameServer.kickAll(false);
					WorldSave.cast(0);
				}
				Main.isRunning = false;
				System.out.println("Le serveur est fermé !");
			}
		});
		System.out.println("Lancement du serveur : "
				+ new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.FRANCE).format(new Date()));
		Main.start();
	}

	public static void start() {
		Console.initialize();
		Config.getInstance().load();
		Database.launchDatabase();
		World.createWorld();
		Config.loadOption();
		new GameServer().initialize();
		new ExchangeClient().initialize();
		if (Config.getInstance().autoReboot) {
			Reboot.getInstance().initialize();
		}
		Console.refreshTitle();
		Console.begin();
		if (!modDebug) {
			Logger root = (Logger) LoggerFactory.getLogger("ROOT");
			root.setLevel(Level.OFF);
		}
	}

	public static void stop() {
		if (!Config.getInstance().script.equalsIgnoreCase("")) {
			Console.println(Main.executeCommand("sh " + Config.getInstance().script), Console.Color.GAME);
		}
		System.exit(0);
	}

	private static String executeCommand(String command) {
		StringBuffer output = new StringBuffer();
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(String.valueOf(line) + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}

}

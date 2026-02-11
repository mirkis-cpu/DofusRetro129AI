/*
 * Decompiled with CFR 0_114.
 */
package org.aestia.kernel;

import java.util.ArrayList;

public class Config {
	public static boolean HEROIC = false;
	public static final Config singleton = new Config();
	public static final int RAM = 600;

	public boolean HALLOWEEN = false;
	public boolean NOEL = false;
	public final long startTime = System.currentTimeMillis();
	public long delayDeblo = 3600000;
	public String NAME;
	public String startMessage = "";
	public String colorMessage = "B9121B";
	public boolean kickIfAfk = false;
	public boolean autoReboot = false;
	public boolean allZaap = false;
	public boolean allEmote = false;
	public boolean onlyLocal = false;
	public int startMap = 0;
	public int startCell = 0;
	public int rateXp = 1;
	public int rateKamas = 1;
	public static int config = 1;
	public int rateDrop = 1;
	public int rateHonor = 1;
	public int rateJob = 1;
	public int rateFm = 1;
	public String PUB1 = "(Message Auto) : Si vous rencontrez un bug n'h\u00e9sitez pas \u00e0 le post\u00e9 sur le forum. Tout report de bug dans les canaux publiques peuvent \u00eatre sanctionn\u00e9 (consid\u00e9rer comme non respect des canaux).";
	public String PUB2 = "(Message Auto) : L'\u00e9quipe est plus disponible sur le forum qu'en jeu, donc n'h\u00e9sitez pas \u00e0 la contacter par message priv\u00e9s sur le forum";
	public String PUB3 = "(Message Auto) : Nous vous invitons \u00e0 voter afin d'\u00eatre bien class\u00e9 dans le classement Rpg-Paradize.";
	public String PUB4 = "(Message Auto) : Les recrutements de Mod\u00e9rateurs sont ouverts. Plus d'informations sur le forum : <a href='http://forum.aestia.fr/'>cliquez ici</a>";
	public String script = "";
	public static ArrayList<Integer> notInHdv = new ArrayList<Integer>();
	public static ArrayList<Integer> arenaMap = new ArrayList<Integer>();
	public static ArrayList<Integer> itemFeedMount = new ArrayList<>();

	public static Config getInstance() {
		return singleton;
	}

	public void set() {
		Main.isRunning = true;
		Main.exchangePort = 451;
		Main.exchangeIp = "127.0.0.1";
		Main.loginHostDB = "127.0.0.1";
		Main.loginNameDB = "aestia_game";
		Main.loginUserDB = "root";
		Main.loginPassDB = "";
		Main.gamePort = 5555;
		Main.hostDB = "127.0.0.1";
		Main.nameDB = "aestia_login";
		Main.userDB = "root";
		Main.passDB = "";
		Main.Ip = "127.0.0.1";
		this.onlyLocal = false;
		Config.getInstance().NAME = "jiva";
		Config.getInstance().kickIfAfk = false;
		Config.getInstance().autoReboot = false;
		Config.getInstance().PUB1 = "(Message Auto) : message 1.";
		Config.getInstance().PUB2 = "(Message Auto) : message 2.";
		Config.getInstance().PUB3 = "(Message Auto) : message 3.";
		Config.getInstance().PUB4 = "(Message Auto) : message 4.";
	}

	public void load() {
		set();
	}

	public static void loadOption() {
		String map = "10131,10132,10133,10134,10135,10136,10137,10138";
		String[] arrstring = map.split("\\,");
		int n = arrstring.length;
		int n2 = 0;
		while (n2 < n) {
			String i = arrstring[n2];
			arenaMap.add(Integer.parseInt(i));
			++n2;
		}
		notInHdv.add(4820);
		itemFeedMount.add(41);
	}

	public static boolean contains(ArrayList<Integer> list, int id0) {
		return list.contains(id0);
	}
}

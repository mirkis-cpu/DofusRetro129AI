/*
 * Decompiled with CFR 0_114.
 */
package org.aestia.db;

import org.aestia.db.game.Dbgame;
import org.aestia.db.statique.Dbstatique;
import org.aestia.kernel.Main;

public class Database {
	private final static Dbgame dbgame = new Dbgame();
	private final static Dbstatique dbstatique = new Dbstatique();

	public static void launchDatabase() {
		if (!dbstatique.initializeConnection() || !dbgame.initializeConnection()) {
			Main.stop();
		}
	}

	public static Dbgame getGame() {
		return dbgame;
	}

	public static Dbstatique getStatique() {
		return dbstatique;
	}
}

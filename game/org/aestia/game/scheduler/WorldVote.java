// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game.scheduler;

import java.util.concurrent.TimeUnit;

import org.aestia.db.Database;
import org.aestia.kernel.Config;
import org.aestia.kernel.Console;

public class WorldVote extends Manageable {
	int threadPool = 10;
	int test = 0;

	@Override
	public void launch() {
		GlobalManager.worldSheduler.scheduleWithFixedDelay(this, 7L, 7L, TimeUnit.MINUTES);
	}

	@Override
	public void run() {
		int currentMemory = (((int) (Runtime.getRuntime().totalMemory() / 1024) / 1024))
				- (((int) (Runtime.getRuntime().freeMemory() / 1024) / 1024));
		System.err.println("Verification de la memoire...(" + currentMemory + ")");

		if (currentMemory > Config.RAM) {
			// GlobalManager.worldSheduler =
			// Executors.newScheduledThreadPool(5);
			threadPool = 5;
			test = 0;
			System.err.println(
					"La memoire utilisé depasse les " + Config.RAM + " Mb, utilisation de 5 Threads au lieu de 10");
		} else {
			if (threadPool == 5 && test > 2) {
				// GlobalManager.worldSheduler =
				// Executors.newScheduledThreadPool(10);
				System.err.println("La memoire utilisé ne depasse plus les " + Config.RAM
						+ " Mb, reutilisation de 10 Threads au lieu de 5");
				test = 0;
			} else {
				System.err.println("Memoire : OK");
			}
			test++;
		}

		System.err.println(Thread.activeCount() + " threads actifs");

		try {
			Database.getStatique().getAccountData().updateVoteAll();
		} catch (Exception e) {
			e.printStackTrace();
			Console.println("Erreur du WorldVote.", Console.Color.ERROR);
			return;
		} finally {
			Console.println("Actualisation des heures de votes.", Console.Color.GAME);
		}
		Console.println("Actualisation des heures de votes.", Console.Color.GAME);
	}
}

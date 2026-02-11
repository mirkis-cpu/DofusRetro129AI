// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class GlobalManager {
	public static final ScheduledExecutorService worldSheduler = Executors.newScheduledThreadPool(10, new ThreadFactory() {
		@Override
		public Thread newThread(Runnable arg0) {
			Thread thread = new Thread(arg0) {{
				setDaemon(true);
			}};
			return thread;
		}
		
	});

	public static void loadWorldScheduler() {
		new WorldMoveEntity().launch();
		new WorldPub().launch();
		new WorldSave().launch();
		new WorldVote().launch();
	}

}

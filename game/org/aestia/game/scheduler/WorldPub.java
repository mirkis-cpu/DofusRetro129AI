// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game.scheduler;

import java.util.concurrent.TimeUnit;

import org.aestia.common.Formulas;
import org.aestia.common.SocketManager;
import org.aestia.kernel.Config;

public class WorldPub extends Manageable {
	@Override
	public void run() {
		switch (Formulas.getRandomValue(1, 4)) {
		case 1: {
			if (!Config.getInstance().PUB1.isEmpty()) {
				SocketManager.GAME_SEND_MESSAGE_TO_ALL(Config.getInstance().PUB1, "046380");
				break;
			}
			break;
		}
		case 2: {
			if (!Config.getInstance().PUB2.isEmpty()) {
				SocketManager.GAME_SEND_MESSAGE_TO_ALL(Config.getInstance().PUB2, "046380");
				break;
			}
			break;
		}
		case 3: {
			if (!Config.getInstance().PUB3.isEmpty()) {
				SocketManager.GAME_SEND_MESSAGE_TO_ALL(Config.getInstance().PUB3, "046380");
				break;
			}
			break;
		}
		case 4: {
			if (!Config.getInstance().PUB4.isEmpty()) {
				SocketManager.GAME_SEND_MESSAGE_TO_ALL(Config.getInstance().PUB4, "046380");
				break;
			}
			break;
		}
		}
	}

	@Override
	public void launch() {
		if (!Config.getInstance().PUB1.isEmpty() && !Config.getInstance().PUB2.isEmpty()
				&& !Config.getInstance().PUB3.isEmpty() && !Config.getInstance().PUB4.isEmpty()) {
			GlobalManager.worldSheduler.scheduleWithFixedDelay(this, 15L, 15L, TimeUnit.MINUTES);
		}
	}
}

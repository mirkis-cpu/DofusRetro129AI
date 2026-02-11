// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.kernel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.aestia.client.Player;
import org.aestia.game.scheduler.WorldSave;
import org.aestia.game.world.World;

public class Reboot extends Timer {
	private static Reboot instance;
	private int restantHour;
	private int restantMinute;

	public void initialize() {
		this.check();
		this.schedule(new TimerTask() {
			@Override
			public void run() {
				if (Reboot.this.check()) {
					if (System.currentTimeMillis() - Config.getInstance().startTime < 300000L) {
						return;
					}
					WorldSave.cast(0);
					for (final Player player : World.getOnlinePersos()) {
						player.send(this.toString());
					}
					Main.stop();
				}
			}
		}, 30000L, 30000L);
	}

	public static Reboot getInstance() {
		if (Reboot.instance == null) {
			Reboot.instance = new Reboot();
		}
		return Reboot.instance;
	}

	public boolean check() {
		final Date date = Calendar.getInstance().getTime();
		final int actualHour = Integer.parseInt(new SimpleDateFormat("HH").format(date));
		final int actualMinute = Integer.parseInt(new SimpleDateFormat("mm").format(date));
		final int total = actualHour * 60 + actualMinute;
		final double restant = 1440 - (total - 300);
		final int hour = (int) (restant / 60.0);
		final int minute = (int) ((restant / 60.0 - hour) * 60.0);
		this.restantHour = hour;
		this.restantMinute = minute;
		switch (actualHour) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4: {
			this.restantHour -= 24;
			break;
		}
		}
		return (hour == 0 && minute == 0) || (actualHour == 4 && actualMinute == 59)
				|| (actualHour == 5 && actualMinute == 0) || (actualHour == 5 && actualMinute == 1);
	}

	@Override
	public String toString() {
		String im = "Im115;";
		if (this.restantHour == 0) {
			im = String.valueOf(im) + this.restantMinute + ((this.restantMinute > 1) ? " minutes" : " minute");
		} else {
			im = String.valueOf(im) + this.restantHour + ((this.restantHour > 1) ? " heures et " : " heure et ");
			im = String.valueOf(im) + this.restantMinute + ((this.restantMinute > 1) ? " minutes" : " minute");
		}
		return im;
	}
}

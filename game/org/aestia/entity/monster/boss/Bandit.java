// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity.monster.boss;

import java.util.ArrayList;

import org.aestia.common.Formulas;
import org.aestia.db.Database;
import org.aestia.entity.monster.Monster;
import org.aestia.game.scheduler.TimerWaiter;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.map.Map;

public class Bandit {
	private static Bandit bandits;
	private ArrayList<Monster> monstres;
	private ArrayList<Map> maps;
	private long time;
	private boolean isPop;
	private TimerWaiter waiter;

	public Bandit(final String mobs, final String maps, final long time) {
		this.monstres = new ArrayList<Monster>();
		this.maps = new ArrayList<Map>();
		this.isPop = false;
		this.waiter = new TimerWaiter();
		if (!mobs.equalsIgnoreCase("")) {
			String[] split;
			for (int length = (split = mobs.split(",")).length, i = 0; i < length; ++i) {
				final String mob = split[i];
				Integer _mob = null;
				try {
					_mob = Integer.parseInt(mob);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (_mob != null) {
					final Monster monstre = World.getMonstre(_mob);
					if (monstre != null) {
						this.monstres.add(monstre);
					}
				}
			}
		}
		if (!maps.equalsIgnoreCase("")) {
			String[] split2;
			for (int length2 = (split2 = maps.split(",")).length, j = 0; j < length2; ++j) {
				final String map = split2[j];
				Short _map = null;
				try {
					_map = Short.parseShort(map);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (_map != null) {
					final Map _Map = World.getMap(_map);
					if (_Map != null) {
						this.maps.add(_Map);
					}
				}
			}
		}
		this.time = time;
		Bandit.bandits = this;
		run();
	}

	public ArrayList<Monster> getMonstres() {
		return this.monstres;
	}

	public ArrayList<Map> getMaps() {
		return this.maps;
	}

	public long getTime() {
		return this.time;
	}

	public void setTime(final long time) {
		this.time = time;
	}

	public TimerWaiter getWaiter() {
		return this.waiter;
	}

	public boolean isPop() {
		return this.isPop;
	}

	public void setPop(final boolean isPop) {
		this.isPop = isPop;
	}

	public static Bandit getBandits() {
		return Bandit.bandits;
	}

	public static void run() {
		final Bandit bandit = getBandits();
		if (bandit.isPop) {
			bandit.getWaiter().addNext(new Runnable() {
				@Override
				public void run() {
					Bandit.run();
				}
			}, 3600000L);
		} else {
			final long time = bandit.getTime();
			final long actuel = System.currentTimeMillis();
			if (time <= 0L) {
				pop(bandit, actuel);
			} else {
				final int random = Formulas.getRandomValue(6, 18);
				final long timeRandom = 3600000 * random;
				if (time + timeRandom <= actuel) {
					pop(bandit, actuel);
				} else {
					bandit.getWaiter().addNext(new Runnable() {
						@Override
						public void run() {
							Bandit.run();
						}
					}, 3600000L);
				}
			}
		}
	}

	public static void pop(final Bandit bandit, final long actuel) {
		try {
			bandit.setTime(actuel);
			final int nbMap = bandit.getMaps().size();
			final int random = Formulas.getRandomValue(0, nbMap - 1);
			final Map map = bandit.getMaps().get(random);
			String groupData = "";
			for (final Monster monstre : bandit.getMonstres()) {
				final Integer id = monstre.getId();
				Integer lvl;
				for (lvl = monstre.getRandomGrade().getLevel(); lvl == null; lvl = monstre.getRandomGrade()
						.getLevel()) {
				}
				if (groupData.equalsIgnoreCase("")) {
					groupData = id + "," + lvl + "," + lvl;
				} else {
					groupData = String.valueOf(groupData) + ";" + id + "," + lvl + "," + lvl;
				}
			}
			Console.println("Ajout du groupe de bandit sur la map " + map.getId() + ".", Console.Color.GAME);
			final Map map2 = map;
			++map2.nextObjectId;
			map.spawnNewGroup(false, map.getRandomFreeCellId(false), groupData, "");
			Database.getGame().getBanditData().update(bandit);
		} catch (Exception e) {
			e.printStackTrace();
			bandit.getWaiter().addNext(new Runnable() {
				@Override
				public void run() {
					Bandit.pop(bandit, actuel);
				}
			}, 60000L);
		}
	}
}

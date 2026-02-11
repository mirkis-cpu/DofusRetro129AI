// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity.monster.boss;

import java.util.ArrayList;
import java.util.Random;

import org.aestia.game.world.World;
import org.aestia.map.Map;

public class MaitreCorbac {
	private short oldMap;
	private short map;

	public MaitreCorbac() {
		this.repop((short) (-1));
	}

	public void repop(final short id) {
		if (this.oldMap == id) {
			return;
		}
		this.oldMap = id;
		final ArrayList<Map> maps = new ArrayList<Map>();
		maps.addAll(World.getSubArea(211).getMaps());
		maps.remove(World.getMap((short) 9589));
		maps.remove(World.getMap((short) 9604));
		int index;
		Map map;
		for (index = new Random().nextInt(maps.size()), map = maps.get(index); map.getId() == id; map = maps
				.get(index)) {
			index = new Random().nextInt(maps.size());
		}
		this.map = map.getId();
		map.spawnGroupOnCommand(map.getRandomFreeCellId(false), "289,120,200;825,90,98;823,90,98;824,80,88", true);
	}

	public int check() {
		switch (this.map) {
		case 9590:
		case 9594:
		case 9596:
		case 9600: {
			return 3188;
		}
		case 9592:
		case 9593:
		case 9597:
		case 9598: {
			return 3193;
		}
		case 9591:
		case 9595:
		case 9599:
		case 9603: {
			return 3191;
		}
		case 9601:
		case 9602:
		case 9723:
		case 9724: {
			return 3194;
		}
		default: {
			return -1;
		}
		}
	}
}

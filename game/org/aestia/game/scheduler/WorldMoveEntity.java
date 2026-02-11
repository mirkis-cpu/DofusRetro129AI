// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game.scheduler;

import java.util.concurrent.TimeUnit;

import org.aestia.game.world.World;
import org.aestia.map.Map;

public class WorldMoveEntity extends Manageable {
	@Override
	public void run() {
		for (final Map map : World.getAllMaps()) {
			map.onMapMonstersDisplacement();
			if (map.getMountPark() != null) {
				map.getMountPark().startMoveMounts();
			}
			if (map.getNpcs() != null) {
				map.onMapNpcDisplacement(false);
			}
		}
	}

	@Override
	public void launch() {
		GlobalManager.worldSheduler.scheduleWithFixedDelay(this, 30L, 30L, TimeUnit.MINUTES);
	}
}

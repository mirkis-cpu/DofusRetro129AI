package org.aestia.kernel;

import java.util.ArrayList;
import java.util.List;

import org.aestia.client.Player;
import org.aestia.client.other.Stats;
import org.aestia.common.SocketManager;
import org.aestia.game.world.World;
import org.aestia.object.ObjectTemplate;

public class Boutique {
	public static final List<ObjectTemplate> items = new ArrayList<>();
	private static String packet;

	public static void initPacket() {
		packet = getObjectList();
	}

	public static void open(Player player) {
		player.boutique = true;
		SocketManager.send(player, "ECK0|1");
		SocketManager.send(player, "EL" + packet);
	}

	private static String getObjectList() {
		StringBuilder items = new StringBuilder();
		for (ObjectTemplate obj : Boutique.items) {
			Stats stats = obj.generateNewStatsFromTemplate(obj.getStrTemplate(), true);
			items.append(obj.getId() + ";" + stats.parseToItemSetStats()).append("|");

		}
		return items.toString();
	}
}

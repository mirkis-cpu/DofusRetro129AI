// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.dynamic;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Account;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.game.world.World;

public class Migration {
	public static Map<Integer, Migration> migrations;
	private int account;
	private ArrayList<Integer> servers;
	private Map<Integer, ArrayList<Integer>> players;
	private StringBuilder packet;

	static {
		Migration.migrations = new TreeMap<Integer, Migration>();
	}

	public Migration(final int account) {
		this.servers = new ArrayList<Integer>();
		this.players = new TreeMap<Integer, ArrayList<Integer>>();
		this.account = account;
		String[] split;
		for (int length = (split = Database.getStatique().getPlayerData().haveOtherPlayer(account)
				.split("\\,")).length, j = 0; j < length; ++j) {
			final String i = split[j];
			if (!this.servers.contains(Integer.parseInt(i))) {
				this.servers.add(Integer.parseInt(i));
			}
		}
		Migration.migrations.put(account, this);
	}

	public Map<Integer, ArrayList<Integer>> getPlayers() {
		return this.players;
	}

	public void add(final int server, final String packet) {
		if (this.packet == null) {
			this.packet = new StringBuilder();
		}
		this.packet.append(packet);
		this.servers.remove((Object) server);
		if (this.servers.isEmpty()) {
			final Account account = World.getCompte(this.account);
			final StringBuilder AM = new StringBuilder("AM!");
			this.parseAM();
			AM.append(account.getSubscribeRemaining()).append("|").append(account.getPersos().size())
					.append(this.packet.toString());
			SocketManager.send(account.getGameClient(), AM.toString());
			this.packet = null;
		}
	}

	public int search(final int player) {
		for (final Map.Entry<Integer, ArrayList<Integer>> entry : this.players.entrySet()) {
			for (final int search : entry.getValue()) {
				if (search == player) {
					return entry.getKey();
				}
			}
		}
		return -1;
	}

	private void parseAM() {
		final String[] players = this.packet.toString().substring(1).split("\\|");
		String[] array2;
		for (int length = (array2 = players).length, i = 0; i < length; ++i) {
			final String player = array2[i];
			final String[] data = player.split("\\;");
			final int server = Integer.parseInt(data[9]);
			ArrayList<Integer> array = this.players.get(server);
			if (array == null) {
				array = new ArrayList<Integer>();
				array.add(Integer.parseInt(data[0]));
				this.players.put(server, array);
			} else {
				this.players.get(server).add(Integer.parseInt(data[0]));
			}
		}
	}
}

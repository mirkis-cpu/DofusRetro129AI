// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity.exchange;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.entity.PetEntry;
import org.aestia.entity.npc.NpcTemplate;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.object.ObjectTemplate;

public class PlayerExchange extends Exchange {
	public PlayerExchange(final Player player1, final Player player2) {
		super(player1, player2);
	}

	private boolean isPodsOK(final byte i) {
		if (this instanceof CraftSecure) {
			return true;
		}
		int newpods = 0;
		int oldpods = 0;
		if (i == 1) {
			final int podsmax = this.player1.getMaxPod();
			final int pods = this.player1.getPodUsed();
			for (final World.Couple<Integer, Integer> couple : this.items2) {
				if (couple.second == 0) {
					continue;
				}
				final org.aestia.object.Object obj = World.getObjet(couple.first);
				newpods += obj.getTemplate().getPod() * couple.second;
			}
			if (newpods == 0) {
				return true;
			}
			for (final World.Couple<Integer, Integer> couple : this.items1) {
				if (couple.second == 0) {
					continue;
				}
				final org.aestia.object.Object obj = World.getObjet(couple.first);
				oldpods += obj.getTemplate().getPod() * couple.second;
			}
			if (newpods + pods - oldpods > podsmax) {
				SocketManager.GAME_SEND_Im_PACKET(this.player1, "170");
				return false;
			}
		} else {
			final int podsmax = this.player2.getMaxPod();
			final int pods = this.player2.getPodUsed();
			for (final World.Couple<Integer, Integer> couple : this.items1) {
				if (couple.second == 0) {
					continue;
				}
				final org.aestia.object.Object obj = World.getObjet(couple.first);
				newpods += obj.getTemplate().getPod() * couple.second;
			}
			if (newpods == 0) {
				return true;
			}
			for (final World.Couple<Integer, Integer> couple : this.items2) {
				if (couple.second == 0) {
					continue;
				}
				final org.aestia.object.Object obj = World.getObjet(couple.first);
				oldpods += obj.getTemplate().getPod() * couple.second;
			}
			if (newpods + pods - oldpods > podsmax) {
				SocketManager.GAME_SEND_Im_PACKET(this.player2, "170");
				return false;
			}
		}
		return true;
	}

	public synchronized long getKamas(final int guid) {
		int i = 0;
		if (this.player1.getId() == guid) {
			i = 1;
		} else if (this.player2.getId() == guid) {
			i = 2;
		}
		if (i == 1) {
			return this.kamas1;
		}
		if (i == 2) {
			return this.kamas2;
		}
		return 0L;
	}

	public synchronized boolean toogleOk(final int guid) {
		final byte i = (byte) ((this.player1.getId() == guid) ? 1 : 2);
		if (this.isPodsOK(i)) {
			if (i == 1) {
				this.ok1 = !this.ok1;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok1, guid);
				SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok1, guid);
			} else if (i == 2) {
				this.ok2 = !this.ok2;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok2, guid);
				SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok2, guid);
			}
			return this.ok1 && this.ok2;
		}
		return false;
	}

	public synchronized void setKamas(final int guid, final long k) {
		this.ok1 = false;
		this.ok2 = false;
		int i = 0;
		if (this.player1.getId() == guid) {
			i = 1;
		} else if (this.player2.getId() == guid) {
			i = 2;
		}
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok2, this.player2.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok2, this.player2.getId());
		if (k < 0L) {
			return;
		}
		if (i == 1) {
			this.kamas1 = k;
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.player1, 'G', "",
					new StringBuilder(String.valueOf(k)).toString());
			SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.player2.getGameClient(), 'G', "",
					new StringBuilder(String.valueOf(k)).toString());
		} else if (i == 2) {
			this.kamas2 = k;
			SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.player1.getGameClient(), 'G', "",
					new StringBuilder(String.valueOf(k)).toString());
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.player2, 'G', "",
					new StringBuilder(String.valueOf(k)).toString());
		}
	}

	@Override
	public synchronized void cancel() {
		if (this.player1.getAccount() != null && this.player1.getGameClient() != null) {
			SocketManager.GAME_SEND_EV_PACKET(this.player1.getGameClient());
		}
		if (this.player2.getAccount() != null && this.player2.getGameClient() != null) {
			SocketManager.GAME_SEND_EV_PACKET(this.player2.getGameClient());
		}
		this.player1.set_isTradingWith(0);
		this.player2.set_isTradingWith(0);
		this.player1.setCurExchange(null);
		this.player2.setCurExchange(null);
	}

	@Override
	public synchronized void apply() {
		String str = "";
		World.Couple<Integer, Integer> actuel = null;
		try {
			str = String.valueOf(str) + this.player1.getName() + " : ";
			final Iterator<World.Couple<Integer, Integer>> iterator = this.items1.iterator();
			while (iterator.hasNext()) {
				final World.Couple<Integer, Integer> couple1 = actuel = iterator.next();
				str = String.valueOf(str) + ", [" + World.getObjet(couple1.first).getTemplate().getId() + "@"
						+ couple1.first + ";" + couple1.second + "]";
			}
			str = String.valueOf(str) + " avec " + this.kamas1 + " K.\n";
		} catch (Exception e) {
			if (actuel != null) {
				Console.println("!URGENT! Erreur avec l'item d'id " + actuel.first + ".", Console.Color.ERROR);
			}
			e.printStackTrace();
		}
		try {
			str = String.valueOf(str) + "Avec " + this.player2.getName();
			final Iterator<World.Couple<Integer, Integer>> iterator2 = this.items2.iterator();
			while (iterator2.hasNext()) {
				final World.Couple<Integer, Integer> couple2 = actuel = iterator2.next();
				str = String.valueOf(str) + ", [" + World.getObjet(couple2.first).getTemplate().getId() + "@"
						+ couple2.first + ";" + couple2.second + "]";
			}
			str = String.valueOf(str) + " avec " + this.kamas2 + " K.";
		} catch (Exception e) {
			if (actuel != null) {
				Console.println("!URGENT! Erreur avec l'item d'id " + actuel.first + ".", Console.Color.ERROR);
			}
			e.printStackTrace();
		}
		this.player1.addKamas(-this.kamas1 + this.kamas2);
		this.player2.addKamas(-this.kamas2 + this.kamas1);
		for (final World.Couple<Integer, Integer> couple3 : this.items1) {
			if (couple3.second == 0) {
				continue;
			}
			if (World.getObjet(couple3.first) == null) {
				continue;
			}
			if (World.getObjet(couple3.first).getPosition() != -1) {
				continue;
			}
			if (!this.player1.hasItemGuid(couple3.first)) {
				couple3.second = 0;
			} else {
				final org.aestia.object.Object obj = World.getObjet(couple3.first);
				if (obj.getQuantity() - couple3.second < 1) {
					this.player1.removeItem(couple3.first);
					couple3.second = obj.getQuantity();
					SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.player1, couple3.first);
					if (this.player2.addObjet(obj, true)) {
						continue;
					}
					World.removeItem(couple3.first);
				} else {
					obj.setQuantity(obj.getQuantity() - couple3.second);
					SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player1, obj);
					final org.aestia.object.Object newObj = org.aestia.object.Object.getCloneObjet(obj, couple3.second);
					if (!this.player2.addObjet(newObj, true)) {
						continue;
					}
					World.addObjet(newObj, true);
				}
			}
		}
		for (final World.Couple<Integer, Integer> couple3 : this.items2) {
			if (couple3.second == 0) {
				continue;
			}
			if (World.getObjet(couple3.first) == null) {
				continue;
			}
			if (World.getObjet(couple3.first).getPosition() != -1) {
				continue;
			}
			if (!this.player2.hasItemGuid(couple3.first)) {
				couple3.second = 0;
			} else {
				final org.aestia.object.Object obj = World.getObjet(couple3.first);
				if (obj.getQuantity() - couple3.second < 1) {
					this.player2.removeItem(couple3.first);
					couple3.second = obj.getQuantity();
					SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.player2, couple3.first);
					if (this.player1.addObjet(obj, true)) {
						continue;
					}
					World.removeItem(couple3.first);
				} else {
					obj.setQuantity(obj.getQuantity() - couple3.second);
					SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player2, obj);
					final org.aestia.object.Object newObj = org.aestia.object.Object.getCloneObjet(obj, couple3.second);
					if (!this.player1.addObjet(newObj, true)) {
						continue;
					}
					World.addObjet(newObj, true);
				}
			}
		}
		this.player1.set_isTradingWith(0);
		this.player2.set_isTradingWith(0);
		this.player1.setCurExchange(null);
		this.player2.setCurExchange(null);
		SocketManager.GAME_SEND_Ow_PACKET(this.player1);
		SocketManager.GAME_SEND_Ow_PACKET(this.player2);
		SocketManager.GAME_SEND_STATS_PACKET(this.player1);
		SocketManager.GAME_SEND_STATS_PACKET(this.player2);
		SocketManager.GAME_SEND_EXCHANGE_VALID(this.player1.getGameClient(), 'a');
		SocketManager.GAME_SEND_EXCHANGE_VALID(this.player2.getGameClient(), 'a');
		Database.getStatique().getPlayerData().update(this.player1, true);
		Database.getStatique().getPlayerData().update(this.player2, true);
	}

	public synchronized void addItem(final int guid, int qua, final int pguid) {
		this.ok1 = false;
		this.ok2 = false;
		final org.aestia.object.Object obj = World.getObjet(guid);
		int i = 0;
		if (this.player1.getId() == pguid) {
			i = 1;
		}
		if (this.player2.getId() == pguid) {
			i = 2;
		}
		if (qua == 1) {
			qua = 1;
		}
		final String str = String.valueOf(guid) + "|" + qua;
		if (obj == null) {
			return;
		}
		if (obj.getPosition() != -1) {
			return;
		}
		if (this instanceof CraftSecure) {
			final ArrayList<ObjectTemplate> tmp = new ArrayList<ObjectTemplate>();
			for (final World.Couple<Integer, Integer> couple : this.items1) {
				final org.aestia.object.Object _tmp = World.getObjet(couple.first);
				if (_tmp == null) {
					continue;
				}
				if (tmp.contains(_tmp.getTemplate())) {
					continue;
				}
				tmp.add(_tmp.getTemplate());
			}
			for (final World.Couple<Integer, Integer> couple : this.items2) {
				final org.aestia.object.Object _tmp = World.getObjet(couple.first);
				if (_tmp == null) {
					continue;
				}
				if (tmp.contains(_tmp.getTemplate())) {
					continue;
				}
				tmp.add(_tmp.getTemplate());
			}
			if (!tmp.contains(obj.getTemplate()) && tmp.size() + 1 > ((CraftSecure) this).getMaxCase()) {
				SocketManager.GAME_SEND_MESSAGE((this.player1.getId() == pguid) ? this.player1 : this.player2,
						"Impossible d'ajouter plus d'ingr\u00e9dients.", "B9121B");
				return;
			}
		}
		final String add = "|" + obj.getTemplate().getId() + "|" + obj.parseStatsString();
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok2, this.player2.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok2, this.player2.getId());
		if (i == 1) {
			final World.Couple<Integer, Integer> couple = Exchange.getCoupleInList(this.items1, guid);
			if (couple != null) {
				final World.Couple<Integer, Integer> couple2 = couple;
				couple2.second += qua;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.player1, 'O', "+", guid + "|" + couple.second);
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.player2.getGameClient(), 'O', "+",
						guid + "|" + couple.second + add);
				return;
			}
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.player1, 'O', "+", str);
			SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.player2.getGameClient(), 'O', "+",
					String.valueOf(str) + add);
			this.items1.add(new World.Couple<Integer, Integer>(guid, qua));
		} else if (i == 2) {
			final World.Couple<Integer, Integer> couple = Exchange.getCoupleInList(this.items2, guid);
			if (couple != null) {
				final World.Couple<Integer, Integer> couple3 = couple;
				couple3.second += qua;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.player2, 'O', "+", guid + "|" + couple.second);
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.player1.getGameClient(), 'O', "+",
						guid + "|" + couple.second + add);
				return;
			}
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.player2, 'O', "+", str);
			SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.player1.getGameClient(), 'O', "+",
					String.valueOf(str) + add);
			this.items2.add(new World.Couple<Integer, Integer>(guid, qua));
		}
	}

	public synchronized void removeItem(final int guid, final int qua, final int pguid) {
		int i = 0;
		if (this.player1.getId() == pguid) {
			i = 1;
		} else if (this.player2.getId() == pguid) {
			i = 2;
		}
		this.ok1 = false;
		this.ok2 = false;
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok2, this.player2.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok2, this.player2.getId());
		final org.aestia.object.Object obj = World.getObjet(guid);
		if (obj == null) {
			return;
		}
		final String add = "|" + obj.getTemplate().getId() + "|" + obj.parseStatsString();
		if (i == 1) {
			final World.Couple<Integer, Integer> couple = Exchange.getCoupleInList(this.items1, guid);
			final int newQua = couple.second - qua;
			if (newQua < 1) {
				this.items1.remove(couple);
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.player1, 'O', "-",
						new StringBuilder().append(guid).toString());
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.player2.getGameClient(), 'O', "-",
						new StringBuilder().append(guid).toString());
			} else {
				couple.second = newQua;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.player1, 'O', "+", guid + "|" + newQua);
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.player2.getGameClient(), 'O', "+",
						guid + "|" + newQua + add);
			}
		} else if (i == 2) {
			final World.Couple<Integer, Integer> couple = Exchange.getCoupleInList(this.items2, guid);
			final int newQua = couple.second - qua;
			if (newQua < 1) {
				this.items2.remove(couple);
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.player1.getGameClient(), 'O', "-",
						new StringBuilder().append(guid).toString());
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.player2, 'O', "-",
						new StringBuilder().append(guid).toString());
			} else {
				couple.second = newQua;
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.player1.getGameClient(), 'O', "+",
						guid + "|" + newQua + add);
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.player2, 'O', "+", guid + "|" + newQua);
			}
		}
	}

	public synchronized int getQuaItem(final int itemID, final int playerGuid) {
		ArrayList<World.Couple<Integer, Integer>> items;
		if (this.player1.getId() == playerGuid) {
			items = this.items1;
		} else {
			items = this.items2;
		}
		for (final World.Couple<Integer, Integer> curCoupl : items) {
			if (curCoupl.first == itemID) {
				return curCoupl.second;
			}
		}
		return 0;
	}

	public static class NpcExchange {
		private Player perso;
		private NpcTemplate npc;
		private long kamas1;
		private long kamas2;
		private ArrayList<World.Couple<Integer, Integer>> items1;
		private ArrayList<World.Couple<Integer, Integer>> items2;
		private boolean ok1;
		private boolean ok2;

		public NpcExchange(final Player p, final NpcTemplate n) {
			this.kamas1 = 0L;
			this.kamas2 = 0L;
			this.items1 = new ArrayList<World.Couple<Integer, Integer>>();
			this.items2 = new ArrayList<World.Couple<Integer, Integer>>();
			this.perso = p;
			this.setNpc(n);
		}

		public synchronized long getKamas(final boolean b) {
			if (b) {
				return this.kamas2;
			}
			return this.kamas1;
		}

		public synchronized void toogleOK(final boolean paramBoolean) {
			if (paramBoolean) {
				this.ok2 = !this.ok2;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			} else {
				this.ok1 = !this.ok1;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			}
			if (this.ok2 && this.ok1) {
				this.apply();
			}
		}

		public synchronized void setKamas(final boolean paramBoolean, final long paramLong) {
			if (paramLong < 0L) {
				return;
			}
			final boolean b = false;
			this.ok1 = b;
			this.ok2 = b;
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			if (paramBoolean) {
				this.kamas2 = paramLong;
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'G', "",
						new StringBuilder(String.valueOf(paramLong)).toString());
				return;
			}
			if (paramLong > this.perso.get_kamas()) {
				return;
			}
			this.kamas1 = paramLong;
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'G', "",
					new StringBuilder(String.valueOf(paramLong)).toString());
		}

		public synchronized void cancel() {
			if (this.perso.getAccount() != null && this.perso.getGameClient() != null) {
				SocketManager.GAME_SEND_EV_PACKET(this.perso.getGameClient());
			}
			this.perso.set_isTradingWith(0);
			this.perso.setCurNpcExchange(null);
		}

		public synchronized void apply() {
			for (final World.Couple<Integer, Integer> couple : this.items1) {
				if (couple.second == 0) {
					continue;
				}
				if (World.getObjet(couple.first).getPosition() != -1) {
					continue;
				}
				if (!this.perso.hasItemGuid(couple.first)) {
					couple.second = 0;
				} else {
					final org.aestia.object.Object obj = World.getObjet(couple.first);
					if (obj.getQuantity() - couple.second < 1) {
						this.perso.removeItem(couple.first);
						World.removeItem(World.getObjet(couple.first).getGuid());
						couple.second = obj.getQuantity();
						SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.perso, couple.first);
					} else {
						obj.setQuantity(obj.getQuantity() - couple.second);
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj);
					}
				}
			}
			for (final World.Couple<Integer, Integer> couple2 : this.items2) {
				if (couple2.second == 0) {
					continue;
				}
				if (World.getObjTemplate(couple2.first) == null) {
					continue;
				}
				final org.aestia.object.Object obj2 = World.getObjTemplate(couple2.first).createNewItem(couple2.second,
						false);
				if (this.perso.addObjet(obj2, true)) {
					World.addObjet(obj2, true);
				}
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "021;" + couple2.second + "~" + couple2.first);
			}
			this.perso.set_isTradingWith(0);
			this.perso.setCurNpcExchange(null);
			SocketManager.GAME_SEND_EXCHANGE_VALID(this.perso.getGameClient(), 'a');
			SocketManager.GAME_SEND_Ow_PACKET(this.perso);
			Database.getStatique().getPlayerData().update(this.perso, true);
		}

		public synchronized void addItem(final int obj, final int qua) {
			if (qua <= 0) {
				return;
			}
			if (World.getObjet(obj) == null) {
				return;
			}
			final boolean b = false;
			this.ok2 = b;
			this.ok1 = b;
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			final String str = String.valueOf(obj) + "|" + qua;
			final World.Couple<Integer, Integer> couple = this.getCoupleInList(this.items1, obj);
			if (couple != null) {
				final World.Couple<Integer, Integer> couple2 = couple;
				couple2.second += qua;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+", obj + "|" + couple.second);
				if (!this.getNpc().verifItemGet(obj)) {
					this.clearNpcItems();
					if (this.ok2) {
						this.ok2 = false;
						SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
					}
				}
				this.putAllGiveItem(this.getNpc().haveAllGetObject(this.items1));
				return;
			}
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+", str);
			this.items1.add(new World.Couple<Integer, Integer>(obj, qua));
			if (!this.getNpc().verifItemGet(obj)) {
				this.clearNpcItems();
				if (this.ok2) {
					this.ok2 = false;
					SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
				}
			}
			this.putAllGiveItem(this.getNpc().haveAllGetObject(this.items1));
		}

		public synchronized void removeItem(final int guid, final int qua) {
			if (qua < 0) {
				return;
			}
			final boolean b = false;
			this.ok2 = b;
			this.ok1 = b;
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			if (World.getObjet(guid) == null) {
				return;
			}
			final World.Couple<Integer, Integer> couple = this.getCoupleInList(this.items1, guid);
			final int newQua = couple.second - qua;
			if (newQua < 1) {
				this.items1.remove(couple);
				if (!this.getNpc().verifItemGet(guid)) {
					this.clearNpcItems();
					if (this.ok2) {
						this.ok2 = false;
						SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
					}
				}
				this.putAllGiveItem(this.getNpc().haveAllGetObject(this.items1));
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "-",
						new StringBuilder().append(guid).toString());
			} else {
				couple.second = newQua;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+", guid + "|" + newQua);
				if (!this.getNpc().verifItemGet(guid)) {
					this.clearNpcItems();
					if (this.ok2) {
						this.ok2 = false;
						SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
					}
				}
				this.putAllGiveItem(this.getNpc().haveAllGetObject(this.items1));
			}
		}

		private synchronized World.Couple<Integer, Integer> a(
				final ArrayList<World.Couple<Integer, Integer>> paramArrayList, final int obj) {
			return null;
		}

		public synchronized int getQuaItem(final int obj, final boolean b) {
			ArrayList<World.Couple<Integer, Integer>> list;
			if (b) {
				list = this.items2;
			} else {
				list = this.items1;
			}
			for (final World.Couple<Integer, Integer> item : list) {
				if (item.first == obj) {
					return item.second;
				}
			}
			return 0;
		}

		public synchronized void clearNpcItems() {
			for (final World.Couple<Integer, Integer> i : this.items2) {
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'O', "-",
						new StringBuilder().append(i.first).toString());
			}
			this.items2.clear();
		}

		private synchronized World.Couple<Integer, Integer> getCoupleInList(
				final ArrayList<World.Couple<Integer, Integer>> items, final int guid) {
			for (final World.Couple<Integer, Integer> couple : items) {
				if (couple.first == guid) {
					return couple;
				}
			}
			return null;
		}

		public synchronized void putAllGiveItem(final int verif) {
			if (verif == 0) {
				return;
			}
			switch (verif) {
			case 1: {
				for (final Map.Entry<Integer, Integer> obj : this.getNpc().getGiveItem1().entrySet()) {
					final String str = obj.getKey() + "|" + this.getNpc().getQuantityOfGive1(obj.getKey()) + "|"
							+ obj.getKey() + "|" + World.getObjTemplate(obj.getKey()).getStrTemplate();
					World.Couple<Integer, Integer> couple2;
					if ((couple2 = this.a(this.items2, obj.getValue())) != null) {
						couple2.second = Integer.valueOf(couple2.second)
								+ this.getNpc().getQuantityOfGive1(obj.getKey());
						SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+",
								obj.getKey() + "|" + couple2.second);
						couple2 = null;
						return;
					}
					this.items2.add(new World.Couple<Integer, Integer>(obj.getKey(),
							this.getNpc().getQuantityOfGive1(obj.getKey())));
					SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'O', "+", str);
				}
				break;
			}
			case 2: {
				for (final Map.Entry<Integer, Integer> obj : this.getNpc().getGiveItem2().entrySet()) {
					final String str = obj.getKey() + "|" + this.getNpc().getQuantityOfGive2(obj.getKey()) + "|"
							+ obj.getKey() + "|" + World.getObjTemplate(obj.getKey()).getStrTemplate();
					World.Couple<Integer, Integer> couple2;
					if ((couple2 = this.a(this.items2, obj.getValue())) != null) {
						couple2.second = Integer.valueOf(couple2.second)
								+ this.getNpc().getQuantityOfGive2(obj.getKey());
						SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+",
								obj.getKey() + "|" + couple2.second);
						couple2 = null;
						return;
					}
					this.items2.add(new World.Couple<Integer, Integer>(obj.getKey(),
							this.getNpc().getQuantityOfGive2(obj.getKey())));
					SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'O', "+", str);
				}
				break;
			}
			case 3: {
				for (final Map.Entry<Integer, Integer> obj : this.getNpc().getGiveItem3().entrySet()) {
					final String str = obj.getKey() + "|" + this.getNpc().getQuantityOfGive3(obj.getKey()) + "|"
							+ obj.getKey() + "|" + World.getObjTemplate(obj.getKey()).getStrTemplate();
					World.Couple<Integer, Integer> couple2;
					if ((couple2 = this.a(this.items2, obj.getValue())) != null) {
						couple2.second = Integer.valueOf(couple2.second)
								+ this.getNpc().getQuantityOfGive3(obj.getKey());
						SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+",
								obj.getKey() + "|" + couple2.second);
						couple2 = null;
						return;
					}
					this.items2.add(new World.Couple<Integer, Integer>(obj.getKey(),
							this.getNpc().getQuantityOfGive3(obj.getKey())));
					SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'O', "+", str);
				}
				break;
			}
			}
			if (!this.ok2) {
				this.ok2 = true;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			}
		}

		public NpcTemplate getNpc() {
			return this.npc;
		}

		public void setNpc(final NpcTemplate npc) {
			this.npc = npc;
		}
	}

	public static class NpcExchangePets {
		private Player perso;
		private NpcTemplate npc;
		private long kamas1;
		private long kamas2;
		private ArrayList<World.Couple<Integer, Integer>> items1;
		private ArrayList<World.Couple<Integer, Integer>> items2;
		private boolean ok1;
		private boolean ok2;

		public NpcExchangePets(final Player p, final NpcTemplate n) {
			this.kamas1 = 0L;
			this.kamas2 = 0L;
			this.items1 = new ArrayList<World.Couple<Integer, Integer>>();
			this.items2 = new ArrayList<World.Couple<Integer, Integer>>();
			this.perso = p;
			this.npc = n;
		}

		public synchronized long getKamas(final boolean b) {
			if (b) {
				return this.kamas2;
			}
			return this.kamas1;
		}

		public synchronized void toogleOK(final boolean paramBoolean) {
			if (paramBoolean) {
				this.ok2 = !this.ok2;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			} else {
				this.ok1 = !this.ok1;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			}
			if (this.ok2 && this.ok1) {
				this.apply();
			}
		}

		public synchronized void setKamas(final boolean paramBoolean, final long paramLong) {
			if (paramLong < 0L) {
				return;
			}
			final boolean b = false;
			this.ok1 = b;
			this.ok2 = b;
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			if (paramBoolean) {
				this.kamas2 = paramLong;
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'G', "",
						new StringBuilder(String.valueOf(paramLong)).toString());
				return;
			}
			if (paramLong > this.perso.get_kamas()) {
				return;
			}
			this.kamas1 = paramLong;
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'G', "",
					new StringBuilder(String.valueOf(paramLong)).toString());
		}

		public synchronized void cancel() {
			if (this.perso.getAccount() != null && this.perso.getGameClient() != null) {
				SocketManager.GAME_SEND_EV_PACKET(this.perso.getGameClient());
			}
			this.perso.set_isTradingWith(0);
			this.perso.setCurNpcExchangePets(null);
		}

		public synchronized void apply() {
			org.aestia.object.Object objetToChange = null;
			for (final World.Couple<Integer, Integer> couple : this.items1) {
				if (couple.second == 0) {
					continue;
				}
				if (World.getObjet(couple.first).getPosition() != -1) {
					continue;
				}
				if (!this.perso.hasItemGuid(couple.first)) {
					couple.second = 0;
				} else {
					final org.aestia.object.Object obj = objetToChange = World.getObjet(couple.first);
					if (obj.getQuantity() - couple.second < 1) {
						this.perso.removeItem(couple.first);
						couple.second = obj.getQuantity();
						SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.perso, couple.first);
					} else {
						obj.setQuantity(obj.getQuantity() - couple.second);
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, obj);
					}
				}
			}
			for (final World.Couple<Integer, Integer> couple2 : this.items2) {
				if (couple2.second == 0) {
					continue;
				}
				if (World.getObjTemplate(couple2.first) == null) {
					continue;
				}
				if (World.getObjet(objetToChange.getGuid()) == null) {
					continue;
				}
				org.aestia.object.Object obj2 = null;
				if (World.getObjTemplate(couple2.first).getType() == 18) {
					obj2 = World.getObjTemplate(couple2.first).createNewFamilier(objetToChange);
				}
				if (World.getObjTemplate(couple2.first).getType() == 77) {
					obj2 = World.getObjTemplate(couple2.first).createNewCertificat(objetToChange);
				}
				if (obj2 == null) {
					continue;
				}
				if (this.perso.addObjet(obj2, true)) {
					World.addObjet(obj2, true);
				}
				SocketManager.GAME_SEND_Im_PACKET(this.perso, "021;" + couple2.second + "~" + couple2.first);
				Database.getStatique().getItemData().save(obj2, false);
			}
			World.removeItem(objetToChange.getGuid());
			this.perso.set_isTradingWith(0);
			this.perso.setCurNpcExchangePets(null);
			SocketManager.GAME_SEND_EXCHANGE_VALID(this.perso.getGameClient(), 'a');
			SocketManager.GAME_SEND_Ow_PACKET(this.perso);
			Database.getStatique().getPlayerData().update(this.perso, true);
		}

		public synchronized void addItem(final int obj, final int qua) {
			if (qua <= 0) {
				return;
			}
			if (World.getObjet(obj) == null) {
				return;
			}
			final boolean b = false;
			this.ok2 = b;
			this.ok1 = b;
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			final String str = String.valueOf(obj) + "|" + qua;
			final World.Couple<Integer, Integer> couple = this.getCoupleInList(this.items1, obj);
			if (couple != null) {
				final World.Couple<Integer, Integer> couple2 = couple;
				couple2.second += qua;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+", obj + "|" + couple.second);
				return;
			}
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+", str);
			this.items1.add(new World.Couple<Integer, Integer>(obj, qua));
			if (this.verifIfAlonePets() || this.verifIfAloneParcho()) {
				if (this.items1.size() == 1) {
					int id = -1;
					org.aestia.object.Object objet = null;
					for (final World.Couple<Integer, Integer> i : this.items1) {
						if (World.getObjet(i.first) == null) {
							continue;
						}
						objet = World.getObjet(i.first);
						if (World.getObjet(i.first).getTemplate().getType() == 18) {
							id = Constant.getParchoByIdPets(World.getObjet(i.first).getTemplate().getId());
						} else {
							if (World.getObjet(i.first).getTemplate().getType() != 77) {
								continue;
							}
							id = Constant.getPetsByIdParcho(World.getObjet(i.first).getTemplate().getId());
						}
					}
					if (id == -1) {
						return;
					}
					final String str2 = String.valueOf(id) + "|" + 1 + "|" + id + "|" + objet.parseStatsString();
					this.items2.add(new World.Couple<Integer, Integer>(id, 1));
					SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'O', "+", str2);
					this.ok2 = true;
					SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
				} else {
					this.clearNpcItems();
					this.ok2 = false;
					SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
				}
			} else {
				this.clearNpcItems();
				this.ok2 = false;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			}
		}

		public synchronized void removeItem(final int guid, final int qua) {
			if (qua < 0) {
				return;
			}
			final boolean b = false;
			this.ok2 = b;
			this.ok1 = b;
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			if (World.getObjet(guid) == null) {
				return;
			}
			final World.Couple<Integer, Integer> couple = this.getCoupleInList(this.items1, guid);
			final int newQua = couple.second - qua;
			if (newQua < 1) {
				this.items1.remove(couple);
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "-",
						new StringBuilder().append(guid).toString());
			} else {
				couple.second = newQua;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+", guid + "|" + newQua);
			}
			if (this.verifIfAlonePets()) {
				if (this.items1.size() == 1) {
					int id = -1;
					org.aestia.object.Object objet = null;
					for (final World.Couple<Integer, Integer> i : this.items1) {
						if (World.getObjet(i.first) == null) {
							continue;
						}
						objet = World.getObjet(i.first);
						if (World.getObjet(i.first).getTemplate().getType() == 18) {
							id = Constant.getParchoByIdPets(World.getObjet(i.first).getTemplate().getId());
						} else {
							if (World.getObjet(i.first).getTemplate().getType() != 77) {
								continue;
							}
							id = Constant.getPetsByIdParcho(World.getObjet(i.first).getTemplate().getId());
						}
					}
					if (id == -1) {
						return;
					}
					final String str = String.valueOf(id) + "|" + 1 + "|" + id + "|" + objet.parseStatsString();
					this.items2.add(new World.Couple<Integer, Integer>(id, 1));
					SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'O', "+", str);
					this.ok2 = true;
					SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
				} else {
					this.clearNpcItems();
					this.ok2 = false;
					SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
				}
			} else {
				this.clearNpcItems();
				this.ok2 = false;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			}
		}

		public boolean verifIfAlonePets() {
			for (final World.Couple<Integer, Integer> i : this.items1) {
				if (World.getObjet(i.first).getTemplate().getType() != 18) {
					return false;
				}
			}
			return true;
		}

		public boolean verifIfAloneParcho() {
			for (final World.Couple<Integer, Integer> i : this.items1) {
				if (World.getObjet(i.first).getTemplate().getType() != 77) {
					return false;
				}
			}
			return true;
		}

		public synchronized void clearNpcItems() {
			for (final World.Couple<Integer, Integer> i : this.items2) {
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'O', "-",
						new StringBuilder().append(i.first).toString());
			}
			this.items2.clear();
		}

		private synchronized World.Couple<Integer, Integer> getCoupleInList(
				final ArrayList<World.Couple<Integer, Integer>> items, final int guid) {
			for (final World.Couple<Integer, Integer> couple : items) {
				if (couple.first == guid) {
					return couple;
				}
			}
			return null;
		}

		public synchronized int getQuaItem(final int obj, final boolean b) {
			ArrayList<World.Couple<Integer, Integer>> list;
			if (b) {
				list = this.items2;
			} else {
				list = this.items1;
			}
			for (final World.Couple<Integer, Integer> item : list) {
				if (item.first == obj) {
					return item.second;
				}
			}
			return 0;
		}

		public NpcTemplate getNpc() {
			return this.npc;
		}

		public void setNpc(final NpcTemplate npc) {
			this.npc = npc;
		}
	}

	public static class NpcRessurectPets {
		private Player perso;
		private NpcTemplate npc;
		private long kamas1;
		private long kamas2;
		private ArrayList<World.Couple<Integer, Integer>> items1;
		private ArrayList<World.Couple<Integer, Integer>> items2;
		private boolean ok1;
		private boolean ok2;

		public NpcRessurectPets(final Player p, final NpcTemplate n) {
			this.kamas1 = 0L;
			this.kamas2 = 0L;
			this.items1 = new ArrayList<World.Couple<Integer, Integer>>();
			this.items2 = new ArrayList<World.Couple<Integer, Integer>>();
			this.perso = p;
			this.npc = n;
		}

		public synchronized long getKamas(final boolean b) {
			if (b) {
				return this.kamas2;
			}
			return this.kamas1;
		}

		public synchronized void toogleOK(final boolean paramBoolean) {
			if (paramBoolean) {
				this.ok2 = !this.ok2;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			} else {
				this.ok1 = !this.ok1;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			}
			if (this.ok2 && this.ok1) {
				this.apply();
			}
		}

		public synchronized void setKamas(final boolean paramBoolean, final long paramLong) {
			if (paramLong < 0L) {
				return;
			}
			final boolean b = false;
			this.ok1 = b;
			this.ok2 = b;
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			if (paramBoolean) {
				this.kamas2 = paramLong;
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'G', "",
						new StringBuilder(String.valueOf(paramLong)).toString());
				return;
			}
			if (paramLong > this.perso.get_kamas()) {
				return;
			}
			this.kamas1 = paramLong;
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'G', "",
					new StringBuilder(String.valueOf(paramLong)).toString());
		}

		public synchronized void cancel() {
			if (this.perso.getAccount() != null && this.perso.getGameClient() != null) {
				SocketManager.GAME_SEND_EV_PACKET(this.perso.getGameClient());
			}
			this.perso.set_isTradingWith(0);
			this.perso.setCurNpcRessurectPets(null);
		}

		public synchronized void apply() {
			for (final World.Couple<Integer, Integer> item : this.items1) {
				final org.aestia.object.Object object = World.getObjet(item.first);
				if (object.getTemplate().getId() == 8012) {
					if (object.getQuantity() - item.second < 1) {
						this.perso.removeItem(item.first);
						item.second = object.getQuantity();
						SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.perso, item.first);
					} else {
						object.setQuantity(object.getQuantity() - item.second);
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.perso, object);
					}
				} else {
					final PetEntry pet = World.getPetsEntry(item.first);
					if (pet == null) {
						continue;
					}
					pet.resurrection();
					SocketManager.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(this.perso, object);
				}
			}
			this.perso.set_isTradingWith(0);
			this.perso.setCurNpcRessurectPets(null);
			SocketManager.GAME_SEND_EXCHANGE_VALID(this.perso.getGameClient(), 'a');
			SocketManager.GAME_SEND_Ow_PACKET(this.perso);
			Database.getStatique().getPlayerData().update(this.perso, true);
		}

		public synchronized void addItem(final int obj, final int qua) {
			if (qua <= 0) {
				return;
			}
			if (World.getObjet(obj) == null) {
				return;
			}
			final boolean b = false;
			this.ok2 = b;
			this.ok1 = b;
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			final String str = String.valueOf(obj) + "|" + qua;
			final World.Couple<Integer, Integer> couple = this.getCoupleInList(this.items1, obj);
			if (couple != null) {
				final World.Couple<Integer, Integer> couple2 = couple;
				couple2.second += qua;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+", obj + "|" + couple.second);
				return;
			}
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+", str);
			this.items1.add(new World.Couple<Integer, Integer>(obj, qua));
			if (this.verification()) {
				if (this.items1.size() == 2) {
					int id = -1;
					org.aestia.object.Object objet = null;
					for (final World.Couple<Integer, Integer> i : this.items1) {
						objet = World.getObjet(i.first);
						if (objet == null) {
							continue;
						}
						if (objet.getTemplate().getType() == 90) {
							id = World.getPetsEntry(i.first).getTemplate();
							break;
						}
					}
					if (id == -1 || objet == null) {
						return;
					}
					final String str2 = String.valueOf(id) + "|" + 1 + "|" + id + "|" + objet.parseStatsString();
					this.items2.add(new World.Couple<Integer, Integer>(id, 1));
					SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'O', "+", str2);
					this.ok2 = true;
					SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
				} else {
					this.clearNpcItems();
					this.ok2 = false;
					SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
				}
			} else {
				this.clearNpcItems();
				this.ok2 = false;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			}
		}

		public synchronized void removeItem(final int guid, final int qua) {
			if (qua < 0) {
				return;
			}
			final boolean b = false;
			this.ok2 = b;
			this.ok1 = b;
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok1, this.perso.getId());
			SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			if (World.getObjet(guid) == null) {
				return;
			}
			final World.Couple<Integer, Integer> couple = this.getCoupleInList(this.items1, guid);
			final int newQua = couple.second - qua;
			if (newQua < 1) {
				this.items1.remove(couple);
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "-",
						new StringBuilder().append(guid).toString());
			} else {
				couple.second = newQua;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.perso, 'O', "+", guid + "|" + newQua);
			}
			if (this.verification()) {
				if (this.items1.size() == 2) {
					int id = -1;
					org.aestia.object.Object objet = null;
					for (final World.Couple<Integer, Integer> i : this.items1) {
						objet = World.getObjet(i.first);
						if (objet == null) {
							continue;
						}
						if (objet.getTemplate().getType() == 90) {
							id = World.getPetsEntry(i.first).getTemplate();
							break;
						}
					}
					if (id == -1 || objet == null) {
						return;
					}
					final String str = String.valueOf(id) + "|" + 1 + "|" + id + "|" + objet.parseStatsString();
					this.items2.add(new World.Couple<Integer, Integer>(id, 1));
					SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'O', "+", str);
					this.ok2 = true;
					SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
				} else {
					this.clearNpcItems();
					this.ok2 = false;
					SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
				}
			} else {
				this.clearNpcItems();
				this.ok2 = false;
				SocketManager.GAME_SEND_EXCHANGE_OK(this.perso.getGameClient(), this.ok2);
			}
		}

		public boolean verification() {
			boolean verif = true;
			for (final World.Couple<Integer, Integer> item : this.items1) {
				final org.aestia.object.Object object = World.getObjet(item.first);
				if ((object.getTemplate().getId() != 8012 && object.getTemplate().getType() != 90) || item.second > 1) {
					verif = false;
				}
			}
			return verif;
		}

		public synchronized void clearNpcItems() {
			for (final World.Couple<Integer, Integer> i : this.items2) {
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(this.perso.getGameClient(), 'O', "-",
						new StringBuilder().append(i.first).toString());
			}
			this.items2.clear();
		}

		private synchronized World.Couple<Integer, Integer> getCoupleInList(
				final ArrayList<World.Couple<Integer, Integer>> items, final int guid) {
			for (final World.Couple<Integer, Integer> couple : items) {
				if (couple.first == guid) {
					return couple;
				}
			}
			return null;
		}

		public synchronized int getQuaItem(final int obj, final boolean b) {
			ArrayList<World.Couple<Integer, Integer>> list;
			if (b) {
				list = this.items2;
			} else {
				list = this.items1;
			}
			for (final World.Couple<Integer, Integer> item : list) {
				if (item.first == obj) {
					return item.second;
				}
			}
			return 0;
		}

		public NpcTemplate getNpc() {
			return this.npc;
		}

		public void setNpc(final NpcTemplate npc) {
			this.npc = npc;
		}
	}
}

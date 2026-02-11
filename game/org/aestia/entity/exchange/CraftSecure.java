// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity.exchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.aestia.client.Player;
import org.aestia.common.Formulas;
import org.aestia.common.SocketManager;
import org.aestia.game.world.World;
import org.aestia.job.JobAction;
import org.aestia.job.JobConstant;
import org.aestia.job.JobStat;
import org.aestia.kernel.Config;

public class CraftSecure extends PlayerExchange {
	private long payKamas;
	private long payIfSuccessKamas;
	private int maxCase;
	private ArrayList<World.Couple<Integer, Integer>> payItems;
	private ArrayList<World.Couple<Integer, Integer>> payItemsIfSuccess;

	public CraftSecure(final Player player1, final Player player2) {
		super(player1, player2);
		this.payKamas = 0L;
		this.payIfSuccessKamas = 0L;
		this.maxCase = 18;
		this.payItems = new ArrayList<World.Couple<Integer, Integer>>();
		this.payItemsIfSuccess = new ArrayList<World.Couple<Integer, Integer>>();
		this.setMaxCase(JobConstant.getTotalCaseByJobLevel(
				this.player1.getMetierBySkill(this.player1.getIsCraftingType().get(1)).get_lvl()));
		if (this.player1.getMetierBySkill(this.player1.getIsCraftingType().get(1)).getTemplate().isMaging()) {
			SocketManager.GAME_SEND_MESSAGE(this.player1,
					"Pas d'invitation pour les m\u00e9tiers de forgemagie pour le moment.");
			SocketManager.GAME_SEND_MESSAGE(this.player2,
					"Pas d'invitation pour les m\u00e9tiers de forgemagie pour le moment.");
			this.cancel();
		}
	}

	public Player getNeeder() {
		return this.player2;
	}

	public void setMaxCase(final int maxCase) {
		this.maxCase = maxCase;
	}

	public int getMaxCase() {
		return this.maxCase;
	}

	@Override
	public synchronized void apply() {
		final JobStat jobStat = this.player1.getMetierBySkill(this.player1.getIsCraftingType().get(1));
		if (jobStat == null) {
			return;
		}
		if (jobStat.getTemplate().isMaging()) {
			return;
		}
		final JobAction jobAction = jobStat.getJobActionBySkill(this.player1.getIsCraftingType().get(1));
		if (jobAction == null) {
			return;
		}
		final Map<Player, ArrayList<World.Couple<Integer, Integer>>> items = new HashMap<Player, ArrayList<World.Couple<Integer, Integer>>>();
		items.put(this.player1, this.items1);
		items.put(this.player2, this.items2);
		final int sizeList = jobAction.sizeList(items);
		final boolean success = jobAction.craftPublicMode(this.player1, this.player2, items);
		this.player1.addKamas(this.payKamas + (success ? this.payIfSuccessKamas : 0L));
		this.player2.addKamas(-this.payKamas - (success ? this.payIfSuccessKamas : 0L));
		for (final World.Couple<Integer, Integer> couple : this.payItems) {
			if (couple.second == 0) {
				continue;
			}
			final org.aestia.object.Object object = World.getObjet(couple.first);
			if (object == null) {
				continue;
			}
			if (object.getPosition() != -1) {
				continue;
			}
			if (!this.player2.hasItemGuid(couple.first)) {
				couple.second = 0;
			} else if (object.getQuantity() - couple.second < 1) {
				this.player2.removeItem(couple.first);
				couple.second = object.getQuantity();
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.player2, couple.first);
				if (this.player1.addObjet(object, true)) {
					continue;
				}
				World.removeItem(couple.first);
			} else {
				object.setQuantity(object.getQuantity() - couple.second);
				SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player2, object);
				final org.aestia.object.Object newObj = org.aestia.object.Object.getCloneObjet(object, couple.second);
				if (!this.player1.addObjet(newObj, true)) {
					continue;
				}
				World.addObjet(newObj, true);
			}
		}
		if (success) {
			for (final World.Couple<Integer, Integer> couple : this.payItemsIfSuccess) {
				if (couple.second == 0) {
					continue;
				}
				final org.aestia.object.Object object = World.getObjet(couple.first);
				if (object == null) {
					continue;
				}
				if (object.getPosition() != -1) {
					continue;
				}
				if (!this.player2.hasItemGuid(couple.first)) {
					couple.second = 0;
				} else if (object.getQuantity() - couple.second < 1) {
					this.player2.removeItem(couple.first);
					couple.second = object.getQuantity();
					SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.player2, couple.first);
					if (this.player1.addObjet(object, true)) {
						continue;
					}
					World.removeItem(couple.first);
				} else {
					object.setQuantity(object.getQuantity() - couple.second);
					SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player2, object);
					final org.aestia.object.Object newObj = org.aestia.object.Object.getCloneObjet(object,
							couple.second);
					if (!this.player1.addObjet(newObj, true)) {
						continue;
					}
					World.addObjet(newObj, true);
				}
			}
		}
		int winXP = 0;
		if (success) {
			winXP = Formulas.calculXpWinCraft(jobStat.get_lvl(), sizeList) * Config.getInstance().rateJob;
		} else if (!jobStat.getTemplate().isMaging()) {
			winXP = Formulas.calculXpWinCraft(jobStat.get_lvl(), sizeList) * Config.getInstance().rateJob;
		}
		if (winXP > 0) {
			jobStat.addXp(this.player1, winXP, true);
			final ArrayList<JobStat> SMs = new ArrayList<JobStat>();
			SMs.add(jobStat);
			SocketManager.GAME_SEND_JX_PACKET(this.player1, SMs);
		}
		SocketManager.GAME_SEND_STATS_PACKET(this.player1);
		SocketManager.GAME_SEND_STATS_PACKET(this.player2);
		this.payIfSuccessKamas = 0L;
		this.payKamas = 0L;
		this.payItems.clear();
		this.payItemsIfSuccess.clear();
		this.items1.clear();
		this.items2.clear();
		this.ok1 = false;
		this.ok2 = false;
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok2, this.player2.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok2, this.player2.getId());
	}

	@Override
	public synchronized void cancel() {
		this.send("EV");
		this.player1.getIsCraftingType().clear();
		this.player2.getIsCraftingType().clear();
		this.player1.set_isTradingWith(0);
		this.player2.set_isTradingWith(0);
		this.player1.setCurExchange(null);
		this.player2.setCurExchange(null);
	}

	public void setPayKamas(final byte type, long kamas) {
		if (kamas < 0L) {
			return;
		}
		if (this.player2.get_kamas() < kamas) {
			kamas = this.player2.get_kamas();
		}
		this.ok1 = false;
		this.ok2 = false;
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok2, this.player2.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok2, this.player2.getId());
		switch (type) {
		case 1: {
			if (this.payIfSuccessKamas > 0L && kamas + this.payIfSuccessKamas > this.player2.get_kamas()) {
				kamas -= this.payIfSuccessKamas;
			}
			this.payKamas = kamas;
			this.send("Ep1;G" + this.payKamas);
			break;
		}
		case 2: {
			if (this.payKamas > 0L && kamas + this.payKamas > this.player2.get_kamas()) {
				kamas -= this.payKamas;
			}
			this.payIfSuccessKamas = kamas;
			this.send("Ep2;G" + this.payIfSuccessKamas);
			break;
		}
		}
	}

	public void setPayItems(final byte type, final boolean adding, final int guid, final int quantity) {
		final org.aestia.object.Object object = World.getObjet(guid);
		if (object == null) {
			return;
		}
		if (object.getPosition() != -1) {
			return;
		}
		this.ok1 = false;
		this.ok2 = false;
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok1, this.player1.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player1.getGameClient(), this.ok2, this.player2.getId());
		SocketManager.GAME_SEND_EXCHANGE_OK(this.player2.getGameClient(), this.ok2, this.player2.getId());
		if (adding) {
			this.addItem(object, quantity, type);
		} else {
			this.removeItem(object, quantity, type);
		}
	}

	private void addItem(final org.aestia.object.Object object, int quantity, final byte type) {
		if (object.getQuantity() < quantity) {
			quantity = object.getQuantity();
		}
		final ArrayList<World.Couple<Integer, Integer>> items = (type == 1) ? this.payItems : this.payItemsIfSuccess;
		final World.Couple<Integer, Integer> couple = Exchange.getCoupleInList(items, object.getGuid());
		final String add = "|" + object.getTemplate().getId() + "|" + object.parseStatsString();
		if (couple != null) {
			final World.Couple<Integer, Integer> couple2 = couple;
			couple2.second += quantity;
			this.player2.send("Ep" + type + ";O+" + object.getGuid() + "|" + couple.second);
			this.player1.send("Ep" + type + ";O+" + object.getGuid() + "|" + couple.second + add);
			return;
		}
		items.add(new World.Couple<Integer, Integer>(object.getGuid(), quantity));
		this.player2.send("Ep" + type + ";O+" + object.getGuid() + "|" + quantity);
		this.player1.send("Ep" + type + ";O+" + object.getGuid() + "|" + quantity + add);
	}

	private void removeItem(final org.aestia.object.Object object, final int quantity, final byte type) {
		final ArrayList<World.Couple<Integer, Integer>> items = (type == 1) ? this.payItems : this.payItemsIfSuccess;
		final World.Couple<Integer, Integer> couple = Exchange.getCoupleInList(items, object.getGuid());
		final int newQua = couple.second - quantity;
		if (newQua < 1) {
			items.remove(couple);
			this.player1.send("Ep" + type + ";O-" + object.getGuid());
			this.player2.send("Ep" + type + ";O-" + object.getGuid());
		} else {
			couple.second = newQua;
			this.player2.send("Ep" + type + ";O+" + object.getGuid() + "|" + newQua);
			this.player1.send("Ep" + type + ";O+" + object.getGuid() + "|" + newQua + "|" + object.getTemplate().getId()
					+ "|" + object.parseStatsString());
		}
	}

	private void send(final String packet) {
		this.player1.send(packet);
		this.player2.send(packet);
	}
}

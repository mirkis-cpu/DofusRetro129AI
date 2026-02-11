// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.game.world.World;

public class PetEntry {
	private int objectId;
	private int template;
	private long lastEatDate;
	private int quaEat;
	private int pdv;
	private int Poids;
	private int corpulence;
	private boolean isEupeoh;

	public PetEntry(final int Oid, final int template, final long lastEatDate, final int quaEat, final int pdv,
			final int corpulence, final boolean isEPO) {
		this.objectId = Oid;
		this.template = template;
		this.lastEatDate = lastEatDate;
		this.quaEat = quaEat;
		this.pdv = pdv;
		this.corpulence = corpulence;
		this.getCurrentStatsPoids();
		this.isEupeoh = isEPO;
	}

	public int getObjectId() {
		return this.objectId;
	}

	public int getTemplate() {
		return this.template;
	}

	public long getLastEatDate() {
		return this.lastEatDate;
	}

	public int getQuaEat() {
		return this.quaEat;
	}

	public int getPdv() {
		return this.pdv;
	}

	public int getCorpulence() {
		return this.corpulence;
	}

	public boolean getIsEupeoh() {
		return this.isEupeoh;
	}

	public String parseLastEatDate() {
		String hexDate = "#";
		final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String date = formatter.format(this.lastEatDate);
		final String[] split = date.split("\\s");
		final String[] split2 = split[0].split("-");
		hexDate = String.valueOf(hexDate) + Integer.toHexString(Integer.parseInt(split2[0])) + "#";
		final int mois = Integer.parseInt(split2[1]) - 1;
		final int jour = Integer.parseInt(split2[2]);
		hexDate = String.valueOf(hexDate) + Integer.toHexString(Integer.parseInt(new StringBuilder()
				.append((mois < 10) ? ("0" + mois) : mois).append((jour < 10) ? ("0" + jour) : jour).toString())) + "#";
		final String[] split3 = split[1].split(":");
		final String heure = String.valueOf(split3[0]) + split3[1];
		hexDate = String.valueOf(hexDate) + Integer.toHexString(Integer.parseInt(heure));
		return hexDate;
	}

	public int parseCorpulence() {
		int corpu = 0;
		if (this.corpulence > 0 || this.corpulence < 0) {
			corpu = 7;
		}
		if (this.corpulence == 0) {
			corpu = 0;
		}
		return corpu;
	}

	public int getCurrentStatsPoids() {
		final org.aestia.object.Object obj = World.getObjet(this.objectId);
		if (obj == null) {
			return 0;
		}
		int cumul = 0;
		for (final Map.Entry<Integer, Integer> entry : obj.getStats().getMap().entrySet()) {
			if (entry.getKey() == Integer.parseInt("320", 16)) {
				continue;
			}
			if (entry.getKey() == Integer.parseInt("326", 16)) {
				continue;
			}
			if (entry.getKey() == Integer.parseInt("328", 16)) {
				continue;
			}
			if (entry.getKey() == Integer.parseInt("8a", 16)) {
				cumul += 2 * entry.getValue();
			} else if (entry.getKey() == Integer.parseInt("7c", 16)) {
				cumul += 3 * entry.getValue();
			} else if (entry.getKey() == Integer.parseInt("d2", 16) || entry.getKey() == Integer.parseInt("d3", 16)
					|| entry.getKey() == Integer.parseInt("d4", 16) || entry.getKey() == Integer.parseInt("d5", 16)
					|| entry.getKey() == Integer.parseInt("d6", 16)) {
				cumul += 4 * entry.getValue();
			} else if (entry.getKey() == Integer.parseInt("b2", 16) || entry.getKey() == Integer.parseInt("70", 16)) {
				cumul += 8 * entry.getValue();
			} else {
				cumul += 1 * entry.getValue();
			}
		}
		return this.Poids = cumul;
	}

	public int getMaxStat() {
		return Integer.parseInt(World.getPets(this.template).getStatsMax());
	}

	public void looseFight(final Player p) {
		final org.aestia.object.Object obj = World.getObjet(this.objectId);
		if (obj == null) {
			return;
		}
		final Pet pets = World.getPets(obj.getTemplate().getId());
		if (pets == null) {
			return;
		}
		--this.pdv;
		obj.getTxtStat().remove(800);
		obj.getTxtStat().put(800, Integer.toHexString((this.pdv > 0) ? this.pdv : 0));
		if (this.pdv <= 0) {
			this.pdv = 0;
			obj.getTxtStat().remove(800);
			obj.getTxtStat().put(800, Integer.toHexString(0));
			if (pets.getDeadTemplate() == 0) {
				World.removeItem(obj.getGuid());
				p.removeItem(obj.getGuid());
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(p, obj.getGuid());
			} else {
				obj.setTemplate(pets.getDeadTemplate());
				if (obj.getPosition() == 8) {
					obj.setPosition(-1);
					SocketManager.GAME_SEND_OBJET_MOVE_PACKET(p, obj);
				}
			}
			SocketManager.GAME_SEND_Im_PACKET(p, "154");
		}
		SocketManager.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(p, obj);
		Database.getStatique().getPets_dataData().update(this);
	}

	public void setMax(Player p,org.aestia.object.Object obj) {
		final Pet pets = World.getPets(this.template);
		if (pets == null) {
			return;
		}
		int statsID = pets.statsIdByEat(obj.getTemplate().getId(), obj.getTemplate().getType(), -1);
		obj.getStats().addOneStat(statsID, Integer.parseInt(pets.getStatsMax()));
		if (this.corpulence <= 0) {
			this.lastEatDate = System.currentTimeMillis();
			++this.corpulence;
			++this.quaEat;
			obj.getTxtStat().remove(806);
			obj.getTxtStat().put(806, Integer.toString(this.corpulence));
			SocketManager.GAME_SEND_Im_PACKET(p, "029");
			if (this.quaEat >= 3) {
				if ((this.getIsEupeoh() ? (pets.getMax() * 1.1) : pets.getMax()) > this.getCurrentStatsPoids()) {
					if (obj.getStats().getMap().containsKey(statsID)) {
						int value = obj.getStats().getMap().get(statsID)
								+ World.getPets(World.getObjet(this.objectId).getTemplate().getId()).getGain();
						if (value > this.getMaxStat()) {
							value = this.getMaxStat();
						}
						obj.getStats().getMap().remove(statsID);
						obj.getStats().addOneStat(statsID, value);
					} else {
						obj.getStats().addOneStat(statsID, pets.getGain());
					}
				}
				this.quaEat = 0;
			}
		} else if (this.lastEatDate + 0 * 3600000 > System.currentTimeMillis() && this.corpulence >= 0) {
			this.lastEatDate = System.currentTimeMillis();
			++this.corpulence;
			obj.getTxtStat().remove(806);
			obj.getTxtStat().put(806, Integer.toString(this.corpulence));
			if (this.corpulence == 1) {
				++this.quaEat;
				SocketManager.GAME_SEND_Im_PACKET(p, "026");
			} else {
				--this.pdv;
				obj.getTxtStat().remove(800);
				obj.getTxtStat().put(800, Integer.toHexString((this.pdv > 0) ? this.pdv : 0));
				SocketManager.GAME_SEND_Im_PACKET(p, "027");
			}
			if (this.quaEat >= 3) {
				if ((this.getIsEupeoh() ? (pets.getMax() * 1.1) : pets.getMax()) > this.getCurrentStatsPoids()) {
					if (obj.getStats().getMap().containsKey(statsID)) {
						int value = obj.getStats().getMap().get(statsID)
								+ World.getPets(World.getObjet(this.objectId).getTemplate().getId()).getGain();
						if (value > this.getMaxStat()) {
							value = this.getMaxStat();
						}
						obj.getStats().getMap().remove(statsID);
						obj.getStats().addOneStat(statsID, value);
					} else {
						obj.getStats().addOneStat(statsID, pets.getGain());
					}
				}
				this.quaEat = 0;
			}
		} else if (this.lastEatDate + 0 * 3600000 < System.currentTimeMillis() && this.corpulence >= 0) {
			this.lastEatDate = System.currentTimeMillis();
			if (statsID == 0) {
				return;
			}
			++this.quaEat;
			if (this.quaEat >= 3) {
				if ((this.getIsEupeoh() ? (pets.getMax() * 1.1) : pets.getMax()) > this.getCurrentStatsPoids()) {
					if (obj.getStats().getMap().containsKey(statsID)) {
						int value = obj.getStats().getMap().get(statsID)
								+ World.getPets(World.getObjet(this.objectId).getTemplate().getId()).getGain();
						if (value > this.getMaxStat()) {
							value = this.getMaxStat();
						}
						obj.getStats().getMap().remove(statsID);
						obj.getStats().addOneStat(statsID, value);
					} else {
						obj.getStats().addOneStat(statsID, pets.getGain());
					}
				}
				this.quaEat = 0;
			}
			SocketManager.GAME_SEND_Im_PACKET(p, "032");
		}
		if (this.pdv <= 0) {
			this.pdv = 0;
			obj.getTxtStat().remove(800);
			obj.getTxtStat().put(800, Integer.toHexString((this.pdv > 0) ? this.pdv : 0));
			if (pets.getDeadTemplate() == 0) {
				World.removeItem(obj.getGuid());
				p.removeItem(obj.getGuid());
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(p, obj.getGuid());
			} else {
				obj.setTemplate(pets.getDeadTemplate());
				if (obj.getPosition() == 8) {
					obj.setPosition(-1);
					SocketManager.GAME_SEND_OBJET_MOVE_PACKET(p, obj);
				}
				Database.getStatique().getItemData().save(obj, false);
			}
			SocketManager.GAME_SEND_Im_PACKET(p, "154");
		}
		if (obj.getTxtStat().containsKey(807)) {
			obj.getTxtStat().remove(807);
			//obj.getTxtStat().put(807, Integer.toHexString(feed.getTemplate().getId()));
		} else {
			//obj.getTxtStat().put(807, Integer.toHexString(feed.getTemplate().getId()));
		}
		SocketManager.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(p, obj);
		Database.getStatique().getPets_dataData().update(this);
		Database.getStatique().getItemData().update(World.getObjet(this.objectId));
	}
	
	public void eat(final Player p, final int min, final int max, final int statsID,
			final org.aestia.object.Object feed) {
		final org.aestia.object.Object obj = World.getObjet(this.objectId);
		if (obj == null) {
			return;
		}
		final Pet pets = World.getPets(obj.getTemplate().getId());
		if (pets == null) {
			return;
		}
		if (this.corpulence <= 0) {
			this.lastEatDate = System.currentTimeMillis();
			++this.corpulence;
			++this.quaEat;
			obj.getTxtStat().remove(806);
			obj.getTxtStat().put(806, Integer.toString(this.corpulence));
			SocketManager.GAME_SEND_Im_PACKET(p, "029");
			if (this.quaEat >= 3) {
				if ((this.getIsEupeoh() ? (pets.getMax() * 1.1) : pets.getMax()) > this.getCurrentStatsPoids()) {
					if (obj.getStats().getMap().containsKey(statsID)) {
						int value = obj.getStats().getMap().get(statsID)
								+ World.getPets(World.getObjet(this.objectId).getTemplate().getId()).getGain();
						if (value > this.getMaxStat()) {
							value = this.getMaxStat();
						}
						obj.getStats().getMap().remove(statsID);
						obj.getStats().addOneStat(statsID, value);
					} else {
						obj.getStats().addOneStat(statsID, pets.getGain());
					}
				}
				this.quaEat = 0;
			}
		} else if (this.lastEatDate + min * 3600000 > System.currentTimeMillis() && this.corpulence >= 0) {
			this.lastEatDate = System.currentTimeMillis();
			++this.corpulence;
			obj.getTxtStat().remove(806);
			obj.getTxtStat().put(806, Integer.toString(this.corpulence));
			if (this.corpulence == 1) {
				++this.quaEat;
				SocketManager.GAME_SEND_Im_PACKET(p, "026");
			} else {
				--this.pdv;
				obj.getTxtStat().remove(800);
				obj.getTxtStat().put(800, Integer.toHexString((this.pdv > 0) ? this.pdv : 0));
				SocketManager.GAME_SEND_Im_PACKET(p, "027");
			}
			if (this.quaEat >= 3) {
				if ((this.getIsEupeoh() ? (pets.getMax() * 1.1) : pets.getMax()) > this.getCurrentStatsPoids()) {
					if (obj.getStats().getMap().containsKey(statsID)) {
						int value = obj.getStats().getMap().get(statsID)
								+ World.getPets(World.getObjet(this.objectId).getTemplate().getId()).getGain();
						if (value > this.getMaxStat()) {
							value = this.getMaxStat();
						}
						obj.getStats().getMap().remove(statsID);
						obj.getStats().addOneStat(statsID, value);
					} else {
						obj.getStats().addOneStat(statsID, pets.getGain());
					}
				}
				this.quaEat = 0;
			}
		} else if (this.lastEatDate + min * 3600000 < System.currentTimeMillis() && this.corpulence >= 0) {
			this.lastEatDate = System.currentTimeMillis();
			if (statsID == 0) {
				return;
			}
			++this.quaEat;
			if (this.quaEat >= 3) {
				if ((this.getIsEupeoh() ? (pets.getMax() * 1.1) : pets.getMax()) > this.getCurrentStatsPoids()) {
					if (obj.getStats().getMap().containsKey(statsID)) {
						int value = obj.getStats().getMap().get(statsID)
								+ World.getPets(World.getObjet(this.objectId).getTemplate().getId()).getGain();
						if (value > this.getMaxStat()) {
							value = this.getMaxStat();
						}
						obj.getStats().getMap().remove(statsID);
						obj.getStats().addOneStat(statsID, value);
					} else {
						obj.getStats().addOneStat(statsID, pets.getGain());
					}
				}
				this.quaEat = 0;
			}
			SocketManager.GAME_SEND_Im_PACKET(p, "032");
		}
		if (this.pdv <= 0) {
			this.pdv = 0;
			obj.getTxtStat().remove(800);
			obj.getTxtStat().put(800, Integer.toHexString((this.pdv > 0) ? this.pdv : 0));
			if (pets.getDeadTemplate() == 0) {
				World.removeItem(obj.getGuid());
				p.removeItem(obj.getGuid());
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(p, obj.getGuid());
			} else {
				obj.setTemplate(pets.getDeadTemplate());
				if (obj.getPosition() == 8) {
					obj.setPosition(-1);
					SocketManager.GAME_SEND_OBJET_MOVE_PACKET(p, obj);
				}
				Database.getStatique().getItemData().save(obj, false);
			}
			SocketManager.GAME_SEND_Im_PACKET(p, "154");
		}
		if (obj.getTxtStat().containsKey(807)) {
			obj.getTxtStat().remove(807);
			obj.getTxtStat().put(807, Integer.toHexString(feed.getTemplate().getId()));
		} else {
			obj.getTxtStat().put(807, Integer.toHexString(feed.getTemplate().getId()));
		}
		SocketManager.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(p, obj);
		Database.getStatique().getPets_dataData().update(this);
		Database.getStatique().getItemData().update(World.getObjet(this.objectId));
	}

	public void eatSouls(final Player p, final Map<Integer, Integer> souls) {
		final org.aestia.object.Object obj = World.getObjet(this.objectId);
		if (obj == null) {
			return;
		}
		final Pet pet = World.getPets(obj.getTemplate().getId());
		if (pet == null || pet.getType() != 1) {
			return;
		}
		for (final Map.Entry<Integer, Integer> entry : souls.entrySet()) {
			final int soul = entry.getKey();
			final int count = entry.getValue();
			if (pet.canEat(-1, -1, soul)) {
				final int statsID = pet.statsIdByEat(-1, -1, soul);
				if (statsID == 0) {
					return;
				}
				if ((this.getIsEupeoh() ? (pet.getMax() * 1.1) : pet.getMax()) <= this.getCurrentStatsPoids()) {
					continue;
				}
				final int soulCount = (obj.getSoulStat().get(soul) != null) ? obj.getSoulStat().get(soul) : 0;
				if (soulCount > 0) {
					obj.getSoulStat().remove(soul);
					obj.getSoulStat().put(soul, count + soulCount);
				} else {
					obj.getSoulStat().put(soul, count);
				}
			}
		}
		for (final Map.Entry<Integer, ArrayList<Map<Integer, Integer>>> ent : pet.getMonsters().entrySet()) {
			for (final Map<Integer, Integer> entry2 : ent.getValue()) {
				for (final Map.Entry<Integer, Integer> monsterEntry : entry2.entrySet()) {
					if (pet.getNumbMonster(ent.getKey(), monsterEntry.getKey()) != 0) {
						int pts = 0;
						for (final Map.Entry<Integer, Integer> list : obj.getSoulStat().entrySet()) {
							final int howIkill = list.getValue();
							final int howIneed = pet.getNumbMonster(ent.getKey(), list.getKey());
							pts += (int) Math.floor(howIkill / howIneed) * pet.getGain();
						}
						if (pts <= 0) {
							continue;
						}
						if (pts > this.getMaxStat()) {
							pts = this.getMaxStat();
						}
						if (obj.getStats().getMap().containsKey(ent.getKey())) {
							obj.getStats().getMap().remove(ent.getKey());
						}
						obj.getStats().getMap().put(ent.getKey(), pts);
					}
				}
			}
		}
		SocketManager.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(p, obj);
		Database.getStatique().getPets_dataData().update(this);
		Database.getStatique().getItemData().update(World.getObjet(this.objectId));
	}

	public void updatePets(final Player p, final int max) {
		final org.aestia.object.Object obj = World.getObjet(this.objectId);
		if (obj == null) {
			return;
		}
		final Pet pets = World.getPets(obj.getTemplate().getId());
		if (pets == null) {
			return;
		}
		if (this.pdv <= 0 && obj.getTemplate().getId() == pets.getDeadTemplate()) {
			return;
		}
		if (this.lastEatDate + max * 3600000 < System.currentTimeMillis()) {
			final int nbrepas = (int) Math.floor((System.currentTimeMillis() - this.lastEatDate) / (max * 3600000));
			this.corpulence -= nbrepas;
			if (nbrepas != 0) {
				obj.getTxtStat().remove(806);
				obj.getTxtStat().put(806, Integer.toString(this.corpulence));
			}
			--this.pdv;
			obj.getTxtStat().remove(800);
			obj.getTxtStat().put(800, Integer.toHexString((this.pdv > 0) ? this.pdv : 0));
			this.lastEatDate = System.currentTimeMillis();
		} else if (this.pdv > 0) {
			SocketManager.GAME_SEND_Im_PACKET(p, "025");
		}
		if (this.pdv <= 0) {
			this.pdv = 0;
			obj.getTxtStat().remove(800);
			obj.getTxtStat().put(800, Integer.toHexString((this.pdv > 0) ? this.pdv : 0));
			if (pets.getDeadTemplate() == 0) {
				World.removeItem(obj.getGuid());
				p.removeItem(obj.getGuid());
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(p, obj.getGuid());
			} else {
				obj.setTemplate(pets.getDeadTemplate());
				if (obj.getPosition() == 8) {
					obj.setPosition(-1);
					SocketManager.GAME_SEND_OBJET_MOVE_PACKET(p, obj);
				}
				Database.getStatique().getItemData().save(obj, false);
			}
			SocketManager.GAME_SEND_Im_PACKET(p, "154");
		}
		SocketManager.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(p, obj);
		Database.getStatique().getPets_dataData().update(this);
	}

	public void resurrection() {
		final org.aestia.object.Object obj = World.getObjet(this.objectId);
		if (obj == null) {
			return;
		}
		obj.setTemplate(this.template);
		this.pdv = 1;
		this.corpulence = 0;
		this.quaEat = 0;
		this.lastEatDate = System.currentTimeMillis();
		obj.getTxtStat().remove(800);
		obj.getTxtStat().put(800, Integer.toHexString(this.pdv));
		Database.getStatique().getItemData().save(obj, false);
		Database.getStatique().getPets_dataData().update(this);
	}

	public void restoreLife(final Player p) {
		final org.aestia.object.Object obj = World.getObjet(this.objectId);
		if (obj == null) {
			return;
		}
		final Pet pets = World.getPets(obj.getTemplate().getId());
		if (pets == null) {
			return;
		}
		if (this.pdv >= 10) {
			SocketManager.GAME_SEND_Im_PACKET(p, "032");
		} else {
			if (this.pdv >= 10 || this.pdv <= 0) {
				return;
			}
			++this.pdv;
			obj.getTxtStat().remove(800);
			obj.getTxtStat().put(800, Integer.toHexString(this.pdv));
			SocketManager.GAME_SEND_Im_PACKET(p, "032");
		}
		Database.getStatique().getPets_dataData().update(this);
	}

	public void giveEpo(final Player p) {
		final org.aestia.object.Object obj = World.getObjet(this.objectId);
		if (obj == null) {
			return;
		}
		final Pet pets = World.getPets(obj.getTemplate().getId());
		if (pets == null) {
			return;
		}
		if (this.isEupeoh) {
			return;
		}
		obj.getTxtStat().put(940, Integer.toHexString(1));
		SocketManager.GAME_SEND_Im_PACKET(p, "032");
		SocketManager.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(p, obj);
		Database.getStatique().getPets_dataData().update(this);
	}
}

// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;
import org.aestia.common.Formulas;
import org.aestia.common.Pathfinding;
import org.aestia.common.SocketManager;
import org.aestia.entity.monster.Monster;
import org.aestia.fight.spells.SpellEffect;
import org.aestia.game.GameAction;
import org.aestia.game.GameServer;
import org.aestia.game.world.World;
import org.aestia.kernel.Config;
import org.aestia.kernel.Constant;
import org.aestia.map.Case;
import org.aestia.map.InteractiveObject;
import org.aestia.object.ObjectTemplate;

public class JobAction {
	private int id;
	private int min;
	private int max;
	private boolean isCraft;
	private int chan;
	private int time;
	private int xpWin;
	public Map<Integer, Integer> ingredients;
	public Map<Integer, Integer> lastCraft;
	public Player player;
	public String data;
	public boolean broke;
	public boolean broken;
	private int reConfigingRunes;
	public boolean isRepeat;
	private JobStat SM;
	private JobCraft jobCraft;
	
	private InteractiveObject IO;

	public JobAction(final int sk, final int min, final int max, final boolean craft, final int arg, final int xpWin) {
		this.min = 1;
		this.max = 1;
		this.chan = 100;
		this.time = 0;
		this.xpWin = 0;
		this.ingredients = new TreeMap<Integer, Integer>();
		this.lastCraft = new TreeMap<Integer, Integer>();
		this.data = "";
		this.broke = false;
		this.broken = false;
		this.reConfigingRunes = -1;
		this.isRepeat = false;
		this.id = sk;
		this.min = min;
		this.max = max;
		this.isCraft = craft;
		if (craft) {
			this.chan = arg;
		} else {
			this.time = arg;
		}
		this.xpWin = xpWin;
	}

	public int getId() {
		return this.id;
	}

	public int getMin() {
		return this.min;
	}

	public int getMax() {
		return this.max;
	}

	public boolean isCraft() {
		return this.isCraft;
	}

	public int getChance() {
		return this.chan;
	}

	public int getTime() {
		return this.time;
	}

	public int getXpWin() {
		return this.xpWin;
	}

	public JobStat getJobStat() {
		return this.SM;
	}

	public JobCraft getJobCraft() {
		return this.jobCraft;
	}

	public void startCraft(final Player P) {
		this.jobCraft = new JobCraft(this, P);
	}

	public void startAction(final Player P, final InteractiveObject IO, final GameAction GA, final Case cell,
			final JobStat SM) {
		System.err.println("Start action");
		this.SM = SM;
		this.IO = IO;
		this.player = P;
		if (P.getObjetByPos(1) != null && SM.getTemplate().getId() == 36
				&& World.getMetier(36).isValidTool(P.getObjetByPos(1).getTemplate().getId())) {
			final int dist = Pathfinding.getDistanceBetween(P.getCurMap(), P.getCurCell().getId(), cell.getId());
			final int distItem = JobConstant.getDistCanne(P.getObjetByPos(1).getTemplate().getId());
			if (distItem < dist) {
				SocketManager.GAME_SEND_MESSAGE(P, "Vous \u00eates trop loin pour pouvoir p\u00eacher ce poisson !");
				SocketManager.GAME_SEND_GA_PACKET(P.getGameClient(), "", "0", "", "");
				P.setCurJobAction(null);
				P.setDoAction(false);
				return;
			}
		}
		if (!this.isCraft) {
			System.err.println("!isCraft");
			P.setCurJobAction(this);
			IO.setInteractive(false);
			IO.setState(2);
			SocketManager.GAME_SEND_GA_PACKET_TO_MAP(P.getCurMap(), new StringBuilder().append(GA.id).toString(), 501,
					new StringBuilder(String.valueOf(P.getId())).toString(),
					String.valueOf(cell.getId()) + "," + this.time);
			SocketManager.GAME_SEND_GDF_PACKET_TO_MAP(P.getCurMap(), cell);
		} else {
			System.err.println("isCraft");
			P.set_away(true);
			IO.setState(2);
			P.setCurJobAction(this);
			SocketManager.GAME_SEND_ECK_PACKET(P, 3, String.valueOf(this.min) + ";" + this.id);
			SocketManager.GAME_SEND_GDF_PACKET_TO_MAP(P.getCurMap(), cell);
		}
		System.err.println("Finish");
	}

	public void startAction(final Player P, final InteractiveObject IO, final GameAction GA, final Case cell) {
		System.err.println("Start action");
		(this.player = P).set_away(true);
		IO.setState(2);
		P.setCurJobAction(this);
		SocketManager.GAME_SEND_ECK_PACKET(P, 3, String.valueOf(this.min) + ";" + this.id);
		SocketManager.GAME_SEND_GDF_PACKET_TO_MAP(P.getCurMap(), cell);
	}
	
	public void cancel(Case cell,Player P) {
			IO.setInteractive(true);
			IO.run();
	}

	public void endAction(final Player P, final InteractiveObject IO, final GameAction GA, final Case cell) {
		P.setDoAction(false);
		if (IO == null) {
			return;
		}
		if (!this.isCraft) {
			IO.setState(3);
			IO.launch();
			SocketManager.GAME_SEND_GDF_PACKET_TO_MAP(P.getCurMap(), cell);
			final int qua = (this.max > this.min) ? Formulas.getRandomValue(this.min, this.max) : this.min;
			if (this.SM.getTemplate().getId() == 36) {
				if (qua > 0) {
					this.SM.addXp(P, this.getXpWin() * Config.getInstance().rateJob, true);
				}
			} else {
				this.SM.addXp(P, this.getXpWin() * Config.getInstance().rateJob, true);
			}
			final int tID = JobConstant.getObjectByJobSkill(this.id);
			if (this.SM.getTemplate().getId() == 36 && qua > 0 && Formulas.getRandomValue(1, 1000) <= 2) {
				final int _tID = JobConstant.getPoissonRare(tID);
				if (_tID != -1) {
					final ObjectTemplate _T = World.getObjTemplate(_tID);
					if (_T != null) {
						final org.aestia.object.Object _O = _T.createNewItem(qua, false);
						if (P.addObjet(_O, true)) {
							World.addObjet(_O, true);
						}
					}
				}
			}
			final ObjectTemplate T = World.getObjTemplate(tID);
			if (T == null) {
				return;
			}
			final org.aestia.object.Object O = T.createNewItem(qua, false);
			if (P.addObjet(O, true)) {
				World.addObjet(O, true);
			}
			SocketManager.GAME_SEND_IQ_PACKET(P, P.getId(), qua);
			SocketManager.GAME_SEND_Ow_PACKET(P);
			if (P.getMetierBySkill(this.id).get_lvl() >= 20 && Formulas.getRandomValue(1, 40) > 39) {
				final int[][] protectors = JobConstant.JOB_PROTECTORS;
				for (int i = 0; i < protectors.length; ++i) {
					if (tID == protectors[i][1]) {
						final int monsterId = protectors[i][0];
						final int monsterLvl = JobConstant.getProtectorLvl(P.getLevel());
						P.getCurMap().startFightVersusProtectors(P, new Monster.MobGroup(P.getCurMap().nextObjectId,
								cell.getId(), String.valueOf(monsterId) + "," + monsterLvl + "," + monsterLvl));
						break;
					}
				}
			}
		}
		P.set_away(false);
	}

	public void modifIngredient(final Player P, final int guid, final int qua) {
		int q = (this.ingredients.get(guid) == null) ? 0 : this.ingredients.get(guid);
		this.ingredients.remove(guid);
		q += qua;
		if (q > 0) {
			this.ingredients.put(guid, q);
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(P, 'O', "+", String.valueOf(guid) + "|" + q);
		} else {
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(P, 'O', "-", new StringBuilder(String.valueOf(guid)).toString());
		}
	}

	public synchronized void craft(final boolean isRepeat, final int repeat) {
		if (!this.isCraft) {
			return;
		}
		boolean signed = false;
		if (this.id == 1 || this.id == 113 || this.id == 115 || this.id == 116 || this.id == 117 || this.id == 118
				|| this.id == 119 || this.id == 120 || (this.id >= 163 && this.id <= 169)) {
			this.doFmCraft(isRepeat, repeat);
			return;
		}
		final Map<Integer, Integer> items = new TreeMap<Integer, Integer>();
		for (final Map.Entry<Integer, Integer> e : this.ingredients.entrySet()) {
			if (!this.player.hasItemGuid(e.getKey())) {
				SocketManager.GAME_SEND_Ec_PACKET(this.player, "EI");
				GameServer.addToLog("/!\\ " + this.player.getName() + " essaye de crafter avec un objet qu'il n'a pas");
				return;
			}
			final org.aestia.object.Object obj = World.getObjet(e.getKey());
			if (obj == null) {
				SocketManager.GAME_SEND_Ec_PACKET(this.player, "EI");
				GameServer.addToLog(
						"/!\\ " + this.player.getName() + " essaye de crafter avec un objet qui n'existe pas");
				return;
			}
			if (obj.getQuantity() < e.getValue()) {
				SocketManager.GAME_SEND_Ec_PACKET(this.player, "EI");
				GameServer.addToLog("/!\\ " + this.player.getName()
						+ " essaye de crafter avec un objet dont la quantite est trop faible");
				return;
			}
			final int newQua = obj.getQuantity() - e.getValue();
			if (newQua < 0) {
				return;
			}
			if (newQua == 0) {
				this.player.removeItem(e.getKey());
				World.removeItem(e.getKey());
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.player, e.getKey());
			} else {
				obj.setQuantity(newQua);
				SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player, obj);
			}
			items.put(obj.getTemplate().getId(), e.getValue());
		}
		if (items.containsKey(7508)) {
			signed = true;
		}
		items.remove(7508);
		SocketManager.GAME_SEND_Ow_PACKET(this.player);
		boolean isUnjobSkill = false;
		switch (this.id) {
		case 110:
		case 151: {
			isUnjobSkill = true;
			break;
		}
		}
		if (!isUnjobSkill) {
			final JobStat SM = this.player.getMetierBySkill(this.id);
			final int tID = World.getObjectByIngredientForJob(SM.getTemplate().getListBySkill(this.id), items);
			if (tID == -1 || !SM.getTemplate().canCraft(this.id, tID)) {
				SocketManager.GAME_SEND_Ec_PACKET(this.player, "EI");
				SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "-");
				this.ingredients.clear();
				return;
			}
			final int chan = JobConstant.getChanceByNbrCaseByLvl(SM.get_lvl(), this.ingredients.size());
			final int jet = Formulas.getRandomValue(1, 100);
			boolean success = chan >= jet;
			switch (this.id) {
			case 109: {
				success = true;
				break;
			}
			}
			if (!success) {
				SocketManager.GAME_SEND_Ec_PACKET(this.player, "EF");
				SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "-" + tID);
				SocketManager.GAME_SEND_Im_PACKET(this.player, "0118");
			} else {
				final org.aestia.object.Object newObj = World.getObjTemplate(tID).createNewItem(1, false);
				if (signed) {
					newObj.addTxtStat(988, this.player.getName());
				}
				boolean add = true;
				int guid = newObj.getGuid();
					for (final Map.Entry<Integer, org.aestia.object.Object> entry : this.player.getItems().entrySet()) {
						final org.aestia.object.Object obj2 = entry.getValue();
						if (obj2.getTemplate().getId() == newObj.getTemplate().getId()
								&& obj2.getTxtStat().equals(newObj.getTxtStat())
								&& obj2.getStats().isSameStats(newObj.getStats()) && obj2.getPosition() == -1) {
							obj2.setQuantity(obj2.getQuantity() + newObj.getQuantity());
							SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player, obj2);
							add = false;
							guid = obj2.getGuid();
						}
					}
					if (add) {
						this.player.getItems().put(newObj.getGuid(), newObj);
						SocketManager.GAME_SEND_OAKO_PACKET(this.player, newObj);
						World.addObjet(newObj, true);
					}
				// monitorexit(this.player.getItems())
				SocketManager.GAME_SEND_Ow_PACKET(this.player);
				SocketManager.GAME_SEND_Em_PACKET(this.player,"KO+" + guid + "|1|" + tID + "|" + newObj.parseStatsString().replace(";", "#"));
				SocketManager.GAME_SEND_Ec_PACKET(this.player, "K;" + tID);
				SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "+" + tID);
			}
			int winXP = 0;
			if (success) {
				winXP = Formulas.calculXpWinCraft(SM.get_lvl(), this.ingredients.size()) * Config.getInstance().rateJob;
			} else if (!SM.getTemplate().isMaging()) {
				winXP = Formulas.calculXpWinCraft(SM.get_lvl(), this.ingredients.size()) * Config.getInstance().rateJob;
			}
			if (winXP > 0) {
				SM.addXp(this.player, winXP, true);
				final ArrayList<JobStat> SMs = new ArrayList<JobStat>();
				SMs.add(SM);
				SocketManager.GAME_SEND_JX_PACKET(this.player, SMs);
			}
		} else {
			final int templateId = World.getObjectByIngredientForJob(World.getMetier(this.id).getListBySkill(this.id),
					items);
			if (templateId == -1 || !World.getMetier(this.id).canCraft(this.id, templateId)) {
				SocketManager.GAME_SEND_Ec_PACKET(this.player, "EI");
				SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "-");
				this.ingredients.clear();
				return;
			}
			final org.aestia.object.Object newObj2 = World.getObjTemplate(templateId).createNewItem(1, false);
			if (signed) {
				newObj2.addTxtStat(988, this.player.getName());
			}
			boolean add2 = true;
			int guid2 = newObj2.getGuid();
			synchronized (this.player.getItems()) {
				for (final Map.Entry<Integer, org.aestia.object.Object> entry2 : this.player.getItems().entrySet()) {
					final org.aestia.object.Object obj3 = entry2.getValue();
					if (obj3.getTemplate().getId() == newObj2.getTemplate().getId()
							&& obj3.getTxtStat().equals(newObj2.getTxtStat())
							&& obj3.getStats().isSameStats(newObj2.getStats()) && obj3.getPosition() == -1) {
						obj3.setQuantity(obj3.getQuantity() + newObj2.getQuantity());
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player, obj3);
						add2 = false;
						guid2 = obj3.getGuid();
					}
				}
				if (add2) {
					this.player.getItems().put(newObj2.getGuid(), newObj2);
					SocketManager.GAME_SEND_OAKO_PACKET(this.player, newObj2);
					World.addObjet(newObj2, true);
				}
			}
			SocketManager.GAME_SEND_Ow_PACKET(this.player);
			SocketManager.GAME_SEND_Em_PACKET(this.player,"KO+" + guid2 + "|1|" + templateId + "|" + newObj2.parseStatsString().replace(";", "#"));
			SocketManager.GAME_SEND_Ec_PACKET(this.player, "K;" + templateId);
			SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "+" + templateId);
		}
		this.lastCraft.clear();
		this.lastCraft.putAll(this.ingredients);
		this.ingredients.clear();
	}

	public int sizeList(final Map<Player, ArrayList<World.Couple<Integer, Integer>>> list) {
		final Map<Integer, Integer> playerItems = new HashMap<Integer, Integer>();
		for (final Map.Entry<Player, ArrayList<World.Couple<Integer, Integer>>> entry : list.entrySet()) {
			for (final World.Couple<Integer, Integer> couple : entry.getValue()) {
				final org.aestia.object.Object ob = World.getObjet(couple.first);
				if (ob == null) {
					continue;
				}
				final ObjectTemplate temp = ob.getTemplate();
				if (temp == null) {
					continue;
				}
				if (temp.getId() == 7508) {
					continue;
				}
				playerItems.put(couple.first, couple.second);
			}
		}
		return playerItems.size();
	}

	public boolean craftPublicMode(final Player crafter, final Player receiver,
			final Map<Player, ArrayList<World.Couple<Integer, Integer>>> list) {
		if (!this.isCraft) {
			return false;
		}
		this.player = crafter;
		boolean signed = false;
		final Map<Integer, Integer> items = new HashMap<Integer, Integer>();
		for (final Map.Entry<Player, ArrayList<World.Couple<Integer, Integer>>> entry : list.entrySet()) {
			final Player player = entry.getKey();
			final Map<Integer, Integer> playerItems = new HashMap<Integer, Integer>();
			for (final World.Couple<Integer, Integer> couple : entry.getValue()) {
				playerItems.put(couple.first, couple.second);
			}
			for (final Map.Entry<Integer, Integer> e : playerItems.entrySet()) {
				if (!player.hasItemGuid(e.getKey())) {
					SocketManager.GAME_SEND_Ec_PACKET(player, "EI");
					SocketManager.GAME_SEND_Ec_PACKET(this.player, "EI");
					return false;
				}
				final org.aestia.object.Object object = World.getObjet(e.getKey());
				if (object == null) {
					SocketManager.GAME_SEND_Ec_PACKET(player, "EI");
					SocketManager.GAME_SEND_Ec_PACKET(this.player, "EI");
					return false;
				}
				if (object.getQuantity() < e.getValue()) {
					SocketManager.GAME_SEND_Ec_PACKET(player, "EI");
					SocketManager.GAME_SEND_Ec_PACKET(this.player, "EI");
					return false;
				}
				final int newQua = object.getQuantity() - e.getValue();
				if (newQua < 0) {
					return false;
				}
				if (newQua == 0) {
					player.removeItem(e.getKey());
					World.removeItem(e.getKey());
					SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(player, e.getKey());
				} else {
					object.setQuantity(newQua);
					SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(player, object);
				}
				items.put(object.getTemplate().getId(), e.getValue());
			}
		}
		SocketManager.GAME_SEND_Ow_PACKET(this.player);
		final JobStat SM = this.player.getMetierBySkill(this.id);
		if (items.containsKey(7508) && SM.get_lvl() == 100) {
			signed = true;
		}
		items.remove(7508);
		final int template = World.getObjectByIngredientForJob(SM.getTemplate().getListBySkill(this.id), items);
		if (template == -1 || !SM.getTemplate().canCraft(this.id, template)) {
			SocketManager.GAME_SEND_Ec_PACKET(this.player, "EI");
			receiver.send("EcEI");
			SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "-");
			items.clear();
			return false;
		}
		final boolean success = JobConstant.getChanceByNbrCaseByLvl(SM.get_lvl(), items.size()) >= Formulas
				.getRandomValue(1, 100);
		if (!success) {
			SocketManager.GAME_SEND_Ec_PACKET(this.player, "EF");
			SocketManager.GAME_SEND_Ec_PACKET(receiver, "EF");
			SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "-" + template);
			SocketManager.GAME_SEND_Im_PACKET(this.player, "0118");
		} else {
			final org.aestia.object.Object newObj = World.getObjTemplate(template).createNewItem(1, false);
			if (signed) {
				newObj.addTxtStat(988, this.player.getName());
			}
			boolean add = true;
			int guid = newObj.getGuid();
			synchronized (receiver.getItems()) {
				for (final Map.Entry<Integer, org.aestia.object.Object> entry2 : receiver.getItems().entrySet()) {
					final org.aestia.object.Object obj = entry2.getValue();
					if (obj.getTemplate().getId() == newObj.getTemplate().getId()
							&& obj.getTxtStat().equals(newObj.getTxtStat())
							&& obj.getStats().isSameStats(newObj.getStats()) && obj.getPosition() == -1) {
						obj.setQuantity(obj.getQuantity() + newObj.getQuantity());
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(receiver, obj);
						add = false;
						guid = obj.getGuid();
					}
				}
				if (add) {
					receiver.getItems().put(newObj.getGuid(), newObj);
					SocketManager.GAME_SEND_OAKO_PACKET(receiver, newObj);
					World.addObjet(newObj, true);
				}
			}
			// monitorexit(receiver.getItems())
			final String stats = newObj.parseStatsString();
			this.player.send("ErKO+" + guid + "|1|" + template + "|" + stats);
			receiver.send("ErKO+" + guid + "|1|" + template + "|" + stats);
			this.player.send("EcK;" + template + ";T" + receiver.getName() + ";" + stats);
			receiver.send("EcK;" + template + ";B" + crafter.getName() + ";" + stats);
			SocketManager.GAME_SEND_Ow_PACKET(this.player);
			SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "+" + template);
		}
		int winXP = Formulas.calculXpWinCraft(SM.get_lvl(), this.ingredients.size()) * Config.getInstance().rateJob;
		if (SM.getTemplate().getId() == 28 && winXP == 1) {
			winXP = 10;
		}
		if (success) {
			SM.addXp(this.player, winXP, true);
			final ArrayList<JobStat> SMs = new ArrayList<JobStat>();
			SMs.add(SM);
			SocketManager.GAME_SEND_JX_PACKET(this.player, SMs);
		}
		this.ingredients.clear();
		return success;
	}

	public void putLastCraftIngredients() {
		if (this.player == null) {
			return;
		}
		if (this.lastCraft == null) {
			return;
		}
		if (!this.ingredients.isEmpty()) {
			return;
		}
		this.ingredients.clear();
		this.ingredients.putAll(this.lastCraft);
		for (final Map.Entry<Integer, Integer> e : this.ingredients.entrySet()) {
			if (World.getObjet(e.getKey()) == null) {
				return;
			}
			if (World.getObjet(e.getKey()).getQuantity() < e.getValue()) {
				return;
			}
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(this.player, 'O', "+", e.getKey() + "|" + e.getValue());
		}
	}

	public void resetCraft() {
		this.ingredients.clear();
		this.lastCraft.clear();
	}

	private synchronized void doFmCraft(final boolean isReapeat, final int repeat) {
		boolean isSigningRune = false;
		org.aestia.object.Object objectFm = null;
		org.aestia.object.Object signingRune = null;
		org.aestia.object.Object runeOrPotion = null;
		int lvlElementRune = 0;
		int statsID = -1;
		int lvlQuaStatsRune = 0;
		int statsAdd = 0;
		int deleteID = -1;
		int poid = 0;
		int idRune = 0;
		boolean bonusRune = false;
		String statsObjectFm = "-1";
		for (final int idIngredient : this.ingredients.keySet()) {
			final org.aestia.object.Object ing = World.getObjet(idIngredient);
			if (ing == null || !this.player.hasItemGuid(idIngredient)) {
				SocketManager.GAME_SEND_Ec_PACKET(this.player, "EI");
				SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "-");
				this.ingredients.clear();
				return;
			}
			final int templateID = ing.getTemplate().getId();
			if (ing.getTemplate().getType() == 78) {
				idRune = idIngredient;
			}
			switch (templateID) {
			case 1333: {
				statsID = 99;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1335: {
				statsID = 96;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1337: {
				statsID = 98;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1338: {
				statsID = 97;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1340: {
				statsID = 97;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1341: {
				statsID = 96;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1342: {
				statsID = 98;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1343: {
				statsID = 99;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1345: {
				statsID = 99;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1346: {
				statsID = 96;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1347: {
				statsID = 98;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1348: {
				statsID = 97;
				lvlElementRune = ing.getTemplate().getLevel();
				runeOrPotion = ing;
				continue;
			}
			case 1519: {
				runeOrPotion = ing;
				statsObjectFm = "76";
				statsAdd = 1;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1521: {
				runeOrPotion = ing;
				statsObjectFm = "7c";
				statsAdd = 1;
				poid = 6;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1522: {
				runeOrPotion = ing;
				statsObjectFm = "7e";
				statsAdd = 1;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1523: {
				runeOrPotion = ing;
				statsObjectFm = "7d";
				statsAdd = 3;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1524: {
				runeOrPotion = ing;
				statsObjectFm = "77";
				statsAdd = 1;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1525: {
				runeOrPotion = ing;
				statsObjectFm = "7b";
				statsAdd = 1;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1545: {
				runeOrPotion = ing;
				statsObjectFm = "76";
				statsAdd = 3;
				poid = 3;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1546: {
				runeOrPotion = ing;
				statsObjectFm = "7c";
				statsAdd = 3;
				poid = 18;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1547: {
				runeOrPotion = ing;
				statsObjectFm = "7e";
				statsAdd = 3;
				poid = 3;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1548: {
				runeOrPotion = ing;
				statsObjectFm = "7d";
				statsAdd = 10;
				poid = 10;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1549: {
				runeOrPotion = ing;
				statsObjectFm = "77";
				statsAdd = 3;
				poid = 3;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1550: {
				runeOrPotion = ing;
				statsObjectFm = "7b";
				statsAdd = 3;
				poid = 10;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1551: {
				runeOrPotion = ing;
				statsObjectFm = "76";
				statsAdd = 10;
				poid = 10;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1552: {
				runeOrPotion = ing;
				statsObjectFm = "7c";
				statsAdd = 10;
				poid = 50;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1553: {
				runeOrPotion = ing;
				statsObjectFm = "7e";
				statsAdd = 10;
				poid = 10;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1554: {
				runeOrPotion = ing;
				statsObjectFm = "7d";
				statsAdd = 30;
				poid = 10;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1555: {
				runeOrPotion = ing;
				statsObjectFm = "77";
				statsAdd = 10;
				poid = 10;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1556: {
				runeOrPotion = ing;
				statsObjectFm = "7b";
				statsAdd = 10;
				poid = 10;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1557: {
				runeOrPotion = ing;
				statsObjectFm = "6f";
				statsAdd = 1;
				poid = 100;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 1558: {
				runeOrPotion = ing;
				statsObjectFm = "80";
				statsAdd = 1;
				poid = 90;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7433: {
				runeOrPotion = ing;
				statsObjectFm = "73";
				statsAdd = 1;
				poid = 30;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7434: {
				runeOrPotion = ing;
				statsObjectFm = "b2";
				statsAdd = 1;
				poid = 20;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7435: {
				runeOrPotion = ing;
				statsObjectFm = "70";
				statsAdd = 1;
				poid = 20;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7436: {
				runeOrPotion = ing;
				statsObjectFm = "8a";
				statsAdd = 1;
				poid = 2;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7437: {
				runeOrPotion = ing;
				statsObjectFm = "dc";
				statsAdd = 1;
				poid = 2;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7438: {
				runeOrPotion = ing;
				statsObjectFm = "75";
				statsAdd = 1;
				poid = 50;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7442: {
				runeOrPotion = ing;
				statsObjectFm = "b6";
				statsAdd = 1;
				poid = 30;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7443: {
				runeOrPotion = ing;
				statsObjectFm = "9e";
				statsAdd = 10;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7444: {
				runeOrPotion = ing;
				statsObjectFm = "9e";
				statsAdd = 30;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7445: {
				runeOrPotion = ing;
				statsObjectFm = "9e";
				statsAdd = 100;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7446: {
				runeOrPotion = ing;
				statsObjectFm = "e1";
				statsAdd = 1;
				poid = 15;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7447: {
				runeOrPotion = ing;
				statsObjectFm = "e2";
				statsAdd = 1;
				poid = 2;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7448: {
				runeOrPotion = ing;
				statsObjectFm = "ae";
				statsAdd = 10;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7449: {
				runeOrPotion = ing;
				statsObjectFm = "ae";
				statsAdd = 30;
				poid = 3;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7450: {
				runeOrPotion = ing;
				statsObjectFm = "ae";
				statsAdd = 100;
				poid = 10;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7451: {
				runeOrPotion = ing;
				statsObjectFm = "b0";
				statsAdd = 1;
				poid = 5;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7452: {
				runeOrPotion = ing;
				statsObjectFm = "f3";
				statsAdd = 1;
				poid = 4;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7453: {
				runeOrPotion = ing;
				statsObjectFm = "f2";
				statsAdd = 1;
				poid = 4;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7454: {
				runeOrPotion = ing;
				statsObjectFm = "f1";
				statsAdd = 1;
				poid = 4;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7455: {
				runeOrPotion = ing;
				statsObjectFm = "f0";
				statsAdd = 1;
				poid = 4;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7456: {
				runeOrPotion = ing;
				statsObjectFm = "f4";
				statsAdd = 1;
				poid = 4;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7457: {
				runeOrPotion = ing;
				statsObjectFm = "d5";
				statsAdd = 1;
				poid = 5;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7458: {
				runeOrPotion = ing;
				statsObjectFm = "d4";
				statsAdd = 1;
				poid = 5;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7459: {
				runeOrPotion = ing;
				statsObjectFm = "d2";
				statsAdd = 1;
				poid = 5;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7460: {
				runeOrPotion = ing;
				statsObjectFm = "d6";
				statsAdd = 1;
				poid = 5;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7560: {
				runeOrPotion = ing;
				statsObjectFm = "d3";
				statsAdd = 1;
				poid = 5;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 8379: {
				runeOrPotion = ing;
				statsObjectFm = "7d";
				statsAdd = 10;
				poid = 10;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 10662: {
				runeOrPotion = ing;
				statsObjectFm = "b0";
				statsAdd = 3;
				poid = 15;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 7508: {
				isSigningRune = true;
				signingRune = ing;
				continue;
			}
			case 11118: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "76";
				statsAdd = 15;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 11119: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "7c";
				statsAdd = 15;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 11120: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "7e";
				statsAdd = 15;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 11121: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "7d";
				statsAdd = 45;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 11122: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "77";
				statsAdd = 15;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 11123: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "7b";
				statsAdd = 15;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 11124: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "b0";
				statsAdd = 10;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 11125: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "73";
				statsAdd = 3;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 11126: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "b2";
				statsAdd = 5;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 11127: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "70";
				statsAdd = 5;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 11128: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "8a";
				statsAdd = 10;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			case 11129: {
				bonusRune = true;
				runeOrPotion = ing;
				statsObjectFm = "dc";
				statsAdd = 5;
				poid = 1;
				lvlQuaStatsRune = ing.getTemplate().getLevel();
				continue;
			}
			default: {
				final int type = ing.getTemplate().getType();
				if ((type >= 1 && type <= 11) || (type >= 16 && type <= 22) || type == 81 || type == 102 || type == 114
						|| ing.getTemplate().getPACost() > 0) {
					objectFm = ing;
					SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK_FM(this.player.getGameClient(), 'O', "+",
							String.valueOf(objectFm.getGuid()) + "|" + 1);
					deleteID = idIngredient;
					final org.aestia.object.Object newObj = org.aestia.object.Object.getCloneObjet(objectFm, 1);
					if (objectFm.getQuantity() > 1) {
						final int newQuant = objectFm.getQuantity() - 1;
						objectFm.setQuantity(newQuant);
						SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player, objectFm);
					} else {
						World.removeItem(idIngredient);
						this.player.removeItem(idIngredient);
						SocketManager.GAME_SEND_DELETE_STATS_ITEM_FM(this.player, idIngredient);
					}
					objectFm = newObj;
					continue;
				}
				continue;
			}
			}
		}
		final double poid2 = World.getPwrPerEffet(Integer.parseInt(statsObjectFm, 16));
		if (poid2 > 0.0) {
			poid = statsAdd * (int) poid2;
		}
		if (this.SM == null || objectFm == null || runeOrPotion == null) {
			if (objectFm != null) {
				World.addObjet(objectFm, true);
				this.player.addObjet(objectFm);
			}
			SocketManager.GAME_SEND_Ec_PACKET(this.player, "EI");
			SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "-");
			this.ingredients.clear();
			return;
		}
		if (deleteID != -1) {
			this.ingredients.remove(deleteID);
		}
		final ObjectTemplate objTemplate = objectFm.getTemplate();
		int chance = 0;
		final int lvlJob = this.SM.get_lvl();
		int currentWeightTotal = 1;
		int pwrPerte = 0;
		ArrayList<Integer> chances = new ArrayList<Integer>();
		final int objTemplateID = objTemplate.getId();
		final String statStringObj = objectFm.parseStatsString();
		if (lvlElementRune > 0 && lvlQuaStatsRune == 0) {
			chance = Formulas.calculChanceByElement(lvlJob, objTemplate.getLevel(), lvlElementRune);
			if (chance > 100 - lvlJob / 20) {
				chance = 100 - lvlJob / 20;
			}
			if (chance < lvlJob / 20) {
				chance = lvlJob / 20;
			}
			chances.add(0, chance);
			chances.add(1, 0);
			chances.add(2, 100 - chance);
		} else if (lvlQuaStatsRune > 0 && lvlElementRune == 0) {
			int currentWeightStats = 1;
			if (!statStringObj.isEmpty()) {
				currentWeightTotal = currentTotalWeigthBase(statStringObj, objectFm);
				currentWeightStats = currentWeithStats(objectFm, statsObjectFm);
			}
			int currentTotalBase = WeithTotalBase(objTemplateID);
			final int currentMinBase = WeithTotalBaseMin(objTemplateID);
			if (currentTotalBase < 0) {
				currentTotalBase = 0;
			}
			if (currentWeightStats < 0) {
				currentWeightStats = 0;
			}
			if (currentWeightTotal < 0) {
				currentWeightTotal = 0;
			}
			float coef = 1.0f;
			final int baseStats = Job.viewBaseStatsItem(objectFm, statsObjectFm);
			final int currentStats = Job.viewActualStatsItem(objectFm, statsObjectFm);
			if ((baseStats == 1 && currentStats == 1) || (baseStats == 1 && currentStats == 0)) {
				coef = 1.0f;
			} else if (baseStats == 2 && currentStats == 2) {
				coef = 0.5f;
			} else if ((baseStats == 0 && currentStats == 0) || (baseStats == 0 && currentStats == 1)) {
				coef = 0.25f;
			}
			float x = 1.0f;
			boolean canFM = true;
			final int statMax = getStatBaseMaxs(objectFm.getTemplate(), statsObjectFm);
			final int actuelJet = Job.getActualJet(objectFm, statsObjectFm);
			if (actuelJet > statMax) {
				x = 0.8f;
				final int overPerEffect = (int) World.getOverPerEffet(Integer.parseInt(statsObjectFm, 16));
				if (statMax == 0) {
					if (actuelJet >= statMax + overPerEffect + 1) {
						canFM = false;
					} else if (actuelJet >= statMax + overPerEffect) {
						canFM = false;
					}
				}
			}
			if (lvlJob < (int) Math.floor(objTemplate.getLevel() / 2)) {
				canFM = false;
			}
			final int diff = (int) Math.abs(currentTotalBase * 1.3f - currentWeightTotal);
			if (canFM) {
				if (objectFm.getTemplate().getId() == 2469 && poid == 100) {
					chances.add(0, 49);
					chances.add(1, 18);
				} else {
					chances = Formulas.chanceFM(currentTotalBase, currentMinBase, currentWeightTotal,
							currentWeightStats, poid, diff, coef, statMax,
							getStatBaseMins(objectFm.getTemplate(), statsObjectFm),
							currentStats(objectFm, statsObjectFm), x, bonusRune, statsAdd);
				}
			} else {
				chances.add(0, 0);
				chances.add(1, 0);
			}
		}
		final int aleatoryChance = Formulas.getRandomValue(1, 100);
		final int SC = chances.get(0);
		final int SN = chances.get(1);
		final boolean successC = aleatoryChance <= SC;
		final boolean successN = aleatoryChance <= SC + SN;
		if (successC || successN) {
			final int winXP = Formulas.calculXpWinFm(objectFm.getTemplate().getLevel(), poid)
					* Config.getInstance().rateJob;
			if (winXP > 0) {
				this.SM.addXp(this.player, winXP, true);
				final ArrayList<JobStat> SMs = new ArrayList<JobStat>();
				SMs.add(this.SM);
				SocketManager.GAME_SEND_JX_PACKET(this.player, SMs);
			}
		}
		if (successC) {
			int coef2 = 0;
			pwrPerte = 0;
			if (lvlElementRune == 1) {
				coef2 = 50;
			} else if (lvlElementRune == 25) {
				coef2 = 65;
			} else if (lvlElementRune == 50) {
				coef2 = 85;
			}
			if (isSigningRune) {
				objectFm.addTxtStat(985, this.player.getName());
			}
			if (lvlElementRune > 0 && lvlQuaStatsRune == 0) {
				for (final SpellEffect effect : objectFm.getEffects()) {
					if (effect.getEffectID() != 100) {
						continue;
					}
					final String[] infos = effect.getArgs().split(";");
					try {
						final int min = Integer.parseInt(infos[0], 16);
						final int max = Integer.parseInt(infos[1], 16);
						int newMin = min * coef2 / 100;
						final int newMax = max * coef2 / 100;
						if (newMin == 0) {
							newMin = 1;
						}
						final String newRange = "1d" + (newMax - newMin + 1) + "+" + (newMin - 1);
						final String newArgs = String.valueOf(Integer.toHexString(newMin)) + ";"
								+ Integer.toHexString(newMax) + ";-1;-1;0;" + newRange;
						effect.setArgs(newArgs);
						effect.setEffectID(statsID);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (lvlQuaStatsRune > 0 && lvlElementRune == 0) {
				boolean negative = false;
				final int currentStats2 = Job.viewActualStatsItem(objectFm, statsObjectFm);
				if (currentStats2 == 2) {
					if (statsObjectFm.compareTo("7b") == 0) {
						statsObjectFm = "98";
						negative = true;
					}
					if (statsObjectFm.compareTo("77") == 0) {
						statsObjectFm = "9a";
						negative = true;
					}
					if (statsObjectFm.compareTo("7e") == 0) {
						statsObjectFm = "9b";
						negative = true;
					}
					if (statsObjectFm.compareTo("76") == 0) {
						statsObjectFm = "9d";
						negative = true;
					}
					if (statsObjectFm.compareTo("7c") == 0) {
						statsObjectFm = "9c";
						negative = true;
					}
					if (statsObjectFm.compareTo("7d") == 0) {
						statsObjectFm = "99";
						negative = true;
					}
				}
				if (currentStats2 == 1 || currentStats2 == 2) {
					if (statStringObj.isEmpty()) {
						final String statsStr = String.valueOf(statsObjectFm) + "#" + Integer.toHexString(statsAdd)
								+ "#0#0#0d0+" + statsAdd;
						objectFm.clearStats();
						objectFm.parseStringToStats(statsStr);
					} else {
						final String statsStr = objectFm.parseFMStatsString(statsObjectFm, objectFm, statsAdd,
								negative);
						objectFm.clearStats();
						objectFm.parseStringToStats(statsStr);
					}
				} else if (statStringObj.isEmpty()) {
					final String statsStr = String.valueOf(statsObjectFm) + "#" + Integer.toHexString(statsAdd)
							+ "#0#0#0d0+" + statsAdd;
					objectFm.clearStats();
					objectFm.parseStringToStats(statsStr);
				} else {
					final String statsStr = String
							.valueOf(objectFm.parseFMStatsString(statsObjectFm, objectFm, statsAdd, negative)) + ","
							+ statsObjectFm + "#" + Integer.toHexString(statsAdd) + "#0#0#0d0+" + statsAdd;
					objectFm.clearStats();
					objectFm.parseStringToStats(statsStr);
				}
			}
			if (signingRune != null) {
				final int newQua = signingRune.getQuantity() - 1;
				if (newQua <= 0) {
					this.player.removeItem(signingRune.getGuid());
					World.removeItem(signingRune.getGuid());
					SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.player, signingRune.getGuid());
				} else {
					signingRune.setQuantity(newQua);
					SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player, signingRune);
				}
			}
			if (runeOrPotion != null) {
				final int newQua = runeOrPotion.getQuantity() - 1;
				if (newQua <= 0) {
					this.player.removeItem(runeOrPotion.getGuid());
					World.removeItem(runeOrPotion.getGuid());
					SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.player, runeOrPotion.getGuid());
				} else {
					runeOrPotion.setQuantity(newQua);
					SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player, runeOrPotion);
				}
			}
			World.addObjet(objectFm, true);
			this.player.addObjet(objectFm);
			SocketManager.GAME_SEND_Ow_PACKET(this.player);
			final String data = String.valueOf(objectFm.getGuid()) + "|1|" + objectFm.getTemplate().getId() + "|"
					+ objectFm.parseStatsString();
			if (!this.isRepeat) {
				this.reConfigingRunes = -1;
			}
			if (this.reConfigingRunes != 0 || this.broken) {
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK_FM(this.player, 'O', "+", data);
			}
			this.data = data;
			SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "+" + objTemplateID);
			SocketManager.GAME_SEND_Ec_PACKET(this.player, "K;" + objTemplateID);
		} else if (successN) {
			pwrPerte = 0;
			if (isSigningRune) {
				objectFm.addTxtStat(985, this.player.getName());
			}
			boolean negative2 = false;
			final int currentStats3 = Job.viewActualStatsItem(objectFm, statsObjectFm);
			if (currentStats3 == 2) {
				if (statsObjectFm.compareTo("7b") == 0) {
					statsObjectFm = "98";
					negative2 = true;
				}
				if (statsObjectFm.compareTo("77") == 0) {
					statsObjectFm = "9a";
					negative2 = true;
				}
				if (statsObjectFm.compareTo("7e") == 0) {
					statsObjectFm = "9b";
					negative2 = true;
				}
				if (statsObjectFm.compareTo("76") == 0) {
					statsObjectFm = "9d";
					negative2 = true;
				}
				if (statsObjectFm.compareTo("7c") == 0) {
					statsObjectFm = "9c";
					negative2 = true;
				}
				if (statsObjectFm.compareTo("7d") == 0) {
					statsObjectFm = "99";
					negative2 = true;
				}
			}
			if (currentStats3 == 1 || currentStats3 == 2) {
				if (statStringObj.isEmpty()) {
					final String statsStr2 = String.valueOf(statsObjectFm) + "#" + Integer.toHexString(statsAdd)
							+ "#0#0#0d0+" + statsAdd;
					objectFm.clearStats();
					objectFm.parseStringToStats(statsStr2);
				} else {
					String statsStr2 = "";
					if (objectFm.getPuit() <= 0) {
						statsStr2 = objectFm.parseStringStatsEC_FM(objectFm, poid, Integer.parseInt(statsObjectFm, 16));
						objectFm.clearStats();
						objectFm.parseStringToStats(statsStr2);
						pwrPerte = currentWeightTotal - currentTotalWeigthBase(statsStr2, objectFm);
					}
					statsStr2 = objectFm.parseFMStatsString(statsObjectFm, objectFm, statsAdd, negative2);
					objectFm.clearStats();
					objectFm.parseStringToStats(statsStr2);
				}
			} else if (statStringObj.isEmpty()) {
				final String statsStr2 = String.valueOf(statsObjectFm) + "#" + Integer.toHexString(statsAdd)
						+ "#0#0#0d0+" + statsAdd;
				objectFm.clearStats();
				objectFm.parseStringToStats(statsStr2);
			} else {
				String statsStr2 = "";
				if (objectFm.getPuit() <= 0) {
					statsStr2 = objectFm.parseStringStatsEC_FM(objectFm, poid, Integer.parseInt(statsObjectFm, 16));
					objectFm.clearStats();
					objectFm.parseStringToStats(statsStr2);
					pwrPerte = currentWeightTotal - currentTotalWeigthBase(statsStr2, objectFm);
				}
				statsStr2 = String.valueOf(objectFm.parseFMStatsString(statsObjectFm, objectFm, statsAdd, negative2))
						+ "," + statsObjectFm + "#" + Integer.toHexString(statsAdd) + "#0#0#0d0+" + statsAdd;
				objectFm.clearStats();
				objectFm.parseStringToStats(statsStr2);
			}
			if (signingRune != null) {
				final int newQua2 = signingRune.getQuantity() - 1;
				if (newQua2 <= 0) {
					this.player.removeItem(signingRune.getGuid());
					World.removeItem(signingRune.getGuid());
					SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.player, signingRune.getGuid());
				} else {
					signingRune.setQuantity(newQua2);
					SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player, signingRune);
				}
			}
			if (runeOrPotion != null) {
				final int newQua2 = runeOrPotion.getQuantity() - 1;
				if (newQua2 <= 0) {
					this.player.removeItem(runeOrPotion.getGuid());
					World.removeItem(runeOrPotion.getGuid());
					SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.player, runeOrPotion.getGuid());
				} else {
					runeOrPotion.setQuantity(newQua2);
					SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player, runeOrPotion);
				}
			}
			World.addObjet(objectFm, true);
			this.player.addObjet(objectFm);
			SocketManager.GAME_SEND_Ow_PACKET(this.player);
			final String data2 = String.valueOf(objectFm.getGuid()) + "|1|" + objectFm.getTemplate().getId() + "|"
					+ objectFm.parseStatsString();
			if (!this.isRepeat) {
				this.reConfigingRunes = -1;
			}
			if (this.reConfigingRunes != 0 || this.broken) {
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK_FM(this.player, 'O', "+", data2);
			}
			this.data = data2;
			SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "+" + objTemplateID);
			if (pwrPerte > 0) {
				SocketManager.GAME_SEND_Ec_PACKET(this.player, "EF");
				SocketManager.GAME_SEND_Im_PACKET(this.player, "0194");
			} else {
				SocketManager.GAME_SEND_Ec_PACKET(this.player, "K;" + objTemplateID);
			}
		} else {
			pwrPerte = 0;
			if (signingRune != null) {
				final int newQua3 = signingRune.getQuantity() - 1;
				if (newQua3 <= 0) {
					this.player.removeItem(signingRune.getGuid());
					World.removeItem(signingRune.getGuid());
					SocketManager.GAME_SEND_DELETE_STATS_ITEM_FM(this.player, signingRune.getGuid());
				} else {
					signingRune.setQuantity(newQua3);
					SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player, signingRune);
				}
			}
			if (runeOrPotion != null) {
				final int newQua3 = runeOrPotion.getQuantity() - 1;
				if (newQua3 <= 0) {
					this.player.removeItem(runeOrPotion.getGuid());
					World.removeItem(runeOrPotion.getGuid());
					SocketManager.GAME_SEND_DELETE_STATS_ITEM_FM(this.player, runeOrPotion.getGuid());
				} else {
					runeOrPotion.setQuantity(newQua3);
					SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player, runeOrPotion);
				}
			}
			String statsStr3 = "";
			if (!statStringObj.isEmpty()) {
				statsStr3 = objectFm.parseStringStatsEC_FM(objectFm, poid, -1);
				objectFm.clearStats();
				objectFm.parseStringToStats(statsStr3);
				pwrPerte = currentWeightTotal - currentTotalWeigthBase(statsStr3, objectFm);
			}
			World.addObjet(objectFm, true);
			this.player.addObjet(objectFm);
			SocketManager.GAME_SEND_Ow_PACKET(this.player);
			final String data = String.valueOf(objectFm.getGuid()) + "|1|" + objectFm.getTemplate().getId() + "|"
					+ objectFm.parseStatsString();
			if (!this.isRepeat) {
				this.reConfigingRunes = -1;
			}
			if (this.reConfigingRunes != 0 || this.broken) {
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK_FM(this.player, 'O', "+", data);
			}
			this.data = data;
			SocketManager.GAME_SEND_IO_PACKET_TO_MAP(this.player.getCurMap(), this.player.getId(), "-" + objTemplateID);
			SocketManager.GAME_SEND_Ec_PACKET(this.player, "EF");
			if (pwrPerte > 0) {
				SocketManager.GAME_SEND_Im_PACKET(this.player, "0117");
			} else {
				SocketManager.GAME_SEND_Im_PACKET(this.player, "0183");
			}
		}
		objectFm.setPuit(objectFm.getPuit() + pwrPerte - poid);
		this.lastCraft.clear();
		this.lastCraft.putAll(this.ingredients);
		this.lastCraft.put(objectFm.getGuid(), 1);
		int nbRunes = 0;
		if (!this.ingredients.isEmpty() && this.ingredients.get(idRune) != null) {
			if (this.isRepeat) {
				nbRunes = this.ingredients.get(idRune) - repeat;
			} else {
				nbRunes = this.ingredients.get(idRune) - 1;
			}
		}
		this.ingredients.clear();
		if (nbRunes > 0) {
			this.modifIngredient(this.player, idRune, nbRunes);
		}
		this.player.getCurJobAction().modifIngredient(this.player, objectFm.getGuid(), 1);
	}

	public static int getStatBaseMaxs(final ObjectTemplate objMod, final String statsModif) {
		final String[] split = objMod.getStrTemplate().split(",");
		String[] array;
		for (int length = (array = split).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			if (stats[0].toLowerCase().compareTo(statsModif.toLowerCase()) <= 0) {
				if (stats[0].toLowerCase().compareTo(statsModif.toLowerCase()) == 0) {
					int max = Integer.parseInt(stats[2], 16);
					if (max == 0) {
						max = Integer.parseInt(stats[1], 16);
					}
					return max;
				}
			}
		}
		return 0;
	}

	public static int getStatBaseMins(final ObjectTemplate objMod, final String statsModif) {
		final String[] split = objMod.getStrTemplate().split(",");
		String[] array;
		for (int length = (array = split).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			if (stats[0].toLowerCase().compareTo(statsModif.toLowerCase()) <= 0) {
				if (stats[0].toLowerCase().compareTo(statsModif.toLowerCase()) == 0) {
					return Integer.parseInt(stats[1], 16);
				}
			}
		}
		return 0;
	}

	public static int WeithTotalBaseMin(final int objTemplateID) {
		int weight = 0;
		int alt = 0;
		String statsTemplate = "";
		statsTemplate = World.getObjTemplate(objTemplateID).getStrTemplate();
		if (statsTemplate == null || statsTemplate.isEmpty()) {
			return 0;
		}
		final String[] split = statsTemplate.split(",");
		String[] array;
		for (int length = (array = split).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			final int statID = Integer.parseInt(stats[0], 16);
			boolean sig = true;
			int[] armes_EFFECT_IDS;
			for (int length2 = (armes_EFFECT_IDS = Constant.ARMES_EFFECT_IDS).length, j = 0; j < length2; ++j) {
				final int a = armes_EFFECT_IDS[j];
				if (a == statID) {
					sig = false;
				}
			}
			if (sig) {
				String jet = "";
				int value = 1;
				try {
					jet = stats[4];
					value = Formulas.getRandomJet(jet);
					try {
						final int min = value = Integer.parseInt(stats[1], 16);
					} catch (Exception e) {
						value = Formulas.getRandomJet(jet);
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				int statX = 1;
				if (statID == 125 || statID == 158 || statID == 174) {
					statX = 1;
				} else if (statID == 118 || statID == 126 || statID == 119 || statID == 123) {
					statX = 2;
				} else if (statID == 138 || statID == 666 || statID == 226 || statID == 220) {
					statX = 3;
				} else if (statID == 124 || statID == 176) {
					statX = 5;
				} else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244) {
					statX = 7;
				} else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214) {
					statX = 8;
				} else if (statID == 225) {
					statX = 15;
				} else if (statID == 178 || statID == 112) {
					statX = 20;
				} else if (statID == 115 || statID == 182) {
					statX = 30;
				} else if (statID == 117) {
					statX = 50;
				} else if (statID == 128) {
					statX = 90;
				} else if (statID == 111) {
					statX = 100;
				}
				weight = value * statX;
				alt += weight;
			}
		}
		return alt;
	}

	public static int WeithTotalBase(final int objTemplateID) {
		int weight = 0;
		int alt = 0;
		String statsTemplate = "";
		statsTemplate = World.getObjTemplate(objTemplateID).getStrTemplate();
		if (statsTemplate == null || statsTemplate.isEmpty()) {
			return 0;
		}
		final String[] split = statsTemplate.split(",");
		String[] array;
		for (int length = (array = split).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			final int statID = Integer.parseInt(stats[0], 16);
			boolean sig = true;
			int[] armes_EFFECT_IDS;
			for (int length2 = (armes_EFFECT_IDS = Constant.ARMES_EFFECT_IDS).length, j = 0; j < length2; ++j) {
				final int a = armes_EFFECT_IDS[j];
				if (a == statID) {
					sig = false;
				}
			}
			if (sig) {
				String jet = "";
				int value = 1;
				try {
					jet = stats[4];
					value = Formulas.getRandomJet(jet);
					try {
						final int min = Integer.parseInt(stats[1], 16);
						final int max = Integer.parseInt(stats[2], 16);
						value = min;
						if (max != 0) {
							value = max;
						}
					} catch (Exception e) {
						e.printStackTrace();
						value = Formulas.getRandomJet(jet);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				int statX = 1;
				if (statID == 125 || statID == 158 || statID == 174) {
					statX = 1;
				} else if (statID == 118 || statID == 126 || statID == 119 || statID == 123) {
					statX = 2;
				} else if (statID == 138 || statID == 666 || statID == 226 || statID == 220) {
					statX = 3;
				} else if (statID == 124 || statID == 176) {
					statX = 5;
				} else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244) {
					statX = 7;
				} else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214) {
					statX = 8;
				} else if (statID == 225) {
					statX = 15;
				} else if (statID == 178 || statID == 112) {
					statX = 20;
				} else if (statID == 115 || statID == 182) {
					statX = 30;
				} else if (statID == 117) {
					statX = 50;
				} else if (statID == 128) {
					statX = 90;
				} else if (statID == 111) {
					statX = 100;
				}
				weight = value * statX;
				alt += weight;
			}
		}
		return alt;
	}

	public static int currentWeithStats(final org.aestia.object.Object obj, final String statsModif) {
		for (final Map.Entry<Integer, Integer> entry : obj.getStats().getMap().entrySet()) {
			final int statID = entry.getKey();
			if (Integer.toHexString(statID).toLowerCase().compareTo(statsModif.toLowerCase()) > 0) {
				continue;
			}
			if (Integer.toHexString(statID).toLowerCase().compareTo(statsModif.toLowerCase()) == 0) {
				int statX = 1;
				int coef = 1;
				final int BaseStats = Job.viewBaseStatsItem(obj, Integer.toHexString(statID));
				if (BaseStats == 2) {
					coef = 3;
				} else if (BaseStats == 0) {
					coef = 8;
				}
				if (statID == 125 || statID == 158 || statID == 174) {
					statX = 1;
				} else if (statID == 118 || statID == 126 || statID == 119 || statID == 123) {
					statX = 2;
				} else if (statID == 138 || statID == 666 || statID == 226 || statID == 220) {
					statX = 3;
				} else if (statID == 124 || statID == 176) {
					statX = 5;
				} else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244) {
					statX = 7;
				} else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214) {
					statX = 8;
				} else if (statID == 225) {
					statX = 15;
				} else if (statID == 178 || statID == 112) {
					statX = 20;
				} else if (statID == 115 || statID == 182) {
					statX = 30;
				} else if (statID == 117) {
					statX = 50;
				} else if (statID == 128) {
					statX = 90;
				} else if (statID == 111) {
					statX = 100;
				}
				final int Weight = entry.getValue() * statX * coef;
				return Weight;
			}
		}
		return 0;
	}

	public static int currentStats(final org.aestia.object.Object obj, final String statsModif) {
		for (final Map.Entry<Integer, Integer> entry : obj.getStats().getMap().entrySet()) {
			final int statID = entry.getKey();
			if (Integer.toHexString(statID).toLowerCase().compareTo(statsModif.toLowerCase()) > 0) {
				continue;
			}
			if (Integer.toHexString(statID).toLowerCase().compareTo(statsModif.toLowerCase()) == 0) {
				return entry.getValue();
			}
		}
		return 0;
	}

	public static int currentTotalWeigthBase(final String statsModelo, final org.aestia.object.Object obj) {
		if (statsModelo.equalsIgnoreCase("")) {
			return 0;
		}
		int Weigth = 0;
		int Alto = 0;
		final String[] split = statsModelo.split(",");
		String[] array;
		for (int length = (array = split).length, i = 0; i < length; ++i) {
			final String s = array[i];
			final String[] stats = s.split("#");
			final int statID = Integer.parseInt(stats[0], 16);
			if (statID != 985) {
				if (statID != 988) {
					boolean xy = false;
					int[] armes_EFFECT_IDS;
					for (int length2 = (armes_EFFECT_IDS = Constant.ARMES_EFFECT_IDS).length, j = 0; j < length2; ++j) {
						final int a = armes_EFFECT_IDS[j];
						if (a == statID) {
							xy = true;
						}
					}
					if (!xy) {
						String jet = "";
						int qua = 1;
						try {
							jet = stats[4];
							qua = Formulas.getRandomJet(jet);
							try {
								final int min = Integer.parseInt(stats[1], 16);
								final int max = Integer.parseInt(stats[2], 16);
								qua = min;
								if (max != 0) {
									qua = max;
								}
							} catch (Exception e) {
								e.printStackTrace();
								qua = Formulas.getRandomJet(jet);
							}
						} catch (Exception ex) {
						}
						int statX = 1;
						int coef = 1;
						final int statsBase = Job.viewBaseStatsItem(obj, stats[0]);
						if (statsBase == 2) {
							coef = 3;
						} else if (statsBase == 0) {
							coef = 2;
						}
						if (statID == 125 || statID == 158 || statID == 174) {
							statX = 1;
						} else if (statID == 118 || statID == 126 || statID == 119 || statID == 123) {
							statX = 2;
						} else if (statID == 138 || statID == 666 || statID == 226 || statID == 220) {
							statX = 3;
						} else if (statID == 124 || statID == 176) {
							statX = 5;
						} else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244) {
							statX = 7;
						} else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214) {
							statX = 8;
						} else if (statID == 225) {
							statX = 15;
						} else if (statID == 178 || statID == 112) {
							statX = 20;
						} else if (statID == 115 || statID == 182) {
							statX = 30;
						} else if (statID == 117) {
							statX = 50;
						} else if (statID == 128) {
							statX = 90;
						} else if (statID == 111) {
							statX = 100;
						}
						Weigth = qua * statX * coef;
						Alto += Weigth;
					}
				}
			}
		}
		return Alto;
	}
}

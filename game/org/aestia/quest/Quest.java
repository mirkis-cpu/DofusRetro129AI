// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.entity.npc.NpcTemplate;
import org.aestia.game.world.World;
import org.aestia.kernel.Config;
import org.aestia.kernel.Console;
import org.aestia.object.ObjectTemplate;
import org.aestia.other.Action;

public class Quest {
	public static Map<Integer, Quest> questDataList;
	private int id;
	private ArrayList<Quest_Etape> questEtapeList;
	private ArrayList<Quest_Objectif> questObjectifList;
	private NpcTemplate npc;
	private ArrayList<Action> actions;
	private boolean delete;
	private World.Couple<Integer, Integer> condition;

	static {
		Quest.questDataList = new TreeMap<Integer, Quest>();
	}

	public Quest(final int aId, final String questEtape, final String aObjectif, final int aNpc, final String action,
			final String args, final boolean delete, final String condition) {
		this.questEtapeList = new ArrayList<Quest_Etape>();
		this.questObjectifList = new ArrayList<Quest_Objectif>();
		this.npc = null;
		this.actions = new ArrayList<Action>();
		this.condition = null;
		this.id = aId;
		this.delete = delete;
		try {
			if (!questEtape.equalsIgnoreCase("")) {
				final String[] split = questEtape.split(";");
				if (split != null && split.length > 0) {
					String[] array;
					for (int length = (array = split).length, i = 0; i < length; ++i) {
						final String qEtape = array[i];
						final Quest_Etape q_Etape = Quest_Etape.getQuestEtapeById(Integer.parseInt(qEtape));
						q_Etape.setQuestData(this);
						this.questEtapeList.add(q_Etape);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (!aObjectif.equalsIgnoreCase("")) {
				final String[] split = aObjectif.split(";");
				if (split != null && split.length > 0) {
					String[] array2;
					for (int length2 = (array2 = split).length, j = 0; j < length2; ++j) {
						final String qObjectif = array2[j];
						this.questObjectifList.add(Quest_Objectif.getQuestObjectifById(Integer.parseInt(qObjectif)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!condition.equalsIgnoreCase("")) {
			try {
				final String[] split = condition.split(":");
				if (split != null && split.length > 0) {
					this.condition = new World.Couple<Integer, Integer>(Integer.parseInt(split[0]),
							Integer.parseInt(split[1]));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.npc = World.getNPCTemplate(aNpc);
		try {
			if (!action.equalsIgnoreCase("") && !args.equalsIgnoreCase("")) {
				final String[] arguments = args.split(";");
				int nbr = 0;
				String[] split2;
				for (int length3 = (split2 = action.split(",")).length, k = 0; k < length3; ++k) {
					final String loc0 = split2[k];
					final int actionId = Integer.parseInt(loc0);
					final String arg = arguments[nbr];
					this.actions.add(new Action(actionId, arg, "-1", null));
					++nbr;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Console.println("Erreur avec l action et les args de la quete " + this.id + ".", Console.Color.ERROR);
		}
	}

	public boolean isDelete() {
		return this.delete;
	}

	public int getId() {
		return this.id;
	}

	public ArrayList<Quest_Objectif> getObjectifList() {
		return this.questObjectifList;
	}

	public NpcTemplate getNpc_Tmpl() {
		return this.npc;
	}

	public ArrayList<Quest_Etape> getQuestEtapeList() {
		return this.questEtapeList;
	}

	public boolean haveRespectCondition(final Quest_Perso qPerso, final Quest_Etape qEtape) {
		final String condition;
		switch (condition = qEtape.getCondition()) {
		case "0": {
			return true;
		}
		case "1": {
			boolean loc2 = true;
			for (final Quest_Etape aEtape : this.questEtapeList) {
				if (aEtape == null) {
					continue;
				}
				if (aEtape.getId() == qEtape.getId()) {
					continue;
				}
				if (qPerso.isQuestEtapeIsValidate(aEtape)) {
					continue;
				}
				loc2 = false;
			}
			return loc2;
		}
		default:
			break;
		}
		return false;
	}

	public String getGmQuestDataPacket(final Player perso) {
		final Quest_Perso qPerso = perso.getQuestPersoByQuest(this);
		final int loc1 = this.getObjectifCurrent(qPerso);
		final int loc2 = this.getObjectifPrevious(qPerso);
		final int loc3 = this.getNextObjectif(Quest_Objectif.getQuestObjectifById(this.getObjectifCurrent(qPerso)));
		final StringBuilder str = new StringBuilder();
		str.append(this.id).append("|");
		str.append((loc1 > 0) ? loc1 : "");
		str.append("|");
		final StringBuilder str_prev = new StringBuilder();
		boolean loc4 = true;
		for (final Quest_Etape qEtape : this.questEtapeList) {
			if (qEtape.getObjectif() != loc1) {
				continue;
			}
			if (!this.haveRespectCondition(qPerso, qEtape)) {
				continue;
			}
			if (!loc4) {
				str_prev.append(";");
			}
			str_prev.append(qEtape.getId());
			str_prev.append(",");
			str_prev.append(qPerso.isQuestEtapeIsValidate(qEtape) ? 1 : 0);
			loc4 = false;
		}
		str.append(str_prev);
		str.append("|");
		str.append((loc2 > 0) ? loc2 : "").append("|");
		str.append((loc3 > 0) ? loc3 : "");
		if (this.npc != null) {
			str.append("|");
			str.append(this.npc.getInitQuestionId(perso.getCurMap().getId())).append("|");
		}
		return str.toString();
	}

	public Quest_Etape getQuestEtapeCurrent(final Quest_Perso qPerso) {
		for (final Quest_Etape qEtape : this.getQuestEtapeList()) {
			if (!qPerso.isQuestEtapeIsValidate(qEtape)) {
				return qEtape;
			}
		}
		return null;
	}

	public int getObjectifCurrent(final Quest_Perso qPerso) {
		for (final Quest_Etape qEtape : this.questEtapeList) {
			if (qPerso.isQuestEtapeIsValidate(qEtape)) {
				continue;
			}
			return qEtape.getObjectif();
		}
		return 0;
	}

	public int getObjectifPrevious(final Quest_Perso qPerso) {
		if (this.questObjectifList.size() == 1) {
			return 0;
		}
		int previousqObjectif = 0;
		for (final Quest_Objectif qObjectif : this.questObjectifList) {
			if (qObjectif.getId() == this.getObjectifCurrent(qPerso)) {
				return previousqObjectif;
			}
			previousqObjectif = qObjectif.getId();
		}
		return 0;
	}

	public int getNextObjectif(final Quest_Objectif qO) {
		if (qO == null) {
			return 0;
		}
		for (final Quest_Objectif qObjectif : this.questObjectifList) {
			if (qObjectif.getId() == qO.getId()) {
				final int index = this.questObjectifList.indexOf(qObjectif);
				if (this.questObjectifList.size() <= index + 1) {
					return 0;
				}
				return this.questObjectifList.get(index + 1).getId();
			}
		}
		return 0;
	}

	public void applyQuest(final Player perso) {
		if (this.condition != null) {
			switch (this.condition.first) {
			case 1: {
				if (perso.getLevel() < this.condition.second) {
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Votre niveau est insuffisant pour apprendre la qu\u00eate.");
					return;
				}
				break;
			}
			}
		}
		final Quest_Perso qPerso = new Quest_Perso(Database.getStatique().getQuest_persoData().getNextQuest(), this.id,
				false, perso.getId(), "");
		perso.addQuestPerso(qPerso);
		SocketManager.GAME_SEND_Im_PACKET(perso, "054;" + this.id);
		Database.getStatique().getQuest_persoData().add(qPerso);
		SocketManager.GAME_SEND_MAP_NPCS_GMS_PACKETS(perso.getGameClient(), perso.getCurMap());
		if (!this.actions.isEmpty()) {
			for (final Action aAction : this.actions) {
				aAction.apply(perso, perso, -1, -1);
			}
		}
		Database.getStatique().getPlayerData().update(perso, true);
	}

	public void updateQuestData(final Player perso, final boolean validation, final int type) {
		final Quest_Perso qPerso = perso.getQuestPersoByQuest(this);
		for (final Quest_Etape qEtape : this.questEtapeList) {
			if (qEtape.getValidationType() != type) {
				continue;
			}
			boolean refresh = false;
			if (qPerso.isQuestEtapeIsValidate(qEtape)) {
				continue;
			}
			if (qEtape.getObjectif() != this.getObjectifCurrent(qPerso)) {
				continue;
			}
			if (!this.haveRespectCondition(qPerso, qEtape)) {
				continue;
			}
			if (validation) {
				refresh = true;
			}
			switch (qEtape.getType()) {
			case 3: {
				if (perso.get_isTalkingWith() != 0 && perso.getCurMap().getNpc(perso.get_isTalkingWith()).getTemplate()
						.get_id() == qEtape.getNpc().get_id()) {
					for (final Map.Entry<Integer, Integer> entry : qEtape.getItemNecessaryList().entrySet()) {
						if (perso.hasItemTemplate(entry.getKey(), entry.getValue())) {
							perso.removeByTemplateID(entry.getKey(), entry.getValue());
							refresh = true;
						}
					}
					break;
				}
				break;
			}
			case 0:
			case 1:
			case 9: {
				if (qEtape.getCondition().equalsIgnoreCase("1")) {
					if (perso.get_isTalkingWith() != 0 && perso.getCurMap().getNpc(perso.get_isTalkingWith())
							.getTemplate().get_id() == qEtape.getNpc().get_id()
							&& this.haveRespectCondition(qPerso, qEtape)) {
						refresh = true;
						break;
					}
					break;
				} else {
					if (perso.get_isTalkingWith() != 0 && perso.getCurMap().getNpc(perso.get_isTalkingWith())
							.getTemplate().get_id() == qEtape.getNpc().get_id()) {
						refresh = true;
						break;
					}
					break;
				}
			}
			case 6: {
				for (final Map.Entry<Integer, Short> entry2 : qPerso.getMonsterKill().entrySet()) {
					if (entry2.getKey() == qEtape.getMonsterId() && entry2.getValue() >= qEtape.getQua()) {
						refresh = true;
					}
				}
				break;
			}
			case 10: {
				if (perso.get_isTalkingWith() == 0 || perso.getCurMap().getNpc(perso.get_isTalkingWith()).getTemplate()
						.get_id() != qEtape.getNpc().get_id()) {
					break;
				}
				final org.aestia.object.Object suiveur = perso.getObjetByPos(24);
				if (suiveur != null) {
					final Map<Integer, Integer> itemNecessaryList = qEtape.getItemNecessaryList();
					for (final Map.Entry<Integer, Integer> entry3 : itemNecessaryList.entrySet()) {
						if (entry3.getKey() == suiveur.getTemplate().getId()) {
							refresh = true;
							perso.setMascotte(0);
						}
					}
					break;
				}
				break;
			}
			}
			if (!refresh) {
				continue;
			}
			final Quest_Objectif ansObjectif = Quest_Objectif.getQuestObjectifById(this.getObjectifCurrent(qPerso));
			qPerso.setQuestEtapeValidate(qEtape);
			SocketManager.GAME_SEND_Im_PACKET(perso, "055;" + this.id);
			if (this.haveFinish(qPerso, ansObjectif)) {
				SocketManager.GAME_SEND_Im_PACKET(perso, "056;" + this.id);
				this.applyButinOfQuest(perso, qPerso, ansObjectif);
				qPerso.setFinish(true);
			} else if (this.getNextObjectif(ansObjectif) != 0 && qPerso.overQuestEtape(ansObjectif)) {
				this.applyButinOfQuest(perso, qPerso, ansObjectif);
			}
			Database.getStatique().getPlayerData().update(perso, true);
		}
	}

	public boolean haveFinish(final Quest_Perso qPerso, final Quest_Objectif qO) {
		return qPerso.overQuestEtape(qO) && this.getNextObjectif(qO) == 0;
	}

	public void applyButinOfQuest(final Player perso, final Quest_Perso qPerso, final Quest_Objectif ansObjectif) {
		long aXp = 0L;
		if ((aXp = ansObjectif.getXp()) > 0L) {
			perso.addXp(aXp * Config.getInstance().rateXp);
			SocketManager.GAME_SEND_Im_PACKET(perso, "08;" + aXp * Config.getInstance().rateXp);
			SocketManager.GAME_SEND_STATS_PACKET(perso);
		}
		if (ansObjectif.getItem().size() > 0) {
			for (final Map.Entry<Integer, Integer> entry : ansObjectif.getItem().entrySet()) {
				final ObjectTemplate objT = World.getObjTemplate(entry.getKey());
				final int qua = entry.getValue();
				final org.aestia.object.Object obj = objT.createNewItem(qua, false);
				if (perso.addObjet(obj, true)) {
					World.addObjet(obj, true);
				}
				SocketManager.GAME_SEND_Im_PACKET(perso, "021;" + qua + "~" + objT.getId());
			}
		}
		int aKamas = 0;
		if ((aKamas = ansObjectif.getKamas()) > 0) {
			perso.set_kamas(perso.get_kamas() + aKamas);
			SocketManager.GAME_SEND_Im_PACKET(perso, "045;" + aKamas);
			SocketManager.GAME_SEND_STATS_PACKET(perso);
		}
		if (this.getNextObjectif(ansObjectif) != ansObjectif.getId()) {
			for (final Action a : ansObjectif.getAction()) {
				a.apply(perso, null, 0, 0);
			}
		}
	}

	public int getQuestEtapeByObjectif(final Quest_Objectif qObjectif) {
		int nbr = 0;
		for (final Quest_Etape qEtape : this.getQuestEtapeList()) {
			if (qEtape.getObjectif() == qObjectif.getId()) {
				++nbr;
			}
		}
		return nbr;
	}

	public static Map<Integer, Quest> getQuestDataList() {
		return Quest.questDataList;
	}

	public static Quest getQuestById(final int id) {
		return Quest.questDataList.get(id);
	}

	public static void setQuestInList(final Quest quest) {
		Quest.questDataList.put(quest.getId(), quest);
	}

	public ArrayList<Action> getActions() {
		return this.actions;
	}

	public void setActions(final ArrayList<Action> actions) {
		this.actions = actions;
	}

	public static class Quest_Perso {
		private int id;
		private Quest quest;
		private boolean finish;
		private Player perso;
		private Map<Integer, Quest_Etape> questEtapeListValidate;
		private Map<Integer, Short> monsterKill;

		public Quest_Perso(final int aId, final int qId, final boolean aFinish, final int pId, final String qEtapeV) {
			this.quest = null;
			this.questEtapeListValidate = new TreeMap<Integer, Quest_Etape>();
			this.monsterKill = new TreeMap<Integer, Short>();
			this.id = aId;
			this.quest = Quest.getQuestById(qId);
			this.finish = aFinish;
			this.perso = World.getPersonnage(pId);
			try {
				final String[] split = qEtapeV.split(";");
				if (split != null && split.length > 0) {
					String[] array;
					for (int length = (array = split).length, i = 0; i < length; ++i) {
						final String loc1 = array[i];
						if (!loc1.equalsIgnoreCase("")) {
							final Quest_Etape qEtape = Quest_Etape.getQuestEtapeById(Integer.parseInt(loc1));
							this.questEtapeListValidate.put(qEtape.getId(), qEtape);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public int getId() {
			return this.id;
		}

		public Quest getQuest() {
			return this.quest;
		}

		public boolean isFinish() {
			return this.finish;
		}

		public void setFinish(final boolean aFinish) {
			this.finish = aFinish;
			if (this.getQuest().isDelete()) {
				this.perso.delQuestPerso(this.getId());
				this.deleteQuestPerso();
			}
		}

		public Player getPlayer() {
			return this.perso;
		}

		public boolean isQuestEtapeIsValidate(final Quest_Etape qEtape) {
			return this.questEtapeListValidate.containsKey(qEtape.getId());
		}

		public Map<Integer, Quest_Etape> getQuestEtapeValidateList() {
			return this.questEtapeListValidate;
		}

		public void setQuestEtapeValidate(final Quest_Etape qEtape) {
			if (!this.questEtapeListValidate.containsKey(qEtape.getId())) {
				this.questEtapeListValidate.put(qEtape.getId(), qEtape);
			}
		}

		public String getQuestEtapeString() {
			final StringBuilder str = new StringBuilder();
			int nb = 0;
			for (final Quest_Etape qEtape : this.questEtapeListValidate.values()) {
				++nb;
				str.append(qEtape.getId());
				if (nb < this.questEtapeListValidate.size()) {
					str.append(";");
				}
			}
			return str.toString();
		}

		public Map<Integer, Short> getMonsterKill() {
			return this.monsterKill;
		}

		public boolean overQuestEtape(final Quest_Objectif qObjectif) {
			int nbrQuest = 0;
			for (final Quest_Etape qEtape : this.questEtapeListValidate.values()) {
				if (qEtape.getObjectif() == qObjectif.getId()) {
					++nbrQuest;
				}
			}
			return qObjectif.getSizeUnique() == nbrQuest;
		}

		public boolean updateQuestPerso() {
			return Database.getStatique().getQuest_persoData().update(this);
		}

		public boolean deleteQuestPerso() {
			return Database.getStatique().getQuest_persoData().delete(this.id);
		}
	}

	public static class Quest_Objectif {
		public static Map<Integer, Quest_Objectif> questObjectifList;
		private int id;
		private int xp;
		private int kamas;
		private Map<Integer, Integer> items;
		private ArrayList<Action> actionList;
		private ArrayList<Quest_Etape> questEtape;

		static {
			Quest_Objectif.questObjectifList = new HashMap<Integer, Quest_Objectif>();
		}

		public Quest_Objectif(final int aId, final int aXp, final int aKamas, final String aItems,
				final String aAction) {
			this.items = new HashMap<Integer, Integer>();
			this.actionList = new ArrayList<Action>();
			this.questEtape = new ArrayList<Quest_Etape>();
			this.id = aId;
			this.xp = aXp;
			this.kamas = aKamas;
			try {
				if (!aItems.equalsIgnoreCase("")) {
					final String[] split = aItems.split(";");
					if (split != null && split.length > 0) {
						String[] array;
						for (int length = (array = split).length, i = 0; i < length; ++i) {
							final String loc1 = array[i];
							if (!loc1.equalsIgnoreCase("")) {
								if (loc1.contains(",")) {
									final String[] loc2 = loc1.split(",");
									this.items.put(Integer.parseInt(loc2[0]), Integer.parseInt(loc2[1]));
								} else {
									this.items.put(Integer.parseInt(loc1), 1);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (aAction != null && !aAction.equalsIgnoreCase("")) {
					final String[] split = aAction.split(";");
					if (split != null & split.length > 0) {
						String[] array2;
						for (int length2 = (array2 = split).length, j = 0; j < length2; ++j) {
							final String loc1 = array2[j];
							final String[] loc2 = loc1.split("\\|");
							final int actionId = Integer.parseInt(loc2[0]);
							final String args = loc2[1];
							final Action action = new Action(actionId, args, "-1", null);
							this.actionList.add(action);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public int getId() {
			return this.id;
		}

		public int getXp() {
			return this.xp;
		}

		public int getKamas() {
			return this.kamas;
		}

		public Map<Integer, Integer> getItem() {
			return this.items;
		}

		public ArrayList<Action> getAction() {
			return this.actionList;
		}

		public int getSizeUnique() {
			int cpt = 0;
			final ArrayList<Integer> id = new ArrayList<Integer>();
			for (final Quest_Etape qe : this.questEtape) {
				if (!id.contains(qe.getId())) {
					id.add(qe.getId());
					++cpt;
				}
			}
			return cpt;
		}

		public ArrayList<Quest_Etape> getQuestEtapeList() {
			return this.questEtape;
		}

		public void setEtape(final Quest_Etape qEtape) {
			if (!this.questEtape.contains(qEtape)) {
				this.questEtape.add(qEtape);
			}
		}

		public static Quest_Objectif getQuestObjectifById(final int id) {
			return Quest_Objectif.questObjectifList.get(id);
		}

		public static Map<Integer, Quest_Objectif> getQuestObjectifList() {
			return Quest_Objectif.questObjectifList;
		}

		public static void setQuest_Objectif(final Quest_Objectif qObjectif) {
			if (!Quest_Objectif.questObjectifList.containsKey(qObjectif.getId())
					&& !Quest_Objectif.questObjectifList.containsValue(qObjectif)) {
				Quest_Objectif.questObjectifList.put(qObjectif.getId(), qObjectif);
			}
		}
	}
}

// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;
import org.aestia.common.Formulas;
import org.aestia.common.SocketManager;
import org.aestia.fight.Fighter;
import org.aestia.game.GameAction;
import org.aestia.game.GameServer;
import org.aestia.game.world.World;
import org.aestia.job.JobConstant;
import org.aestia.job.maging.Concasseur;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.kernel.Main;
import org.aestia.other.Action;
import org.aestia.other.House;
import org.aestia.other.Trunk;

public class Case {
	private int id;
	private boolean walkable;
	private boolean walkableInFight;
	private boolean loS;
	private short map;
	private Map<Integer, Player> characters;
	private Map<Integer, Fighter> fighters;
	private ArrayList<Action> onCellStop;
	private InteractiveObject object;
	private org.aestia.object.Object droppedItem;

	public Case(final org.aestia.map.Map map, final int id, final boolean walkable, final boolean loS,
			final int objId) {
		this.walkable = true;
		this.walkableInFight = false;
		this.loS = true;
		this.id = id;
		this.walkable = walkable;
		this.walkableInFight = walkable;
		this.loS = loS;
		this.map = map.getId();
		if (objId == -1) {
			return;
		}
		this.object = new InteractiveObject(objId, map, this);
	}

	public int getId() {
		return this.id;
	}

	public boolean isWalkable(final boolean useObject) {
		if (this.object != null && useObject) {
			return this.walkable && this.object.isWalkable();
		}
		return this.walkable;
	}

	public boolean isWalkable(final boolean useObject, final boolean inFight, final int targetCell) {
		if (this.object != null && useObject) {
			if ((inFight || this.getId() != targetCell) && this.object.getTemplate() != null) {
				switch (this.object.getTemplate().getId()) {
				case 7511:
				case 7512:
				case 7513:
				case 7515:
				case 7516:
				case 7517:
				case 7518:
				case 7533:
				case 7534:
				case 7535:
				case 7550:
				case 7551: {
					return this.walkable;
				}
				}
			}
			return this.walkable && this.object.isWalkable();
		}
		return this.walkable;
	}

	public void setWalkable(final boolean walkable) {
		this.walkable = walkable;
	}

	public void setWalkableInFight(final boolean walkable) {
		this.walkableInFight = walkable;
	}

	public boolean isWalkableInFight() {
		return this.walkableInFight;
	}

	public boolean isLoS() {
		return this.loS;
	}

	public boolean blockLoS() {
		if (this.fighters == null) {
			return this.loS;
		}
		boolean fighter = true;
		for (final Map.Entry<Integer, Fighter> f : this.fighters.entrySet()) {
			if (!f.getValue().isHide()) {
				fighter = false;
			}
		}
		return this.loS && fighter;
	}

	public void addCharacter(final Player perso) {
		if (this.characters == null) {
			this.characters = new TreeMap<Integer, Player>();
		}
		this.characters.put(perso.getId(), perso);
	}

	public void removeCharacter(final int guid) {
		if (this.characters == null) {
			return;
		}
		this.characters.remove(guid);
		if (this.characters.isEmpty()) {
			this.characters = null;
		}
	}

	public Map<Integer, Player> getCharacters() {
		if (this.characters == null) {
			return new TreeMap<Integer, Player>();
		}
		return this.characters;
	}

	public void addFighter(final Fighter fighter) {
		if (this.fighters == null) {
			this.fighters = new TreeMap<Integer, Fighter>();
		}
		this.fighters.put(fighter.getId(), fighter);
	}

	public void removeFighter(final Fighter fighter) {
		this.fighters.remove(fighter.getId());
	}

	public Map<Integer, Fighter> getFighters() {
		if (this.fighters == null) {
			return new TreeMap<Integer, Fighter>();
		}
		return this.fighters;
	}

	public Fighter getFirstFighter() {
		if (this.fighters == null) {
			return null;
		}
		final Iterator<Map.Entry<Integer, Fighter>> iterator = this.fighters.entrySet().iterator();
		if (iterator.hasNext()) {
			final Map.Entry<Integer, Fighter> entry = iterator.next();
			return entry.getValue();
		}
		return null;
	}

	public void addOnCellStopAction(final int id, final String args, final String cond, final org.aestia.map.Map map) {
		if (this.onCellStop == null) {
			this.onCellStop = new ArrayList<Action>();
		}
		this.onCellStop.add(new Action(id, args, cond, map));
	}

	public void applyOnCellStopActions(final Player perso) {
		if (this.onCellStop == null) {
			return;
		}
		for (final Action act : this.onCellStop) {
			act.apply(perso, null, -1, -1);
		}
	}

	public boolean getOnCellStopAction() {
		return this.onCellStop != null;
	}

	public ArrayList<Action> getValueOfOnCellStopAction() {
		return this.onCellStop;
	}

	public void setOnCellStopAction(final ArrayList<Action> onCellStop) {
		this.onCellStop = onCellStop;
	}

	public void clearOnCellAction() {
		this.onCellStop = null;
	}

	public InteractiveObject getObject() {
		return this.object;
	}

	public void addDroppedItem(final org.aestia.object.Object obj) {
		this.droppedItem = obj;
	}

	public org.aestia.object.Object getDroppedItem(final boolean delete) {
		final org.aestia.object.Object obj = this.droppedItem;
		if (delete) {
			this.droppedItem = null;
		}
		return obj;
	}

	public void clearDroppedItem() {
		this.droppedItem = null;
	}

	public boolean canDoAction(final int id) {
		if (this.object == null) {
			return false;
		}
		switch (id) {
		case 151: {
			return this.object.getId() == 7028;
		}
		case 62: {
			return this.object.getId() == 7004;
		}
		case 47:
		case 122: {
			return this.object.getId() == 7007;
		}
		case 45: {
			switch (this.object.getId()) {
			case 7511: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 53: {
			switch (this.object.getId()) {
			case 7515: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 57: {
			switch (this.object.getId()) {
			case 7517: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 46: {
			switch (this.object.getId()) {
			case 7512: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 50:
		case 68: {
			switch (this.object.getId()) {
			case 7513: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 159: {
			switch (this.object.getId()) {
			case 7550: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 52: {
			switch (this.object.getId()) {
			case 7516: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 58: {
			switch (this.object.getId()) {
			case 7518: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 54:
		case 69: {
			switch (this.object.getId()) {
			case 7514: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 101: {
			return this.object.getId() == 7003;
		}
		case 6: {
			switch (this.object.getId()) {
			case 7500: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 39: {
			switch (this.object.getId()) {
			case 7501: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 40: {
			switch (this.object.getId()) {
			case 7502: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 10: {
			switch (this.object.getId()) {
			case 7503: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 141: {
			switch (this.object.getId()) {
			case 7542: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 139: {
			switch (this.object.getId()) {
			case 7541: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 37: {
			switch (this.object.getId()) {
			case 7504: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 154: {
			switch (this.object.getId()) {
			case 7553: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 33: {
			switch (this.object.getId()) {
			case 7505: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 41: {
			switch (this.object.getId()) {
			case 7506: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 34: {
			switch (this.object.getId()) {
			case 7507: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 174: {
			switch (this.object.getId()) {
			case 7557: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 38: {
			switch (this.object.getId()) {
			case 7508: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 35: {
			switch (this.object.getId()) {
			case 7509: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 155: {
			switch (this.object.getId()) {
			case 7554: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 158: {
			switch (this.object.getId()) {
			case 7552: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 102: {
			switch (this.object.getId()) {
			case 7519: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 48: {
			return this.object.getId() == 7005;
		}
		case 42: {
			return this.object.getId() == 7510;
		}
		case 32: {
			return this.object.getId() == 7002;
		}
		case 24: {
			switch (this.object.getId()) {
			case 7520: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 25: {
			switch (this.object.getId()) {
			case 7522: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 26: {
			switch (this.object.getId()) {
			case 7523: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 28: {
			switch (this.object.getId()) {
			case 7525: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 56: {
			switch (this.object.getId()) {
			case 7524: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 162: {
			switch (this.object.getId()) {
			case 7556: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 55: {
			switch (this.object.getId()) {
			case 7521: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 29: {
			switch (this.object.getId()) {
			case 7526: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 31: {
			switch (this.object.getId()) {
			case 7528: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 30: {
			switch (this.object.getId()) {
			case 7527: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 161: {
			switch (this.object.getId()) {
			case 7555: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 23: {
			return this.object.getId() == 7019;
		}
		case 71: {
			switch (this.object.getId()) {
			case 7533: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 72: {
			switch (this.object.getId()) {
			case 7534: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 73: {
			switch (this.object.getId()) {
			case 7535: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 74: {
			switch (this.object.getId()) {
			case 7536: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 160: {
			switch (this.object.getId()) {
			case 7551: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 133: {
			return this.object.getId() == 7024;
		}
		case 128: {
			switch (this.object.getId()) {
			case 7530: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 124: {
			switch (this.object.getId()) {
			case 7529: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 136: {
			switch (this.object.getId()) {
			case 7544: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 140: {
			switch (this.object.getId()) {
			case 7543: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 125: {
			switch (this.object.getId()) {
			case 7532: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 129: {
			switch (this.object.getId()) {
			case 7531: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 126: {
			switch (this.object.getId()) {
			case 7537: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 130: {
			switch (this.object.getId()) {
			case 7538: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 127: {
			switch (this.object.getId()) {
			case 7539: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 131: {
			switch (this.object.getId()) {
			case 7540: {
				return this.object.getState() == 1;
			}
			default: {
				return false;
			}
			}
		}
		case 27:
		case 109: {
			return this.object.getId() == 7001;
		}
		case 135: {
			return this.object.getId() == 7022;
		}
		case 134: {
			return this.object.getId() == 7023;
		}
		case 132: {
			return this.object.getId() == 7025;
		}
		case 157: {
			return this.object.getId() == 7030 || this.object.getId() == 7031;
		}
		case 44:
		case 114: {
			switch (this.object.getId()) {
			case 4287:
			case 7000:
			case 7026:
			case 7029: {
				return true;
			}
			default: {
				return false;
			}
			}
		}
		case 175:
		case 176:
		case 177:
		case 178: {
			switch (this.object.getId()) {
			case 6763:
			case 6766:
			case 6767:
			case 6772: {
				return true;
			}
			default: {
				return false;
			}
			}
		}
		case 179: {
			return this.object.getId() == 7045;
		}
		case 183: {
			switch (this.object.getId()) {
			case 1845:
			case 1853:
			case 1854:
			case 1855:
			case 1856:
			case 1857:
			case 1858:
			case 1859:
			case 1860:
			case 1861:
			case 1862:
			case 2319: {
				return true;
			}
			default: {
				return false;
			}
			}
		}
		case 1:
		case 113:
		case 115:
		case 116:
		case 117:
		case 118:
		case 119:
		case 120: {
			return this.object.getId() == 7020;
		}
		case 18:
		case 19:
		case 20:
		case 21:
		case 65:
		case 66:
		case 67:
		case 142:
		case 143:
		case 144:
		case 145:
		case 146: {
			return this.object.getId() == 7012;
		}
		case 165:
		case 166:
		case 167: {
			return this.object.getId() == 7036;
		}
		case 163:
		case 164: {
			return this.object.getId() == 7037;
		}
		case 168:
		case 169: {
			return this.object.getId() == 7038;
		}
		case 171:
		case 182: {
			return this.object.getId() == 7039;
		}
		case 156: {
			return this.object.getId() == 7027;
		}
		case 13:
		case 14: {
			return this.object.getId() == 7011;
		}
		case 64:
		case 123: {
			return this.object.getId() == 7015;
		}
		case 15:
		case 16:
		case 17:
		case 147:
		case 148:
		case 149: {
			return this.object.getId() == 7013;
		}
		case 63: {
			return this.object.getId() == 7014 || this.object.getId() == 7016;
		}
		case 11:
		case 12: {
			return this.object.getId() >= 7008 && this.object.getId() <= 7010;
		}
		case 81:
		case 84:
		case 97:
		case 98:
		case 108: {
			return this.object.getId() >= 6700 && this.object.getId() <= 6776;
		}
		case 104:
		case 105: {
			return this.object.getId() == 7350 || this.object.getId() == 7351 || this.object.getId() == 7353;
		}
		case 170: {
			return this.object.getId() == 7035;
		}
		case 121:
		case 181: {
			return this.object.getId() == 7021;
		}
		case 110: {
			return this.object.getId() == 7018;
		}
		case 153: {
			return this.object.getId() == 7352;
		}
		default: {
			return false;
		}
		}
	}

	public void startAction(final Player perso, final GameAction GA) {
		int actionID = -1;
		short CcellID = -1;
		try {
			actionID = Integer.parseInt(GA.args.split(";")[1]);
			CcellID = Short.parseShort(GA.args.split(";")[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (actionID == -1) {
			SocketManager.GAME_SEND_MESSAGE(perso, "Erreur action id null.");
			return;
		}
		if (perso.getDoAction()) {
			SocketManager.GAME_SEND_MESSAGE(perso,
					"Vous avez d\u00e9j\u00e0 une action en cours. Signaler si le probl\u00e8me persiste.");
			return;
		}
		if (!JobConstant.isJobAction(actionID) || perso.get_fight() != null) {
			switch (actionID) {
			case 62: {
				if (perso.getLevel() > 5) {
					return;
				}
				SocketManager.GAME_SEND_MESSAGE(perso, "La magie op\u00e8re et t'offre ta sant\u00e9 au maximum..");
				perso.fullPDV();
				break;
			}
			case 44: {
				if (!perso.verifOtomaiZaap()) {
					return;
				}
				final String str = String.valueOf(this.map) + "," + this.id;
				perso.set_savePos(str);
				SocketManager.GAME_SEND_Im_PACKET(perso, "06");
				break;
			}
			case 102: {
				if (!this.object.isInteractive()) {
					return;
				}
				if (this.object.getState() != 1) {
					return;
				}
				this.object.setState(2);
				this.object.setInteractive(false);
				SocketManager.GAME_SEND_GA_PACKET_TO_MAP(perso.getCurMap(),
						new StringBuilder().append(GA.id).toString(), 501,
						new StringBuilder(String.valueOf(perso.getId())).toString(), String.valueOf(this.id) + ","
								+ this.object.getUseDuration() + "," + this.object.getUnknowValue());
				SocketManager.GAME_SEND_GDF_PACKET_TO_MAP(perso.getCurMap(), this);
				final org.aestia.map.Map _map = perso.getCurMap();
				perso.getWaiter().addNext(new Runnable() {
					@Override
					public void run() {
						Case.this.object.setState(3);
						Case.this.object.setInteractive(false);
						Case.this.object.launch();
						SocketManager.GAME_SEND_GDF_PACKET_TO_MAP(_map, Case.this);
						final int qua = Formulas.getRandomValue(1, 10);
						final org.aestia.object.Object obj = World.getObjTemplate(311).createNewItem(qua, false);
						if (perso.addObjet(obj, true)) {
							World.addObjet(obj, true);
						}
						SocketManager.GAME_SEND_IQ_PACKET(perso, perso.getId(), qua);
					}
				}, 1000L);
				break;
			}
			case 114: {
				perso.openZaapMenu();
				perso.getGameClient().removeAction(GA);
				break;
			}
			case 157: {
				String ZaapiList = "";
				int count = 0;
				int price = 20;
				String[] Zaapis;
				if (perso.getCurMap().getSubArea().getArea().get_id() == 7
						&& (perso.get_align() == 1 || perso.get_align() == 0 || perso.get_align() == 3)) {
					Zaapis = Constant.ZAAPI.get(1).split(",");
					if (perso.get_align() == 1) {
						price = 10;
					}
				} else if (perso.getCurMap().getSubArea().getArea().get_id() == 11
						&& (perso.get_align() == 2 || perso.get_align() == 0 || perso.get_align() == 3)) {
					Zaapis = Constant.ZAAPI.get(2).split(",");
					if (perso.get_align() == 2) {
						price = 10;
					}
				} else {
					Zaapis = Constant.ZAAPI.get(-1).split(",");
				}
				if (Zaapis.length > 0) {
					String[] array;
					for (int length = (array = Zaapis).length, i = 0; i < length; ++i) {
						final String s = array[i];
						if (count == Zaapis.length) {
							ZaapiList = String.valueOf(ZaapiList) + s + ";" + price;
						} else {
							ZaapiList = String.valueOf(ZaapiList) + s + ";" + price + "|";
						}
						++count;
					}
					perso.SetZaaping(true);
					SocketManager.GAME_SEND_ZAAPI_PACKET(perso, ZaapiList);
					break;
				}
				break;
			}
			case 175: {
				if (this.object.getState() != 3) {
				}
				perso.openMountPark();
				break;
			}
			case 176: {
				final MountPark MP = perso.getCurMap().getMountPark();
				if (MP.getOwner() == -1) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "196");
					return;
				}
				if (MP.getPrice() == 0) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "197");
					return;
				}
				if (perso.get_guild() == null) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "1135");
					return;
				}
				if (perso.getGuildMember().getRank() != 1) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "198");
					return;
				}
				SocketManager.GAME_SEND_R_PACKET(perso, "D" + MP.getPrice() + "|" + MP.getPrice());
				break;
			}
			case 177:
			case 178: {
				final MountPark MP2 = perso.getCurMap().getMountPark();
				if (MP2.getOwner() == -1) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "194");
					return;
				}
				if (MP2.getOwner() != perso.getId()) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "195");
					return;
				}
				SocketManager.GAME_SEND_R_PACKET(perso, "D" + MP2.getPrice() + "|" + MP2.getPrice());
				break;
			}
			case 183: {
				if (perso.getLevel() > 15) {
					SocketManager.GAME_SEND_Im_PACKET(perso, "1127");
					perso.getGameClient().removeAction(GA);
					return;
				}
				final short mapID = Constant.getStartMap(perso.getClasse());
				final int cellID = Constant.getStartCell(perso.getClasse());
				perso.teleport(mapID, cellID);
				perso.getGameClient().removeAction(GA);
				break;
			}
			case 81: {
				final House h = House.getHouseIdByCoord(perso.getCurMap().getId(), CcellID);
				if (h == null) {
					return;
				}
				perso.setInHouse(h);
				h.lock(perso);
				break;
			}
			case 84: {
				final House h2 = House.getHouseIdByCoord(perso.getCurMap().getId(), CcellID);
				if (h2 == null) {
					return;
				}
				final org.aestia.map.Map mapHouse = World.getMap((short) h2.getHouseMapId());
				if (mapHouse == null) {
					SocketManager.GAME_SEND_MESSAGE(perso,
							"La maison est cass\u00e9e.. Contactez un administrateur sur le forum.");
					return;
				}
				final Case caseHouse = mapHouse.getCase(h2.getHouseCellId());
				if (caseHouse == null || !caseHouse.isWalkable(true)) {
					SocketManager.GAME_SEND_MESSAGE(perso,
							"La maison est cass\u00e9e.. Contactez un administrateur sur le forum.");
					return;
				}
				perso.setInHouse(h2);
				h2.enter(perso);
				break;
			}
			case 97: {
				final House h3 = House.getHouseIdByCoord(perso.getCurMap().getId(), CcellID);
				if (h3 == null) {
					return;
				}
				perso.setInHouse(h3);
				h3.buyIt(perso);
				break;
			}
			case 104: {
				final Trunk trunk = Trunk.getTrunkIdByCoord(perso.getCurMap().getId(), CcellID);
				if (trunk == null) {
					return;
				}
				perso.setInTrunk(trunk);
				if (trunk != null) {
					trunk.enter(perso);
					break;
				}
				break;
			}
			case 105: {
				final Trunk t = Trunk.getTrunkIdByCoord(perso.getCurMap().getId(), CcellID);
				if (t == null) {
					return;
				}
				perso.setInTrunk(t);
				t.Lock(perso);
				break;
			}
			case 153: {
				final Trunk poub = Trunk.getTrunkIdByCoord(perso.getCurMap().getId(), CcellID);
				if (poub.getPlayer() != null) {
					perso.send("Im120");
					return;
				}
				perso.setInTrunk(poub);
				Trunk.open(perso, "-", true);
				break;
			}
			case 98:
			case 108: {
				final House h4 = House.getHouseIdByCoord(perso.getCurMap().getId(), CcellID);
				if (h4 == null) {
					return;
				}
				perso.setInHouse(h4);
				h4.sellIt(perso);
				break;
			}
			case 170: {
				perso.setLivreArtisant(true);
				SocketManager.GAME_SEND_ECK_PACKET(perso, 14,
						"2;11;13;14;15;16;17;18;19;20;24;25;26;27;28;31;33;36;41;43;44;45;46;47;48;49;50;56;58;62;63;64;65");
				break;
			}
			case 181: {
				SocketManager.SEND_GDF_PERSO(perso, CcellID, 3, 1);
				SocketManager.GAME_SEND_ECK_PACKET(perso, 3, "8;181");
				perso.setConcasseur(new Concasseur());
				break;
			}
			default: {
				if (Main.modDebug) {
					GameServer.addToLog("Case.startAction non definie pour l'actionID = " + actionID);
					break;
				}
				break;
			}
			}
			perso.getGameClient().removeAction(GA);
			return;
		}
		if (perso.getPodUsed() > perso.getMaxPod()) {
			SocketManager.GAME_SEND_Im_PACKET(perso, "112");
			return;
		}
		if (perso.getMount() != null && perso.getMount().get_podsActuels() > perso.getMount().getTotalPod()) {
			SocketManager.GAME_SEND_Im_PACKET(perso, "112");
			return;
		}
		perso.setDoAction(true);
		perso.doJobAction(actionID, this.object, GA, this);
		System.err.println("Start action on cell " + id);
	}

	public void finishAction(final Player perso, final GameAction GA) {
		int actionID = -1;
		try {
			actionID = Integer.parseInt(GA.args.split(";")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (actionID == -1) {
			return;
		}
		if (JobConstant.isJobAction(actionID)) {
			perso.finishJobAction(actionID, this.object, GA, this);
			perso.setDoAction(false);
			return;
		}
		perso.setDoAction(false);
		switch (actionID) {
		case 44:
		case 81:
		case 84:
		case 97:
		case 98:
		case 104:
		case 105:
		case 108:
		case 110:
		case 121:
		case 153:
		case 157:
		case 181:
		case 183: {
			break;
		}
		case 102: {
			if (this.object == null) {
				Console.println("Erreur lors de la fin de l'action puiser sur le joueur " + perso.getName()
						+ " sur la map " + perso.getCurMap().getId() + ".", Console.Color.ERROR);
				return;
			}
			this.object.setState(3);
			this.object.setInteractive(false);
			this.object.launch();
			SocketManager.GAME_SEND_GDF_PACKET_TO_MAP(perso.getCurMap(), this);
			final int qua = Formulas.getRandomValue(1, 10);
			final org.aestia.object.Object obj = World.getObjTemplate(311).createNewItem(qua, false);
			if (perso.addObjet(obj, true)) {
				World.addObjet(obj, true);
			}
			SocketManager.GAME_SEND_IQ_PACKET(perso, perso.getId(), qua);
			break;
		}
		default: {
			if (Main.modDebug) {
				GameServer.addToLog("[FIXME]Case.finishAction non definie pour l'actionID = " + actionID);
				break;
			}
			break;
		}
		}
	}
}

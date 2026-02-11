// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.map;

import java.util.concurrent.TimeUnit;

import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.entity.monster.Monster;
import org.aestia.game.scheduler.GlobalManager;
import org.aestia.game.scheduler.Manageable;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.map.laby.Toror;
import org.aestia.quest.Quest;
import org.aestia.quest.Quest_Etape;

public class InteractiveObject extends Manageable {
	private int id;
	private int state;
	private Map map;
	private Case cell;
	private boolean interactive;
	private boolean walkable;
	private InteractiveObjectTemplate template;

	public InteractiveObject(final int id, final Map iMap, final Case iCell) {
		this.interactive = true;
		this.id = id;
		this.map = iMap;
		this.cell = iCell;
		this.state = 1;
		this.setTemplate(World.getIOTemplate(this.id));
		if (this.getTemplate() == null) {
			this.walkable = false;
		} else {
			this.walkable = (this.getTemplate().isWalkable() && this.state == 1);
		}
	}

	public int getId() {
		return this.id;
	}

	public int getState() {
		return this.state;
	}

	public void setState(final int state) {
		this.state = state;
	}

	public boolean isInteractive() {
		return this.interactive;
	}

	public void setInteractive(final boolean interactive) {
		this.interactive = interactive;
	}

	public int getUseDuration() {
		int duration = 1500;
		if (this.getTemplate() != null) {
			duration = this.getTemplate().getDuration();
		}
		return duration;
	}

	public int getUnknowValue() {
		int unk = 4;
		if (this.getTemplate() != null) {
			unk = this.getTemplate().getUnk();
		}
		return unk;
	}

	public boolean isWalkable() {
		return this.walkable;
	}

	public void setWalkable(final boolean b) {
		this.walkable = b;
	}

	public static void getActionIO(final Player perso, final Case cell, final int id) {
		switch (id) {
		case 542:
		case 1524: {
			if (!perso.isGhost()) {
				break;
			}
			perso.set_Alive();
			final Quest q = Quest.getQuestById(190);
			if (q == null) {
				break;
			}
			final Quest.Quest_Perso qp = perso.getQuestPersoByQuest(q);
			if (qp == null) {
				break;
			}
			final Quest_Etape qe = q.getQuestEtapeCurrent(qp);
			if (qe == null) {
				Console.println("Erreur sur une Quest_Etape de la qu\u00eate d'id " + q.getId()
						+ ". A v\u00e9rifier dans la base de donn\u00e9es.", Console.Color.ERROR);
				break;
			}
			if (qe.getId() == 783) {
				q.updateQuestData(perso, true, qe.getValidationType());
				break;
			}
			break;
		}
		case 684: {
			if (!perso.hasItemTemplate(1570, 1)) {
				SocketManager.GAME_SEND_MESSAGE(perso, "Vous ne possedez pas la clef n\u00e9cessaire.", "009900");
				break;
			}
			perso.removeByTemplateID(1570, 1);
			SocketManager.GAME_SEND_Im_PACKET(perso, "022;1~1570");
			perso.teleport((short) 2110, 118);
			break;
		}
		case 1330: {
			perso.getCurMap().startFightVersusProtectors(perso, new Monster.MobGroup(perso.getCurMap().nextObjectId,
					cell.getId(), String.valueOf(getKwakere(perso.getCurMap().getId())) + "," + 40 + "," + 40));
			break;
		}
		case 1679: {
			perso.warpToSavePos();
			break;
		}
		case 1748: {
			if (perso.getCurMap().getId() == 6692) {
				if (perso.getCurMap().requiredCell.size() == 2) {
					perso.getCurMap().openDoor();
				}
				perso.getCurMap().requiredCell.clear();
			}
			if (perso.getCurMap().getId() != 6720) {
				break;
			}
			if (perso.getCurMap().getCase(121).getDroppedItem(false) == null
					|| perso.getCurMap().getCase(136).getDroppedItem(false) == null
					|| perso.getCurMap().getCase(151).getDroppedItem(false) == null
					|| perso.getCurMap().getCase(271).getDroppedItem(false) == null
					|| perso.getCurMap().getCase(286).getDroppedItem(false) == null
					|| perso.getCurMap().getCase(301).getDroppedItem(false) == null) {
				return;
			}
			if (perso.getCurMap().getCase(121).getDroppedItem(false).getTemplate().getId() == 362
					&& perso.getCurMap().getCase(136).getDroppedItem(false).getTemplate().getId() == 363
					&& perso.getCurMap().getCase(151).getDroppedItem(false).getTemplate().getId() == 364
					&& perso.getCurMap().getCase(271).getDroppedItem(false).getTemplate().getId() == 362
					&& perso.getCurMap().getCase(286).getDroppedItem(false).getTemplate().getId() == 363
					&& perso.getCurMap().getCase(301).getDroppedItem(false).getTemplate().getId() == 364) {
				World.getMap((short) 6904).openDoor();
				break;
			}
			break;
		}
		case 3000: {
			if (perso.hasEquiped(1718) && perso.hasEquiped(1719) && perso.hasEquiped(1720)
					&& perso.getStats().getEffect(125) == 120 && perso.getStats().getEffect(124) == 0
					&& perso.getStats().getEffect(118) == 60 && perso.getStats().getEffect(126) == 50
					&& perso.getStats().getEffect(119) == 0 && perso.getStats().getEffect(123) == 0) {
				SocketManager.GAME_SEND_ACTION_TO_DOOR(perso.getCurMap(), 237, true);
				SocketManager.GAME_SEND_MESSAGE(perso, "Le crocoburio a \u00e9t\u00e9 d\u00e9sactiv\u00e9.");
				break;
			}
			SocketManager.GAME_SEND_Im_PACKET(perso, "119");
			break;
		}
		case 7546:
		case 7547: {
			SocketManager.send(perso, "GDF|" + cell.getId() + ";3");
			break;
		}
		case 1324: {
			switch (perso.getCurMap().getId()) {
			case 2196: {
				if (perso.is_away()) {
					return;
				}
				if (perso.get_guild() != null || (perso.getGuildMember() != null && perso.hasItemTemplate(1575, 1))) {
					perso.removeByTemplateID(1575, 1);
					SocketManager.GAME_SEND_gC_PACKET(perso, "Ea");
					SocketManager.GAME_SEND_Im_PACKET(perso, "14");
					return;
				}
				SocketManager.GAME_SEND_gn_PACKET(perso);
				break;
			}
			case 2037: {
				perso.addStaticEmote(2);
				break;
			}
			case 2025: {
				perso.addStaticEmote(3);
				break;
			}
			case 2039: {
				perso.addStaticEmote(4);
				break;
			}
			case 2047: {
				perso.addStaticEmote(5);
				break;
			}
			case 8254: {
				perso.addStaticEmote(6);
				break;
			}
			case 2099: {
				perso.addStaticEmote(9);
				break;
			}
			case 8539: {
				perso.addStaticEmote(14);
				break;
			}
			}
			break;
		}
		case 1694: {
			SocketManager.GAME_SEND_GA_PACKET(perso.getGameClient(), "", "2",
					new StringBuilder(String.valueOf(perso.getId())).toString(), "4");
			perso.teleport((short) 6848, 390);
			break;
		}
		case 1695: {
			SocketManager.GAME_SEND_GA_PACKET(perso.getGameClient(), "", "2",
					new StringBuilder(String.valueOf(perso.getId())).toString(), "3");
			perso.teleport((short) 6844, 268);
			break;
		}
		case 7045: {
			final Map map = perso.getCurMap();
			switch (map.getId()) {
			case 8279: {
				for (final Monster.MobGroup mob : map.getMobGroups().values()) {
					switch (mob.getCellId()) {
					case 369:
					case 383:
					case 384:
					case 398: {
						map.openDoor();
					}
					default: {
						continue;
					}
					}
				}
				break;
			}
			case 6165: {
				World.getMap((short) 6164).openDoor();
				break;
			}
			case 6172: {
				World.getMap((short) 6171).openDoor();
				break;
			}
			case 2034: {
				if (!map.getCase(226).getCharacters().isEmpty() && !map.getCase(241).getCharacters().isEmpty()
						&& !map.getCase(269).getCharacters().isEmpty() && !map.getCase(312).getCharacters().isEmpty()
						&& !map.getCase(313).getCharacters().isEmpty() && !map.getCase(328).getCharacters().isEmpty()) {
					map.openDoor();
					break;
				}
				break;
			}
			case 2029: {
				if (!map.getCase(268).getCharacters().isEmpty() && !map.getCase(283).getCharacters().isEmpty()
						&& !map.getCase(311).getCharacters().isEmpty() && !map.getCase(354).getCharacters().isEmpty()
						&& !map.getCase(355).getCharacters().isEmpty() && !map.getCase(370).getCharacters().isEmpty()) {
					map.openDoor();
					break;
				}
				break;
			}
			case 8269: {
				if (!map.getCase(240).getCharacters().isEmpty() && !map.getCase(254).getCharacters().isEmpty()
						&& !map.getCase(255).getCharacters().isEmpty() && !map.getCase(269).getCharacters().isEmpty()) {
					map.openDoor();
					break;
				}
				break;
			}
			case 7288: {
				if (!map.getCase(253).getCharacters().isEmpty() && !map.getCase(324).getCharacters().isEmpty()
						&& !map.getCase(370).getCharacters().isEmpty() && !map.getCase(370).getCharacters().isEmpty()) {
					map.openDoor();
					break;
				}
				break;
			}
			case 2032: {
				if (!map.getCase(209).getCharacters().isEmpty() && !map.getCase(223).getCharacters().isEmpty()
						&& !map.getCase(237).getCharacters().isEmpty() && !map.getCase(238).getCharacters().isEmpty()
						&& !map.getCase(239).getCharacters().isEmpty() && !map.getCase(267).getCharacters().isEmpty()) {
					map.openDoor();
					break;
				}
				break;
			}
			case 2027: {
				if (!map.getCase(297).getCharacters().isEmpty() && !map.getCase(311).getCharacters().isEmpty()
						&& !map.getCase(325).getCharacters().isEmpty() && !map.getCase(326).getCharacters().isEmpty()
						&& !map.getCase(327).getCharacters().isEmpty() && !map.getCase(355).getCharacters().isEmpty()) {
					map.openDoor();
					break;
				}
				break;
			}
			case 2017: {
				if (!map.getCase(238).getCharacters().isEmpty() && !map.getCase(267).getCharacters().isEmpty()
						&& !map.getCase(268).getCharacters().isEmpty()) {
					map.openDoor();
					break;
				}
				break;
			}
			case 2018: {
				if (!map.getCase(296).getCharacters().isEmpty() && !map.getCase(298).getCharacters().isEmpty()
						&& !map.getCase(311).getCharacters().isEmpty() && !map.getCase(312).getCharacters().isEmpty()
						&& !map.getCase(326).getCharacters().isEmpty() && !map.getCase(340).getCharacters().isEmpty()
						&& !map.getCase(341).getCharacters().isEmpty() && !map.getCase(354).getCharacters().isEmpty()
						&& !map.getCase(356).getCharacters().isEmpty()) {
					map.openDoor();
					break;
				}
				break;
			}
			case 8539: {
				if (!map.getCase(238).getCharacters().isEmpty() && !map.getCase(240).getCharacters().isEmpty()
						&& !map.getCase(256).getCharacters().isEmpty() && !map.getCase(314).getCharacters().isEmpty()
						&& !map.getCase(323).getCharacters().isEmpty() && !map.getCase(325).getCharacters().isEmpty()
						&& !map.getCase(372).getCharacters().isEmpty() && !map.getCase(399).getCharacters().isEmpty()) {
					map.openDoor();
					break;
				}
				break;
			}
			}
			break;
		}
		case 7041: {
			SocketManager.GAME_SEND_ACTION_TO_DOOR(perso.getCurMap(), cell.getId(), true);
			Toror.ouvrirBas(perso.getCurMap());
			break;
		}
		case 7042: {
			SocketManager.GAME_SEND_ACTION_TO_DOOR(perso.getCurMap(), cell.getId(), true);
			Toror.ouvrirHaut(perso.getCurMap());
			break;
		}
		case 7043: {
			SocketManager.GAME_SEND_ACTION_TO_DOOR(perso.getCurMap(), cell.getId(), true);
			Toror.ouvrirGauche(perso.getCurMap());
			break;
		}
		case 7044: {
			SocketManager.GAME_SEND_ACTION_TO_DOOR(perso.getCurMap(), cell.getId(), true);
			Toror.ouvrirDroite(perso.getCurMap());
			break;
		}
		}
	}

	public static void getSignIO(final Player perso, final int cell, final int id) {
		switch (perso.getCurMap().getId()) {
		case 7460: {
			final String[][] q = Constant.HUNTING_QUESTS;
			for (int v = 0; v < q.length; ++v) {
				if (Integer.parseInt(q[v][1]) == cell && Integer.parseInt(q[v][0]) == id) {
					SocketManager.send(perso, "dCK" + q[v][2]);
					break;
				}
			}
			break;
		}
		case 7411: {
			if (id == 1531 && cell == 230) {
				SocketManager.send(perso, "dCK139_0612131303");
				break;
			}
			break;
		}
		case 7543: {
			if (id == 1528 && cell == 262) {
				SocketManager.send(perso, "dCK75_0603101710");
			}
			if (id == 1533 && cell == 169) {
				SocketManager.send(perso, "dCK74_0603101709");
			}
			if (id == 1528 && cell == 169) {
				SocketManager.send(perso, "dCK73_0706211414");
				break;
			}
			break;
		}
		case 7314: {
			if (id == 1531 && cell == 93) {
				SocketManager.send(perso, "dCK78_0706221019");
			}
			if (id == 1532 && cell == 256) {
				SocketManager.send(perso, "dCK76_0603091219");
			}
			if (id == 1533 && cell == 415) {
				SocketManager.send(perso, "dCK77_0603091218");
				break;
			}
			break;
		}
		case 7417: {
			if (id == 1532 && cell == 264) {
				SocketManager.send(perso, "dCK79_0603101711");
			}
			if (id == 1528 && cell == 211) {
				SocketManager.send(perso, "dCK80_0510251009");
			}
			if (id == 1532 && cell == 212) {
				SocketManager.send(perso, "dCK77_0603091218");
			}
			if (id == 1529 && cell == 212) {
				SocketManager.send(perso, "dCK81_0510251010");
				break;
			}
			break;
		}
		case 2698: {
			if (id == 1531 && cell == 93) {
				SocketManager.send(perso, "dCK51_0706211150");
			}
			if (id == 1528 && cell == 109) {
				SocketManager.send(perso, "dCK41_0706221516");
				break;
			}
			break;
		}
		case 2814: {
			if (id == 1533 && cell == 415) {
				SocketManager.send(perso, "dCK43_0706201719");
			}
			if (id == 1532 && cell == 326) {
				SocketManager.send(perso, "dCK50_0706211149");
			}
			if (id == 1529 && cell == 325) {
				SocketManager.send(perso, "dCK41_0706221516");
				break;
			}
			break;
		}
		case 3087: {
			if (id == 1529 && cell == 89) {
				SocketManager.send(perso, "dCK41_0706221516");
				break;
			}
			break;
		}
		case 3018: {
			if (id == 1530 && cell == 354) {
				SocketManager.send(perso, "dCK52_0706211152");
			}
			if (id == 1532 && cell == 256) {
				SocketManager.send(perso, "dCK50_0706211149");
			}
			if (id == 1528 && cell == 255) {
				SocketManager.send(perso, "dCK41_0706221516");
				break;
			}
			break;
		}
		case 3433: {
			if (id == 1533 && cell == 282) {
				SocketManager.send(perso, "dCK53_0706211407");
			}
			if (id == 1531 && cell == 179) {
				SocketManager.send(perso, "dCK50_0706211149");
			}
			if (id == 1529 && cell == 178) {
				SocketManager.send(perso, "dCK41_0706221516");
				break;
			}
			break;
		}
		case 4493: {
			if (id == 1533 && cell == 415) {
				SocketManager.send(perso, "dCK43_0706201719");
			}
			if (id == 1532 && cell == 326) {
				SocketManager.send(perso, "dCK50_0706211149");
			}
			if (id == 1529 && cell == 325) {
				SocketManager.send(perso, "dCK41_0706221516");
				break;
			}
			break;
		}
		case 4876: {
			if (id == 1532 && cell == 316) {
				SocketManager.send(perso, "dCK54_0706211408");
			}
			if (id == 1531 && cell == 283) {
				SocketManager.send(perso, "dCK51_0706211150");
			}
			if (id == 1530 && cell == 282) {
				SocketManager.send(perso, "dCK52_0706211152");
				break;
			}
			break;
		}
		}
	}

	private static int getKwakere(final int i) {
		switch (i) {
		case 2072: {
			return 270;
		}
		case 2071: {
			return 269;
		}
		case 2067: {
			return 272;
		}
		case 2068: {
			return 271;
		}
		default: {
			return 269;
		}
		}
	}

	public InteractiveObjectTemplate getTemplate() {
		return this.template;
	}

	public void setTemplate(final InteractiveObjectTemplate template) {
		this.template = template;
	}
	
	@Override
	public void launch() {
		if (this.template != null) {
			GlobalManager.worldSheduler.schedule(this, this.template.getRespawnTime(), TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void run() {
		this.state = 5;
		this.interactive = true;
		SocketManager.GAME_SEND_GDF_PACKET_TO_MAP(this.map, this.cell);
		this.state = 1;
	}

	public static class InteractiveObjectTemplate {
		private int id;
		private int respawnTime;
		private int duration;
		private int unk;
		private boolean walkable;

		public InteractiveObjectTemplate(final int id, final int respawnTime, final int duration, final int unk,
				final boolean walkable) {
			this.id = id;
			this.respawnTime = respawnTime;
			this.duration = duration;
			this.unk = unk;
			this.walkable = walkable;
		}

		public int getId() {
			return this.id;
		}

		public boolean isWalkable() {
			return this.walkable;
		}

		public int getRespawnTime() {
			return this.respawnTime;
		}

		public int getDuration() {
			return this.duration;
		}

		public int getUnk() {
			return this.unk;
		}
	}
}

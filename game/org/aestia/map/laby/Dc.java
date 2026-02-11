// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.map.laby;

import java.util.concurrent.TimeUnit;

import org.aestia.client.Player;
import org.aestia.common.Formulas;
import org.aestia.common.SocketManager;
import org.aestia.game.scheduler.TimerWaiter;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.map.Case;
import org.aestia.map.Map;

public class Dc {
	private static Case interieur;
	private static Case exterieur;
	private static TimerWaiter waiter;

	static {
		Dc.waiter = new TimerWaiter();
	}

	public static void initialize() {
		setExterieur(null);
		setInterieur(null);
		initializeMap(9371, 413, 274, 262, 36);
		initializeMap(9372, 442, 320, 216, 22);
		initializeMap(9373, 414, 262, 144, 48);
		initializeMap(9374, 417, 262, 231, 51);
		initializeMap(9375, 413, 274, 262, 36);
		initializeMap(9376, 413, 274, 262, 36);
		initializeMap(9377, 413, 274, 262, 36);
		initializeMap(9378, 413, 274, 262, 36);
		initializeMap(9379, 413, 274, 262, 36);
		initializeMap(9380, 442, 320, 216, 22);
		initializeMap(9381, 442, 320, 216, 22);
		initializeMap(9382, 442, 320, 216, 22);
		initializeMap(9383, 442, 320, 216, 22);
		initializeMap(9384, 442, 320, 216, 22);
		initializeMap(9385, 414, 262, 144, 48);
		initializeMap(9386, 414, 262, 144, 48);
		initializeMap(9387, 414, 262, 144, 48);
		initializeMap(9388, 414, 262, 144, 48);
		initializeMap(9389, 414, 262, 144, 48);
		initializeMap(9390, 417, 262, 231, 51);
		initializeMap(9391, 417, 262, 231, 51);
		initializeMap(9392, 417, 262, 231, 51);
		initializeMap(9393, 417, 262, 231, 51);
		initializeMap(9394, 417, 262, 231, 51);
		initializeMap(9395, 417, 262, 231, 51);
		if (Dc.exterieur == null) {
			initializeExt();
		}
		Dc.waiter.addNow(new Runnable() {
			@Override
			public void run() {
				checkExterieur();
			}
		}, 5L, TimeUnit.MINUTES);
	}

	private static void checkExterieur() {
		Map actuel = World.getMap((short) 9375);
		if (actuel.getCase(returnCell(actuel, 413)).isLoS()) {
			Dc.waiter.addNow(new Runnable() {
				@Override
				public void run() {
					checkExterieur();
				}
			}, 5L, TimeUnit.MINUTES);
			return;
		}
		actuel = World.getMap((short) 9377);
		if (actuel.getCase(returnCell(actuel, 36)).isLoS()) {
			Dc.waiter.addNow(new Runnable() {
				@Override
				public void run() {
					checkExterieur();
				}
			}, 5L, TimeUnit.MINUTES);
			return;
		}
		actuel = World.getMap((short) 9381);
		if (actuel.getCase(returnCell(actuel, 216)).isLoS()) {
			Dc.waiter.addNow(new Runnable() {
				@Override
				public void run() {
					checkExterieur();
				}
			}, 5L, TimeUnit.MINUTES);
			return;
		}
		actuel = World.getMap((short) 9387);
		if (actuel.getCase(returnCell(actuel, 262)).isLoS()) {
			Dc.waiter.addNow(new Runnable() {
				@Override
				public void run() {
					checkExterieur();
				}
			}, 5L, TimeUnit.MINUTES);
			return;
		}
		Console.println("Le CheckExterieur a echoue. Initialisation du labyrinthe DC.", Console.Color.GAME);
		initialize();
	}

	public static void fermer(final Map map, final Case cell) {
		if (map == null) {
			return;
		}
		if (cell == null) {
			return;
		}
		switch (cell.getId()) {
		case 320: {
			close(map, 306);
			break;
		}
		case 262: {
			switch (map.getId()) {
			case 9371:
			case 9374:
			case 9375:
			case 9376:
			case 9377:
			case 9378:
			case 9379:
			case 9390:
			case 9391:
			case 9392:
			case 9393:
			case 9394: {
				close(map, 248);
				break;
			}
			case 9373:
			case 9385:
			case 9386:
			case 9388:
			case 9389: {
				close(map, 277);
				break;
			}
			case 9387: {
				if (isExterieur(map, 277)) {
					setExterieur(null);
				}
				close(map, 277);
				break;
			}
			case 9395: {
				if (isInterieur(262)) {
					setInterieur(null);
				}
				close(map, 248);
				break;
			}
			}
			break;
		}
		case 274: {
			close(map, 259);
			break;
		}
		case 231: {
			if (map.getId() == 9395 && isInterieur(231)) {
				setInterieur(null);
			}
			close(map, 216);
			break;
		}
		case 216: {
			if (map.getId() == 9381 && isExterieur(map, 201)) {
				setExterieur(null);
			}
			close(map, 201);
			break;
		}
		case 144: {
			close(map, 158);
			break;
		}
		case 51: {
			if (map.getId() == 9395 && isInterieur(51)) {
				setInterieur(null);
			}
			close(map, 65);
			break;
		}
		case 48: {
			close(map, 63);
			break;
		}
		case 36: {
			if (map.getId() == 9377 && isExterieur(map, 36)) {
				setExterieur(null);
			}
			close(map, 50);
			break;
		}
		case 22: {
			close(map, 37);
			break;
		}
		case 442: {
			close(map, 413);
			close(map, 428);
			break;
		}
		case 417: {
			if (map.getId() == 9395 && isInterieur(417)) {
				setInterieur(null);
			}
			close(map, 402);
			break;
		}
		case 414: {
			close(map, 399);
			break;
		}
		case 413: {
			if (map.getId() == 9375 && isExterieur(map, 399)) {
				setExterieur(null);
			}
			close(map, 399);
			break;
		}
		}
	}

	public static void ouvrir(final Map map, final Case cell) {
		if (map == null) {
			return;
		}
		if (cell == null) {
			return;
		}
		if (map.getId() == 9395 && Dc.interieur != null) {
			fermer(map, cell);
		}
		switch (cell.getId()) {
		case 320: {
			open(map, 306);
			break;
		}
		case 262: {
			switch (map.getId()) {
			case 9371:
			case 9374:
			case 9375:
			case 9376:
			case 9377:
			case 9378:
			case 9379:
			case 9390:
			case 9391:
			case 9392:
			case 9393:
			case 9394:
			case 9395: {
				if (map.getId() == 9395) {
					setInterieur(map.getCase(248));
				}
				open(map, 248);
				break;
			}
			case 9373:
			case 9385:
			case 9386:
			case 9388:
			case 9389: {
				open(map, 277);
				break;
			}
			case 9387: {
				if (Dc.exterieur == null) {
					setExterieur(map.getCase(277));
					open(map, 277);
					break;
				}
				close(map, 277);
				break;
			}
			}
			break;
		}
		case 274: {
			open(map, 259);
			break;
		}
		case 231: {
			if (map.getId() == 9395) {
				setInterieur(map.getCase(216));
			}
			open(map, 216);
			break;
		}
		case 216: {
			if (map.getId() != 9381) {
				open(map, 201);
				break;
			}
			if (Dc.exterieur == null) {
				setExterieur(map.getCase(201));
				open(map, 201);
				break;
			}
			close(map, 201);
			break;
		}
		case 144: {
			open(map, 158);
			break;
		}
		case 51: {
			if (map.getId() == 9395) {
				setInterieur(map.getCase(65));
			}
			open(map, 65);
			break;
		}
		case 48: {
			open(map, 63);
			break;
		}
		case 36: {
			if (map.getId() != 9377) {
				open(map, 50);
				break;
			}
			if (Dc.exterieur == null) {
				setExterieur(map.getCase(50));
				open(map, 50);
				break;
			}
			close(map, 50);
			break;
		}
		case 22: {
			open(map, 37);
			break;
		}
		case 442: {
			open(map, 413);
			open(map, 428);
			break;
		}
		case 417: {
			if (map.getId() == 9395) {
				setInterieur(map.getCase(402));
			}
			open(map, 402);
			break;
		}
		case 414: {
			open(map, 399);
			break;
		}
		case 413: {
			if (map.getId() != 9375) {
				open(map, 399);
				break;
			}
			if (Dc.exterieur == null) {
				setExterieur(map.getCase(399));
				open(map, 399);
				break;
			}
			close(map, 399);
			break;
		}
		}
	}

	public static Case getCaseGauche(final Map map) {
		if (map == null) {
			return null;
		}
		switch (map.getId()) {
		case 9372:
		case 9380:
		case 9381:
		case 9382:
		case 9383:
		case 9384: {
			return map.getCase(320);
		}
		case 9371:
		case 9373:
		case 9374:
		case 9375:
		case 9376:
		case 9377:
		case 9378:
		case 9379:
		case 9385:
		case 9386:
		case 9387:
		case 9388:
		case 9389:
		case 9390:
		case 9391:
		case 9392:
		case 9393:
		case 9394:
		case 9395: {
			return map.getCase(262);
		}
		default: {
			return null;
		}
		}
	}

	public static Case getCaseDroite(final Map map) {
		if (map == null) {
			return null;
		}
		switch (map.getId()) {
		case 9371:
		case 9375:
		case 9376:
		case 9377:
		case 9378:
		case 9379: {
			return map.getCase(274);
		}
		case 9374:
		case 9390:
		case 9391:
		case 9392:
		case 9393:
		case 9394:
		case 9395: {
			return map.getCase(231);
		}
		case 9372:
		case 9380:
		case 9381:
		case 9382:
		case 9383:
		case 9384: {
			return map.getCase(216);
		}
		case 9373:
		case 9385:
		case 9386:
		case 9387:
		case 9388:
		case 9389: {
			return map.getCase(144);
		}
		default: {
			return null;
		}
		}
	}

	public static Case getCaseHaut(final Map map) {
		if (map == null) {
			return null;
		}
		switch (map.getId()) {
		case 9374:
		case 9390:
		case 9391:
		case 9392:
		case 9393:
		case 9394:
		case 9395: {
			return map.getCase(51);
		}
		case 9373:
		case 9385:
		case 9386:
		case 9387:
		case 9388:
		case 9389: {
			return map.getCase(48);
		}
		case 9371:
		case 9375:
		case 9376:
		case 9377:
		case 9378:
		case 9379: {
			return map.getCase(36);
		}
		case 9372:
		case 9380:
		case 9381:
		case 9382:
		case 9383:
		case 9384: {
			return map.getCase(22);
		}
		default: {
			return null;
		}
		}
	}

	public static Case getCaseBas(final Map map) {
		if (map == null) {
			return null;
		}
		switch (map.getId()) {
		case 9372:
		case 9380:
		case 9381:
		case 9382:
		case 9383:
		case 9384: {
			return map.getCase(442);
		}
		case 9374:
		case 9390:
		case 9391:
		case 9392:
		case 9393:
		case 9394:
		case 9395: {
			return map.getCase(417);
		}
		case 9373:
		case 9385:
		case 9386:
		case 9387:
		case 9388:
		case 9389: {
			return map.getCase(414);
		}
		case 9371:
		case 9375:
		case 9376:
		case 9377:
		case 9378:
		case 9379: {
			return map.getCase(413);
		}
		default: {
			return null;
		}
		}
	}

	private static void open(final Map map, final int cellId) {
		sendOpen(map, cellId);
		map.getCases().remove(cellId);
		map.getCases().put(cellId, new Case(map, cellId, true, true, -1));
	}

	private static void close(final Map map, final int cellId) {
		sendClose(map, cellId);
		map.getCases().remove(cellId);
		map.getCases().put(cellId, new Case(map, cellId, false, false, -1));
	}

	private static void sendOpen(final Map map, final int cellId) {
		SocketManager.GAME_UPDATE_CELL(map, String.valueOf(cellId) + ";aaGaaaaaaa801;1");
		SocketManager.GAME_SEND_ACTION_TO_DOOR(map, cellId, true);
	}

	private static void sendOpen(final Player p, final int cellId) {
		SocketManager.GAME_UPDATE_CELL(p, String.valueOf(cellId) + ";aaGaaaaaaa801;1");
		SocketManager.GAME_SEND_ACTION_TO_DOOR(p, cellId, true);
	}

	private static void sendClose(final Map map, final int cellId) {
		SocketManager.GAME_UPDATE_CELL(map, String.valueOf(cellId) + ";aaaaaaaaaa801;1");
		SocketManager.GAME_SEND_ACTION_TO_DOOR(map, cellId, false);
	}

	private static void sendClose(final Player p, final int cellId) {
		SocketManager.GAME_UPDATE_CELL(p, String.valueOf(cellId) + ";aaaaaaaaaa801;1");
		SocketManager.GAME_SEND_ACTION_TO_DOOR(p, cellId, false);
	}

	private static void initializeMap(final int _map, final int c1, final int c2, final int c3, final int c4) {
		fermerMap(_map, c1, c2, c3, c4);
		final Map map = World.getMap((short) _map);
		Case _case = randomCase(map, c1, c2, c3, c4);
		switch (_map) {
		case 9395: {
			ouvrir(map, _case);
			break;
		}
		case 9375: {
			if (_case.getId() == 413) {
				if (Dc.exterieur == null) {
					setExterieur(map.getCase(returnCell(map, 413)));
				} else {
					while (_case.getId() == 413) {
						_case = randomCase(map, c1, c2, c3, c4);
					}
				}
			}
			open(map, returnCell(map, _case.getId()));
			break;
		}
		case 9381: {
			if (_case.getId() == 216) {
				if (Dc.exterieur == null) {
					setExterieur(map.getCase(returnCell(map, 216)));
				} else {
					while (_case.getId() == 216) {
						_case = randomCase(map, c1, c2, c3, c4);
					}
				}
			}
			open(map, returnCell(map, _case.getId()));
			break;
		}
		case 9387: {
			if (_case.getId() == 262) {
				if (Dc.exterieur == null) {
					setExterieur(map.getCase(returnCell(map, 262)));
				} else {
					while (_case.getId() == 262) {
						_case = randomCase(map, c1, c2, c3, c4);
					}
				}
			}
			open(map, returnCell(map, _case.getId()));
			break;
		}
		case 9377: {
			if (_case.getId() == 36) {
				if (Dc.exterieur == null) {
					setExterieur(map.getCase(returnCell(map, 36)));
				} else {
					while (_case.getId() == 36) {
						_case = randomCase(map, c1, c2, c3, c4);
					}
				}
			}
			open(map, returnCell(map, _case.getId()));
			break;
		}
		default: {
			ouvrir(map, _case);
			break;
		}
		}
	}

	private static Case randomCase(final Map map, final int c1, final int c2, final int c3, final int c4) {
		switch (Formulas.getRandomValue(0, 3)) {
		case 0: {
			return map.getCase(c1);
		}
		case 1: {
			return map.getCase(c2);
		}
		case 2: {
			return map.getCase(c3);
		}
		case 3: {
			return map.getCase(c4);
		}
		default: {
			return map.getCase(c1);
		}
		}
	}

	private static void initializeExt() {
		Map map = null;
		Case cell = null;
		switch (Formulas.getRandomValue(0, 3)) {
		case 0: {
			fermerMap(9375, 413, 274, 262, 36);
			map = World.getMap((short) 9375);
			cell = map.getCase(413);
			ouvrir(map, cell);
			break;
		}
		case 1: {
			fermerMap(9381, 442, 320, 216, 22);
			map = World.getMap((short) 9381);
			cell = map.getCase(216);
			ouvrir(map, cell);
			break;
		}
		case 2: {
			fermerMap(9387, 414, 262, 144, 48);
			map = World.getMap((short) 9387);
			cell = map.getCase(262);
			ouvrir(map, cell);
			break;
		}
		case 3: {
			fermerMap(9377, 413, 274, 262, 36);
			map = World.getMap((short) 9377);
			cell = map.getCase(36);
			ouvrir(map, cell);
			break;
		}
		}
	}

	private static void fermerMap(final int _map, final int c1, final int c2, final int c3, final int c4) {
		final Map map = World.getMap((short) _map);
		fermer(map, map.getCase(c1));
		fermer(map, map.getCase(c2));
		fermer(map, map.getCase(c3));
		fermer(map, map.getCase(c4));
	}

	public static void sendPacketMap(final Player perso) {
		final Map map = perso.getCurMap();
		Case c1 = null;
		Case c2 = null;
		Case c3 = null;
		Case c4 = null;
		switch (map.getId()) {
		case 9371:
		case 9375:
		case 9376:
		case 9377:
		case 9378:
		case 9379: {
			c1 = map.getCase(returnCell(map, 413));
			c2 = map.getCase(returnCell(map, 274));
			c3 = map.getCase(returnCell(map, 262));
			c4 = map.getCase(returnCell(map, 36));
			break;
		}
		case 9372:
		case 9380:
		case 9381:
		case 9382:
		case 9383:
		case 9384: {
			c1 = map.getCase(returnCell(map, 442));
			c2 = map.getCase(returnCell(map, 320));
			c3 = map.getCase(returnCell(map, 216));
			c4 = map.getCase(returnCell(map, 22));
			break;
		}
		case 9373:
		case 9385:
		case 9386:
		case 9387:
		case 9388:
		case 9389: {
			c1 = map.getCase(returnCell(map, 414));
			c2 = map.getCase(returnCell(map, 262));
			c3 = map.getCase(returnCell(map, 144));
			c4 = map.getCase(returnCell(map, 48));
			break;
		}
		case 9374:
		case 9390:
		case 9391:
		case 9392:
		case 9393:
		case 9394:
		case 9395: {
			c1 = map.getCase(returnCell(map, 417));
			c2 = map.getCase(returnCell(map, 262));
			c3 = map.getCase(returnCell(map, 231));
			c4 = map.getCase(returnCell(map, 51));
			break;
		}
		}
		if (c1.isLoS()) {
			sendOpen(perso, c1.getId());
		} else {
			sendClose(perso, c1.getId());
		}
		if (c2.isLoS()) {
			sendOpen(perso, c2.getId());
		} else {
			sendClose(perso, c2.getId());
		}
		if (c3.isLoS()) {
			sendOpen(perso, c3.getId());
		} else {
			sendClose(perso, c3.getId());
		}
		if (c4.isLoS()) {
			sendOpen(perso, c4.getId());
		} else {
			sendClose(perso, c4.getId());
		}
	}

	public static int returnCell(final Map map, final int cell) {
		Label_0299: {
			switch (cell) {
			case 320: {
				return 306;
			}
			case 262: {
				switch (map.getId()) {
				case 9371:
				case 9374:
				case 9375:
				case 9376:
				case 9377:
				case 9378:
				case 9379:
				case 9390:
				case 9391:
				case 9392:
				case 9393:
				case 9394:
				case 9395: {
					return 248;
				}
				case 9373:
				case 9385:
				case 9386:
				case 9387:
				case 9388:
				case 9389: {
					return 277;
				}
				default: {
					break Label_0299;
				}
				}
			}
			case 274: {
				return 259;
			}
			case 231: {
				return 216;
			}
			case 216: {
				return 201;
			}
			case 144: {
				return 158;
			}
			case 51: {
				return 65;
			}
			case 48: {
				return 63;
			}
			case 36: {
				return 50;
			}
			case 22: {
				return 37;
			}
			case 442: {
				return 428;
			}
			case 417: {
				return 402;
			}
			case 413:
			case 414: {
				return 399;
			}
			}
		}
		return -1;
	}

	private static void setExterieur(final Case cell) {
		Dc.exterieur = cell;
	}

	private static void setInterieur(final Case cell) {
		Dc.interieur = cell;
	}

	private static boolean isExterieur(final Map map, final int cell) {
		return Dc.exterieur != null && cell == Dc.exterieur.getId();
	}

	private static boolean isInterieur(final int cell) {
		return Dc.interieur != null && cell == Dc.exterieur.getId();
	}
}

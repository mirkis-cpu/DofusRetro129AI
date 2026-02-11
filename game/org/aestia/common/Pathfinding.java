// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.common;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.aestia.client.Player;
import org.aestia.fight.Fight;
import org.aestia.fight.Fighter;
import org.aestia.fight.spells.Spell;
import org.aestia.fight.traps.Trap;
import org.aestia.game.GameServer;
import org.aestia.map.Case;
import org.aestia.map.Map;

public class Pathfinding {
	private static Integer nSteps;

	static {
		Pathfinding.nSteps = new Integer(0);
	}

	public static int isValidPath(final Map map, final int cellID, final AtomicReference<String> pathRef,
			final Fight fight, final Player perso, final int targetCell) {
		synchronized (Pathfinding.nSteps) {
			Pathfinding.nSteps = 0;
			int newPos = cellID;
			int Steps = 0;
			final String path = pathRef.get();
			String newPath = "";
			int i = 0;
			while (i < path.length()) {
				final String SmallPath = path.substring(i, i + 3);
				final char dir = SmallPath.charAt(0);
				final int dirCaseID = CryptManager.cellCode_To_ID(SmallPath.substring(1));
				Pathfinding.nSteps = 0;
				if (fight != null && i != 0 && getEnemyFighterArround(newPos, map, fight) != null) {
					pathRef.set(newPath);
					// monitorexit(Pathfinding.nSteps)
					return Steps;
				}
				if (fight != null && i != 0) {
					for (final Trap p : fight.getAllTraps()) {
						final int dist = getDistanceBetween(map, p.getCell().getId(), newPos);
						if (dist <= p.getSize()) {
							pathRef.set(newPath);
							// monitorexit(Pathfinding.nSteps)
							return Steps;
						}
					}
				}
				final String[] aPathInfos = ValidSinglePath(newPos, SmallPath, map, fight, perso, targetCell)
						.split(":");
				if (aPathInfos[0].equalsIgnoreCase("stop")) {
					newPos = Integer.parseInt(aPathInfos[1]);
					Steps += Pathfinding.nSteps;
					newPath = String.valueOf(newPath) + dir + CryptManager.cellID_To_Code(newPos);
					pathRef.set(newPath);
					// monitorexit(Pathfinding.nSteps)
					return -Steps;
				}
				if (aPathInfos[0].equalsIgnoreCase("ok")) {
					newPos = dirCaseID;
					Steps += Pathfinding.nSteps;
					newPath = String.valueOf(newPath) + dir + CryptManager.cellID_To_Code(newPos);
					i += 3;
				} else {
					if (aPathInfos[0].equalsIgnoreCase("stoptp")) {
						newPos = Integer.parseInt(aPathInfos[1]);
						Steps += Pathfinding.nSteps;
						newPath = String.valueOf(newPath) + dir + CryptManager.cellID_To_Code(newPos);
						pathRef.set(newPath);
						// monitorexit(Pathfinding.nSteps)
						return -Steps - 10000;
					}
					pathRef.set(newPath);
					// monitorexit(Pathfinding.nSteps)
					return -1000;
				}
			}
			pathRef.set(newPath);
			// monitorexit(Pathfinding.nSteps)
			return Steps;
		}
	}

	public static boolean isCACwithEnnemy(final Fighter fighter, final ArrayList<Fighter> Ennemys) {
		for (final Fighter f : Ennemys) {
			if (isNextTo(fighter.getFight().getMap(), fighter.getCell().getId(), f.getCell().getId())) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Fighter> getEnemyFighterArround(final int cellID, final Map map, final Fight fight) {
		final char[] dirs = { 'b', 'd', 'f', 'h' };
		final ArrayList<Fighter> enemy = new ArrayList<Fighter>();
		char[] array;
		for (int length = (array = dirs).length, i = 0; i < length; ++i) {
			final char dir = array[i];
			final Fighter f = map.getCase(GetCaseIDFromDirrection(cellID, dir, map, false)).getFirstFighter();
			if (f != null) {
				if (f.getFight() == fight) {
					if (f.getTeam() != fight.getFighterByOrdreJeu().getTeam()) {
						enemy.add(f);
					}
				}
			}
		}
		if (enemy.size() == 0 || enemy.size() == 4) {
			return null;
		}
		return enemy;
	}

	public static boolean isNextTo(final Map map, final int cell1, final int cell2) {
		boolean result = false;
		result = (cell1 + 14 == cell2 || cell1 + 15 == cell2 || cell1 - 14 == cell2 || cell1 - 15 == cell2);
		return result;
	}

	public static String ValidSinglePath(final int CurrentPos, final String Path, final Map map, final Fight fight,
			final Player perso, final int targetCell) {
		Pathfinding.nSteps = 0;
		final char dir = Path.charAt(0);
		final int dirCaseID = CryptManager.cellCode_To_ID(Path.substring(1));
		int check = "353;339;325;311;297;283;269;255;241;227;213;228;368;354;340;326;312;298;284;270;256;242;243;257;271;285;299;313;327;341;355;369;383"
				.contains(String.valueOf(targetCell)) ? 1 : 0;
		if (fight != null && fight.isOccuped(dirCaseID)) {
			return "no:";
		}
		try {
			if (perso.getCases && !perso.thisCases.contains(CurrentPos)) {
				perso.thisCases.add(CurrentPos);
			}
		} catch (Exception ex) {
		}
		int lastPos = CurrentPos;
		Pathfinding.nSteps = 1;
		while (Pathfinding.nSteps <= 64) {
			if (GetCaseIDFromDirrection(lastPos, dir, map, fight != null) == dirCaseID) {
				if (fight != null && fight.isOccuped(dirCaseID)) {
					return "stop:" + lastPos;
				}
				if (map.getCase(dirCaseID).isWalkable(true, fight != null, targetCell)) {
					return "ok:";
				}
				--Pathfinding.nSteps;
				return "stop:" + lastPos;
			} else {
				lastPos = GetCaseIDFromDirrection(lastPos, dir, map, fight != null);
				Label_0571: {
					if (fight == null && perso != null) {
						if (perso.getCurMap().getId() == 9588) {
							final String cell = "353;339;325;311;297;283;269;255;241;227;213;228;368;354;340;326;312;298;284;270;256;242;243;257;271;285;299;313;327;341;355;369;383";
							if (cell.contains(String.valueOf(lastPos))) {
								++check;
							}
							if (check > 1) {
								return "stoptp:" + lastPos;
							}
						}
						try {
							if (perso.getCases && !perso.thisCases.contains(lastPos)) {
								perso.thisCases.add(lastPos);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (lastPos < 0) {
							break Label_0571;
						}
						final Case _case = map.getCase(lastPos);
						if (_case == null) {
							break Label_0571;
						}
						if (_case.getOnCellStopAction()) {
							return "stop:" + lastPos;
						}
						if (map.isAggroByMob(perso, lastPos)) {
							return "stop:" + lastPos;
						}
					}
					if (fight != null) {
						if (fight.isOccuped(lastPos)) {
							return "no:";
						}
						if (getEnemyFighterArround(lastPos, map, fight) != null) {
							return "stop:" + lastPos;
						}
						for (final Trap p : fight.getAllTraps()) {
							if (getDistanceBetween(map, p.getCell().getId(), lastPos) <= p.getSize()) {
								return "stop:" + lastPos;
							}
						}
					}
				}
				++Pathfinding.nSteps;
			}
		}
		return "no:";
	}

	public static ArrayList<Integer> getListCaseFromFighter(final Fight fight, final Fighter fighter,
			final int cellStart, final ArrayList<Spell.SortStats> SS) {
		int bestPo = 0;
		if (SS != null) {
			for (final Spell.SortStats sort : SS) {
				if (sort.getMaxPO() > bestPo) {
					bestPo = sort.getMaxPO();
				}
			}
		}
		final int pmNumber = fighter.getCurPm(fight);
		final int cellNumber = Formulas.countCell(pmNumber + 1);
		int _loc1_ = 0;
		int _loc3_ = 0;
		final char[] dirs = { 'b', 'd', 'f', 'h' };
		final ArrayList<Integer> cellT = new ArrayList<Integer>();
		final ArrayList<Integer> cellY = new ArrayList<Integer>();
		cellT.add(cellStart);
		if (fighter.getCurPm(fight) <= 0) {
			return cellT;
		}
		final ArrayList<Integer> cell = new ArrayList<Integer>();
		while (_loc1_++ < cellNumber) {
			int _loc2_ = 0;
			if (cellT.size() <= _loc3_ || cellT.isEmpty()) {
				cell.addAll(cellT);
				cellT.clear();
				cellT.addAll(cellY);
				cellY.clear();
				_loc3_ = 0;
			}
			if (cellT.isEmpty() && cellY.isEmpty()) {
				return cell;
			}
			_loc2_ = cellT.get(_loc3_);
			char[] array;
			for (int length = (array = dirs).length, i = 0; i < length; ++i) {
				final char dir = array[i];
				final int _loc4_ = getCaseIDFromDirrection(_loc2_, dir, fight.getMapOld());
				if (_loc4_ >= 0 && fight.getMap().getCase(_loc4_) != null && !cell.contains(_loc4_)
						&& !cellT.contains(_loc4_)) {
					if (!cellY.contains(_loc4_)) {
						if (!haveFighterOnThisCell(_loc4_, fight)) {
							if (fight.getMapOld().getCase(_loc4_).isWalkable(true, true, -1)) {
								cellY.add(_loc4_);
							}
						}
					}
				}
			}
			++_loc3_;
		}
		return cell;
	}

	public static ArrayList<Integer> getListCaseFromFighter(final Fight fight, final Fighter fighter,
			final ArrayList<Spell.SortStats> SS, final Fighter nearest) {
		int bestPo = 0;
		for (final Spell.SortStats sort : SS) {
			if (sort.getMaxPO() > bestPo) {
				bestPo = sort.getMaxPO();
			}
		}
		final int cellNumber = Formulas.countCell(fighter.getCurPm(fight) + 1);
		int _loc1_ = 0;
		int _loc3_ = 0;
		final char[] dirs = { 'b', 'd', 'f', 'h' };
		final ArrayList<Integer> cellT = new ArrayList<Integer>();
		final ArrayList<Integer> cellY = new ArrayList<Integer>();
		cellT.add(fighter.getCell().getId());
		final ArrayList<Integer> cell = new ArrayList<Integer>();
		while (_loc1_++ < cellNumber) {
			int _loc2_ = 0;
			if (cellT.size() <= _loc3_ || cellT.isEmpty()) {
				cell.addAll(cellT);
				cellT.clear();
				cellT.addAll(cellY);
				cellY.clear();
				_loc3_ = 0;
			}
			if (cellT.isEmpty() && cellY.isEmpty()) {
				return cell;
			}
			_loc2_ = cellT.get(_loc3_);
			char[] array;
			for (int length = (array = dirs).length, i = 0; i < length; ++i) {
				final char dir = array[i];
				final int _loc4_ = getCaseIDFromDirrection(_loc2_, dir, fight.getMapOld());
				if (_loc4_ >= 0 && fight.getMap().getCase(_loc4_) != null && !cell.contains(_loc4_)
						&& !cellT.contains(_loc4_)) {
					if (!cellY.contains(_loc4_)) {
						if (!haveFighterOnThisCell(_loc4_, fight)) {
							if (fight.getMapOld().getCase(_loc4_).isWalkable(true, true, -1)) {
								cellY.add(_loc4_);
							}
						}
					}
				}
			}
			++_loc3_;
		}
		return cell;
	}

	public static ArrayList<Integer> getAllCaseIdAllDirrection(final int caseId, final Map map) {
		final ArrayList<Integer> list = new ArrayList<Integer>();
		final char[] dir = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
		int _c = -1;
		char[] array;
		for (int length = (array = dir).length, i = 0; i < length; ++i) {
			final char d = array[i];
			_c = GetCaseIDFromDirrection(caseId, d, map, false);
			if (_c > 0) {
				list.add(_c);
			}
		}
		return list;
	}

	public static int GetCaseIDFromDirrection(final int CaseID, final char Direction, final Map map,
			final boolean Combat) {
		if (map == null) {
			return -1;
		}
		switch (Direction) {
		case 'a': {
			return Combat ? -1 : (CaseID + 1);
		}
		case 'b': {
			return CaseID + map.getW();
		}
		case 'c': {
			return Combat ? -1 : (CaseID + (map.getW() * 2 - 1));
		}
		case 'd': {
			return CaseID + (map.getW() - 1);
		}
		case 'e': {
			return Combat ? -1 : (CaseID - 1);
		}
		case 'f': {
			return CaseID - map.getW();
		}
		case 'g': {
			return Combat ? -1 : (CaseID - (map.getW() * 2 - 1));
		}
		case 'h': {
			return CaseID - map.getW() + 1;
		}
		default: {
			return -1;
		}
		}
	}

	public static int getDistanceBetween(final Map map, final int id1, final int id2) {
		if (id1 == id2) {
			return 0;
		}
		if (map == null) {
			return 0;
		}
		final int diffX = Math.abs(getCellXCoord(map, id1) - getCellXCoord(map, id2));
		final int diffY = Math.abs(getCellYCoord(map, id1) - getCellYCoord(map, id2));
		return diffX + diffY;
	}

	public static Fighter getEnemyAround(final int cellId, final Map map, final Fight fight) {
		final char[] dirs = { 'b', 'd', 'f', 'h' };
		char[] array;
		for (int length = (array = dirs).length, i = 0; i < length; ++i) {
			final char dir = array[i];
			final Case cell = map.getCase(GetCaseIDFromDirrection(cellId, dir, map, false));
			if (cell != null) {
				final Fighter f = cell.getFirstFighter();
				if (f != null && f.getFight() == fight && f.getTeam() != fight.getFighterByOrdreJeu().getTeam()) {
					return f;
				}
			}
		}
		return null;
	}

	public static int newCaseAfterPush(final Fight fight, final Case CCase, final Case TCase, int value) {
		final Map map = fight.getMap();
		if (CCase.getId() == TCase.getId()) {
			return 0;
		}
		char c = getDirBetweenTwoCase(CCase.getId(), TCase.getId(), map, true);
		int id = TCase.getId();
		if (value < 0) {
			c = getOpositeDirection(c);
			value = -value;
		}
		boolean b = false;
		for (int a = 0; a < value; ++a) {
			final int nextCase = GetCaseIDFromDirrection(id, c, map, true);
			for (final Trap p : fight.getAllTraps()) {
				if (distBetweenTwoCase(map, p.getCell(), map.getCase(nextCase)) <= p.getSize()) {
					id = nextCase;
					b = true;
				}
			}
			if (b) {
				break;
			}
			if (map.getCase(nextCase) == null || !map.getCase(nextCase).isWalkable(true)
					|| !map.getCase(nextCase).isWalkableInFight() || !map.getCase(nextCase).getFighters().isEmpty()) {
				return -(value - a);
			}
			id = nextCase;
		}
		if (id == TCase.getId()) {
			id = 0;
		}
		return id;
	}

	public static int distBetweenTwoCase(final Map map, final Case c1, final Case c2) {
		int dist = 0;
		if (c1 == null || c2 == null) {
			return dist;
		}
		if (c1.getId() == c2.getId()) {
			return dist;
		}
		int id = c1.getId();
		final char c3 = getDirBetweenTwoCase(c1.getId(), c2.getId(), map, true);
		while (c2 != map.getCase(id)) {
			id = GetCaseIDFromDirrection(id, c3, map, true);
			if (map.getCase(id) == null) {
				return dist;
			}
			++dist;
		}
		return dist;
	}

	public static char getOpositeDirection(final char c) {
		switch (c) {
		case 'a': {
			return 'e';
		}
		case 'b': {
			return 'f';
		}
		case 'c': {
			return 'g';
		}
		case 'd': {
			return 'h';
		}
		case 'e': {
			return 'a';
		}
		case 'f': {
			return 'b';
		}
		case 'g': {
			return 'c';
		}
		case 'h': {
			return 'd';
		}
		default: {
			return '\0';
		}
		}
	}

	public static boolean casesAreInSameLine(final Map map, int c1, final int c2, final char dir) {
		if (c1 == c2) {
			return true;
		}
		if (dir != 'z') {
			for (int a = 0; a < 70; ++a) {
				if (GetCaseIDFromDirrection(c1, dir, map, true) == c2) {
					return true;
				}
				if (GetCaseIDFromDirrection(c1, dir, map, true) == -1) {
					break;
				}
				c1 = GetCaseIDFromDirrection(c1, dir, map, true);
			}
		} else {
			final char[] dirs = { 'b', 'd', 'f', 'h' };
			char[] array;
			for (int length = (array = dirs).length, i = 0; i < length; ++i) {
				final char d = array[i];
				int c3 = c1;
				for (int a2 = 0; a2 < 70; ++a2) {
					if (GetCaseIDFromDirrection(c3, d, map, true) == c2) {
						return true;
					}
					c3 = GetCaseIDFromDirrection(c3, d, map, true);
				}
			}
		}
		return false;
	}

	public static ArrayList<Fighter> getCiblesByZoneByWeapon(final Fight fight, final int type, final Case cell,
			final int castCellID) {
		final ArrayList<Fighter> cibles = new ArrayList<Fighter>();
		final char c = getDirBetweenTwoCase(castCellID, cell.getId(), fight.getMap(), true);
		if (c == '\0') {
			if (cell.getFirstFighter() != null) {
				cibles.add(cell.getFirstFighter());
			}
			return cibles;
		}
		switch (type) {
		case 7: {
			final Fighter f = getFighter2CellBefore(castCellID, c, fight.getMap());
			if (f != null) {
				cibles.add(f);
			}
			final Fighter g = get1StFighterOnCellFromDirection(fight.getMap(), castCellID, (char) (c - '\u0001'));
			if (g != null) {
				cibles.add(g);
			}
			final Fighter h = get1StFighterOnCellFromDirection(fight.getMap(), castCellID, (char) (c + '\u0001'));
			if (h != null) {
				cibles.add(h);
			}
			final Fighter i = cell.getFirstFighter();
			if (i != null) {
				cibles.add(i);
				break;
			}
			break;
		}
		case 4: {
			final Fighter j = get1StFighterOnCellFromDirection(fight.getMap(), castCellID, (char) (c - '\u0001'));
			if (j != null) {
				cibles.add(j);
			}
			final Fighter k = get1StFighterOnCellFromDirection(fight.getMap(), castCellID, (char) (c + '\u0001'));
			if (k != null) {
				cibles.add(k);
			}
			final Fighter l = cell.getFirstFighter();
			if (l != null) {
				cibles.add(l);
				break;
			}
			break;
		}
		case 2:
		case 3:
		case 5:
		case 6:
		case 8:
		case 19:
		case 21:
		case 22: {
			final Fighter m = cell.getFirstFighter();
			if (m != null) {
				cibles.add(m);
				break;
			}
			break;
		}
		}
		return cibles;
	}

	private static Fighter get1StFighterOnCellFromDirection(final Map map, final int id, char c) {
		if (c == '`') {
			c = 'h';
		}
		if (c == 'i') {
			c = 'a';
		}
		return map.getCase(GetCaseIDFromDirrection(id, c, map, false)).getFirstFighter();
	}

	private static Fighter getFighter2CellBefore(final int CellID, final char c, final Map map) {
		final int new2CellID = GetCaseIDFromDirrection(GetCaseIDFromDirrection(CellID, c, map, false), c, map, false);
		return map.getCase(new2CellID).getFirstFighter();
	}

	public static char getDirBetweenTwoCase(final int cell1ID, final int cell2ID, final Map map, final boolean Combat) {
		final ArrayList<Character> dirs = new ArrayList<Character>();
		dirs.add('b');
		dirs.add('d');
		dirs.add('f');
		dirs.add('h');
		if (!Combat) {
			dirs.add('a');
			dirs.add('b');
			dirs.add('c');
			dirs.add('d');
		}
		for (final char c : dirs) {
			int cell = cell1ID;
			for (int i = 0; i <= 64; ++i) {
				if (GetCaseIDFromDirrection(cell, c, map, Combat) == cell2ID) {
					return c;
				}
				cell = GetCaseIDFromDirrection(cell, c, map, Combat);
			}
		}
		return '\0';
	}

	public static ArrayList<Case> getCellListFromAreaString(final Map map, int cellID, final int castCellID,
			final String zoneStr, final int PONum, final boolean isCC) {
		final ArrayList<Case> cases = new ArrayList<Case>();
		if (map.getCase(cellID) == null) {
			return cases;
		}
		cases.add(map.getCase(cellID));
		final int taille = CryptManager.getIntByHashedValue(zoneStr.charAt(PONum + 1));
		switch (zoneStr.charAt(PONum)) {
		case 'C': {
			for (int a = 0; a < taille; ++a) {
				final char[] dirs = { 'b', 'd', 'f', 'h' };
				final ArrayList<Case> cases2 = new ArrayList<Case>();
				cases2.addAll(cases);
				for (final Case aCell : cases2) {
					char[] array;
					for (int length = (array = dirs).length, i = 0; i < length; ++i) {
						final char d = array[i];
						final Case cell = map.getCase(GetCaseIDFromDirrection(aCell.getId(), d, map, true));
						if (cell != null) {
							if (!cases.contains(cell)) {
								cases.add(cell);
							}
						}
					}
				}
			}
			break;
		}
		case 'X': {
			final char[] dirs2 = { 'b', 'd', 'f', 'h' };
			char[] array2;
			for (int length2 = (array2 = dirs2).length, j = 0; j < length2; ++j) {
				final char d2 = array2[j];
				int cID = cellID;
				for (int a2 = 0; a2 < taille; ++a2) {
					cases.add(map.getCase(GetCaseIDFromDirrection(cID, d2, map, true)));
					cID = GetCaseIDFromDirrection(cID, d2, map, true);
				}
			}
			break;
		}
		case 'L': {
			final char dir = getDirBetweenTwoCase(castCellID, cellID, map, true);
			for (int a3 = 0; a3 < taille; ++a3) {
				cases.add(map.getCase(GetCaseIDFromDirrection(cellID, dir, map, true)));
				cellID = GetCaseIDFromDirrection(cellID, dir, map, true);
			}
			break;
		}
		case 'P': {
			break;
		}
		default: {
			GameServer.addToLog("[FIXME]Type de port\u00e9e non reconnue: " + zoneStr.charAt(0));
			break;
		}
		}
		return cases;
	}

	public static int getCellXCoord(final Map map, final int cellID) {
		if (map == null) {
			return 0;
		}
		final int w = map.getW();
		return (cellID - (w - 1) * getCellYCoord(map, cellID)) / w;
	}

	public static int getCellYCoord(final Map map, final int cellID) {
		final int w = map.getW();
		final int loc5 = cellID / (w * 2 - 1);
		final int loc6 = cellID - loc5 * (w * 2 - 1);
		final int loc7 = loc6 % w;
		return loc5 - loc7;
	}

	public static boolean checkLoS(final Map map, final int cell1, final int cell2, final Fighter fighter) {
		if (fighter.getPersonnage() != null) {
			return true;
		}
		final int dist = getDistanceBetween(map, cell1, cell2);
		ArrayList<Integer> los = new ArrayList<Integer>();
		if (dist > 2) {
			los = getLoS(cell1, cell2);
		}
		if (los != null && dist > 2) {
			for (final int i : los) {
				if (i != cell1 && i != cell2 && !map.getCase(i).blockLoS()) {
					return false;
				}
			}
		}
		if (dist > 2) {
			final int cell3 = getNearestCellAround(map, cell2, cell1, null);
			if (cell3 != -1 && !map.getCase(cell3).blockLoS()) {
				return false;
			}
		}
		return true;
	}

	public static int getNearestCellAround(final Map map, final int startCell, final int endCell,
			ArrayList<Case> forbidens) {
		if (map == null) {
			return -1;
		}
		int dist = 1000;
		int cellID = startCell;
		if (forbidens == null) {
			forbidens = new ArrayList<Case>();
		}
		final char[] dirs = { 'b', 'd', 'f', 'h' };
		char[] array;
		for (int length = (array = dirs).length, i = 0; i < length; ++i) {
			final char d = array[i];
			final int c = GetCaseIDFromDirrection(startCell, d, map, true);
			if (map.getCase(c) != null) {
				final int dis = getDistanceBetween(map, endCell, c);
				if (dis < dist && map.getCase(c).isWalkable(true, true, -1) && map.getCase(c).getFirstFighter() == null
						&& !forbidens.contains(map.getCase(c))) {
					dist = dis;
					cellID = c;
				}
			}
		}
		return (cellID == startCell) ? -1 : cellID;
	}

	public static int getNearestCellAroundGA(final Map map, final int startCell, final int endCell,
			ArrayList<Case> forbidens) {
		int dist = 1000;
		int cellID = startCell;
		if (forbidens == null) {
			forbidens = new ArrayList<Case>();
		}
		final char[] dirs = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
		char[] array;
		for (int length = (array = dirs).length, i = 0; i < length; ++i) {
			final char d = array[i];
			final int c = GetCaseIDFromDirrection(startCell, d, map, true);
			final int dis = getDistanceBetween(map, endCell, c);
			if (map.getCase(c) != null) {
				if (dis < dist && map.getCase(c).isWalkable(true) && map.getCase(c).getFirstFighter() == null
						&& !forbidens.contains(map.getCase(c))) {
					dist = dis;
					cellID = c;
				}
			}
		}
		return (cellID == startCell) ? -1 : cellID;
	}

	public static ArrayList<Case> getShortestPathBetween(final Map map, final int start, final int dest,
			final int distMax) {
		ArrayList<Case> curPath = new ArrayList<Case>();
		final ArrayList<Case> curPath2 = new ArrayList<Case>();
		final ArrayList<Case> closeCells = new ArrayList<Case>();
		final int limit = 1000;
		Case curCase = map.getCase(start);
		int stepNum = 0;
		final boolean stop = false;
		while (!stop && stepNum++ <= limit) {
			final int nearestCell = getNearestCellAround(map, curCase.getId(), dest, closeCells);
			if (nearestCell == -1) {
				closeCells.add(curCase);
				if (curPath.size() > 0) {
					curPath.remove(curPath.size() - 1);
					if (curPath.size() > 0) {
						curCase = curPath.get(curPath.size() - 1);
					} else {
						curCase = map.getCase(start);
					}
				} else {
					curCase = map.getCase(start);
				}
			} else {
				if (distMax == 0 && nearestCell == dest) {
					curPath.add(map.getCase(dest));
					break;
				}
				if (distMax > getDistanceBetween(map, nearestCell, dest)) {
					curPath.add(map.getCase(dest));
					break;
				}
				curCase = map.getCase(nearestCell);
				closeCells.add(curCase);
				curPath.add(curCase);
			}
		}
		curCase = map.getCase(start);
		closeCells.clear();
		if (!curPath.isEmpty()) {
			closeCells.add(curPath.get(0));
		}
		while (!stop && stepNum++ <= limit) {
			final int nearestCell = getNearestCellAround(map, curCase.getId(), dest, closeCells);
			if (nearestCell == -1) {
				closeCells.add(curCase);
				if (curPath2.size() > 0) {
					curPath2.remove(curPath2.size() - 1);
					if (curPath2.size() > 0) {
						curCase = curPath2.get(curPath2.size() - 1);
					} else {
						curCase = map.getCase(start);
					}
				} else {
					curCase = map.getCase(start);
				}
			} else {
				if (distMax == 0 && nearestCell == dest) {
					curPath2.add(map.getCase(dest));
					break;
				}
				if (distMax > getDistanceBetween(map, nearestCell, dest)) {
					curPath2.add(map.getCase(dest));
					break;
				}
				curCase = map.getCase(nearestCell);
				closeCells.add(curCase);
				curPath2.add(curCase);
			}
		}
		if ((curPath2.size() < curPath.size() && curPath2.size() > 0) || curPath.isEmpty()) {
			curPath = curPath2;
		}
		return curPath;
	}

	public static String getShortestStringPathBetween(final Map map, final int start, final int dest,
			final int distMax) {
		if (start == dest) {
			return null;
		}
		final ArrayList<Case> path = getShortestPathBetween(map, start, dest, distMax);
		if (path == null) {
			return null;
		}
		String pathstr = "";
		int curCaseID = start;
		char curDir = '\0';
		for (final Case c : path) {
			final char d = getDirBetweenTwoCase(curCaseID, c.getId(), map, true);
			if (d == '\0') {
				return null;
			}
			if (curDir != d) {
				if (path.indexOf(c) != 0) {
					pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
				}
				pathstr = String.valueOf(pathstr) + d;
				curDir = d;
			}
			curCaseID = c.getId();
		}
		if (curCaseID != start) {
			pathstr = String.valueOf(pathstr) + CryptManager.cellID_To_Code(curCaseID);
		}
		if (pathstr == "") {
			return null;
		}
		return "a" + CryptManager.cellID_To_Code(start) + pathstr;
	}

	public static int getCellFromPath(final int start, final int[] path) {
		int cell = start;
		for (int i = 0; i < path.length; ++i) {
			if (path[i] == 1) {
				cell -= 15;
			}
			if (path[i] == 2) {
				cell -= 14;
			}
			if (path[i] == 3) {
				cell += 15;
			}
			if (path[i] == 4) {
				cell += 14;
			}
		}
		return cell;
	}

	public static ArrayList<Integer> triCellList(final Fight fight, final Fighter fighter,
			final ArrayList<Integer> cells) {
		final ArrayList<Integer> Fcells = new ArrayList<Integer>();
		int dist = 100;
		int curCell = 0;
		int curIndex = 0;
		while (cells.size() > 0) {
			dist = 100;
			for (final int i : cells) {
				final int d = getDistanceBetween(fight.getMap(), fighter.getCell().getId(), i);
				if (dist > d) {
					dist = d;
					curCell = i;
					curIndex = cells.indexOf(i);
				}
			}
			Fcells.add(curCell);
			cells.remove(curIndex);
		}
		return Fcells;
	}

	public static boolean isBord1(final int id) {
		final int[] bords = { 1, 30, 59, 88, 117, 146, 175, 204, 233, 262, 291, 320, 349, 378, 407, 436, 465, 15, 44,
				73, 102, 131, 160, 189, 218, 247, 276, 305, 334, 363, 392, 421, 450, 479 };
		final ArrayList<Integer> test = new ArrayList<Integer>();
		int[] array;
		for (int length = (array = bords).length, j = 0; j < length; ++j) {
			final int i = array[j];
			test.add(i);
		}
		return test.contains(id);
	}

	public static boolean isBord2(final int id) {
		final int[] bords = { 16, 45, 74, 103, 132, 161, 190, 219, 248, 277, 306, 335, 364, 393, 422, 451, 29, 58, 87,
				116, 145, 174, 203, 232, 261, 290, 319, 348, 377, 406, 435, 464 };
		final ArrayList<Integer> test = new ArrayList<Integer>();
		int[] array;
		for (int length = (array = bords).length, j = 0; j < length; ++j) {
			final int i = array[j];
			test.add(i);
		}
		return test.contains(id);
	}

	public static ArrayList<Integer> getLoS(final int cell1, final int cell2) {
		final ArrayList<Integer> Los = new ArrayList<Integer>();
		boolean next = false;
		final int[] dir1 = { 1, -1, 29, -29, 15, 14, -15, -14 };
		int[] array;
		for (int length = (array = dir1).length, j = 0; j < length; ++j) {
			final int i = array[j];
			Los.clear();
			int cell3 = cell1;
			Los.add(cell3);
			next = false;
			while (!next) {
				cell3 += i;
				Los.add(cell3);
				if (isBord2(cell3) || isBord1(cell3) || cell3 <= 0 || cell3 >= 480) {
					next = true;
				}
				if (cell3 == cell2) {
					return Los;
				}
			}
		}
		return null;
	}

	public static boolean checkLoS(final Map map, final int cell1, final int cell2, final Fighter fighter,
			final boolean isPeur) {
		if (fighter != null && fighter.getPersonnage() != null) {
			return true;
		}
		ArrayList<Integer> CellsToConsider = new ArrayList<Integer>();
		CellsToConsider = getLoSBotheringIDCases(map, cell1, cell2, true);
		if (CellsToConsider == null) {
			return true;
		}
		for (final Integer cellID : CellsToConsider) {
			if (map.getCase(cellID) != null
					&& (!map.getCase(cellID).blockLoS() || (!map.getCase(cellID).isWalkable(false) && isPeur))) {
				return false;
			}
		}
		return true;
	}

	private static ArrayList<Integer> getLoSBotheringIDCases(final Map map, final int cellID1, final int cellID2,
			final boolean Combat) {
		final ArrayList<Integer> toReturn = new ArrayList<Integer>();
		int consideredCell1 = cellID1;
		int consideredCell2 = cellID2;
		char dir = 'b';
		int diffX = 0;
		int diffY = 0;
		int compteur = 0;
		final ArrayList<Character> dirs = new ArrayList<Character>();
		dirs.add('b');
		dirs.add('d');
		dirs.add('f');
		dirs.add('h');
		while (getDistanceBetween(map, consideredCell1, consideredCell2) > 2 && compteur < 300) {
			diffX = getCellXCoord(map, consideredCell1) - getCellXCoord(map, consideredCell2);
			diffY = getCellYCoord(map, consideredCell1) - getCellYCoord(map, consideredCell2);
			if (Math.abs(diffX) > Math.abs(diffY)) {
				if (diffX > 0) {
					dir = 'f';
				} else {
					dir = 'b';
				}
				consideredCell1 = GetCaseIDFromDirrection(consideredCell1, dir, map, Combat);
				consideredCell2 = GetCaseIDFromDirrection(consideredCell2, getOpositeDirection(dir), map, Combat);
				toReturn.add(consideredCell1);
				toReturn.add(consideredCell2);
			} else if (Math.abs(diffX) < Math.abs(diffY)) {
				if (diffY > 0) {
					dir = 'h';
				} else {
					dir = 'd';
				}
				consideredCell1 = GetCaseIDFromDirrection(consideredCell1, dir, map, Combat);
				consideredCell2 = GetCaseIDFromDirrection(consideredCell2, getOpositeDirection(dir), map, Combat);
				toReturn.add(consideredCell1);
				toReturn.add(consideredCell2);
			} else {
				if (compteur == 0) {
					return getLoSBotheringCasesInDiagonal(map, cellID1, cellID2, diffX, diffY);
				}
				if (dir == 'f' || dir == 'b') {
					if (diffY > 0) {
						dir = 'h';
					} else {
						dir = 'd';
					}
				} else if (dir == 'h' || dir == 'd') {
					if (diffX > 0) {
						dir = 'f';
					} else {
						dir = 'b';
					}
				}
				consideredCell1 = GetCaseIDFromDirrection(consideredCell1, dir, map, Combat);
				consideredCell2 = GetCaseIDFromDirrection(consideredCell2, getOpositeDirection(dir), map, Combat);
				toReturn.add(consideredCell1);
				toReturn.add(consideredCell2);
			}
			++compteur;
		}
		if (getDistanceBetween(map, consideredCell1, consideredCell2) == 2) {
			dir = '\0';
			diffX = getCellXCoord(map, consideredCell1) - getCellXCoord(map, consideredCell2);
			diffY = getCellYCoord(map, consideredCell1) - getCellYCoord(map, consideredCell2);
			if (diffX == 0) {
				if (diffY > 0) {
					dir = 'h';
				} else {
					dir = 'd';
				}
			}
			if (diffY == 0) {
				if (diffX > 0) {
					dir = 'f';
				} else {
					dir = 'b';
				}
			}
			if (dir != '\0') {
				toReturn.add(GetCaseIDFromDirrection(consideredCell1, dir, map, Combat));
			}
		}
		return toReturn;
	}

	private static ArrayList<Integer> getLoSBotheringCasesInDiagonal(final Map map, final int cellID1,
			final int cellID2, final int diffX, final int diffY) {
		final ArrayList<Integer> toReturn = new ArrayList<Integer>();
		char dir = 'a';
		if (diffX > 0 && diffY > 0) {
			dir = 'g';
		}
		if (diffX > 0 && diffY < 0) {
			dir = 'e';
		}
		if (diffX < 0 && diffY > 0) {
			dir = 'a';
		}
		if (diffX < 0 && diffY < 0) {
			dir = 'c';
		}
		for (int consideredCell = cellID1, compteur = 0; consideredCell != -1 && compteur < 100; ++compteur) {
			consideredCell = GetCaseIDFromDirrection(consideredCell, dir, map, true);
			if (consideredCell == cellID2) {
				return toReturn;
			}
			toReturn.add(consideredCell);
		}
		return toReturn;
	}

	public static ArrayList<Fighter> getFightersAround(final int cellID, final Map map, final Fight fight) {
		final char[] dirs = { 'b', 'd', 'f', 'h' };
		final ArrayList<Fighter> fighters = new ArrayList<Fighter>();
		char[] array;
		for (int length = (array = dirs).length, i = 0; i < length; ++i) {
			final char dir = array[i];
			final Fighter f = map.getCase(GetCaseIDFromDirrection(cellID, dir, map, false)).getFirstFighter();
			if (f != null) {
				fighters.add(f);
			}
		}
		return fighters;
	}

	public static char getDirEntreDosCeldas(final Map map, final int id1, final int id2) {
		if (id1 == id2) {
			return '\0';
		}
		if (map == null) {
			return '\0';
		}
		final int difX = getCellXCoord(map, id1) - getCellXCoord(map, id2);
		final int difY = getCellYCoord(map, id1) - getCellYCoord(map, id2);
		final int difXabs = Math.abs(difX);
		final int difYabs = Math.abs(difY);
		if (difXabs > difYabs) {
			if (difX > 0) {
				return 'f';
			}
			return 'b';
		} else {
			if (difY > 0) {
				return 'h';
			}
			return 'd';
		}
	}

	public static int getCellArroundByDir(final int cellId, final char dir, final Map map) {
		if (map == null) {
			return -1;
		}
		switch (dir) {
		case 'b': {
			return cellId + map.getW();
		}
		case 'd': {
			return cellId + (map.getW() - 1);
		}
		case 'f': {
			return cellId - map.getW();
		}
		case 'h': {
			return cellId - map.getW() + 1;
		}
		default: {
			return -1;
		}
		}
	}

	public static Case checkIfCanPushEntity(final Fight fight, final int startCell, final int endCell,
			final char direction) {
		final Map map = fight.getMap();
		Case actualCell;
		Case oldCell = actualCell = map.getCase(getCellArroundByDir(startCell, direction, map));
		while (actualCell.getId() != endCell) {
			actualCell = map.getCase(getCellArroundByDir(actualCell.getId(), direction, map));
			if (!actualCell.getFighters().isEmpty() || !actualCell.isWalkable(true)) {
				return oldCell;
			}
			for (final Trap trap : fight.getAllTraps()) {
				if (getDistanceBetween(fight.getMap(), trap.getCell().getId(), actualCell.getId()) <= trap.getSize()) {
					return actualCell;
				}
			}
			oldCell = actualCell;
		}
		return null;
	}

	public static boolean haveFighterOnThisCell(final int cell, final Fight fight) {
		for (final Fighter f : fight.getFighters(3)) {
			if (f.getCell().getId() == cell && !f.isDead()) {
				return true;
			}
		}
		return false;
	}

	public static int getCaseIDFromDirrection(final int CaseID, final char Direccion, final Map map) {
		switch (Direccion) {
		case 'b': {
			return CaseID + map.getW();
		}
		case 'd': {
			return CaseID + (map.getW() - 1);
		}
		case 'f': {
			return CaseID - map.getW();
		}
		case 'h': {
			return CaseID - map.getW() + 1;
		}
		default: {
			return -1;
		}
		}
	}

	public static boolean cellArroundCaseIDisOccuped(final Fight fight, final int cell) {
		final char[] dirs = { 'b', 'd', 'f', 'h' };
		final ArrayList<Integer> Cases = new ArrayList<Integer>();
		char[] array;
		for (int length = (array = dirs).length, i = 0; i < length; ++i) {
			final char dir = array[i];
			final int caseID = GetCaseIDFromDirrection(cell, dir, fight.getMap(), true);
			Cases.add(caseID);
		}
		int ha = 0;
		for (int o = 0; o < Cases.size(); ++o) {
			if (fight.getMap().getCase(Cases.get(o)).getFirstFighter() != null) {
				++ha;
			}
		}
		return ha != 4;
	}
}

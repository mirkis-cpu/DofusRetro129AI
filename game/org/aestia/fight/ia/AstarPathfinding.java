// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight.ia;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.common.Pathfinding;
import org.aestia.fight.Fight;
import org.aestia.map.Case;

public class AstarPathfinding {
	private Map<Integer, Node> openList;
	private Map<Integer, Node> closeList;
	private org.aestia.map.Map map;
	private Fight fight;
	private int cellStart;
	private int cellEnd;

	public AstarPathfinding(final org.aestia.map.Map map, final Fight fight, final int cellStart, final int cellEnd) {
		this.openList = new TreeMap<Integer, Node>();
		this.closeList = new LinkedHashMap<Integer, Node>();
		this.setMap(map);
		this.setFight(fight);
		this.setCellStart(cellStart);
		this.setCellEnd(cellEnd);
	}

	public ArrayList<Case> getShortestPath(final int value) {
		final Node nodeStart = new Node(this.getCellStart(), null);
		this.openList.put(this.getCellStart(), nodeStart);
		while (!this.openList.isEmpty() && !this.closeList.containsKey(this.getCellEnd())) {
			final char[] dirs = { 'b', 'd', 'f', 'h' };
			final Node nodeCurrent = this.bestNode();
			if (nodeCurrent.getCellId() == this.getCellEnd()
					&& !Pathfinding.cellArroundCaseIDisOccuped(this.getFight(), nodeCurrent.getCellId())) {
				return this.getPath();
			}
			this.addListClose(nodeCurrent);
			for (int loc0 = 0; loc0 < 4; ++loc0) {
				final int cell = Pathfinding.getCaseIDFromDirrection(nodeCurrent.getCellId(), dirs[loc0],
						this.getMap());
				final Node node = new Node(cell, nodeCurrent);
				if (this.getMap().getCase(cell) != null) {
					if (this.getMap().getCase(cell).isWalkable(true, true, -1) || cell == this.getCellEnd()) {
						if (!Pathfinding.haveFighterOnThisCell(cell, this.getFight()) || cell == this.getCellEnd()) {
							if (!this.closeList.containsKey(cell)) {
								if (this.openList.containsKey(cell)) {
									if (this.openList.get(cell).getCountG() > this.getCostG(node)) {
										nodeCurrent.setChild(this.openList.get(cell));
										this.openList.get(cell).setParent(nodeCurrent);
										this.openList.get(cell).setCountG(this.getCostG(node));
										this.openList.get(cell).setHeristic(
												Pathfinding.getDistanceBetween(this.getMap(), cell, this.getCellEnd())
														* 10);
										this.openList.get(cell).setCountF(this.openList.get(cell).getCountG()
												+ this.openList.get(cell).getHeristic());
									}
								} else {
									if (value == 0 && Pathfinding.casesAreInSameLine(this.getMap(), cell,
											this.getCellEnd(), dirs[loc0])) {
										node.setCountF(node.getCountG() + node.getHeristic() - 10);
									}
									this.openList.put(cell, node);
									nodeCurrent.setChild(node);
									node.setParent(nodeCurrent);
									node.setCountG(this.getCostG(node));
									node.setHeristic(
											Pathfinding.getDistanceBetween(this.getMap(), cell, this.getCellEnd())
													* 10);
									node.setCountF(node.getCountG() + node.getHeristic());
								}
							}
						}
					}
				}
			}
		}
		return this.getPath();
	}

	private ArrayList<Case> getPath() {
		Node current = this.getLastNode(this.closeList);
		if (current == null) {
			return null;
		}
		final ArrayList<Case> path = new ArrayList<Case>();
		final Map<Integer, Case> path2 = new TreeMap<Integer, Case>();
		int index = this.closeList.size();
		while (current.getCellId() != this.getCellStart()) {
			if (current.getCellId() != this.getCellStart()) {
				path2.put(index, this.getMap().getCase(current.getCellId()));
				current = current.getParent();
			}
			--index;
		}
		index = -1;
		while (path.size() != path2.size()) {
			++index;
			if (path2.get(index) == null) {
				continue;
			}
			path.add(path2.get(index));
		}
		return path;
	}

	private Node getLastNode(final Map<Integer, Node> list) {
		Node node = null;
		for (final Map.Entry<Integer, Node> entry : list.entrySet()) {
			node = entry.getValue();
		}
		return node;
	}

	private Node bestNode() {
		int bestCountF = 150000;
		Node bestNode = null;
		for (final Node node : this.openList.values()) {
			if (node.getCountF() < bestCountF) {
				bestCountF = node.getCountF();
				bestNode = node;
			}
		}
		return bestNode;
	}

	private void addListClose(final Node node) {
		if (this.openList.containsKey(node.getCellId())) {
			this.openList.remove(node.getCellId());
		}
		if (!this.closeList.containsKey(node.getCellId())) {
			this.closeList.put(node.getCellId(), node);
		}
	}

	private int getCostG(Node node) {
		int costG;
		for (costG = 0; node.getCellId() == this.getCellStart(); node = node.getParent(), costG += 10) {
		}
		return costG;
	}

	public org.aestia.map.Map getMap() {
		return this.map;
	}

	public void setMap(final org.aestia.map.Map map) {
		this.map = map;
	}

	public Fight getFight() {
		return this.fight;
	}

	public void setFight(final Fight fight) {
		this.fight = fight;
	}

	public int getCellStart() {
		return this.cellStart;
	}

	public void setCellStart(final int cellStart) {
		this.cellStart = cellStart;
	}

	public int getCellEnd() {
		return this.cellEnd;
	}

	public void setCellEnd(final int cellEnd) {
		this.cellEnd = cellEnd;
	}
}

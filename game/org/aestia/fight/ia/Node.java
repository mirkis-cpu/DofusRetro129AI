// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.fight.ia;

public class Node {
	private int countG;
	private int countF;
	private int heristic;
	private int cellId;
	private Node parent;
	private Node child;

	public Node(final int cellId, final Node parent) {
		this.countG = 0;
		this.countF = 0;
		this.heristic = 0;
		this.setCellId(cellId);
		this.setParent(parent);
	}

	public int getCountG() {
		return this.countG;
	}

	public void setCountG(final int countG) {
		this.countG = countG;
	}

	public int getCountF() {
		return this.countF;
	}

	public void setCountF(final int countF) {
		this.countF = countF;
	}

	public int getHeristic() {
		return this.heristic;
	}

	public void setHeristic(final int heristic) {
		this.heristic = heristic;
	}

	public int getCellId() {
		return this.cellId;
	}

	public void setCellId(final int cellId) {
		this.cellId = cellId;
	}

	public Node getParent() {
		return this.parent;
	}

	public void setParent(final Node parent) {
		this.parent = parent;
	}

	public Node getChild() {
		return this.child;
	}

	public void setChild(final Node child) {
		this.child = child;
	}
}

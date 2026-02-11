// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.hdv;

public class HdvEntry implements Comparable<HdvEntry> {
	private int id;
	private int hdvId;
	private int lineId;
	private int owner;
	private int price;
	private byte amount;
	private org.aestia.object.Object object;
	public boolean buy;

	public HdvEntry(final int id, final int price, final byte amount, final int owner,
			final org.aestia.object.Object object) {
		this.buy = false;
		this.setId(id);
		this.price = price;
		this.amount = amount;
		this.object = object;
		this.owner = owner;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setHdvId(final int id) {
		this.hdvId = id;
	}

	public int getHdvId() {
		return this.hdvId;
	}

	public int getLineId() {
		return this.lineId;
	}

	public void setLineId(final int lineId) {
		this.lineId = lineId;
	}

	public int getOwner() {
		return this.owner;
	}

	public int getPrice() {
		return this.price;
	}

	public byte getAmount(final boolean ok) {
		if (ok) {
			return (byte) (Math.pow(10.0, this.amount) / 10.0);
		}
		return this.amount;
	}

	public org.aestia.object.Object getObject() {
		return this.object;
	}

	public String parseToEL() {
		final StringBuilder toReturn = new StringBuilder();
		final int count = this.getAmount(true);
		toReturn.append(this.getLineId()).append(";").append(count).append(";")
				.append(this.getObject().getTemplate().getId()).append(";").append(this.getObject().parseStatsString())
				.append(";").append(this.price).append(";350");
		return toReturn.toString();
	}

	public String parseToEmK() {
		final StringBuilder toReturn = new StringBuilder();
		final int count = this.getAmount(true);
		toReturn.append(this.getObject().getGuid()).append("|").append(count).append("|")
				.append(this.getObject().getTemplate().getId()).append("|").append(this.getObject().parseStatsString())
				.append("|").append(this.price).append("|350");
		return toReturn.toString();
	}

	@Override
	public int compareTo(final HdvEntry o) {
		final int celuiCi = this.getPrice();
		final int autre = o.getPrice();
		if (autre > celuiCi) {
			return -1;
		}
		if (autre == celuiCi) {
			return 0;
		}
		if (autre < celuiCi) {
			return 1;
		}
		return 0;
	}
}

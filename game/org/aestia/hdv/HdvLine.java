// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.hdv;

import java.util.ArrayList;
import java.util.Collections;

public class HdvLine {
	private int lineId;
	private int templateId;
	private ArrayList<ArrayList<HdvEntry>> entries;
	private String strStats;

	public HdvLine(final int lineId, final HdvEntry toAdd) {
		this.entries = new ArrayList<ArrayList<HdvEntry>>(3);
		this.lineId = lineId;
		this.templateId = toAdd.getObject().getTemplate().getId();
		this.strStats = toAdd.getObject().parseStatsString();
		for (int i = 0; i < 3; ++i) {
			this.getEntries().add(new ArrayList<HdvEntry>());
		}
		this.addEntry(toAdd);
	}

	public int getLineId() {
		return this.lineId;
	}

	public int getTemplateId() {
		return this.templateId;
	}

	public ArrayList<ArrayList<HdvEntry>> getEntries() {
		return this.entries;
	}

	public String getStrStats() {
		return this.strStats;
	}

	public boolean haveSameStats(final HdvEntry toAdd) {
		return this.getStrStats().equalsIgnoreCase(toAdd.getObject().parseStatsStringSansUserObvi())
				&& toAdd.getObject().getTemplate().getType() != 85;
	}

	public void sort(final byte index) {
		Collections.sort(this.getEntries().get(index));
	}

	public boolean addEntry(final HdvEntry toAdd) {
		if (!this.haveSameStats(toAdd) && !this.isEmpty()) {
			return false;
		}
		toAdd.setLineId(this.getLineId());
		final byte index = (byte) (toAdd.getAmount(false) - 1);
		this.getEntries().get(index).add(toAdd);
		this.sort(index);
		return true;
	}

	public boolean delEntry(final HdvEntry toDel) {
		final byte index = (byte) (toDel.getAmount(false) - 1);
		final boolean toReturn = this.getEntries().get(index).remove(toDel);
		this.sort(index);
		return toReturn;
	}

	public HdvEntry doYouHave(final int amount, final int price) {
		for (int index = amount - 1, i = 0; i < this.getEntries().get(index).size(); ++i) {
			if (this.getEntries().get(index).get(i).getPrice() == price) {
				return this.getEntries().get(index).get(i);
			}
		}
		return null;
	}

	public int[] getFirsts() {
		final int[] toReturn = new int[3];
		for (int i = 0; i < this.getEntries().size(); ++i) {
			try {
				toReturn[i] = this.getEntries().get(i).get(0).getPrice();
			} catch (IndexOutOfBoundsException e) {
				toReturn[i] = 0;
			}
		}
		return toReturn;
	}

	public ArrayList<HdvEntry> getAll() {
		final int totalSize = this.getEntries().get(0).size() + this.getEntries().get(1).size()
				+ this.getEntries().get(2).size();
		final ArrayList<HdvEntry> toReturn = new ArrayList<HdvEntry>(totalSize);
		for (int qte = 0; qte < this.getEntries().size(); ++qte) {
			toReturn.addAll(this.getEntries().get(qte));
		}
		return toReturn;
	}

	public boolean isEmpty() {
		if (this.getEntries().isEmpty()) {
			return true;
		}
		for (int i = 0; i < this.getEntries().size(); ++i) {
			try {
				if (!this.getEntries().get(i).isEmpty()) {
					if (this.getEntries().get(i).get(0) != null) {
						return false;
					}
				}
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public String parseToEHl() {
		final StringBuilder toReturn = new StringBuilder();
		final int[] price = this.getFirsts();
		toReturn.append(this.getLineId()).append(";").append(this.getStrStats()).append(";")
				.append((price[0] == 0) ? "" : price[0]).append(";").append((price[1] == 0) ? "" : price[1]).append(";")
				.append((price[2] == 0) ? "" : price[2]);
		return toReturn.toString();
	}

	public String parseToEHm() {
		final StringBuilder toReturn = new StringBuilder();
		final int[] prix = this.getFirsts();
		toReturn.append(this.getLineId()).append("|").append(this.getTemplateId()).append("|")
				.append(this.getStrStats()).append("|").append((prix[0] == 0) ? "" : prix[0]).append("|")
				.append((prix[1] == 0) ? "" : prix[1]).append("|").append((prix[2] == 0) ? "" : prix[2]);
		return toReturn.toString();
	}
}

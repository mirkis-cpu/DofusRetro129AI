// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.hdv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.aestia.game.world.World;

public class HdvTemplate {
	private int templateId;
	private Map<Integer, HdvLine> lines;

	public HdvTemplate(final int templateId, final HdvEntry toAdd) {
		this.lines = new HashMap<Integer, HdvLine>();
		this.templateId = templateId;
		this.addEntry(toAdd);
	}

	public int getTemplateId() {
		return this.templateId;
	}

	public Map<Integer, HdvLine> getLines() {
		return this.lines;
	}

	public HdvLine getLine(final int lineId) {
		return this.lines.get(lineId);
	}

	public void addEntry(final HdvEntry toAdd) {
		for (final HdvLine line : this.getLines().values()) {
			if (line.addEntry(toAdd)) {
				return;
			}
		}
		final int lineId = World.getNextLigneID();
		this.getLines().put(lineId, new HdvLine(lineId, toAdd));
	}

	public boolean delEntry(final HdvEntry toDel) {
		final boolean toReturn = this.getLines().get(toDel.getLineId()).delEntry(toDel);
		if (this.getLines().get(toDel.getLineId()).isEmpty()) {
			this.getLines().remove(toDel.getLineId());
		}
		return toReturn;
	}

	public ArrayList<HdvEntry> getAllEntry() {
		final ArrayList<HdvEntry> toReturn = new ArrayList<HdvEntry>();
		for (final HdvLine line : this.getLines().values()) {
			toReturn.addAll(line.getAll());
		}
		return toReturn;
	}

	public boolean isEmpty() {
		return this.getLines().size() == 0;
	}

	public String parseToEHl() {
		String toReturn = String.valueOf(this.getTemplateId()) + "|";
		boolean isFirst = true;
		for (final HdvLine line : this.getLines().values()) {
			if (!isFirst) {
				toReturn = String.valueOf(toReturn) + "|";
			}
			toReturn = String.valueOf(toReturn) + line.parseToEHl();
			isFirst = false;
		}
		return toReturn;
	}
}

// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity.npc;

import org.aestia.client.Player;
import org.aestia.quest.Quest;

public class Npc {
	private int id;
	private int cellid;
	private byte orientation;
	private NpcTemplate template;
	private boolean isMovable;

	public Npc(final int id, final int cellid, final byte orientation, final NpcTemplate template,
			final boolean isMovable) {
		this.id = id;
		this.cellid = cellid;
		this.orientation = orientation;
		this.template = template;
		this.isMovable = isMovable;
	}

	public boolean isMovable() {
		return this.isMovable;
	}

	public int getId() {
		return this.id;
	}

	public int getCellid() {
		return this.cellid;
	}

	public void setCellid(final int cellid) {
		this.cellid = cellid;
	}

	public int getOrientation() {
		return this.orientation;
	}

	public void setOrientation(final byte orientation) {
		this.orientation = orientation;
	}

	public NpcTemplate getTemplate() {
		return this.template;
	}

	public String parse(final boolean alter, final Player p) {
		final StringBuilder sock = new StringBuilder();
		sock.append(alter ? "~" : "+");
		sock.append(this.cellid).append(";");
		sock.append(this.orientation).append(";");
		sock.append("0").append(";");
		sock.append(this.id).append(";");
		sock.append(this.template.get_id()).append(";");
		sock.append("-4").append(";");
		sock.append(this.template.get_gfxID()).append("^");
		if (this.template.get_scaleX() == this.template.get_scaleY()) {
			sock.append(this.template.get_scaleY()).append(";");
		} else {
			sock.append(this.template.get_scaleX()).append("x").append(this.template.get_scaleY()).append(";");
		}
		sock.append(this.template.get_sex()).append(";");
		sock.append((this.template.get_color1() != -1) ? Integer.toHexString(this.template.get_color1()) : "-1")
				.append(";");
		sock.append((this.template.get_color2() != -1) ? Integer.toHexString(this.template.get_color2()) : "-1")
				.append(";");
		sock.append((this.template.get_color3() != -1) ? Integer.toHexString(this.template.get_color3()) : "-1")
				.append(";");
		sock.append(this.template.get_acces()).append(";");
		final Quest q = this.template.getQuest();
		if (q == null) {
			sock.append(-1).append(";");
		} else if (p.getQuestPersoByQuest(q) == null) {
			sock.append((this.template.get_extraClip() != -1) ? this.template.get_extraClip() : "").append(";");
		} else {
			sock.append(-1).append(";");
		}
		sock.append(this.template.get_customArtWork());
		return sock.toString();
	}
}

/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 */
package org.aestia.db.statique.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.db.statique.AbstractDAO;
import org.aestia.entity.npc.NpcTemplate;
import org.aestia.game.world.World;
import org.aestia.kernel.Main;
import org.aestia.object.ObjectTemplate;
import org.aestia.quest.Quest;

public class Npc_templateData extends AbstractDAO<NpcTemplate> {
	public Npc_templateData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(NpcTemplate npc) {
		String i = "";
		boolean first = true;
		for (ObjectTemplate obj : npc.getAllItem()) {
			i = first ? String.valueOf(i) + obj.getId() : String.valueOf(i) + "," + obj.getId();
			first = false;
		}
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE npc_template SET `ventes` = ? WHERE `id` = ?");
			p.setString(1, i);
			p.setInt(2, npc.get_id());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Npc_templateData update", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM npc_template");

				while (RS.next()) {
					int id = RS.getInt("id");
					int bonusValue = RS.getInt("bonusValue");
					int gfxID = RS.getInt("gfxID");
					int scaleX = RS.getInt("scaleX");
					int scaleY = RS.getInt("scaleY");
					int sex = RS.getInt("sex");
					int color1 = RS.getInt("color1");
					int color2 = RS.getInt("color2");
					int color3 = RS.getInt("color3");
					String access = RS.getString("accessories");
					int extraClip = RS.getInt("extraClip");
					int customArtWork = RS.getInt("customArtWork");
					String initQId = RS.getString("initQuestion");
					String ventes = RS.getString("ventes");
					String quests = RS.getString("quests");
					String exchanges = RS.getString("exchanges");
					World.addNpcTemplate(new NpcTemplate(id, bonusValue, gfxID, scaleX, scaleY, sex, color1, color2,
							color3, access, extraClip, customArtWork, initQId, ventes, quests, exchanges));
				}
			} catch (SQLException e) {
				super.sendError("Npc_templateData load", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
	}

	public void loadQuest() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT id, quests FROM npc_template");

				while (RS.next()) {
					Quest q;
					NpcTemplate nt;
					int id = RS.getInt("id");
					String quests = RS.getString("quests");
					if (quests.equalsIgnoreCase("") || (nt = World.getNPCTemplate(id)) == null
							|| (q = Quest.getQuestById(Integer.parseInt(quests))) == null)
						continue;
					nt.setQuest(q);
				}
			} catch (Exception e) {
				super.sendError("Npc_templateData loadQuest", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
	}

	public void reload() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM npc_template");

				while (RS.next()) {
					int scaleX;
					String exchanges;
					String quests;
					String access;
					int gfxID;
					int customArtWork;
					int color1;
					int color3;
					String ventes;
					int id;
					String initQId;
					int extraClip;
					int sex;
					int scaleY;
					int bonusValue;
					int color2;
					if (World.getNPCTemplate(RS.getInt("id")) == null) {
						id = RS.getInt("id");
						bonusValue = RS.getInt("bonusValue");
						gfxID = RS.getInt("gfxID");
						scaleX = RS.getInt("scaleX");
						scaleY = RS.getInt("scaleY");
						sex = RS.getInt("sex");
						color1 = RS.getInt("color1");
						color2 = RS.getInt("color2");
						color3 = RS.getInt("color3");
						access = RS.getString("accessories");
						extraClip = RS.getInt("extraClip");
						customArtWork = RS.getInt("customArtWork");
						initQId = RS.getString("initQuestion");
						ventes = RS.getString("ventes");
						quests = RS.getString("quests");
						exchanges = RS.getString("exchanges");
						World.addNpcTemplate(new NpcTemplate(id, bonusValue, gfxID, scaleX, scaleY, sex, color1, color2,
								color3, access, extraClip, customArtWork, initQId, ventes, quests, exchanges));
						continue;
					}
					id = RS.getInt("id");
					bonusValue = RS.getInt("bonusValue");
					gfxID = RS.getInt("gfxID");
					scaleX = RS.getInt("scaleX");
					scaleY = RS.getInt("scaleY");
					sex = RS.getInt("sex");
					color1 = RS.getInt("color1");
					color2 = RS.getInt("color2");
					color3 = RS.getInt("color3");
					access = RS.getString("accessories");
					extraClip = RS.getInt("extraClip");
					customArtWork = RS.getInt("customArtWork");
					initQId = RS.getString("initQuestion");
					ventes = RS.getString("ventes");
					quests = RS.getString("quests");
					exchanges = RS.getString("exchanges");
					World.getNPCTemplate(RS.getInt("id")).setInfos(id, bonusValue, gfxID, scaleX, scaleY, sex, color1,
							color2, color3, access, extraClip, customArtWork, initQId, ventes, quests, exchanges);
				}
			} catch (SQLException e) {
				super.sendError("Npc_templateData reload", e);
			}
		} finally {
			this.close(RS);
		}
	}

	public boolean updateInitQuestion(int id, int q) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE `npc_template` SET `initQuestion` = ? WHERE `id` = ?");
			p.setInt(1, q);
			p.setInt(2, id);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Npc_templateData updateInitQuestion", e);
		} finally {
			this.close(p);
		}
		return false;
	}
}

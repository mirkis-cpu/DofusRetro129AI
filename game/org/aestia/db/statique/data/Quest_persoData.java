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

import org.aestia.client.Player;
import org.aestia.db.statique.AbstractDAO;
import org.aestia.kernel.Main;
import org.aestia.quest.Quest;

public class Quest_persoData extends AbstractDAO<Quest.Quest_Perso> {
	public Quest_persoData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Quest.Quest_Perso qp) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE quest_perso SET `finish` = ?, `etapeValidate` = ? WHERE `id` = ?");
			p.setInt(1, qp.isFinish() ? 1 : 0);
			p.setString(2, qp.getQuestEtapeString());
			p.setInt(3, qp.getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Quest_persoData update", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public void loadPerso(Player perso) {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM quest_perso WHERE perso = " + perso.getId());

				while (RS.next()) {
					Quest.Quest_Perso questPe = new Quest.Quest_Perso(RS.getInt("guid"), RS.getInt("id"),
							RS.getInt("finish") == 1, RS.getInt("perso"), RS.getString("etapeValidate"));
					perso.addQuestPerso(questPe);
				}
			} catch (SQLException e) {
				super.sendError("Quest_persoData loadPerso", e);
			}
		} finally {
			this.close(RS);
		}
	}

	public boolean delete(int id) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("DELETE FROM quest_perso WHERE guid = ?");
			p.setInt(1, id);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Quest_persoData delete", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public int getNextQuest() {
		int guid;
		block6: {
			ResultSet RS = null;
			guid = 0;
			try {
				try {
					RS = this.getData("SELECT guid FROM quest_perso ORDER BY guid DESC LIMIT 1");

					if (!RS.first()) {
						guid = 1;
						break block6;
					}
					guid = RS.getInt("guid") + 1;
				} catch (SQLException e) {
					super.sendError("Quest_persoData getNextQuest", e);
					Main.stop();
				}
			} finally {
				this.close(RS);
			}
		}
		return guid;
	}

	public boolean add(Quest.Quest_Perso QP) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("INSERT INTO `quest_perso` VALUES (?,?,?,?,?)");
			p.setInt(1, QP.getId());
			p.setInt(2, QP.getQuest().getId());
			p.setInt(3, QP.isFinish() ? 1 : 0);
			p.setInt(4, QP.getPlayer().getId());
			p.setString(5, QP.getQuestEtapeString());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Quest_persoData add", e);
		} finally {
			this.close(p);
		}
		return false;
	}
}

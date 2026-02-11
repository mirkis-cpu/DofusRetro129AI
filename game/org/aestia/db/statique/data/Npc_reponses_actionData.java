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
import org.aestia.entity.npc.NpcAnswer;
import org.aestia.game.world.World;
import org.aestia.kernel.Main;
import org.aestia.other.Action;

public class Npc_reponses_actionData extends AbstractDAO<Object> {
	public Npc_reponses_actionData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Object obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM npc_reponses_actions");

				while (RS.next()) {
					int id = RS.getInt("ID");
					int type = RS.getInt("type");
					String args = RS.getString("args");
					if (World.getNpcAnswer(id) == null) {
						World.addNpcAnswer(new NpcAnswer(id));
					}
					World.getNpcAnswer(id).addAction(new Action(type, args, "", null));
				}
			} catch (SQLException e) {
				super.sendError("Npc_reponses_actionData load", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
	}

	public boolean add(int repID, int type, String args) {
		PreparedStatement p;
		p = null;
		try {
			try {
				p = this.getPreparedStatement("DELETE FROM `npc_reponses_actions` WHERE `ID` = ? AND `type` = ?");
				p.setInt(1, repID);
				p.setInt(2, type);
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("Npc_reponses_actionData add", e);
			}
		} catch (Throwable var6_7) {
			this.close(p);
			throw var6_7;
		} finally {
			this.close(p);
		}

		try {
			p = this.getPreparedStatement("INSERT INTO `npc_reponses_actions` VALUES (?,?,?)");
			p.setInt(1, repID);
			p.setInt(2, type);
			p.setString(3, args);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Npc_reponses_actionData add", e);
		} finally {
			this.close(p);
		}
		return false;
	}
}

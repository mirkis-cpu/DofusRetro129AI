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
import org.aestia.entity.npc.NpcQuestion;
import org.aestia.game.world.World;
import org.aestia.kernel.Main;

public class Npc_questionData extends AbstractDAO<NpcQuestion> {
	public Npc_questionData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(NpcQuestion obj) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE `npc_questions` SET `responses` = ?WHERE `ID` = ?");
			p.setString(1, obj.getAnwsers());
			p.setInt(2, obj.getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Npc_questionData update", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public void updateLot() {
		int lot = Integer.parseInt(World.getNPCQuestion(1646).getArgs()) + 50;
		World.getNPCQuestion(1646).setArgs(String.valueOf(lot));
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `npc_questions` SET params='" + lot + "' WHERE `id`=1646");
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("Npc_questionData updateLot", e);
			}
		} finally {
			this.close(p);
		}
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM npc_questions");

				while (RS.next()) {
					World.addNPCQuestion(new NpcQuestion(RS.getInt("ID"), RS.getString("responses"),
							RS.getString("params"), RS.getString("cond"), RS.getString("ifFalse")));
				}
			} catch (SQLException e) {
				super.sendError("Npc_questionData load", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
	}
}

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
import org.aestia.game.GameServer;
import org.aestia.game.world.World;
import org.aestia.kernel.Main;
import org.aestia.map.Case;
import org.aestia.map.Map;

public class Scripted_cellData extends AbstractDAO<Object> {
	public Scripted_cellData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Object obj) {
		return false;
	}

	public int load() {
		int nbr;
		ResultSet RS = null;
		nbr = 0;
		try {
			try {
				RS = this.getData("SELECT * FROM `scripted_cells`");

				while (RS.next()) {
					Case cell;
					short mapId = RS.getShort("MapID");
					int cellId = RS.getInt("CellID");
					Map map = World.getMap(mapId);
					if (map == null || (cell = map.getCase(cellId)) == null)
						continue;
					switch (RS.getInt("EventID")) {
					case 1: {
						cell.addOnCellStopAction(RS.getInt("ActionID"), RS.getString("ActionsArgs"),
								RS.getString("Conditions"), null);
						break;
					}
					default: {
						GameServer.addToLog(
								"Action Event " + RS.getInt("EventID") + " non implante " + mapId + "," + cellId);
					}
					}
					++nbr;
				}
			} catch (SQLException e) {
				super.sendError("Scripted_cellData load", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
		return nbr;
	}

	public boolean update(int mapID1, int cellID1, int action, int event, String args, String cond) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("REPLACE INTO `scripted_cells` VALUES (?,?,?,?,?,?)");
			p.setInt(1, mapID1);
			p.setInt(2, cellID1);
			p.setInt(3, action);
			p.setInt(4, event);
			p.setString(5, args);
			p.setString(6, cond);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Scripted_cellData update", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public boolean delete(int mapID, int cellID) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("DELETE FROM `scripted_cells` WHERE `MapID` = ? AND `CellID` = ?");
			p.setInt(1, mapID);
			p.setInt(2, cellID);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Scripted_cellData delete", e);
		} finally {
			this.close(p);
		}
		return false;
	}
}

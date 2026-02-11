/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 */
package org.aestia.db.game.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.db.game.AbstractDAO;
import org.aestia.game.world.World;

public class Subarea_dataData extends AbstractDAO<World.SubArea> {
	public Subarea_dataData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(World.SubArea subarea) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement(
					"UPDATE `subarea_data` SET `alignement` = ?, `prisme` = ?, `conquistable` = ? WHERE id = ?");
			p.setInt(1, subarea.getAlignement());
			p.setInt(2, subarea.getPrismId());
			p.setInt(3, subarea.getConquistable() ? 0 : 1);
			p.setInt(4, subarea.getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from subarea_data");

				while (RS.next()) {
					int id = RS.getInt("id");
					int alignement = RS.getInt("alignement");
					int conquistable = RS.getInt("conquistable");
					int prisme = RS.getInt("Prisme");
					World.SubArea SA = World.getSubArea(id);
					SA.setAlignement(alignement);
					SA.setPrismId(prisme);
					SA.setConquistable(conquistable);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}
}

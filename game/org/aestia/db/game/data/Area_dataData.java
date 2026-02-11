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

public class Area_dataData extends AbstractDAO<World.Area> {

	public Area_dataData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(World.Area area) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE `area_data` SET `alignement` = ?, `Prisme` = ? WHERE id = ?");
			p.setInt(1, area.getalignement());
			p.setInt(2, area.getPrismeID());
			p.setInt(3, area.get_id());
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
				RS = this.getData("SELECT * from area_data");
				while (RS.next()) {
					int id = RS.getInt("id");
					int alignement = RS.getInt("alignement");
					int prisme = RS.getInt("Prisme");
					World.Area A = World.getArea(id);
					A.setalignement(alignement);
					A.setPrismeID(prisme);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}
}

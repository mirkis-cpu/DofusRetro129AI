/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 */
package org.aestia.db.statique.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.db.statique.AbstractDAO;
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
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from area_data");

				while (RS.next()) {
					World.Area A = new World.Area(RS.getInt("id"), RS.getInt("superarea"), RS.getString("name"));
					World.addArea(A);
					A.get_superArea().addArea(A);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}
}

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

public class Subarea_dataData extends AbstractDAO<World.SubArea> {
	public Subarea_dataData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(World.SubArea subarea) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from subarea_data");

				while (RS.next()) {
					World.SubArea SA = new World.SubArea(RS.getInt("id"), RS.getInt("area"), RS.getString("name"));
					World.addSubArea(SA);
					if (SA.getArea() == null)
						continue;
					SA.getArea().addSubArea(SA);
				}
			} catch (SQLException e) {
				super.sendError("Subarea_dataData load", e);
			}
		} finally {
			this.close(RS);
		}
	}
}

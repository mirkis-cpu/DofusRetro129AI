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
import org.aestia.map.Map;
import org.aestia.other.House;

public class HouseData extends AbstractDAO<House> {
	public HouseData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(House h) {
		return false;
	}

	public int load() {
		int nbr;
		ResultSet RS = null;
		nbr = 0;
		try {
			try {
				RS = this.getData("SELECT * from houses");

				while (RS.next()) {
					Map map = World.getMap(RS.getShort("map_id"));
					if (map == null)
						continue;
					World.addHouse(new House(RS.getInt("id"), RS.getShort("map_id"), RS.getInt("cell_id"),
							RS.getInt("mapid"), RS.getInt("caseid")));
					++nbr;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				nbr = 0;
			}
		} finally {
			this.close(RS);
		}
		return nbr;
	}
}

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
import java.util.ArrayList;

import org.aestia.db.statique.AbstractDAO;
import org.aestia.game.world.World;

public class CraftData extends AbstractDAO<Object> {
	public CraftData(Connection dataSource) {
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
				RS = this.getData("SELECT * from crafts");

				while (RS.next()) {
					ArrayList<World.Couple<Integer, Integer>> m = new ArrayList<World.Couple<Integer, Integer>>();
					boolean cont = true;
					String[] arrstring = RS.getString("craft").split(";");
					int n = arrstring.length;
					int n2 = 0;
					while (n2 < n) {
						String str = arrstring[n2];
						try {
							int tID = Integer.parseInt(str.split("\\*")[0]);
							int qua = Integer.parseInt(str.split("\\*")[1]);
							m.add(new World.Couple<Integer, Integer>(tID, qua));
						} catch (Exception e) {
							e.printStackTrace();
							cont = false;
						}
						++n2;
					}
					if (!cont)
						continue;
					World.addCraft(RS.getInt("id"), m);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}
}

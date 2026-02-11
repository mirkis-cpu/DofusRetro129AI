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
import org.aestia.object.ObjectSet;

public class ItemsetData extends AbstractDAO<ObjectSet> {
	public ItemsetData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(ObjectSet obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from itemsets");

				while (RS.next()) {
					World.addItemSet(new ObjectSet(RS.getInt("id"), RS.getString("items"), RS.getString("bonus")));
				}
				this.close(RS);
			} catch (SQLException e) {
				super.sendError("ItemsetData load", e);
				this.close(RS);
			}
		} finally {
			this.close(RS);
		}
	}
}

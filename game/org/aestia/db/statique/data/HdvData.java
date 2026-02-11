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
import org.aestia.hdv.Hdv;

public class HdvData extends AbstractDAO<Hdv> {
	public HdvData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Hdv obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM `hdvs` ORDER BY id ASC");

				while (RS.next()) {
					World.addHdv(new Hdv(RS.getInt("map"), RS.getFloat("sellTaxe"), RS.getShort("sellTime"),
							RS.getShort("accountItem"), RS.getShort("lvlMax"), RS.getString("categories")));
				}
				this.close(RS);
				RS = this.getData("SELECT id MAX FROM `hdvs`");
				RS.first();
				World.setNextHdvID(RS.getInt("MAX"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}
}

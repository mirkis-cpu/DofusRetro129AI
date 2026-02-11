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

import org.aestia.command.server.Groupes;
import org.aestia.db.statique.AbstractDAO;
import org.aestia.kernel.Main;

public class GroupeData extends AbstractDAO<Groupes> {
	public GroupeData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Groupes obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM groupes");

				while (RS.next()) {
					new org.aestia.command.server.Groupes(RS.getInt("id"), RS.getString("nom"),
							RS.getBoolean("isPlayer"), RS.getString("commandes"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
	}
}

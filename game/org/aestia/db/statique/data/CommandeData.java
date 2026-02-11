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

import org.aestia.command.server.Commandes;
import org.aestia.db.statique.AbstractDAO;
import org.aestia.kernel.Main;

public class CommandeData extends AbstractDAO<Commandes> {
	public CommandeData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Commandes obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM commandes");

				while (RS.next()) {
					new org.aestia.command.server.Commandes(RS.getInt("id"), RS.getString("commande"),
							RS.getString("args"), RS.getString("description"));
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

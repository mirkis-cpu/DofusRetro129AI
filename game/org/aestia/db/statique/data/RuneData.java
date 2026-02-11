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
import org.aestia.job.maging.Rune;

public class RuneData extends AbstractDAO<Rune> {
	public RuneData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Rune obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM runes");

				while (RS.next()) {
					new org.aestia.job.maging.Rune(RS.getInt("characteristics"), RS.getFloat("weight"),
							RS.getString("bonus"));
				}
			} catch (SQLException e) {
				super.sendError("RuneData load", e);
			}
		} finally {
			this.close(RS);
		}
	}
}

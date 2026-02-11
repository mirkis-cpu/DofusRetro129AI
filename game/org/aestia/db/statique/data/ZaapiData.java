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
import org.aestia.kernel.Constant;

public class ZaapiData extends AbstractDAO<Object> {

	public ZaapiData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Object obj) {
		return false;
	}

	public int load() {
		int i = 0;
		String Bonta = "";
		String Brak = "";
		String Neutre = "";
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT mapid, align from zaapi");

				while (RS.next()) {
					if (RS.getInt("align") == 1) {
						Bonta = String.valueOf(Bonta) + RS.getString("mapid");
						if (!RS.isLast()) {
							Bonta = String.valueOf(Bonta) + ",";
						}
					} else if (RS.getInt("align") == 2) {
						Brak = String.valueOf(Brak) + RS.getString("mapid");
						if (!RS.isLast()) {
							Brak = String.valueOf(Brak) + ",";
						}
					} else {
						Neutre = String.valueOf(Neutre) + RS.getString("mapid");
						if (!RS.isLast()) {
							Neutre = String.valueOf(Neutre) + ",";
						}
					}
					++i;
				}
				Constant.ZAAPI.put(1, Bonta);
				Constant.ZAAPI.put(2, Brak);
				Constant.ZAAPI.put(-1, Neutre);
			} catch (SQLException e) {
				super.sendError("ZaapiData load", e);
			}
		} finally {
			this.close(RS);
		}
		return i;
	}
}

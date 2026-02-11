/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 */
package org.aestia.db.statique.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.db.statique.AbstractDAO;

public class GiftData extends AbstractDAO<Object> {
	public GiftData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Object obj) {
		return false;
	}

	public boolean create(int guid) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("INSERT INTO gifts(`id`, `objects`) VALUES ('" + guid + "', '');");
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public boolean existByAccount(int guid) {
		boolean exist;
		exist = false;
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM gifts WHERE id = '" + guid + "'");

				if (RS.next()) {
					exist = RS.getInt("id") > 0;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			super.close(RS);
		}
		return exist;
	}

	public String getByAccount(int guid) {
		String gift;
		ResultSet RS = null;
		gift = null;
		try {
			try {
				RS = this.getData("SELECT * FROM gifts WHERE id = '" + guid + "';");

				if (RS.next()) {
					gift = RS.getString("objects");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			super.close(RS);
		}
		return gift;
	}

	public void update(int acc, String objects) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `gifts` SET `objects` = ? WHERE `id` = ?");
				p.setString(1, objects);
				p.setInt(2, acc);
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}
}

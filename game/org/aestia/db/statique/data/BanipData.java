/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 */
package org.aestia.db.statique.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.aestia.db.statique.AbstractDAO;

public class BanipData extends AbstractDAO<Object> {
	public BanipData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Object obj) {
		return false;
	}

	public boolean add(String ip) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("INSERT INTO `banip` VALUES (?)");
			p.setString(1, ip);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public boolean delete(String ip) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("DELETE FROM `banip` WHERE `ip` = ?");
			p.setString(1, ip);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}
}

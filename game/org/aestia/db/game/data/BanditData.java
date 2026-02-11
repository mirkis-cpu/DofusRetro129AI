/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 */
package org.aestia.db.game.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.db.game.AbstractDAO;
import org.aestia.entity.monster.boss.Bandit;

public class BanditData extends AbstractDAO<Bandit> {
	public BanditData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Bandit obj) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE `bandits` SET `time` = ?");
			p.setLong(1, obj.getTime());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM bandits");
				if (RS.next()) {
					new org.aestia.entity.monster.boss.Bandit(RS.getString("mobs"), RS.getString("maps"),
							RS.getLong("time"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}
}

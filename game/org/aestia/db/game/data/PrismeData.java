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
import org.aestia.entity.Prism;
import org.aestia.game.world.World;

public class PrismeData extends AbstractDAO<Prism> {
	public PrismeData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Prism Prisme) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE prismes SET `level` = ?, `honor` = ?, `area`= ? WHERE `id` = ?");
			p.setInt(1, Prisme.getLevel());
			p.setInt(2, Prisme.getHonor());
			p.setInt(3, Prisme.getConquestArea());
			p.setInt(4, Prisme.getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public int load() {
		int numero = 0;
		ResultSet RS = null;
		try {
			RS = this.getData("SELECT * from prismes");

			while (RS.next()) {
				World.addPrisme(new Prism(RS.getInt("id"), RS.getInt("alignement"), RS.getInt("level"),
						RS.getShort("carte"), RS.getInt("celda"), RS.getInt("honor"), RS.getInt("area")));
				++numero;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(RS);
		}
		return numero;
	}

	public void add(Prism Prisme) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("REPLACE INTO `prismes` VALUES(?,?,?,?,?,?,?)");
				p.setInt(1, Prisme.getId());
				p.setInt(2, Prisme.getAlignement());
				p.setInt(3, Prisme.getLevel());
				p.setInt(4, Prisme.getMap());
				p.setInt(5, Prisme.getCell());
				p.setInt(6, Prisme.getConquestArea());
				p.setInt(7, Prisme.getHonor());
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public void delete(int id) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("DELETE FROM prismes WHERE id = ?");
				p.setInt(1, id);
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}
}

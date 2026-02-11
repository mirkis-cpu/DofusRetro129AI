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
import org.aestia.game.world.World;
import org.aestia.kernel.Main;
import org.aestia.map.SchemaFight;

public class SchemafightData extends AbstractDAO<SchemaFight> {
	public SchemafightData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(SchemaFight obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT  * from schemafights");

				while (RS.next()) {
					if (World.getSchemaFight(RS.getInt("id")) != null) {
						World.getSchemaFight(RS.getInt("id")).setPlacesStr(RS.getString("places"));
						continue;
					}
					World.addSchemaFight(new SchemaFight((short) RS.getInt("id"), RS.getString("places")));
				}
			} catch (SQLException e) {
				super.sendError("SchemafightData load", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
	}

	public boolean add(SchemaFight toAdd) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("INSERT INTO `schemafights` (`places`) VALUES(?)");
			p.setString(1, toAdd.getPlacesStr());
			this.execute(p);
			ResultSet RS = this.getData("SELECT `id` FROM `schemafights` WHERE `places` = '" + toAdd.getPlacesStr() + "'");

			while (RS.next()) {
				World.editSchema(toAdd, (short) RS.getInt("id"));
			}
			return true;
		} catch (SQLException e) {
			super.sendError("SchemafightData add", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public boolean delete(SchemaFight toAdd) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("DELETE FROM `schemafights` WHERE id = ?");
			p.setInt(1, toAdd.getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("SchemafightData delete", e);
		} finally {
			this.close(p);
		}
		return false;
	}
}

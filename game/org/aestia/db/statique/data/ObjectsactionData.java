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
import org.aestia.kernel.Main;
import org.aestia.object.ObjectAction;

public class ObjectsactionData extends AbstractDAO<ObjectAction> {
	public ObjectsactionData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(ObjectAction obj) {
		return false;
	}

	public int load() {
		int nbr;
		ResultSet RS = null;
		nbr = 0;
		try {
			try {
				RS = this.getData("SELECT * FROM objectsactions");

				while (RS.next()) {
					int id = RS.getInt("template");
					String type = RS.getString("type");
					String args = RS.getString("args");
					if (World.getObjTemplate(id) == null)
						continue;
					World.getObjTemplate(id).addAction(new ObjectAction(type, args, ""));
					++nbr;
				}
			} catch (SQLException e) {
				super.sendError("ObjectsactionData load", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
		return nbr;
	}

	public int reload() {
		int nbr;
		ResultSet RS = null;
		nbr = 0;
		try {
			try {
				RS = this.getData("SELECT * FROM objectsactions");

				while (RS.next()) {
					int id = RS.getInt("template");
					String type = RS.getString("type");
					String args = RS.getString("args");
					if (World.getObjTemplate(id) == null)
						continue;
					World.getObjTemplate(id).getOnUseActions().clear();
					World.getObjTemplate(id).addAction(new ObjectAction(type, args, ""));
					++nbr;
				}
			} catch (SQLException e) {
				super.sendError("ObjectsactionData reload", e);
			}
		} finally {
			this.close(RS);
		}
		return nbr;
	}
}

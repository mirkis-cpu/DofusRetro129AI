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
import org.aestia.entity.monster.Monster;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;

public class DropData extends AbstractDAO<World.Drop> {
	public DropData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(World.Drop obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from drops");

				while (RS.next()) {
					Monster MT = World.getMonstre(RS.getInt("mob"));
					if (World.getObjTemplate(RS.getInt("item")) != null && MT != null) {
						String action = RS.getString("action");
						String condition = "";
						if (!action.equals("-1") && !action.equals("1") && action.contains(":")) {
							condition = action.split(":")[1];
							action = action.split(":")[0];
						}
						MT.addDrop(new World.Drop(RS.getInt("item"), RS.getInt("seuil"), RS.getFloat("taux"),
								RS.getInt("max"), RS.getInt("max_combat"), RS.getInt("level"), Integer.parseInt(action),
								condition));
						continue;
					}
					Console.println(
							"## Erreur drop id item " + RS.getInt("item") + " et du monstre " + RS.getInt("mob"),
							Console.Color.ERROR);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}

	public void reload() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from drops");

				for (Monster m : World.getMonstres()) {
					if (m == null || m.getDrops() == null)
						continue;
					m.getDrops().clear();
				}
				while (RS.next()) {
					Monster MT = World.getMonstre(RS.getInt("mob"));
					if (World.getObjTemplate(RS.getInt("item")) == null)
						continue;
					String action = RS.getString("action");
					String condition = "";
					if (!action.equals("1") && !action.equals("-1") && action.contains(":")) {
						condition = action.split(":")[1];
						action = action.split(":")[0];
					}
					MT.addDrop(
							new World.Drop(RS.getInt("item"), RS.getInt("seuil"), RS.getFloat("taux"), RS.getInt("max"),
									RS.getInt("max_combat"), RS.getInt("level"), Integer.parseInt(action), condition));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}
}

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
import org.aestia.other.Tutorial;

public class TutorielData extends AbstractDAO<Tutorial> {
	public TutorielData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Tutorial obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM tutoriel");

				while (RS.next()) {
					int id = RS.getInt("id");
					String start = RS.getString("start");
					String reward = String.valueOf(RS.getString("reward1")) + "$" + RS.getString("reward2") + "$"
							+ RS.getString("reward3") + "$" + RS.getString("reward4");
					String end = RS.getString("end");
					World.addTutorial(new Tutorial(id, reward, start, end));
				}
			} catch (SQLException e) {
				super.sendError("TutorielData load", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
	}
}

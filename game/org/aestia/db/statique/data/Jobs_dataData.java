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
import org.aestia.job.Job;

public class Jobs_dataData extends AbstractDAO<Job> {
	public Jobs_dataData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Job obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from jobs_data");

				while (RS.next()) {
					String skills = "";
					if (RS.getString("skills") != null) {
						skills = RS.getString("skills");
					}
					World.addJob(new Job(RS.getInt("id"), RS.getString("tools"), RS.getString("crafts"), skills,
							RS.getString("AP")));
				}
			} catch (SQLException e) {
				super.sendError("Jobs_dataData load", e);
				this.close(RS);
			}
		} finally {
			this.close(RS);
		}
	}
}

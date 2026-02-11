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
import org.aestia.other.Animation;

public class AnimationData extends AbstractDAO<Animation> {
	public AnimationData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from animations");

				while (RS.next()) {
					World.addAnimation(new Animation(RS.getInt("guid"), RS.getInt("id"), RS.getString("nom"),
							RS.getInt("area"), RS.getInt("action"), RS.getInt("size")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}

	@Override
	public boolean update(Animation obj) {
		return false;
	}
}

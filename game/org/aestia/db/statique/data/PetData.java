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
import org.aestia.entity.Pet;
import org.aestia.game.world.World;

public class PetData extends AbstractDAO<Pet> {
	public PetData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Pet obj) {
		return false;
	}

	public int load() {
		int i;
		ResultSet RS = null;
		i = 0;
		try {
			try {
				RS = this.getData("SELECT * from pets");

				while (RS.next()) {
					++i;
					World.addPets(new Pet(RS.getInt("TemplateID"), RS.getInt("Type"), RS.getString("Gap"),
							RS.getString("StatsUp"), RS.getString("StatsMax"), RS.getInt("Max"), RS.getInt("Gain"),
							RS.getInt("DeadTemplate"), RS.getInt("Epo")));
				}
			} catch (SQLException e) {
				super.sendError("PetData load", e);
			}
		} finally {
			this.close(RS);
		}
		return i;
	}
}

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
import org.aestia.entity.PetEntry;
import org.aestia.game.world.World;

public class Pets_dataData extends AbstractDAO<PetEntry> {
	public Pets_dataData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(PetEntry pets) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement(
					"UPDATE `pets_data` SET `LastEatDate`=?, `quaEat`=?, `pdv`=?, `Corpulence`=?, `isEPO`=? WHERE `id`=?");
			p.setLong(1, pets.getLastEatDate());
			p.setInt(2, pets.getQuaEat());
			p.setInt(3, pets.getPdv());
			p.setInt(4, pets.getCorpulence());
			p.setInt(5, pets.getIsEupeoh() ? 1 : 0);
			p.setInt(6, pets.getObjectId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Pets_dataData update", e);
		} finally {
			this.close(p);
		}
		return false;
	}
	

	public int load() {
		int i;
		ResultSet RS = null;
		i = 0;
		try {
			try {
				RS = this.getData("SELECT * from pets_data");

				while (RS.next()) {
					++i;
					World.addPetsEntry(new PetEntry(RS.getInt("id"), RS.getInt("template"), RS.getLong("LastEatDate"),
							RS.getInt("quaEat"), RS.getInt("pdv"), RS.getInt("Corpulence"), RS.getInt("isEPO") == 1));
				}
			} catch (SQLException e) {
				super.sendError("Pets_dataData load", e);
			}
		} finally {
			this.close(RS);
		}
		return i;
	}

	public void add(int id, long LastEatDate, int template) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement(
						"INSERT INTO pets_data(`id`, `template`, `LastEatDate`, `quaEat`, `pdv`, `Corpulence`, `isEPO`) VALUES (?,?,?,?,?,?,?)");
				p.setInt(1, id);
				p.setInt(2, template);
				p.setLong(3, LastEatDate);
				p.setInt(4, 0);
				p.setInt(5, 10);
				p.setInt(6, 0);
				p.setInt(7, 0);
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("Pets_dataData add", e);
			}
		} finally {
			this.close(p);
		}
	}

	public void delete(int id) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("DELETE FROM pets_data WHERE id = ?");
				p.setInt(1, id);
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("Pets_dataData delete", e);
			}
		} finally {
			this.close(p);
		}
	}
}

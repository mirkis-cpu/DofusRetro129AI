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

import org.aestia.client.Player;
import org.aestia.db.game.AbstractDAO;
import org.aestia.entity.Collector;
import org.aestia.game.world.World;
import org.aestia.map.Map;

public class PercepteurData extends AbstractDAO<Collector> {
	public PercepteurData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Collector P) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE `percepteurs` SET `objets` = ?,`kamas` = ?,`xp` = ? WHERE guid = ?");
			p.setString(1, P.parseItemCollector());
			p.setLong(2, P.getKamas());
			p.setLong(3, P.getXp());
			p.setInt(4, P.getId());
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
		int nbr = 0;
		ResultSet RS = null;
		try {
			RS = this.getData("SELECT * from percepteurs");

			while (RS.next()) {
				Map map = World.getMap(RS.getShort("mapid"));
				if (map == null)
					continue;
				Player perso = null;
				Integer poseur_id = RS.getInt("poseur_id");
				if (poseur_id != null && poseur_id > 0) {
					perso = World.getPersonnage(poseur_id);
				}
				String date = RS.getString("date");
				long time = 0;
				if (date != null && !date.equals("")) {
					time = Long.parseLong(date);
				}
				World.addCollector(new Collector(RS.getInt("guid"), RS.getShort("mapid"), RS.getInt("cellid"),
						RS.getByte("orientation"), RS.getInt("guild_id"), RS.getShort("N1"), RS.getShort("N2"), perso,
						time, RS.getString("objets"), RS.getLong("kamas"), RS.getLong("xp")));
				++nbr;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(RS);
		}
		return nbr;
	}

	public boolean delete(int id) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("DELETE FROM percepteurs WHERE guid = ?");
			p.setInt(1, id);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public boolean add(int guid, int mapid, int guildID, int poseur_id, long date, int cellid, int o, short N1,
			short N2) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("INSERT INTO `percepteurs` VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
			p.setInt(1, guid);
			p.setInt(2, mapid);
			p.setInt(3, cellid);
			p.setInt(4, o);
			p.setInt(5, guildID);
			p.setInt(6, poseur_id);
			p.setString(7, Long.toString(date));
			p.setShort(8, N1);
			p.setShort(9, N2);
			p.setString(10, "");
			p.setLong(11, 0);
			p.setLong(12, 0);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public int getId() {
		ResultSet RS = null;
		int i = -50;
		try {
			RS = this.getData("SELECT `guid` FROM `percepteurs` ORDER BY `guid` ASC LIMIT 0 , 1");

			while (RS.next())
				i = RS.getInt("guid") - 1;
			if (i >= -9999) {
				i = -10000;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(RS);
		}
		return i;
	}
}

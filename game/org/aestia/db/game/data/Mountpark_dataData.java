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
import org.aestia.game.world.World;
import org.aestia.map.Map;
import org.aestia.map.MountPark;

public class Mountpark_dataData extends AbstractDAO<MountPark> {
	public Mountpark_dataData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(MountPark MP) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement(
					"UPDATE `mountpark_data` SET  `owner`=?, `guild`=?, `price` =?, `data` =?, `enclos` =?, `ObjetPlacer`=?, `durabilite`=? WHERE `mapid`=?");
			p.setInt(1, MP.getOwner());
			p.setInt(2, MP.getGuild() != null ? MP.getGuild().getId() : -1);
			p.setInt(3, MP.getPrice());
			p.setString(4, MP.parseStringEtableId());
			p.setString(5, MP.getData());
			p.setString(6, MP.getStringObject());
			p.setString(7, MP.getStringObjDurab());
			p.setInt(8, MP.getMap().getId());
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
		int nbr;
		ResultSet RS = null;
		nbr = 0;
		try {
			try {
				RS = this.getData("SELECT * from mountpark_data");

				while (RS.next()) {
					MountPark MP;
					Map map = World.getMap(RS.getShort("mapid"));
					if (map == null || (MP = World.getMountPark().get(map.getId())) == null)
						continue;
					int owner = RS.getInt("owner");
					int guild = RS.getInt("guild");
					int price = RS.getInt("price");
					String data = RS.getString("data");
					String enclos = RS.getString("enclos");
					String objetPlacer = RS.getString("ObjetPlacer");
					String durabilite = RS.getString("durabilite");
					MP.setOwner(owner);
					MP.setGuild(World.getGuild(guild));
					MP.setPrice(price);
					MP.setDataParse(data);
					MP.setEnclosParse(enclos);
					MP.setObjetPlacerParse(objetPlacer);
					MP.setDurabiliteParse(durabilite);
					++nbr;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
		return nbr;
	}

	public void reload(int i) {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from mountpark_data");

				while (RS.next()) {
					MountPark MP;
					Map map = World.getMap(RS.getShort("mapid"));
					if (map == null || RS.getShort("mapid") != i
							|| (MP = World.getMountPark().get(map.getId())) == null)
						continue;
					int owner = RS.getInt("owner");
					int guild = RS.getInt("guild");
					int price = RS.getInt("price");
					String data = RS.getString("data");
					String enclos = RS.getString("enclos");
					String objetPlacer = RS.getString("ObjetPlacer");
					String durabilite = RS.getString("durabilite");
					MP.setOwner(owner);
					MP.setGuild(World.getGuild(guild));
					MP.setPrice(price);
					MP.setDataParse(data);
					MP.setEnclosParse(enclos);
					MP.setObjetPlacerParse(objetPlacer);
					MP.setDurabiliteParse(durabilite);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}
}

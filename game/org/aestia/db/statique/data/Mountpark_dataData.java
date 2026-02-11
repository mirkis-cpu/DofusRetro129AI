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
					"UPDATE `mountpark_data` SET `cellMount` =?, `cellPorte`=?, `cellEnclos`=? WHERE `mapid`=?");
			p.setInt(1, MP.getMountcell());
			p.setInt(2, MP.getDoor());
			p.setString(3, MP.getCellObjectParse());
			p.setInt(4, MP.getMap().getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("Mountpark_dataData update", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public int load() {
		int nbr;
		nbr = 0;
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from mountpark_data");

				while (RS.next()) {
					Map map = World.getMap(RS.getShort("mapid"));
					if (map == null)
						continue;
					MountPark MP = new MountPark(map, RS.getInt("cellid"), RS.getInt("size"), RS.getInt("cellMount"),
							RS.getInt("cellporte"), RS.getString("cellEnclos"), RS.getInt("sizeObj"));
					World.addMountPark(MP);
					++nbr;
				}
			} catch (SQLException e) {
				super.sendError("Mountpark_dataData load", e);
				this.close(RS);
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
					Map map = World.getMap(RS.getShort("mapid"));
					if (map == null || RS.getShort("mapid") != i)
						continue;
					if (!World.getMountPark().containsKey(RS.getShort("mapid"))) {
						MountPark MP = new MountPark(map, RS.getInt("cellid"), RS.getInt("size"),
								RS.getInt("cellMount"), RS.getInt("cellporte"), RS.getString("cellEnclos"),
								RS.getInt("sizeObj"));
						World.addMountPark(MP);
						continue;
					}
					World.getMountPark().get(RS.getShort("mapid")).setInfos(map, RS.getInt("cellid"), RS.getInt("size"),
							RS.getInt("cellMount"), RS.getInt("cellporte"), RS.getString("cellEnclos"),
							RS.getInt("sizeObj"));
				}
			} catch (SQLException e) {
				super.sendError("Mountpark_dataData reload", e);
				this.close(RS);
			}
		} finally {
			this.close(RS);
		}
	}
}

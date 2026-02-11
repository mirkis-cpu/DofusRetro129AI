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
import org.aestia.map.SchemaFight;

public class MapData extends AbstractDAO<Map> {
	public MapData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Map obj) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE `maps` SET `places` = ?, `numgroup` = ? WHERE id = ?");
			p.setString(1, obj.getPlaces());
			p.setInt(2, obj.getMaxGroupNumb());
			p.setInt(3, obj.getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("MapData update", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public boolean updateGs(Map map) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement(
					"UPDATE `maps` SET `numgroup` = ?, `minSize` = ?, `fixSize` = ?, `maxSize` = ? WHERE id = ?");
			p.setInt(1, map.getMaxGroupNumb());
			p.setInt(2, map.getMinSize());
			p.setInt(3, map.getFixSize());
			p.setInt(4, map.getMaxSize());
			p.setInt(5, map.getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("MapData updateGs", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public boolean updateMonster(Map map, String monsters) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE `maps` SET `monsters` = ? WHERE id = ?");
			p.setString(1, monsters);
			p.setInt(2, map.getId());
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("MapData updateMonster", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public boolean updatePlaces(SchemaFight toMod, int id) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE `maps` SET `places` = ? WHERE `ID` = ?");
			p.setString(1, toMod.getPlacesStr());
			p.setInt(2, id);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("MapData updatePlaces", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT  * from maps LIMIT 30000");

				while (RS.next()) {
					World.addMap(new Map(RS.getShort("id"), RS.getString("date"), RS.getByte("width"),
							RS.getByte("heigth"), RS.getString("key"), RS.getString("places"), RS.getString("mapData"),
							RS.getString("cells"), RS.getString("monsters"), RS.getString("mappos"),
							RS.getByte("numgroup"), RS.getByte("fixSize"), RS.getByte("minSize"), RS.getByte("maxSize"),
							RS.getString("cases"), RS.getString("forbidden")));
				}
				this.close(RS);
				RS = this.getData("SELECT  * from mobgroups_fix");
				while (RS.next()) {
					Map c = World.getMap(RS.getShort("mapid"));
					if (c == null || c.getCase(RS.getInt("cellid")) == null)
						continue;
					c.addStaticGroup(RS.getInt("cellid"), RS.getString("groupData"), false);
					World.addGroupFix(String.valueOf(RS.getInt("mapid")) + ";" + RS.getInt("cellid"),
							RS.getString("groupData"), RS.getInt("Timer"));
				}
			} catch (SQLException e) {
				super.sendError("MapData load", e);
				this.close(RS);
			}
		} finally {
			this.close(RS);
		}
	}

	public void reload() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT  * from maps LIMIT 30000");

				while (RS.next()) {
					Map map = World.getMap(RS.getShort("id"));
					if (map == null) {
						World.addMap(new Map(RS.getShort("id"), RS.getString("date"), RS.getByte("width"),
								RS.getByte("heigth"), RS.getString("key"), RS.getString("places"),
								RS.getString("mapData"), RS.getString("cells"), RS.getString("monsters"),
								RS.getString("mappos"), RS.getByte("numgroup"), RS.getByte("fixSize"),
								RS.getByte("minSize"), RS.getByte("maxSize"), RS.getString("cases"),
								RS.getString("forbidden")));
						continue;
					}
					map.setInfos(RS.getString("date"), RS.getString("monsters"), RS.getString("mappos"),
							RS.getByte("numgroup"), RS.getByte("fixSize"), RS.getByte("minSize"), RS.getByte("maxSize"),
							RS.getString("cases"), RS.getString("forbidden"));
				}
			} catch (SQLException e) {
				super.sendError("MapData reload", e);
				this.close(RS);
			}
		} finally {
			this.close(RS);
		}
	}
}

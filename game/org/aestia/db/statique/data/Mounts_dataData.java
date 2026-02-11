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
import java.util.Map;

import org.aestia.client.Player;
import org.aestia.db.Database;
import org.aestia.db.statique.AbstractDAO;
import org.aestia.entity.Dragodinde;
import org.aestia.game.world.World;
import org.aestia.object.Object;

public class Mounts_dataData extends AbstractDAO<Dragodinde> {
	public Mounts_dataData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(java.lang.Object obj) {
	}

	@Override
	public boolean update(Dragodinde DD) {
		return this.update(DD, false);
	}

	public boolean update(Dragodinde DD, boolean saveItem) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement(
					"UPDATE mounts_data SET `name` = ?, `xp` = ?,`level` = ?,`endurance` = ?, `amour` = ?,`maturite` = ?,`serenite` = ?, `reproductions` = ?,`fatigue` = ?,`energie` = ?, `ancetres` = ?,`items` = ?,`maitre` = ?, `ability` = ? WHERE `id` = ?");
			p.setString(1, DD.getName());
			p.setLong(2, DD.getExp());
			p.setInt(3, DD.getLvl());
			p.setInt(4, DD.get_endurance());
			p.setInt(5, DD.get_amour());
			p.setInt(6, DD.getMaturite());
			p.setInt(7, DD.getSerenite());
			p.setInt(8, DD.getReprod());
			p.setInt(9, DD.getFatigue());
			p.setInt(10, DD.getEnergie());
			p.setString(11, DD.get_ancetres());
			p.setString(12, DD.getItemsId());
			p.setInt(13, DD.getPerso());
			p.setString(14, DD.getAbility());
			p.setInt(15, DD.getId());
			this.execute(p);
			if (saveItem) {
				Map<Integer, Object> map = DD.getItems();
				synchronized (map) {
					for (Object obj : DD.getItems().values()) {
						try {
							if (obj == null)
								continue;
							p = this.getPreparedStatement("REPLACE INTO `items` VALUES (?,?,?,?,?,?)");
							p.setInt(1, obj.getGuid());
							p.setInt(2, obj.getTemplate().getId());
							p.setInt(3, obj.getQuantity());
							p.setInt(4, obj.getPosition());
							p.setString(5, obj.parseToSave());
							p.setInt(6, obj.getPuit());
							this.execute(p);
							continue;
						} catch (Exception e) {
							super.sendError("Mounts_dataData update", e);
						}
					}
				}
			}
			return true;
		} catch (SQLException e) {
			super.sendError("Mounts_dataData update", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from mounts_data");

				while (RS.next()) {
					int maitre = RS.getInt("maitre");
					Player p = World.getPersonnage(maitre);
					if (p == null)
						continue;
					World.addDragodinde(
							new Dragodinde(RS.getInt("id"), RS.getInt("color"), RS.getInt("sexe"), RS.getInt("amour"),
									RS.getInt("endurance"), RS.getInt("level"), RS.getLong("xp"), RS.getString("name"),
									RS.getInt("fatigue"), RS.getInt("energie"), RS.getInt("reproductions"),
									RS.getInt("maturite"), RS.getInt("serenite"), RS.getString("items"),
									RS.getString("ancetres"), RS.getString("ability"), RS.getInt("taille"),
									RS.getInt("CellID"), RS.getShort("mapID"), maitre, RS.getInt("orientation"),
									RS.getInt("fecondable"), RS.getInt("paire"), RS.getInt("sauvage")));
				}
			} catch (SQLException e) {
				super.sendError("Mounts_dataData load", e);
				this.close(RS);
			}
		} finally {
			this.close(RS);
		}
	}

	public void delete(int DID) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("DELETE FROM `mounts_data` WHERE `id` = ?");
				p.setInt(1, DID);
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("Mounts_dataData delete", e);
				this.close(p);
			}
		} finally {
			this.close(p);
		}
	}

	public void delete(Player perso) {
		block5: {
			PreparedStatement p;
			p = null;
			try {
				try {
					p = this.getPreparedStatement("DELETE FROM mounts_data WHERE id = ?");
					p.setInt(1, perso.getMount().getId());
					this.execute(p);
				} catch (SQLException e) {
					super.sendError("Mounts_dataData delete", e);
					this.close(p);
					break block5;
				}
			} catch (Throwable var4_4) {
				this.close(p);
				throw var4_4;
			}
			this.close(p);
		}
		World.delDragoByID(perso.getMount().getId());
		perso.setMountGiveXp(0);
		perso.setMount(null);
		Database.getStatique().getPlayerData().update(perso, true);
	}

	public void add(Dragodinde DP) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement(
						"REPLACE INTO `mounts_data`(`id`,`color`,`sexe`,`name`,`xp`,`level`, `endurance`,`amour`,`maturite`,`serenite`,`reproductions`,`fatigue`,`items`, `ancetres`,`energie`,`taille`,`CellID`,`mapID`,`maitre`,`orientation`,`fecondable`,`paire`,`sauvage`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				p.setInt(1, DP.getId());
				p.setInt(2, DP.getColor());
				p.setInt(3, DP.getSexe());
				p.setString(4, DP.getName());
				p.setLong(5, DP.getExp());
				p.setInt(6, DP.getLvl());
				p.setInt(7, DP.get_endurance());
				p.setInt(8, DP.get_amour());
				p.setInt(9, DP.getMaturite());
				p.setInt(10, DP.getSerenite());
				p.setInt(11, DP.getReprod());
				p.setInt(12, DP.getFatigue());
				p.setString(13, DP.getItemsId());
				p.setString(14, DP.get_ancetres());
				p.setInt(15, DP.getEnergie());
				p.setInt(16, DP.getSize());
				p.setInt(17, DP.getCell());
				p.setInt(18, DP.getMap());
				p.setInt(19, DP.getPerso());
				p.setInt(20, DP.getOrientacion());
				p.setInt(21, DP.getFecundatedAgo());
				p.setInt(22, DP.getCouple());
				p.setInt(23, DP.IsSauvage());
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("Mounts_dataData add", e);
				this.close(p);
			}
		} finally {
			this.close(p);
		}
	}
}

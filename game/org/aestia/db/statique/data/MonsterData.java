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

import org.aestia.db.statique.AbstractDAO;
import org.aestia.entity.monster.Monster;
import org.aestia.game.world.World;
import org.aestia.kernel.Main;

public class MonsterData extends AbstractDAO<Monster> {
	public MonsterData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Monster obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM monsters");

				while (RS.next()) {
					int id = RS.getInt("id");
					int gfxID = RS.getInt("gfxID");
					int align = RS.getInt("align");
					String colors = RS.getString("colors");
					String grades = RS.getString("grades");
					String spells = RS.getString("spells");
					String stats = RS.getString("stats");
					String statsInfos = RS.getString("statsInfos");
					String pdvs = RS.getString("pdvs");
					String pts = RS.getString("points");
					String inits = RS.getString("inits");
					int mK = RS.getInt("minKamas");
					int MK = RS.getInt("maxKamas");
					int IAType = RS.getInt("AI_Type");
					String xp = RS.getString("exps");
					int aggroDistance = RS.getInt("aggroDistance");
					boolean capturable = RS.getInt("capturable") == 1;
					World.addMobTemplate(id, new Monster(id, gfxID, align, colors, grades, spells, stats, statsInfos,
							pdvs, pts, inits, mK, MK, xp, IAType, capturable, aggroDistance));
				}
			} catch (SQLException e) {
				super.sendError("MonsterData load", e);
				Main.stop();
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
				RS = this.getData("SELECT * FROM monsters");

				while (RS.next()) {
					boolean capturable;
					int id = RS.getInt("id");
					int gfxID = RS.getInt("gfxID");
					int align = RS.getInt("align");
					String colors = RS.getString("colors");
					String grades = RS.getString("grades");
					String spells = RS.getString("spells");
					String stats = RS.getString("stats");
					String statsInfos = RS.getString("statsInfos");
					String pdvs = RS.getString("pdvs");
					String pts = RS.getString("points");
					String inits = RS.getString("inits");
					int mK = RS.getInt("minKamas");
					int MK = RS.getInt("maxKamas");
					int IAType = RS.getInt("AI_Type");
					String xp = RS.getString("exps");
					int aggroDistance = RS.getInt("aggroDistance");
					capturable = RS.getInt("capturable") == 1;
					if (World.getMonstre(id) == null) {
						World.addMobTemplate(id, new Monster(id, gfxID, align, colors, grades, spells, stats,
								statsInfos, pdvs, pts, inits, mK, MK, xp, IAType, capturable, aggroDistance));
						continue;
					}
					World.getMonstre(id).setInfos(gfxID, align, colors, grades, spells, stats, statsInfos, pdvs, pts,
							inits, mK, MK, xp, IAType, capturable, aggroDistance);
				}
			} catch (SQLException e) {
				super.sendError("MonsterData reload", e);
				this.close(RS);
			}
		} finally {
			this.close(RS);
		}
	}

	public void updateXp(Monster m) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE monsters SET `exps` = ? WHERE `id` = ?");
				Map<Integer, Monster.MobGrade> grades = m.getGrades();
				String xp = "";
				for (Map.Entry<Integer, Monster.MobGrade> entry : grades.entrySet()) {
					xp = xp.equalsIgnoreCase("")
							? String.valueOf(xp) + (int) Math.floor(entry.getValue().getBaseXp() / 2.0)
							: String.valueOf(xp) + "|" + (int) Math.floor(entry.getValue().getBaseXp() / 2.0);
				}
				p.setString(1, xp);
				p.setInt(2, m.getId());
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("MonsterData updateXp", e);
				this.close(p);
			}
		} finally {
			this.close(p);
		}
	}
}

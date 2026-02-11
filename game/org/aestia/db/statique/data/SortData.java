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
import org.aestia.entity.monster.Monster;
import org.aestia.fight.spells.Spell;
import org.aestia.game.world.World;
import org.aestia.kernel.Main;

public class SortData extends AbstractDAO<Spell> {
	public SortData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Spell obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT  * from sorts");

				boolean modif = false;
				while (RS.next()) {
					Spell.SortStats l3;
					Spell.SortStats l4;
					Spell.SortStats l5;
					Spell.SortStats l6;
					Object l2;
					Spell.SortStats l1;
					int id = RS.getInt("id");
					if (World.getSort(id) != null) {
						Spell spell = World.getSort(id);
						spell.setInfos(RS.getInt("sprite"), RS.getString("spriteInfos"), RS.getString("effectTarget"),
								RS.getInt("type"));
						l1 = this.parseSortStats(id, 1, RS.getString("lvl1"));
						l2 = this.parseSortStats(id, 2, RS.getString("lvl2"));
						l3 = this.parseSortStats(id, 3, RS.getString("lvl3"));
						l4 = this.parseSortStats(id, 4, RS.getString("lvl4"));
						l5 = null;
						if (!RS.getString("lvl5").equalsIgnoreCase("-1")) {
							l5 = this.parseSortStats(id, 5, RS.getString("lvl5"));
						}
						l6 = null;
						if (!RS.getString("lvl6").equalsIgnoreCase("-1")) {
							l6 = this.parseSortStats(id, 6, RS.getString("lvl6"));
						}
						spell.getSortsStats().clear();
						spell.addSortStats(1, l1);
						spell.addSortStats(2, (Spell.SortStats) l2);
						spell.addSortStats(3, l3);
						spell.addSortStats(4, l4);
						spell.addSortStats(5, l5);
						spell.addSortStats(6, l6);
						modif = true;
						continue;
					}
					Spell sort = new Spell(id, RS.getString("nom"), RS.getInt("sprite"), RS.getString("spriteInfos"),
							RS.getString("effectTarget"), RS.getInt("type"));
					l1 = this.parseSortStats(id, 1, RS.getString("lvl1"));
					l2 = this.parseSortStats(id, 2, RS.getString("lvl2"));
					l3 = this.parseSortStats(id, 3, RS.getString("lvl3"));
					l4 = this.parseSortStats(id, 4, RS.getString("lvl4"));
					l5 = null;
					if (!RS.getString("lvl5").equalsIgnoreCase("-1")) {
						l5 = this.parseSortStats(id, 5, RS.getString("lvl5"));
					}
					l6 = null;
					if (!RS.getString("lvl6").equalsIgnoreCase("-1")) {
						l6 = this.parseSortStats(id, 6, RS.getString("lvl6"));
					}
					sort.addSortStats(1, l1);
					sort.addSortStats(2, (Spell.SortStats) l2);
					sort.addSortStats(3, l3);
					sort.addSortStats(4, l4);
					sort.addSortStats(5, l5);
					sort.addSortStats(6, l6);
					World.addSort(sort);
				}
				if (modif) {
					for (Monster monster : World.getMonstres()) {
						for (Monster.MobGrade mg : monster.getGrades().values()) {
							mg.refresh();
						}
					}
				}
			} catch (SQLException e) {
				super.sendError("SortData load", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
	}

	private Spell.SortStats parseSortStats(int id, int lvl, String str) {
		try {
			Spell.SortStats stats = null;
			String[] stat = str.split(",");
			String effets = stat[0];
			String CCeffets = stat[1];
			int PACOST = 6;
			try {
				PACOST = Integer.parseInt(stat[2].trim());
			} catch (NumberFormatException var9_10) {
				// empty catch block
			}
			int POm = Integer.parseInt(stat[3].trim());
			int POM = Integer.parseInt(stat[4].trim());
			int TCC = Integer.parseInt(stat[5].trim());
			int TEC = Integer.parseInt(stat[6].trim());
			boolean line = stat[7].trim().equalsIgnoreCase("true");
			boolean LDV = stat[8].trim().equalsIgnoreCase("true");
			boolean emptyCell = stat[9].trim().equalsIgnoreCase("true");
			boolean MODPO = stat[10].trim().equalsIgnoreCase("true");
			int MaxByTurn = Integer.parseInt(stat[12].trim());
			int MaxByTarget = Integer.parseInt(stat[13].trim());
			int CoolDown = Integer.parseInt(stat[14].trim());
			String type = stat[15].trim();
			int level = Integer.parseInt(stat[stat.length - 2].trim());
			boolean endTurn = stat[19].trim().equalsIgnoreCase("true");
			stats = new Spell.SortStats(id, lvl, PACOST, POm, POM, TCC, TEC, line, LDV, emptyCell, MODPO, MaxByTurn,
					MaxByTarget, CoolDown, level, endTurn, effets, CCeffets, type);
			return stats;
		} catch (Exception e) {
			super.sendError("SortData parseSortStats", e);
			return null;
		}
	}
}

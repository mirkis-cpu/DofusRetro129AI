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
import org.aestia.kernel.Boutique;
import org.aestia.kernel.Main;
import org.aestia.object.ObjectTemplate;

public class Item_templateData extends AbstractDAO<ObjectTemplate> {
	public Item_templateData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(ObjectTemplate obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT  * from item_template");

				while (RS.next()) {
					if (World.getObjTemplate(RS.getInt("id")) != null) {
						World.getObjTemplate(RS.getInt("id")).setInfos(RS.getString("statsTemplate"),
								RS.getString("name"), RS.getInt("type"), RS.getInt("level"), RS.getInt("pod"),
								RS.getInt("prix"), RS.getInt("panoplie"), RS.getString("conditions"),
								RS.getString("armesInfos"), RS.getInt("sold"), RS.getInt("avgPrice"),
								RS.getInt("points"));
						continue;
					}
					ObjectTemplate template = new ObjectTemplate(RS.getInt("id"), RS.getString("statsTemplate"),
							RS.getString("name"), RS.getInt("type"), RS.getInt("level"), RS.getInt("pod"),
							RS.getInt("prix"), RS.getInt("panoplie"), RS.getString("conditions"),
							RS.getString("armesInfos"), RS.getInt("sold"), RS.getInt("avgPrice"), RS.getInt("points"),RS.getInt("boutique"));
					World.addObjTemplate(template);
					if(RS.getInt("boutique") != 0) {
						Boutique.items.add(template);
					}
				}
				

			} catch (SQLException e) {
				e.printStackTrace();
				Main.stop();
				this.close(RS);
			}
		} finally {
			this.close(RS);
		}
	}

	public void saveAvgprice(ObjectTemplate template) {
		if (template == null) {
			return;
		}
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `item_template` SET sold = ?,avgPrice = ? WHERE id = ?");
				p.setLong(1, template.getSold());
				p.setInt(2, template.getAvgPrice());
				p.setInt(3, template.getId());
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public String getStatsTemplate(int i) {
		String statsTemplate;
		ResultSet RS = null;
		statsTemplate = null;
		try {
			try {
				RS = this.getData("SELECT statsTemplate from `item_template` WHERE `id`='" + i + "'");

				if (RS.next()) {
					statsTemplate = RS.getString("statsTemplate");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
		return statsTemplate;
	}
}

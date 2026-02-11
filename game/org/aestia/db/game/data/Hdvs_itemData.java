package org.aestia.db.game.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.db.Database;
import org.aestia.db.game.AbstractDAO;
import org.aestia.game.world.World;
import org.aestia.hdv.Hdv;
import org.aestia.hdv.HdvEntry;

public class Hdvs_itemData extends AbstractDAO<java.lang.Object> {
	public Hdvs_itemData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(java.lang.Object obj) {
	}

	@Override
	public boolean update(java.lang.Object obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM `hdvs_items`");

				while (RS.next()) {
					Hdv tempHdv = World.getHdv(RS.getInt("map"));
					if (tempHdv == null)
						continue;
					if (World.getObjet(RS.getInt("itemID")) == null) {
						Database.getGame().getHdvs_itemData().delete(RS.getInt("id"));
						continue;
					}
					tempHdv.addEntry(new HdvEntry(RS.getInt("id"), RS.getInt("price"), RS.getByte("count"),
							RS.getInt("ownerGuid"), World.getObjet(RS.getInt("itemID"))), true);
					World.setNextHdvItemID(RS.getInt("id"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}

	public boolean add(HdvEntry toAdd) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement(
					"INSERT INTO `hdvs_items` (`map`,`ownerGuid`,`price`,`count`,`itemID`) VALUES(?,?,?,?,?)");
			p.setInt(1, toAdd.getHdvId());
			p.setInt(2, toAdd.getOwner());
			p.setInt(3, toAdd.getPrice());
			p.setInt(4, toAdd.getAmount(false));
			p.setInt(5, toAdd.getObject().getGuid());
			this.execute(p);
			Database.getStatique().getItem_templateData().saveAvgprice(toAdd.getObject().getTemplate());
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}
		return false;
	}

	public void delete(int id) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("DELETE FROM hdvs_items WHERE itemID = ?");
				p.setInt(1, id);
				this.execute(p);
				if (World.getObjet(id) != null) {
					Database.getStatique().getItem_templateData().saveAvgprice(World.getObjet(id).getTemplate());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}
}

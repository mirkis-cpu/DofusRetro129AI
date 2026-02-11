package org.aestia.db.game.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.aestia.client.Player;
import org.aestia.db.game.AbstractDAO;
import org.aestia.game.world.World;
import org.aestia.other.House;
import org.aestia.other.Trunk;

public class CoffreData extends AbstractDAO<Trunk> {
	public CoffreData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Trunk t) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("UPDATE `coffres` SET `kamas`=?, `object`=? WHERE `id`=?");
			p.setLong(1, t.getKamas());
			p.setString(2, t.parseTrunkObjetsToDB());
			p.setInt(3, t.getId());
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
				RS = this.getData("SELECT * from coffres");
				while (RS.next()) {
					int id = RS.getInt("id");
					String objects = RS.getString("object");
					int kamas = RS.getInt("kamas");
					int owner_id = RS.getInt("owner_id");
					String key = RS.getString("key");
					Trunk t = World.getTrunk(id);
					t.setObjects(objects);
					t.setKamas(kamas);
					t.setOwnerId(owner_id);
					t.setKey(key);
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

	public void update(Player P, House h) {
		ArrayList<Trunk> trunks = Trunk.getTrunksByHouse(h);
		PreparedStatement p = null;
		for (Trunk trunk : trunks) {
			trunk.setOwnerId(P.getAccID());
			trunk.setKey("-");
			try {
				try {
					p = this.getPreparedStatement("UPDATE `coffres` SET `owner_id`=?, `key`='-' WHERE `id`=?");
					p.setInt(1, P.getAccID());
					p.setInt(2, trunk.getId());
					this.execute(p);
				} catch (SQLException e) {
					e.printStackTrace();
					this.close(p);
					continue;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			this.close(p);
		}
	}

	public void updateCode(Player P, Trunk t, String packet) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `coffres` SET `key`=? WHERE `id`=? AND owner_id=?");
				p.setString(1, packet);
				p.setInt(2, t.getId());
				p.setInt(3, P.getAccID());
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}
}

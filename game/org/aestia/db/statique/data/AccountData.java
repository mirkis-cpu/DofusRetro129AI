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

import org.aestia.client.Account;
import org.aestia.db.statique.AbstractDAO;
import org.aestia.game.world.World;

public class AccountData extends AbstractDAO<Account> {
	public AccountData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object id) {
		ResultSet RS = null;
		try {
			try {
				RS = super.getData("SELECT * FROM accounts WHERE guid = " + id.toString());

				while (RS.next()) {
					Account a = World.getCompte(RS.getInt("guid"));
					if (a != null && a.isOnline())
						continue;
					Account C = new Account(RS.getInt("guid"), RS.getString("account").toLowerCase(),
							RS.getString("pass"), RS.getString("pseudo"), RS.getString("question"),
							RS.getString("reponse"), RS.getInt("vip"), RS.getInt("banned") == 1, RS.getString("lastIP"),
							RS.getString("lastConnectionDate"), RS.getString("friends"), RS.getString("enemy"),
							RS.getInt("points"), RS.getLong("subscribe"), RS.getLong("muteTime"),
							RS.getString("muteRaison"), RS.getString("mutePseudo"), RS.getString("lastConnectDay"),
							RS.getString("lastVoteIP"), RS.getString("heurevote"), RS.getString("bank"));
					World.addAccount(C);
					World.ReassignAccountToChar(C);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = super.getData("SELECT * from accounts");

				while (RS.next()) {
					Account a = new Account(RS.getInt("guid"), RS.getString("account").toLowerCase(),
							RS.getString("pass"), RS.getString("pseudo"), RS.getString("question"),
							RS.getString("reponse"), RS.getInt("vip"), RS.getInt("banned") == 1, RS.getString("lastIP"),
							RS.getString("lastConnectionDate"), RS.getString("friends"), RS.getString("enemy"),
							RS.getInt("points"), RS.getLong("subscribe"), RS.getLong("muteTime"),
							RS.getString("muteRaison"), RS.getString("mutePseudo"), RS.getString("lastConnectDay"),
							RS.getString("lastVoteIP"), RS.getString("heurevote"), RS.getString("bank"));
					World.addAccount(a);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}

	public int loadPoints(String user) {
		int points = 0;
		ResultSet RS = null;
		try {
			try {
				RS = super.getData("SELECT * from accounts WHERE `account` LIKE '" + user + "'");

				if (RS.next()) {
					points = RS.getInt("points");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
		return points;
	}

	public void updateVoteAll() {
		ResultSet RS = null;
		Account a = null;
		try {
			try {
				RS = super.getData("SELECT guid, heurevote, lastVoteIP from accounts");

				while (RS.next()) {
					a = World.getCompte(RS.getInt("guid"));
					if (a == null)
						continue;
					a.updateVote(RS.getString("heurevote"), RS.getString("lastVoteIP"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}

	@Override
	public boolean update(Account acc) {
		PreparedStatement statement = null;
		try {
			statement = this.getPreparedStatement("UPDATE accounts " + "SET banned = '" + (acc.isBanned() ? 1 : 0)
					+ "', friends = '" + acc.parseFriendListToDB() + "'" + ", enemy = '" + acc.parseEnemyListToDB()
					+ "', muteTime = '" + acc.getMuteTime() + "', muteRaison = '" + acc.getMuteRaison()
					+ "', mutePseudo = '" + acc.getMutePseudo() + "' WHERE guid = '" + acc.getGuid() + "'");
			this.execute(statement);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(statement);
		}
		return false;
	}
	
	public void updateBank(Account acc) {
		PreparedStatement statement = null;
		String items = "";
		for(org.aestia.object.Object object : acc.getBank().values()) 
			items += object.getGuid() + ",";
		String bank = acc.getBankKamas() + ";" + items;
		try {
			statement = this.getPreparedStatement("UPDATE accounts " + "SET bank = '" + bank
					+ "' WHERE guid = '" + acc.getGuid() + "'");
			this.execute(statement);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(statement);
		}
	}

	public void updateLastConnection(Account compte) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement(
						"UPDATE accounts SET `lastIP` = ?, `lastConnectionDate` = ? WHERE `guid` = ?");
				p.setString(1, compte.getCurIP());
				p.setString(2, compte.getLastConnectionDate());
				p.setInt(3, compte.getGuid());
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public void updatePoints(int id, int points) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE accounts SET `points` = ? WHERE `guid` = ?");
				p.setInt(1, points);
				p.setInt(2, id);
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public void setLogged(int id, int logged) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `accounts` SET logged = ? WHERE `guid` = ?");
				p.setInt(1, logged);
				p.setInt(2, id);
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public boolean canMigrate(String user) {
		boolean can = false;
		ResultSet RS = null;
		try {
			try {
				RS = super.getData("SELECT * from accounts WHERE `account` LIKE '" + user + "'");

				while (RS.next()) {
					can = RS.getInt("migration") == 1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
		return can;
	}

	public void setMigration(int id, int logged) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `accounts` SET logged = ? WHERE `guid` = ?");
				p.setInt(1, logged);
				p.setInt(2, id);
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}

	public void setLastConnectDay(Account a) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `accounts` SET `lastConnectDay` = ? WHERE `guid` = ?");
				p.setString(1, String.valueOf(a.getLastConnectDay()));
				p.setInt(2, a.getGuid());
				this.execute(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(p);
		}
	}
}

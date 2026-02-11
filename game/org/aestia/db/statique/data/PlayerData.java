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
import java.util.TreeMap;

import org.aestia.client.Account;
import org.aestia.client.Player;
import org.aestia.command.server.Groupes;
import org.aestia.db.Database;
import org.aestia.db.statique.AbstractDAO;
import org.aestia.game.world.World;
import org.aestia.kernel.Main;
import org.aestia.quest.Quest;

public class PlayerData extends AbstractDAO<Player> {
	public PlayerData(Connection dataSource) {
		super(dataSource);
	}

	public int getNextId() {
		int guid;
		block6: {
			ResultSet RS = null;
			guid = 0;
			try {
				try {
					RS = this.getData("SELECT id FROM players ORDER BY id DESC LIMIT 1");

					if (!RS.first()) {
						guid = 1;
						break block6;
					}
					guid = RS.getInt("id") + 1;
				} catch (SQLException e) {
					super.sendError("PlayerData getNextId", e);
				}
			} finally {
				this.close(RS);
			}
		}
		return guid;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM players");

				while (RS.next()) {
					if (RS.getInt("server") != Main.serverId)
						continue;
					TreeMap<Integer, Integer> stats = new TreeMap<Integer, Integer>();
					stats.put(125, RS.getInt("vitalite"));
					stats.put(118, RS.getInt("force"));
					stats.put(124, RS.getInt("sagesse"));
					stats.put(126, RS.getInt("intelligence"));
					stats.put(123, RS.getInt("chance"));
					stats.put(119, RS.getInt("agilite"));
					Player perso = new Player(RS.getInt("id"), RS.getString("name"), RS.getInt("groupe"),
							RS.getInt("sexe"), RS.getInt("class"), RS.getInt("color1"), RS.getInt("color2"),
							RS.getInt("color3"), RS.getLong("kamas"), RS.getInt("spellboost"), RS.getInt("capital"),
							RS.getInt("energy"), RS.getInt("level"), RS.getLong("xp"), RS.getInt("size"),
							RS.getInt("gfx"), RS.getByte("alignement"), RS.getInt("account"), stats,
							RS.getByte("seeFriend"), RS.getByte("seeAlign"), RS.getByte("seeSeller"),
							RS.getString("canaux"), RS.getShort("map"), RS.getInt("cell"), RS.getString("objets"),
							RS.getString("storeObjets"), RS.getInt("pdvper"), RS.getString("spells"),
							RS.getString("savepos"), RS.getString("jobs"), RS.getInt("mountxpgive"), RS.getInt("mount"),
							RS.getInt("honor"), RS.getInt("deshonor"), RS.getInt("alvl"), RS.getString("zaaps"),
							RS.getByte("title"), RS.getInt("wife"), RS.getString("morphMode"), RS.getString("allTitle"),
							RS.getString("emotes"), RS.getLong("prison"), false, RS.getString("parcho"),
							RS.getLong("timeDeblo"), RS.getBoolean("noall"));
					perso.VerifAndChangeItemPlace();
					World.addPersonnage(perso);
					if (!perso.isShowSeller())
						continue;
					World.addSeller(perso);
				}
			} catch (SQLException e) {
				super.sendError("PlayerData load", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
	}

	@Override
	public void load(Object obj) {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM players WHERE id = '" + obj + "'");

				while (RS.next()) {
					if (RS.getInt("server") != Main.serverId)
						continue;
					TreeMap<Integer, Integer> stats = new TreeMap<Integer, Integer>();
					stats.put(125, RS.getInt("vitalite"));
					stats.put(118, RS.getInt("force"));
					stats.put(124, RS.getInt("sagesse"));
					stats.put(126, RS.getInt("intelligence"));
					stats.put(123, RS.getInt("chance"));
					stats.put(119, RS.getInt("agilite"));
					Player perso = new Player(RS.getInt("id"), RS.getString("name"), RS.getInt("groupe"),
							RS.getInt("sexe"), RS.getInt("class"), RS.getInt("color1"), RS.getInt("color2"),
							RS.getInt("color3"), RS.getLong("kamas"), RS.getInt("spellboost"), RS.getInt("capital"),
							RS.getInt("energy"), RS.getInt("level"), RS.getLong("xp"), RS.getInt("size"),
							RS.getInt("gfx"), RS.getByte("alignement"), RS.getInt("account"), stats,
							RS.getByte("seeFriend"), RS.getByte("seeAlign"), RS.getByte("seeSeller"),
							RS.getString("canaux"), RS.getShort("map"), RS.getInt("cell"), RS.getString("objets"),
							RS.getString("storeObjets"), RS.getInt("pdvper"), RS.getString("spells"),
							RS.getString("savepos"), RS.getString("jobs"), RS.getInt("mountxpgive"), RS.getInt("mount"),
							RS.getInt("honor"), RS.getInt("deshonor"), RS.getInt("alvl"), RS.getString("zaaps"),
							RS.getByte("title"), RS.getInt("wife"), RS.getString("morphMode"), RS.getString("allTitle"),
							RS.getString("emotes"), RS.getLong("prison"), false, RS.getString("parcho"),
							RS.getLong("timeDeblo"), RS.getBoolean("noall"));
					perso.VerifAndChangeItemPlace();
					World.addPersonnage(perso);
					int guild = Database.getGame().getGuild_memberData().isPersoInGuild(RS.getInt("id"));
					if (guild < 0)
						continue;
					perso.setGuildMember(World.getGuild(guild).getMember(RS.getInt("id")));
				}
			} catch (SQLException e) {
				super.sendError("PlayerData load id", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
	}

	public void loadByAccountId(int id) {
		try {
			Account c = World.getCompte(id);
			if (c != null && c.getPersos() != null) {
				for (Player p : c.getPersos().values()) {
					if (p == null)
						continue;
					World.verifyClone(p);
				}
			}
		} catch (Exception e) {
			super.sendError("PlayerData loadByAccountId clone", e);
		}
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM players WHERE account = '" + id + "'");

				while (RS.next()) {
					Player p;
					if (RS.getInt("server") != Main.serverId
							|| (p = World.getPersonnage(RS.getInt("id"))) != null && p.get_fight() != null)
						continue;
					TreeMap<Integer, Integer> stats = new TreeMap<Integer, Integer>();
					stats.put(125, RS.getInt("vitalite"));
					stats.put(118, RS.getInt("force"));
					stats.put(124, RS.getInt("sagesse"));
					stats.put(126, RS.getInt("intelligence"));
					stats.put(123, RS.getInt("chance"));
					stats.put(119, RS.getInt("agilite"));
					Player perso = new Player(RS.getInt("id"), RS.getString("name"), RS.getInt("groupe"),
							RS.getInt("sexe"), RS.getInt("class"), RS.getInt("color1"), RS.getInt("color2"),
							RS.getInt("color3"), RS.getLong("kamas"), RS.getInt("spellboost"), RS.getInt("capital"),
							RS.getInt("energy"), RS.getInt("level"), RS.getLong("xp"), RS.getInt("size"),
							RS.getInt("gfx"), RS.getByte("alignement"), RS.getInt("account"), stats,
							RS.getByte("seeFriend"), RS.getByte("seeAlign"), RS.getByte("seeSeller"),
							RS.getString("canaux"), RS.getShort("map"), RS.getInt("cell"), RS.getString("objets"),
							RS.getString("storeObjets"), RS.getInt("pdvper"), RS.getString("spells"),
							RS.getString("savepos"), RS.getString("jobs"), RS.getInt("mountxpgive"), RS.getInt("mount"),
							RS.getInt("honor"), RS.getInt("deshonor"), RS.getInt("alvl"), RS.getString("zaaps"),
							RS.getByte("title"), RS.getInt("wife"), RS.getString("morphMode"), RS.getString("allTitle"),
							RS.getString("emotes"), RS.getLong("prison"), false, RS.getString("parcho"),
							RS.getLong("timeDeblo"), RS.getBoolean("noall"));
					perso.VerifAndChangeItemPlace();
					World.addPersonnage(perso);
					int guild = Database.getGame().getGuild_memberData().isPersoInGuild(RS.getInt("id"));
					if (guild < 0)
						continue;
					perso.setGuildMember(World.getGuild(guild).getMember(RS.getInt("id")));
				}
			} catch (SQLException e) {
				super.sendError("PlayerData loadByAccountId", e);
				Main.stop();
			}
		} finally {
			this.close(RS);
		}
	}

	public String loadTitles(int guid) {
		String title;
		ResultSet RS = null;
		title = "";
		try {
			try {
				RS = this.getData("SELECT * FROM players WHERE id = '" + guid + "';");

				if (RS.next()) {
					title = RS.getString("allTitle");
				}
			} catch (SQLException e) {
				super.sendError("PlayerData loadTitles", e);
			}
		} finally {
			this.close(RS);
		}
		return title;
	}

	public boolean add(Player perso) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement(
					"INSERT INTO players(`id`, `name`, `sexe`, `class`, `color1`, `color2`, `color3`, `kamas`, `spellboost`, `capital`, `energy`, `level`, `xp`, `size`, `gfx`, `account`, `cell`, `map`, `spells`, `objets`, `storeObjets`, `morphMode`, `server`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'','','0',?)");
			p.setInt(1, perso.getId());
			p.setString(2, perso.getName());
			p.setInt(3, perso.getSexe());
			p.setInt(4, perso.getClasse());
			p.setInt(5, perso.getColor1());
			p.setInt(6, perso.getColor2());
			p.setInt(7, perso.getColor3());
			p.setLong(8, perso.get_kamas());
			p.setInt(9, perso.get_spellPts());
			p.setInt(10, perso.get_capital());
			p.setInt(11, perso.getEnergy());
			p.setInt(12, perso.getLevel());
			p.setLong(13, perso.getExp());
			p.setInt(14, perso.get_size());
			p.setInt(15, perso.get_gfxID());
			p.setInt(16, perso.getAccID());
			p.setInt(17, perso.getCurCell().getId());
			p.setInt(18, perso.getCurMap().getId());
			p.setString(19, perso.parseSpellToDB());
			p.setInt(20, Main.serverId);
			this.execute(p);
			return true;
		} catch (SQLException e) {
			super.sendError("PlayerData add", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	public boolean delete(Player perso) {
		PreparedStatement p = null;
		try {
			p = this.getPreparedStatement("DELETE FROM players WHERE id = ?");
			p.setInt(1, perso.getId());
			this.execute(p);
			if (!perso.getItemsIDSplitByChar(",").equals("")) {
				p = this.getPreparedStatement("DELETE FROM `items` WHERE guid IN (?)");
				p.setString(1, perso.getItemsIDSplitByChar(","));
				this.execute(p);
			}
			if (!perso.getStoreItemsIDSplitByChar(",").equals("")) {
				p = this.getPreparedStatement("DELETE FROM `items` WHERE guid IN (?)");
				p.setString(1, perso.getStoreItemsIDSplitByChar(","));
				this.execute(p);
			}
			if (perso.getMount() != null) {
				p = this.getPreparedStatement("DELETE FROM `mounts_data` WHERE id = ?");
				p.setInt(1, perso.getMount().getId());
				this.execute(p);
				World.delDragoByID(perso.getMount().getId());
			}
			return true;
		} catch (SQLException e) {
			super.sendError("PlayerData delete", e);
		} finally {
			this.close(p);
		}
		return false;
	}

	@Override
	public boolean update(Player obj) {
		return this.update(obj, true);
	}

	/*
	 * Enabled force condition propagation Lifted jumps to return sites
	 */
	public boolean update(Player perso, boolean saveItem) {
		if (perso == null) {
			super.sendError("PlayerData update", new Exception("perso is null"));
			return false;
		}
		Map<Integer, org.aestia.object.Object> map;
		PreparedStatement p = null;
		org.aestia.object.Object obj;
		Map<Integer, org.aestia.object.Object> items;

		try {
			p = this.getPreparedStatement(
					"UPDATE `players` SET `kamas`= ?, `spellboost`= ?, `capital`= ?, `energy`= ?, `level`= ?, `xp`= ?, `size` = ?, `gfx`= ?, `alignement`= ?, `honor`= ?, `deshonor`= ?, `alvl`= ?, `vitalite`= ?, `force`= ?, `sagesse`= ?, `intelligence`= ?, `chance`= ?, `agilite`= ?, `seeFriend`= ?, `seeAlign`= ?, `seeSeller`= ?, `canaux`= ?, `map`= ?, `cell`= ?, `pdvper`= ?, `spells`= ?, `objets`= ?, `storeObjets`= ?, `savepos`= ?, `zaaps`= ?, `jobs`= ?, `mountxpgive`= ?, `mount`= ?, `title`= ?, `wife`= ?, `morphMode`= ?, `allTitle` = ?, `emotes` = ?, `prison` = ?, `parcho` = ?, `timeDeblo` = ?, `noall` = ? WHERE `players`.`id` = ? LIMIT 1");
			p.setLong(1, perso.get_kamas());
			p.setInt(2, perso.get_spellPts());
			p.setInt(3, perso.get_capital());
			p.setInt(4, perso.getEnergy());
			p.setInt(5, perso.getLevel());
			p.setLong(6, perso.getExp());
			p.setInt(7, perso.get_size());
			p.setInt(8, perso.get_gfxID());
			p.setInt(9, perso.get_align());
			p.setInt(10, perso.get_honor());
			p.setInt(11, perso.getDeshonor());
			p.setInt(12, perso.getALvl());
			p.setInt(13, perso.stats.getEffect(125));
			p.setInt(14, perso.stats.getEffect(118));
			p.setInt(15, perso.stats.getEffect(124));
			p.setInt(16, perso.stats.getEffect(126));
			p.setInt(17, perso.stats.getEffect(123));
			p.setInt(18, perso.stats.getEffect(119));
			p.setInt(19, perso.is_showFriendConnection() ? 1 : 0);
			p.setInt(20, perso.is_showWings() ? 1 : 0);
			p.setInt(21, perso.isShowSeller() ? 1 : 0);
			p.setString(22, perso.get_canaux());
			if (perso.getCurMap() != null) {
				p.setInt(23, perso.getCurMap().getId());
			} else {
				p.setInt(23, 7411);
			}
			if (perso.getCurCell() != null) {
				p.setInt(24, perso.getCurCell().getId());
			} else {
				p.setInt(24, 311);
			}
			p.setInt(25, perso.get_pdvper());
			p.setString(26, perso.parseSpellToDB());
			p.setString(27, perso.parseObjetsToDB());
			p.setString(28, perso.parseStoreItemstoBD());
			p.setString(29, perso.get_savePos());
			p.setString(30, perso.parseZaaps());
			p.setString(31, perso.parseJobData());
			p.setInt(32, perso.getMountXpGive());
			p.setInt(33, perso.getMount() != null ? perso.getMount().getId() : -1);
			p.setByte(34, perso.get_title());
			p.setInt(35, perso.getWife());
			p.setString(36, String.valueOf(perso.getMorphMode() ? 1 : 0) + ";" + perso.getMorphId());
			p.setString(37, perso.getAllTitle());
			p.setString(38, perso.parseEmoteToDB());
			p.setLong(39, perso.isInEnnemyFaction ? perso.enteredOnEnnemyFaction : 0);
			p.setString(40, perso.parseStatsParcho());
			p.setLong(41, perso.restriction.timeDeblo);
			p.setBoolean(42, perso.noall);
			p.setInt(43, perso.getId());
			this.execute(p);
			if (perso.getGuildMember() != null) {
				Database.getGame().getGuild_memberData().update(perso);
			}
			if (perso.getMount() != null) {
				Database.getStatique().getMounts_dataData().update(perso.getMount(), true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(p);
		}

		if (perso.getQuestPerso() != null && !perso.getQuestPerso().isEmpty()) {
			for (Quest.Quest_Perso QP : perso.getQuestPerso().values()) {
				if (QP == null)
					continue;
				try {
					p = this.getPreparedStatement(
							"UPDATE `quest_perso` SET `id`= ?, `finish`= ?, `perso`=?, `etapeValidate`=? WHERE `guid` = ?");
					p.setInt(1, QP.getQuest().getId());
					p.setInt(2, QP.isFinish() ? 1 : 0);
					p.setInt(3, perso.getId());
					p.setString(4, QP.getQuestEtapeString());
					p.setInt(5, QP.getId());
					this.execute(p);
				} catch (SQLException e) {
					e.printStackTrace();

				} finally {
					this.close(p);
				}

			}
		}
		if (!saveItem)
			return true;
		items = perso.getItems();
		if (items != null && !items.isEmpty()) {
			map = items;
			for (Integer key : items.keySet()) {
				try {
					p = this.getPreparedStatement("UPDATE `items` SET qua = ?, pos= ?, stats = ? WHERE guid = ?");
					obj = World.getObjet(key);
					if (obj != null)
						break;
					p.setInt(1, obj.getQuantity());
					p.setInt(2, obj.getPosition());
					p.setString(3, obj.parseToSave());
					p.setInt(4, key);
					this.execute(p);
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					this.close(p);
				}

			}
		}

		items = perso.getAccount().getBank();
		if (perso.getItems() == null || items.isEmpty())
			return true;
		map = items;
		for (Integer key : items.keySet()) {
			try {
				try {
					p = this.getPreparedStatement("UPDATE `items` SET qua = ?, pos= ?, stats = ? WHERE guid = ?");
					obj = World.getObjet(key);
					if (obj != null)
						break;
					p.setInt(1, obj.getQuantity());
					p.setInt(2, obj.getPosition());
					p.setString(3, obj.parseToSave());
					p.setInt(4, key);
					this.execute(p);
				} catch (Exception e) {
					super.sendError("PlayerData update", e);
					continue;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				this.close(p);
			}

		}

		return true;
	}

	

	public void updateInfos(Player perso) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement(
						"UPDATE `players` SET `name` = ?, `sexe`=?, `class`= ?, `spells`= ? WHERE `id`= ?");
				p.setString(1, perso.getName());
				p.setInt(2, perso.getSexe());
				p.setInt(3, perso.getClasse());
				p.setString(4, perso.parseSpellToDB());
				p.setInt(5, perso.getId());
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("PlayerData updateInfos", e);
			}
		} finally {
			this.close(p);
		}
	}

	public void updateGroupe(Player perso) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `players` SET `groupe` = ? WHERE `id`= ?");
				int id = perso.getGroupe() != null ? perso.getGroupe().getId() : -1;
				p.setInt(1, id);
				p.setInt(2, perso.getId());
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("PlayerData updateGroupe", e);
			}
		} finally {
			this.close(p);
		}
	}

	public void updateTimeDeblo(int guid, long timeDeblo) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE players SET `timeDeblo` = ? WHERE `id` = ?");
				p.setLong(1, timeDeblo);
				p.setInt(2, guid);
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("PlayerData updateTimeDeblo", e);
			}
		} finally {
			this.close(p);
		}
	}

	public void updateTitles(int guid, String title) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE players SET `allTitle` = ? WHERE `id` = ?");
				p.setString(1, title);
				p.setInt(2, guid);
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("PlayerData updateTitles", e);
			}
		} finally {
			this.close(p);
		}
	}

	public void updateLogged(int guid, int logged) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE players SET `logged` = ? WHERE `id` = ?");
				p.setInt(1, logged);
				p.setInt(2, guid);
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("PlayerData updateLogged", e);
			}
		} finally {
			this.close(p);
		}
	}

	public void updateAllLogged(int guid, int logged) {
		PreparedStatement p = null;
		try {
			try {
				p = this.getPreparedStatement("UPDATE `players` SET `logged` = ? WHERE `account` = ?");
				p.setInt(1, logged);
				p.setInt(2, guid);
				this.execute(p);
			} catch (SQLException e) {
				super.sendError("PlayerData updateAllLogged", e);
			}
		} finally {
			this.close(p);
		}
	}

	public boolean exist(String name) {
		boolean exist;
		ResultSet RS = null;
		exist = false;
		try {
			try {
				RS = this.getData("SELECT COUNT(*) AS exist FROM players WHERE name LIKE '" + name + "'");

				if (RS.next() && RS.getInt("exist") > 0) {
					exist = true;
				}
			} catch (SQLException e) {
				super.sendError("PlayerData exist", e);
			}
		} finally {
			this.close(RS);
		}
		return exist;
	}

	public String haveOtherPlayer(int account) {
		String servers;
		ResultSet RS = null;
		servers = "";
		try {
			try {
				RS = this.getData("SELECT server FROM players WHERE account = '" + account + "' AND NOT server = '"
						+ Main.serverId + "'");

				while (RS.next()) {
					servers = String.valueOf(servers) + (servers.isEmpty() ? Integer.valueOf(RS.getInt("server"))
							: new StringBuilder(",").append(RS.getInt("server")).toString());
				}
			} catch (SQLException e) {
				super.sendError("PlayerData haveOtherPlayer", e);
			}
		} finally {
			this.close(RS);
		}
		return servers;
	}

	public void reloadGroup(Player p) {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT groupe FROM players WHERE id = '" + p.getId() + "'");

				if (RS.next()) {
					int group = RS.getInt("groupe");
					Groupes g = Groupes.getGroupeById(group);
					p.setGroupe(g, false);
				}
			} catch (SQLException e) {
				super.sendError("PlayerData reloadGroup", e);
			}
		} finally {
			this.close(RS);
		}
	}
}

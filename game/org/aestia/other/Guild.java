// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.client.Player;
import org.aestia.client.other.Stats;
import org.aestia.db.Database;
import org.aestia.entity.Collector;
import org.aestia.fight.spells.Spell;
import org.aestia.game.world.World;
import org.aestia.kernel.Constant;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class Guild {
	private int id;
	private String name;
	private String emblem;
	private Map<Integer, GuildMember> members;
	private int lvl;
	private long xp;
	private int capital;
	private int nbrPerco;
	private Map<Integer, Spell.SortStats> spells;
	private Map<Integer, Integer> stats;
	private Map<Integer, Integer> statsFight;
	public Map<Short, Long> timePutCollector;

	public Guild(final Player owner, final String name, final String emblem) {
		this.name = "";
		this.emblem = "";
		this.members = new TreeMap<Integer, GuildMember>();
		this.capital = 0;
		this.nbrPerco = 0;
		this.spells = new TreeMap<Integer, Spell.SortStats>();
		this.stats = new TreeMap<Integer, Integer>();
		this.statsFight = new TreeMap<Integer, Integer>();
		this.timePutCollector = new HashMap<Short, Long>();
		this.id = World.getNextHighestGuildID();
		this.name = name;
		this.emblem = emblem;
		this.lvl = 1;
		this.xp = 0L;
		this.decompileSpell("462;0|461;0|460;0|459;0|458;0|457;0|456;0|455;0|454;0|453;0|452;0|451;0|");
		this.decompileStats("176;100|158;1000|124;100|");
	}

	public Guild(final int id, final String name, final String emblem, final int lvl, final long xp, final int capital,
			final int nbrmax, final String sorts, final String stats) {
		this.name = "";
		this.emblem = "";
		this.members = new TreeMap<Integer, GuildMember>();
		this.capital = 0;
		this.nbrPerco = 0;
		this.spells = new TreeMap<Integer, Spell.SortStats>();
		this.stats = new TreeMap<Integer, Integer>();
		this.statsFight = new TreeMap<Integer, Integer>();
		this.timePutCollector = new HashMap<Short, Long>();
		this.id = id;
		this.name = name;
		this.emblem = emblem;
		this.xp = xp;
		this.lvl = lvl;
		this.capital = capital;
		this.nbrPerco = nbrmax;
		this.decompileSpell(sorts);
		this.decompileStats(stats);
		this.statsFight.clear();
		this.statsFight.put(118, this.lvl);
		this.statsFight.put(124, this.getStats(124));
		this.statsFight.put(126, this.lvl);
		this.statsFight.put(123, this.lvl);
		this.statsFight.put(119, this.lvl);
		this.statsFight.put(214, (int) Math.floor(this.getLvl() / 2));
		this.statsFight.put(213, (int) Math.floor(this.getLvl() / 2));
		this.statsFight.put(211, (int) Math.floor(this.getLvl() / 2));
		this.statsFight.put(212, (int) Math.floor(this.getLvl() / 2));
		this.statsFight.put(210, (int) Math.floor(this.getLvl() / 2));
		this.statsFight.put(160, (int) Math.floor(this.getLvl() / 2));
		this.statsFight.put(161, (int) Math.floor(this.getLvl() / 2));
	}

	public GuildMember addMember(final int guid, final String name, final int lvl, int gfx, final int r, final byte pXp,
			final long x, final int ri, final byte a, final String lastCo) {
		final Player p = World.getPersonnage(guid);
		if (p == null) {
			Database.getGame().getGuild_memberData().delete(guid);
			return null;
		}
		if (gfx > 121 || gfx < 10) {
			gfx = p.getClasse() * 10 + p.getSexe();
		}
		final GuildMember GM = new GuildMember(guid, this, p.getName(), lvl, gfx, r, x, pXp, ri, a, lastCo);
		this.members.put(guid, GM);
		p.setGuildMember(GM);
		return GM;
	}

	public GuildMember addNewMember(final Player p) {
		final GuildMember GM = new GuildMember(p.getId(), this, p.getName(), p.getLevel(), p.get_gfxID(), 0, 0L,
				(byte) 0, 0, p.get_align(), p.getAccount().getLastConnectionDate());
		this.members.put(p.getId(), GM);
		p.setGuildMember(GM);
		return GM;
	}

	public int getId() {
		return this.id;
	}

	public int getNbrPerco() {
		return this.nbrPerco;
	}

	public void setNbrPerco(final int nbr) {
		this.nbrPerco = nbr;
	}

	public int getCapital() {
		return this.capital;
	}

	public void setCapital(final int nbr) {
		this.capital = nbr;
	}

	public Map<Integer, Spell.SortStats> getSpells() {
		return this.spells;
	}

	public Map<Integer, Integer> getStats() {
		return this.stats;
	}

	public void addStat(final int stat, final int qte) {
		final int old = this.stats.get(stat);
		this.stats.put(stat, old + qte);
	}

	public void boostSpell(final int ID) {
		final Spell.SortStats SS = this.spells.get(ID);
		if (SS != null && SS.getLevel() == 5) {
			return;
		}
		this.spells.put(ID, (SS == null) ? World.getSort(ID).getStatsByLevel(1)
				: World.getSort(ID).getStatsByLevel(SS.getLevel() + 1));
	}

	public Stats getStatsFight() {
		return new Stats(this.statsFight);
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getEmblem() {
		return this.emblem;
	}

	public long getXp() {
		return this.xp;
	}

	public int getLvl() {
		return this.lvl;
	}

	public boolean haveTenMembers() {
		return this.id == 1 || this.id == 2 || this.members.size() >= 10;
	}

	public int getSize() {
		return this.members.size();
	}

	public String parseMembersToGM() {
		final StringBuilder str = new StringBuilder();
		for (final GuildMember GM : this.members.values()) {
			String online = "0";
			if (GM.getPerso() != null && GM.getPerso().isOnline()) {
				online = "1";
			}
			if (str.length() != 0) {
				str.append("|");
			}
			str.append(GM.getGuid()).append(";");
			str.append(GM.getName()).append(";");
			str.append(GM.getLvl()).append(";");
			str.append(GM.getGfx()).append(";");
			str.append(GM.getRank()).append(";");
			str.append(GM.getXpGave()).append(";");
			str.append(GM.getPXpGive()).append(";");
			str.append(GM.getRights()).append(";");
			str.append(online).append(";");
			str.append(GM.getAlign()).append(";");
			str.append(GM.getHoursFromLastCo());
		}
		return str.toString();
	}

	public ArrayList<Player> getMembers() {
		final ArrayList<Player> a = new ArrayList<Player>();
		for (final GuildMember GM : this.members.values()) {
			if (GM.getPerso() != null) {
				a.add(GM.getPerso());
			}
		}
		return a;
	}

	public GuildMember getMember(final int guid) {
		return this.members.get(guid);
	}

	public void removeMember(final Player perso) {
		final House h = House.getHouseByPerso(perso);
		if (h != null && House.houseOnGuild(this.id) > 0) {
			Database.getGame().getHouseData().updateGuild(h, 0, 0);
		}
		this.members.remove(perso.getId());
		Database.getGame().getGuild_memberData().delete(perso.getId());
	}

	public void addXp(final long xp) {
		this.xp += xp;
		while (this.xp >= World.getGuildXpMax(this.lvl) && this.lvl < 200) {
			this.levelUp();
		}
	}

	public void levelUp() {
		++this.lvl;
		this.capital += 5;
	}

	public void decompileSpell(final String spellStr) {
		String[] split2;
		for (int length = (split2 = spellStr.split("\\|")).length, i = 0; i < length; ++i) {
			final String split = split2[i];
			final int id = Integer.parseInt(split.split(";")[0]);
			final int lvl = Integer.parseInt(split.split(";")[1]);
			this.spells.put(id, World.getSort(id).getStatsByLevel(lvl));
		}
	}

	public void decompileStats(final String statsStr) {
		String[] split2;
		for (int length = (split2 = statsStr.split("\\|")).length, i = 0; i < length; ++i) {
			final String split = split2[i];
			final int id = Integer.parseInt(split.split(";")[0]);
			final int value = Integer.parseInt(split.split(";")[1]);
			this.stats.put(id, value);
		}
	}

	public String compileSpell() {
		if (this.spells.isEmpty()) {
			return "";
		}
		final StringBuilder toReturn = new StringBuilder();
		boolean isFirst = true;
		for (final Map.Entry<Integer, Spell.SortStats> curSpell : this.spells.entrySet()) {
			if (!isFirst) {
				toReturn.append("|");
			}
			toReturn.append(curSpell.getKey()).append(";")
					.append((curSpell.getValue() == null) ? 0 : curSpell.getValue().getLevel());
			isFirst = false;
		}
		return toReturn.toString();
	}

	public String compileStats() {
		if (this.stats.isEmpty()) {
			return "";
		}
		final StringBuilder toReturn = new StringBuilder();
		boolean isFirst = true;
		for (final Map.Entry<Integer, Integer> curStats : this.stats.entrySet()) {
			if (!isFirst) {
				toReturn.append("|");
			}
			toReturn.append(curStats.getKey()).append(";").append(curStats.getValue());
			isFirst = false;
		}
		return toReturn.toString();
	}

	public void upgradeStats(final int statsid, final int add) {
		final int actual = this.stats.get(statsid);
		this.stats.put(statsid, actual + add);
	}

	public int getStats(final int statsid) {
		int value = 0;
		for (final Map.Entry<Integer, Integer> curStats : this.stats.entrySet()) {
			if (curStats.getKey() == statsid) {
				value = curStats.getValue();
			}
		}
		return value;
	}

	public String parseCollectorToGuild() {
		final StringBuilder packet = new StringBuilder();
		packet.append(this.getNbrPerco()).append("|");
		packet.append(Collector.countCollectorGuild(this.getId())).append("|");
		packet.append(100 * this.getLvl()).append("|").append(this.getLvl()).append("|");
		packet.append(this.getStats(158)).append("|").append(this.getStats(176)).append("|");
		packet.append(this.getStats(124)).append("|").append(this.getNbrPerco()).append("|");
		packet.append(this.getCapital()).append("|").append(1000 + 10 * this.getLvl()).append("|")
				.append(this.compileSpell());
		return packet.toString();
	}

	public String parseQuestionTaxCollector() {
		final StringBuilder packet = new StringBuilder(10);
		packet.append('1').append(';');
		packet.append(this.getName()).append(',');
		packet.append(this.getStats(158)).append(',');
		packet.append(this.getStats(176)).append(',');
		packet.append(this.getStats(124)).append(',');
		packet.append(this.getNbrPerco());
		return packet.toString();
	}

	public static class GuildMember {
		private int id;
		private Guild guild;
		private String name;
		private int level;
		private int gfx;
		private byte align;
		private int rank;
		private byte pXpGive;
		private long xpGave;
		private int rights;
		private String lastCo;
		private Map<Integer, Boolean> haveRight;

		public GuildMember(final int id, final Guild guild, final String name, final int lvl, final int gfx,
				final int rank, final long x, final byte pXp, final int ri, final byte a, final String lastCo) {
			this.rank = 0;
			this.pXpGive = 0;
			this.xpGave = 0L;
			this.rights = 0;
			this.haveRight = new TreeMap<Integer, Boolean>();
			this.id = id;
			this.guild = guild;
			this.name = name;
			this.level = lvl;
			this.gfx = gfx;
			this.rank = rank;
			this.xpGave = x;
			this.pXpGive = pXp;
			this.rights = ri;
			this.align = a;
			this.lastCo = lastCo;
			this.parseIntToRight(this.rights);
		}

		public int getAlign() {
			return this.align;
		}

		public int getGfx() {
			return this.gfx;
		}

		public int getLvl() {
			return this.level;
		}

		public String getName() {
			return this.name;
		}

		public int getGuid() {
			return this.id;
		}

		public int getRank() {
			return this.rank;
		}

		public Guild getGuild() {
			return this.guild;
		}

		public String parseRights() {
			return Integer.toString(this.rights, 36);
		}

		public int getRights() {
			return this.rights;
		}

		public long getXpGave() {
			return this.xpGave;
		}

		public int getPXpGive() {
			return this.pXpGive;
		}

		public String getLastCo() {
			return this.lastCo;
		}

		public int getHoursFromLastCo() {
			final String[] strDate = this.lastCo.toString().split("~");
			final LocalDate lastCo = new LocalDate(Integer.parseInt(strDate[0]), Integer.parseInt(strDate[1]),
					Integer.parseInt(strDate[2]));
			final LocalDate now = new LocalDate();
			return Days.daysBetween(lastCo, now).getDays() * 24;
		}

		public Player getPerso() {
			return World.getPersonnage(this.id);
		}

		public boolean canDo(final int rightValue) {
			return this.rights == 1 || this.haveRight.get(rightValue);
		}

		public void setRank(final int i) {
			this.rank = i;
		}

		public void setAllRights(int rank, byte xp, int right, final Player perso) {
			if (rank == -1) {
				rank = this.rank;
			}
			if (xp < 0) {
				xp = this.pXpGive;
			}
			if (xp > 90) {
				xp = 90;
			}
			if (right == -1) {
				right = this.rights;
			}
			this.rank = rank;
			this.pXpGive = xp;
			if (right != this.rights && right != 1) {
				this.parseIntToRight(right);
			}
			this.rights = right;
			Database.getGame().getGuild_memberData().update(perso);
		}

		public void setAlign(final byte lvl) {
			this.align = lvl;
		}

		public void setLevel(final int lvl) {
			this.level = lvl;
		}

		public void giveXpToGuild(final long xp) {
			this.xpGave += xp;
			this.guild.addXp(xp);
		}

		public void initRight() {
			this.haveRight.put(Constant.G_BOOST, false);
			this.haveRight.put(Constant.G_RIGHT, false);
			this.haveRight.put(Constant.G_INVITE, false);
			this.haveRight.put(Constant.G_BAN, false);
			this.haveRight.put(Constant.G_ALLXP, false);
			this.haveRight.put(Constant.G_HISXP, false);
			this.haveRight.put(Constant.G_RANK, false);
			this.haveRight.put(Constant.G_POSPERCO, false);
			this.haveRight.put(Constant.G_COLLPERCO, false);
			this.haveRight.put(Constant.G_USEENCLOS, false);
			this.haveRight.put(Constant.G_AMENCLOS, false);
			this.haveRight.put(Constant.G_OTHDINDE, false);
		}

		public void parseIntToRight(int total) {
			if (this.haveRight.isEmpty()) {
				this.initRight();
			}
			if (total == 1) {
				return;
			}
			if (this.haveRight.size() > 0) {
				this.haveRight.clear();
			}
			this.initRight();
			final Integer[] mapKey = this.haveRight.keySet().toArray(new Integer[this.haveRight.size()]);
			while (total > 0) {
				for (int i = this.haveRight.size() - 1; i < this.haveRight.size(); --i) {
					if (mapKey[i] <= total) {
						total ^= mapKey[i];
						this.haveRight.put(mapKey[i], true);
						break;
					}
				}
			}
		}

		public void setLastCo(final String lastCo) {
			this.lastCo = lastCo;
		}
	}
}

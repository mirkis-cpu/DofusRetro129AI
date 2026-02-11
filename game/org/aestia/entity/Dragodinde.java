// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Timer;

import org.aestia.client.Player;
import org.aestia.client.other.Stats;
import org.aestia.common.CryptManager;
import org.aestia.common.Formulas;
import org.aestia.common.Pathfinding;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.game.GameServer;
import org.aestia.game.scheduler.TimerWaiter;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.kernel.Constant;
import org.aestia.kernel.Main;
import org.aestia.map.MountPark;

public class Dragodinde {
	private int _id;
	private int _color;
	private int _sexe;
	private int _amour;
	private int _endurance;
	private int _level;
	private long _exp;
	private String _name;
	private int _fatigue;
	private int _energie;
	private int _reprod;
	private int _maturite;
	private int _serenite;
	private int _isSauvage;
	private Stats _stats;
	private String _ancetres;
	private Map<Integer, org.aestia.object.Object> _items;
	private List<Integer> _capacity;
	String _ability;
	private int _cellId;
	private int _owner;
	private int _size;
	private short _mapId;
	private int _orientation;
	private int _fecundatedAgo;
	private int _couple;
	private Timer _upFecundo;
	private Timer _resFatigue;
	private TimerWaiter waiter;

	public Dragodinde(final int color, final int perso, final boolean sauvage) {
		this._stats = new Stats();
		this._ancetres = "?,?,?,?,?,?,?,?,?,?,?,?,?,?";
		this._items = new TreeMap<Integer, org.aestia.object.Object>();
		this._capacity = new ArrayList<Integer>(2);
		this._ability = ",";
		this._upFecundo = new Timer(60000, new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final Dragodinde this$0 = Dragodinde.this;
				Dragodinde.access$1(this$0, this$0._fecundatedAgo + 1);
				Dragodinde.this.startFecundo();
			}
		});
		this._resFatigue = new Timer(360000, new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (World.getDragoByID(Dragodinde.this._id) == null) {
					Dragodinde.this._resFatigue.stop();
				}
				if (Dragodinde.this._fatigue <= 240) {
					Database.getStatique().getMounts_dataData().update(World.getDragoByID(Dragodinde.this._id), false);
					final Dragodinde this$0 = Dragodinde.this;
					Dragodinde.access$5(this$0, this$0._fatigue - 1);
					if (Dragodinde.this._fatigue < 0) {
						Dragodinde.access$5(Dragodinde.this, 0);
					}
				}
			}
		});
		this.waiter = new TimerWaiter();
		final int sexe = Formulas.getRandomValue(0, 1);
		this._id = World.getNextIdForMount();
		this._color = color;
		this._sexe = sexe;
		this._level = 1;
		this._exp = 0L;
		this._name = "SansNom";
		this._fatigue = 0;
		this._energie = 0;
		if (color == 75 || color == 88) {
			this._reprod = -1;
		} else {
			this._reprod = 0;
		}
		this._maturite = 0;
		this._serenite = 0;
		this._stats = Constant.getMountStats(this._color, this._level);
		this._ancetres = "?,?,?,?,?,?,?,?,?,?,?,?,?,?";
		this._ability = "0";
		this._size = 100;
		this._owner = perso;
		this._cellId = -1;
		this._mapId = -1;
		this._orientation = 1;
		this._fecundatedAgo = -1;
		this._couple = -1;
		if (sauvage) {
			this._isSauvage = 1;
		} else {
			this._isSauvage = 0;
		}
		World.addDragodinde(this);
		Database.getStatique().getMounts_dataData().add(this);
		this._resFatigue.start();
	}

	public Dragodinde(final int color, final Dragodinde mother, final Dragodinde father) {
		this._stats = new Stats();
		this._ancetres = "?,?,?,?,?,?,?,?,?,?,?,?,?,?";
		this._items = new TreeMap<Integer, org.aestia.object.Object>();
		this._capacity = new ArrayList<Integer>(2);
		this._ability = ",";
		this._upFecundo = new Timer(60000, new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final Dragodinde this$0 = Dragodinde.this;
				Dragodinde.access$1(this$0, this$0._fecundatedAgo + 1);
				Dragodinde.this.startFecundo();
			}
		});
		this._resFatigue = new Timer(360000, new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (World.getDragoByID(Dragodinde.this._id) == null) {
					Dragodinde.this._resFatigue.stop();
				}
				if (Dragodinde.this._fatigue <= 240) {
					Database.getStatique().getMounts_dataData().update(World.getDragoByID(Dragodinde.this._id), false);
					final Dragodinde this$0 = Dragodinde.this;
					Dragodinde.access$5(this$0, this$0._fatigue - 1);
					if (Dragodinde.this._fatigue < 0) {
						Dragodinde.access$5(Dragodinde.this, 0);
					}
				}
			}
		});
		this.waiter = new TimerWaiter();
		final int[] sexe = { 0, 0, 1, 0 };
		this._id = World.getNextIdForMount();
		this._color = color;
		this._sexe = sexe[Formulas.getRandomValue(0, 3)];
		this._level = 5;
		this._exp = 0L;
		this._name = "SansNom";
		this._fatigue = 0;
		this._energie = 0;
		this._reprod = 0;
		this._maturite = 0;
		this._serenite = 0;
		this._stats = Constant.getMountStats(this._color, this._level);
		final String[] papa = father.get_ancetres().split(",");
		final String[] mama = mother.get_ancetres().split(",");
		final String primerapapa = String.valueOf(papa[0]) + "," + papa[1];
		final String primeramama = String.valueOf(mama[0]) + "," + mama[1];
		final String segundapapa = String.valueOf(papa[2]) + "," + papa[3] + "," + papa[4] + "," + papa[5];
		final String segundamama = String.valueOf(mama[2]) + "," + mama[3] + "," + mama[4] + "," + mama[5];
		this._ancetres = String.valueOf(father.getColor()) + "," + mother.getColor() + "," + primerapapa + ","
				+ primeramama + "," + segundapapa + "," + segundamama;
		final int habilidad = Formulas.getRandomValue(1, 8);
		this._ability = new StringBuilder().append(habilidad).toString();
		this._cellId = -1;
		this._mapId = -1;
		this._owner = mother.getPerso();
		this._size = 50;
		this._orientation = 1;
		this._fecundatedAgo = -1;
		this._couple = -1;
		this._isSauvage = 0;
		World.addDragodinde(this);
		Database.getStatique().getMounts_dataData().add(this);
		this._resFatigue.start();
	}

	public Dragodinde(final int id, final int color, final int sexe, final int amor, final int resistencia,
			final int nivel, final long exp, final String nombre, final int fatiga, final int energia, final int reprod,
			final int madurez, final int serenidad, final String items, final String anc, final String habilidad,
			final int talla, final int cell, final short map, final int perso, final int orientacion,
			final int fecundable, final int pareja, final int sauvage) {
		this._stats = new Stats();
		this._ancetres = "?,?,?,?,?,?,?,?,?,?,?,?,?,?";
		this._items = new TreeMap<Integer, org.aestia.object.Object>();
		this._capacity = new ArrayList<Integer>(2);
		this._ability = ",";
		this._upFecundo = new Timer(60000, new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final Dragodinde this$0 = Dragodinde.this;
				Dragodinde.access$1(this$0, this$0._fecundatedAgo + 1);
				Dragodinde.this.startFecundo();
			}
		});
		this._resFatigue = new Timer(360000, new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (World.getDragoByID(Dragodinde.this._id) == null) {
					Dragodinde.this._resFatigue.stop();
				}
				if (Dragodinde.this._fatigue <= 240) {
					Database.getStatique().getMounts_dataData().update(World.getDragoByID(Dragodinde.this._id), false);
					final Dragodinde this$0 = Dragodinde.this;
					Dragodinde.access$5(this$0, this$0._fatigue - 1);
					if (Dragodinde.this._fatigue < 0) {
						Dragodinde.access$5(Dragodinde.this, 0);
					}
				}
			}
		});
		this.waiter = new TimerWaiter();
		this._id = id;
		this._color = color;
		this._sexe = sexe;
		this._amour = amor;
		this._endurance = resistencia;
		this._level = nivel;
		this._exp = exp;
		this._name = nombre;
		this._fatigue = fatiga;
		this._energie = energia;
		this._reprod = reprod;
		this._maturite = madurez;
		this._serenite = serenidad;
		this._ancetres = anc;
		this._stats = Constant.getMountStats(this._color, this._level);
		this._ability = habilidad;
		this._size = talla;
		this._cellId = cell;
		this._mapId = map;
		this._owner = perso;
		this._orientation = orientacion;
		this._fecundatedAgo = fecundable;
		this._couple = pareja;
		this._isSauvage = sauvage;
		this._resFatigue.start();
		String[] split;
		for (int length = (split = habilidad.split(",", 2)).length, i = 0; i < length; ++i) {
			final String s = split[i];
			if (s != null) {
				final int a = Integer.parseInt(s);
				try {
					this._capacity.add(a);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		String[] split2;
		for (int length2 = (split2 = items.split(";")).length, j = 0; j < length2; ++j) {
			final String str = split2[j];
			if (!str.equalsIgnoreCase("")) {
				try {
					final org.aestia.object.Object obj = World.getObjet(Integer.parseInt(str));
					synchronized (this._items) {
						if (obj != null) {
							this._items.put(obj.getGuid(), obj);
						}
					}
					// monitorexit(this._items)
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	public void startFecundo() {
		if (this._fecundatedAgo < 120 && this._fecundatedAgo > 0) {
			this._upFecundo.restart();
		} else {
			this._upFecundo.stop();
		}
	}

	public int getId() {
		return this._id;
	}

	public String getName() {
		return this._name;
	}

	public void setName(final String n) {
		this._name = n;
	}

	public int getColor() {
		return this._color;
	}

	public int getSexe() {
		return this._sexe;
	}

	public int get_amour() {
		return this._amour;
	}

	public String get_ancetres() {
		return this._ancetres;
	}

	public int get_endurance() {
		return this._endurance;
	}

	public void setPerso(final int perso) {
		this._owner = perso;
	}

	public int getLvl() {
		return this._level;
	}

	public long getExp() {
		return this._exp;
	}

	public int getPerso() {
		return this._owner;
	}

	public boolean isRaising() {
		return this._cellId != -1;
	}

	public void actCapacidades() {
		this._capacity.clear();
		String[] split;
		for (int length = (split = this._ability.split(",", 2)).length, i = 0; i < length; ++i) {
			final String s = split[i];
			if (s != null) {
				final int a = Integer.parseInt(s);
				try {
					this._capacity.add(a);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int getFecundatedAgo() {
		if (this._reprod == -1 || this._reprod >= 20) {
			return this._fecundatedAgo = -1;
		}
		if (this._fecundatedAgo >= 1) {
			return this._fecundatedAgo;
		}
		return this._fecundatedAgo = -1;
	}

	public void setAbility(final String a) {
		this._ability = a;
		this.actCapacidades();
	}

	public int getFecunda() {
		if (this._reprod == -1) {
			return 0;
		}
		if (this._amour >= 7500 && this._endurance >= 7500) {
			return 10;
		}
		return 0;
	}

	public void setMapCell(final short map, final int cell) {
		this._mapId = map;
		this._cellId = cell;
	}

	public int getFatigue() {
		return this._fatigue;
	}

	public short getMap() {
		return this._mapId;
	}

	public int getCell() {
		return this._cellId;
	}

	public int getSize() {
		return this._size;
	}

	public int getEnergie() {
		return this._energie;
	}

	public int getReprod() {
		return this._reprod;
	}

	public int getMaturite() {
		return this._maturite;
	}

	public int getSerenite() {
		if (this._serenite > 10000) {
			this._serenite = 10000;
		}
		return this._serenite;
	}

	public Stats getStats() {
		return this._stats;
	}

	public Map<Integer, org.aestia.object.Object> getItems() {
		return this._items;
	}

	public List<Integer> getCapacites() {
		return this._capacity;
	}

	public String parseToDinde() {
		final StringBuilder str = new StringBuilder();
		str.append(this._id).append(":").append(this._color).append(":").append(this._ancetres).append(":").append(",,")
				.append(this._ability).append(":").append(this._name).append(":").append(this._sexe).append(":")
				.append(this.parseXpString()).append(":").append(this._level).append(":").append(this.isMontable())
				.append(":").append(this.getTotalPod()).append(":").append(this.IsSauvage()).append(":")
				.append(this._endurance).append(",10000:").append(this._maturite).append(",")
				.append(this.getMaxMaturite()).append(":").append(this._energie).append(",")
				.append(this.getMaxEnergie()).append(":").append(this._serenite).append(",-10000,10000:")
				.append(this._amour).append(",10000:").append(this.getFecundatedAgo()).append(":")
				.append(this.getFecunda()).append(":").append(this.convertirStringAStats()).append(":")
				.append(this._fatigue).append(",240:").append(this._reprod).append(",20:");
		return str.toString();
	}

	public int IsSauvage() {
		return this._isSauvage;
	}

	public void setCastred() {
		this._reprod = -1;
	}

	private String convertirStringAStats() {
		final StringBuilder stats = new StringBuilder();
		for (final Map.Entry<Integer, Integer> entry : this._stats.getMap().entrySet()) {
			if (entry.getValue() <= 0) {
				continue;
			}
			if (stats.length() > 0) {
				stats.append(",");
			}
			stats.append(Integer.toHexString(entry.getKey())).append("#").append(Integer.toHexString(entry.getValue()))
					.append("#0#0");
		}
		return stats.toString();
	}

	private int getMaxEnergie() {
		return 10 * this._level + 150 * Constant.getGeneration(this._color);
	}

	private int getMaxMaturite() {
		return 1500 * Constant.getGeneration(this._color);
	}

	private String parseXpString() {
		final StringBuilder str = new StringBuilder();
		str.append(this._exp).append(",").append(World.getExpLevel(this._level).dinde).append(",")
				.append(World.getExpLevel(this._level + 1).dinde);
		return str.toString();
	}

	public int isMontable() {
		if (this._maturite < this.getMaxMaturite() || this._fatigue == 240 || this._isSauvage == 1) {
			return 0;
		}
		return 1;
	}

	public void aumFatige() {
		++this._fatigue;
		if (this._fatigue > 240) {
			this._fatigue = 240;
		}
	}

	public void aumResistence(final int Resist) {
		this._endurance += (int) (Resist / 100 * this.getBonusFatigue(this._fatigue));
		if (this._capacity.contains(5)) {
			++this._endurance;
		}
		if (this._endurance > 10000) {
			this._endurance = 10000;
		}
	}

	public void setAmour(final int amor) {
		this._amour = amor;
	}

	public void setResistence(final int resistencia) {
		this._endurance = resistencia;
	}

	public void setMaxMaturite() {
		this._maturite = this.getMaxMaturite();
	}

	public void setMaxEnergie() {
		this._energie = this.getMaxEnergie();
	}

	public void aumMaduturite(final int Resist) {
		final int maxMadurez = this.getMaxMaturite();
		if (this._maturite < maxMadurez) {
			this._maturite += (int) (Resist / 100 * this.getBonusFatigue(this._fatigue));
			if (this._capacity.contains(7)) {
				this._maturite += Resist / 100;
			}
			if (this._size < 100) {
				final org.aestia.map.Map map = World.getMap(this._mapId);
				if (maxMadurez / this._maturite <= 1) {
					this._size = 100;
					SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(map, this._id);
					SocketManager.GAME_SEND_GM_MOUNT_TO_MAP(map, this);
					return;
				}
				if (this._size < 75 && maxMadurez / this._maturite == 2) {
					this._size = 75;
					SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(map, this._id);
					SocketManager.GAME_SEND_GM_MOUNT_TO_MAP(map, this);
					return;
				}
				if (this._size < 50 && maxMadurez / this._maturite == 3) {
					this._size = 50;
					SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(map, this._id);
					SocketManager.GAME_SEND_GM_MOUNT_TO_MAP(map, this);
					return;
				}
			}
		}
		if (this._maturite > maxMadurez) {
			this._maturite = maxMadurez;
		}
	}

	public void aumAmour(final int Resist) {
		this._amour += (int) (Resist / 100 * this.getBonusFatigue(this._fatigue));
		if (this._amour > 10000) {
			this._amour = 10000;
		}
	}

	public void aumSerenite(final int Resist) {
		this._serenite += (int) (Resist / 100 * this.getBonusFatigue(this._fatigue));
		if (this._serenite > 10000) {
			this._serenite = 10000;
		}
	}

	public void resSerenite(final int Resist) {
		this._serenite -= Resist / 100;
		if (this._serenite < -10000) {
			this._serenite = -10000;
		}
	}

	public void SereniteMale() {
		this._serenite -= 2;
		if (this._serenite < -10000) {
			this._serenite = -10000;
		}
	}

	public void SereniteFemelle() {
		this._serenite += 2;
		if (this._serenite < -10000) {
			this._serenite = -10000;
		}
	}

	public void aumEnergie(final int Resist) {
		this._energie += Resist / 500;
		if (this._capacity.contains(1)) {
			this._energie += Resist / 500;
		}
		final int maxEnergia = this.getMaxEnergie();
		if (this._energie > maxEnergia) {
			this._energie = maxEnergia;
		}
	}

	public void aumEnergie(final int valor, final int veces) {
		this._energie += valor * veces;
		final int maxEnergia = this.getMaxEnergie();
		if (this._energie > maxEnergia) {
			this._energie = maxEnergia;
		}
	}

	public void resFatige() {
		this._fatigue -= 20;
		if (this._fatigue < 0) {
			this._fatigue = 0;
		}
	}

	public void resAmour(final int amor) {
		this._amour -= amor;
		if (this._amour < 0) {
			this._amour = 0;
		}
	}

	public void resResistence(final int resistencia) {
		this._endurance -= resistencia;
		if (this._endurance < 0) {
			this._endurance = 0;
		}
	}

	public void aumReprodution() {
		if (this._reprod == -1) {
			return;
		}
		++this._reprod;
	}

	public String getItemsId() {
		String str = "";
		synchronized (this._items) {
			for (final org.aestia.object.Object obj : this._items.values()) {
				str = String.valueOf(str) + ((str.length() > 0) ? ";" : "") + obj.getGuid();
			}
		}
		// monitorexit(this._items)
		return str;
	}

	public void addXp(final long amount) {
		this._exp += amount;
		while (this._exp >= World.getExpLevel(this._level + 1).dinde && this._level < 100) {
			this.addLvl();
		}
	}

	public void addLvl() {
		++this._level;
		this._stats = Constant.getMountStats(this._color, this._level);
	}

	public String getStringColor(final String colorpersoPavo) {
		String b = "";
		if (this._capacity.contains(9)) {
			b = "," + colorpersoPavo;
		}
		if (this._color == 75) {
			final int colorRandom = Formulas.getRandomValue(1, 87);
			b = "," + Constant.getStringColorDragodinde(colorRandom);
		}
		return String.valueOf(this._color) + b;
	}

	public String getAbility() {
		return this._ability;
	}

	public boolean addCapacite(final String capa) {
		int c = 0;
		String[] split;
		for (int length = (split = capa.split(",", 2)).length, i = 0; i < length; ++i) {
			final String s = split[i];
			if (this._capacity.size() >= 2) {
				return false;
			}
			try {
				c = Integer.parseInt(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (c != 0) {
				this._capacity.add(c);
			}
			if (this._capacity.size() == 1) {
				this._ability = this._capacity.get(0) + ",";
			} else {
				this._ability = this._capacity.get(0) + "," + this._capacity.get(1);
			}
		}
		return true;
	}

	public void setEnergie(final int e) {
		this._energie = e;
	}

	public int getCouple() {
		return this._couple;
	}

	public void setCouple(final int c) {
		this._couple = c;
	}

	public int getOrientacion() {
		return this._orientation;
	}

	public void setFecundadaHace(final int f) {
		if (this._reprod == -1) {
			return;
		}
		this._fecundatedAgo = f;
	}

	public boolean isCastrated() {
		return this._reprod == -1;
	}

	public synchronized String getRaisingMount(final MountPark park) {
		final StringBuilder str = new StringBuilder();
		str.append("GM|+");
		if (this._cellId == -1 && this._mapId == -1) {
			str.append(park.getPlaceOfSpawn()).append(";");
		} else {
			str.append(this._cellId).append(";");
		}
		str.append(this._orientation).append(";0;").append(this._id).append(";").append(this._name).append(";-9;");
		if (this._color == 88) {
			str.append(7005);
		} else {
			str.append(7002);
		}
		str.append("^").append(this._size).append(";");
		if (World.getPersonnage(this._owner) == null) {
			str.append("Sans Maitre");
		} else {
			str.append(World.getPersonnage(this._owner).getName());
		}
		str.append(";").append(this._level).append(";").append(this._color);
		return str.toString();
	}

	public synchronized void moveMounts(final Player perso, final int cellules, final boolean remove) {
		int action = 0;
		if (perso == null) {
			return;
		}
		if (perso.getCurCell().getId() == this._cellId) {
			return;
		}
		String path = "";
		final org.aestia.map.Map map = perso.getCurMap();
		if (map.getMountPark() == null) {
			return;
		}
		final MountPark MP = map.getMountPark();
		final int azar = Formulas.getRandomValue(1, 10);
		char dir = Pathfinding.getDirEntreDosCeldas(map, this._cellId, perso.getCurCell().getId());
		if (remove) {
			dir = Pathfinding.getOpositeDirection(dir);
		}
		int cell = this._cellId;
		int cellTest = this._cellId;
		int i = 0;
		while (i < cellules) {
			cellTest = Pathfinding.GetCaseIDFromDirrection(cellTest, dir, map.getMountPark().getMap(), false);
			if (map.getCase(cellTest) == null) {
				return;
			}
			if (MP.getCellAndObject().containsKey(cellTest) && this._fatigue < 240) {
				final int item = MP.getCellAndObject().get(cellTest);
				if (item == 7755 || item == 7756 || item == 7757 || item == 7758 || item == 7759 || item == 7760
						|| item == 7761 || item == 7762 || item == 7763 || item == 7764 || item == 7765 || item == 7766
						|| item == 7767 || item == 7768 || item == 7769 || item == 7770 || item == 7771 || item == 7772
						|| item == 7773 || item == 7774 || item == 7625 || item == 7626 || item == 7627
						|| item == 7629) {
					this.resSerenite(org.aestia.map.Map.getObjResist(perso, cellTest, item));
					if (this._sexe == 0) {
						this.SereniteMale();
					} else {
						this.SereniteFemelle();
					}
					this.aumFatige();
				} else if (item == 7775 || item == 7776 || item == 7777 || item == 7778 || item == 7779 || item == 7780
						|| item == 7781 || item == 7782 || item == 7783 || item == 7784 || item == 7785 || item == 7786
						|| item == 7787 || item == 7788 || item == 7789 || item == 7790 || item == 7791 || item == 7792
						|| item == 7793 || item == 7794 || item == 7795 || item == 7796 || item == 7797
						|| item == 7798) {
					if (this._serenite < 0) {
						this.aumResistence(org.aestia.map.Map.getObjResist(perso, cellTest, item));
					}
					if (this._sexe == 0) {
						this.SereniteMale();
					} else {
						this.SereniteFemelle();
					}
					this.aumFatige();
				} else if (item == 7606 || item == 7607 || item == 7608 || item == 7609 || item == 7610 || item == 7611
						|| item == 7612 || item == 7613 || item == 7614 || item == 7615 || item == 7616 || item == 7617
						|| item == 7618 || item == 7619 || item == 7620 || item == 7621 || item == 7683 || item == 7684
						|| item == 7685 || item == 7686 || item == 7687 || item == 7688 || item == 7689
						|| item == 7690) {
					this.resFatige();
					this.aumEnergie(org.aestia.map.Map.getObjResist(perso, cellTest, item));
					if (this._sexe == 0) {
						this.SereniteMale();
					} else {
						this.SereniteFemelle();
					}
				} else if (item == 7634 || item == 7635 || item == 7636 || item == 7637 || item == 7691 || item == 7692
						|| item == 7693 || item == 7694 || item == 7695 || item == 7696 || item == 7697 || item == 7698
						|| item == 7699 || item == 7700) {
					if (this._serenite > 0) {
						this.aumAmour(org.aestia.map.Map.getObjResist(perso, cellTest, item));
					}
					if (this._sexe == 0) {
						this.SereniteMale();
					} else {
						this.SereniteFemelle();
					}
					this.aumFatige();
				} else if (item == 7628 || item == 7622 || item == 7623 || item == 7624 || item == 7733 || item == 7734
						|| item == 7735 || item == 7736 || item == 7737 || item == 7738 || item == 7739 || item == 7740
						|| item == 7741 || item == 7742 || item == 7743 || item == 7744 || item == 7745
						|| item == 7746) {
					this.aumSerenite(org.aestia.map.Map.getObjResist(perso, cellTest, item));
					if (this._sexe == 0) {
						this.SereniteMale();
					} else {
						this.SereniteFemelle();
					}
					this.aumFatige();
				} else if (item == 7590 || item == 7591 || item == 7592 || item == 7593 || item == 7594 || item == 7595
						|| item == 7596 || item == 7597 || item == 7598 || item == 7599 || item == 7600 || item == 7601
						|| item == 7602 || item == 7603 || item == 7604 || item == 7605 || item == 7673 || item == 7674
						|| item == 7675 || item == 7676 || item == 7677 || item == 7678 || item == 7679
						|| item == 7682) {
					if (this._serenite <= 2000 && this._serenite >= -2000) {
						this.aumMaduturite(org.aestia.map.Map.getObjResist(perso, cellTest, item));
					}
					if (this._sexe == 0) {
						this.SereniteMale();
					} else {
						this.SereniteFemelle();
					}
					this.aumFatige();
				}
				if (item == 7590 || item == 7591 || item == 7592 || item == 7593 || item == 7594 || item == 7595
						|| item == 7596 || item == 7597 || item == 7598 || item == 7599 || item == 7600 || item == 7601
						|| item == 7602 || item == 7603 || item == 7604 || item == 7605 || item == 7673 || item == 7674
						|| item == 7675 || item == 7676 || item == 7677 || item == 7678 || item == 7679 || item == 7682
						|| item == 7606 || item == 7607 || item == 7608 || item == 7609 || item == 7610 || item == 7611
						|| item == 7612 || item == 7613 || item == 7614 || item == 7615 || item == 7616 || item == 7617
						|| item == 7618 || item == 7619 || item == 7620 || item == 7621 || item == 7683 || item == 7684
						|| item == 7685 || item == 7686 || item == 7687 || item == 7688 || item == 7689 || item == 7690
						|| item == 7755 || item == 7756 || item == 7757 || item == 7758 || item == 7759 || item == 7760
						|| item == 7761 || item == 7762 || item == 7763 || item == 7764 || item == 7765 || item == 7766
						|| item == 7767 || item == 7768 || item == 7769 || item == 7770 || item == 7771 || item == 7772
						|| item == 7773 || item == 7774 || item == 7625 || item == 7626 || item == 7627 || item == 7629
						|| item == 7628 || item == 7622 || item == 7623 || item == 7624 || item == 7733 || item == 7734
						|| item == 7735 || item == 7736 || item == 7737 || item == 7738 || item == 7739 || item == 7740
						|| item == 7741 || item == 7742 || item == 7743 || item == 7744 || item == 7745 || item == 7746
						|| item == 7634 || item == 7635 || item == 7636 || item == 7637 || item == 7691 || item == 7692
						|| item == 7693 || item == 7694 || item == 7695 || item == 7696 || item == 7697 || item == 7698
						|| item == 7699 || item == 7700 || item == 7775 || item == 7776 || item == 7777 || item == 7778
						|| item == 7779 || item == 7780 || item == 7781 || item == 7782 || item == 7783 || item == 7784
						|| item == 7785 || item == 7786 || item == 7787 || item == 7788 || item == 7789 || item == 7790
						|| item == 7791 || item == 7792 || item == 7793 || item == 7794 || item == 7795 || item == 7796
						|| item == 7797 || item == 7798) {
					break;
				}
				final StringBuilder mess = new StringBuilder();
				mess.append("L'itemID ").append(item).append("non reconnu.")
						.append("\n RDV dans Objects -> Dragodinde.Java -> moverMontura (ligne 669)");
				Console.println("Dragodinde.java : " + mess.toString(), Console.Color.INFORMATION);
				if (Main.modDebug) {
					GameServer.addToLog(mess.toString());
					break;
				}
				break;
			} else {
				if (!map.getCase(cellTest).isWalkable(false) || MP.getDoor() == cellTest
						|| map.cellSide(cell, cellTest)) {
					break;
				}
				cell = cellTest;
				path = String.valueOf(path) + dir + CryptManager.cellID_To_Code(cell);
				++i;
			}
		}
		if (cell == this._cellId) {
			this._orientation = CryptManager.getIntByHashedValue(dir);
			SocketManager.GAME_SEND_eD_PACKET_TO_MAP(map, this._id, this._orientation);
			SocketManager.SEND_GDE_FRAME_OBJECT_EXTERNAL(map, String.valueOf(cellTest) + ";4");
			SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(map, this._id, action);
			return;
		}
		if (azar == 5) {
			action = 8;
		}
		if (MP.getListOfRaising().size() > 1) {
			for (final Integer pavo : MP.getListOfRaising()) {
				final Dragodinde dd = World.getDragoByID(pavo);
				if (dd._sexe != this._sexe && dd.getFecunda() != 0 && this.getFecunda() != 0 && dd.getCell() == cellTest
						&& dd._reprod < 20 && this._reprod < 20 && !dd.isCastrated() && !this.isCastrated()) {
					final int aparearce = 3;
					if (aparearce == 3) {
						if (dd._sexe == 1) {
							dd._fecundatedAgo = 1;
							dd._upFecundo.start();
							dd.setCouple(this._id);
							this.resAmour(7500);
							this.resResistence(7500);
						} else if (this._sexe == 1) {
							this._fecundatedAgo = 1;
							this._upFecundo.start();
							this._couple = dd.getId();
							dd.resAmour(7500);
							dd.resResistence(7500);
						}
						action = 4;
						break;
					}
					continue;
				}
			}
		}
		SocketManager.GAME_SEND_GA_PACKET_TO_MAP(map, "0", 1, new StringBuilder(String.valueOf(this._id)).toString(),
				"a" + CryptManager.cellID_To_Code(this._cellId) + path);
		this._cellId = cell;
		this._orientation = CryptManager.getIntByHashedValue(dir);
		final int ID = this._id;
		final int cellule = cellTest;
		this.waiter.addNext(new Runnable() {
			@Override
			public void run() {
				SocketManager.SEND_GDE_FRAME_OBJECT_EXTERNAL(map, String.valueOf(cellule) + ";4");
			}
		}, 1250L);
		final int _action = action;
		this.waiter.addNext(new Runnable() {
			@Override
			public void run() {
				SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(map, ID, _action);
			}
		}, 500L);
	}

	public synchronized void moveMountsAuto(final char direction, final int cellules, final boolean remove) {
		int action = 0;
		String path = "";
		final org.aestia.map.Map map = World.getMap(this._mapId);
		if (map == null) {
			return;
		}
		if (map.getMountPark() == null) {
			return;
		}
		final MountPark MP = map.getMountPark();
		final int random = Formulas.getRandomValue(1, 10);
		int cell = this._cellId;
		int cellTest = this._cellId;
		int i = 0;
		while (i < cellules) {
			cellTest = Pathfinding.getCellArroundByDir(cellTest, direction, World.getMap(this._mapId));
			if (map.getCase(cellTest) == null) {
				return;
			}
			if (MP.getCellAndObject().containsKey(cellTest) && this._fatigue >= 240) {
				break;
			}
			if (MP.getCellAndObject().containsKey(cellTest) && this._fatigue < 240) {
				final int item = MP.getCellAndObject().get(cellTest);
				if (item == 7755 || item == 7756 || item == 7757 || item == 7758 || item == 7759 || item == 7760
						|| item == 7761 || item == 7762 || item == 7763 || item == 7764 || item == 7765 || item == 7766
						|| item == 7767 || item == 7768 || item == 7769 || item == 7770 || item == 7771 || item == 7772
						|| item == 7773 || item == 7774 || item == 7625 || item == 7626 || item == 7627
						|| item == 7629) {
					this.resSerenite(org.aestia.map.Map.getObjResist(MP, cellTest, item));
					if (this._sexe == 0) {
						this.SereniteMale();
					} else {
						this.SereniteFemelle();
					}
					this.aumFatige();
					break;
				}
				if (item == 7775 || item == 7776 || item == 7777 || item == 7778 || item == 7779 || item == 7780
						|| item == 7781 || item == 7782 || item == 7783 || item == 7784 || item == 7785 || item == 7786
						|| item == 7787 || item == 7788 || item == 7789 || item == 7790 || item == 7791 || item == 7792
						|| item == 7793 || item == 7794 || item == 7795 || item == 7796 || item == 7797
						|| item == 7798) {
					if (this._serenite < 0) {
						this.aumResistence(org.aestia.map.Map.getObjResist(MP, cellTest, item));
					}
					if (this._sexe == 0) {
						this.SereniteMale();
					} else {
						this.SereniteFemelle();
					}
					this.aumFatige();
					break;
				}
				if (item == 7606 || item == 7607 || item == 7608 || item == 7609 || item == 7610 || item == 7611
						|| item == 7612 || item == 7613 || item == 7614 || item == 7615 || item == 7616 || item == 7617
						|| item == 7618 || item == 7619 || item == 7620 || item == 7621 || item == 7683 || item == 7684
						|| item == 7685 || item == 7686 || item == 7687 || item == 7688 || item == 7689
						|| item == 7690) {
					this.resFatige();
					this.aumEnergie(org.aestia.map.Map.getObjResist(MP, cellTest, item));
					if (this._sexe == 0) {
						this.SereniteMale();
						break;
					}
					this.SereniteFemelle();
					break;
				} else {
					if (item == 7634 || item == 7635 || item == 7636 || item == 7637 || item == 7691 || item == 7692
							|| item == 7693 || item == 7694 || item == 7695 || item == 7696 || item == 7697
							|| item == 7698 || item == 7699 || item == 7700) {
						if (this._serenite > 0) {
							this.aumAmour(org.aestia.map.Map.getObjResist(MP, cellTest, item));
						}
						if (this._sexe == 0) {
							this.SereniteMale();
						} else {
							this.SereniteFemelle();
						}
						this.aumFatige();
						break;
					}
					if (item == 7628 || item == 7622 || item == 7623 || item == 7624 || item == 7733 || item == 7734
							|| item == 7735 || item == 7736 || item == 7737 || item == 7738 || item == 7739
							|| item == 7740 || item == 7741 || item == 7742 || item == 7743 || item == 7744
							|| item == 7745 || item == 7746) {
						this.aumSerenite(org.aestia.map.Map.getObjResist(MP, cellTest, item));
						if (this._sexe == 0) {
							this.SereniteMale();
						} else {
							this.SereniteFemelle();
						}
						this.aumFatige();
						break;
					}
					if (item == 7590 || item == 7591 || item == 7592 || item == 7593 || item == 7594 || item == 7595
							|| item == 7596 || item == 7597 || item == 7598 || item == 7599 || item == 7600
							|| item == 7601 || item == 7602 || item == 7603 || item == 7604 || item == 7605
							|| item == 7673 || item == 7674 || item == 7675 || item == 7676 || item == 7677
							|| item == 7678 || item == 7679 || item == 7682) {
						if (this._serenite <= 2000 && this._serenite >= -2000) {
							this.aumMaduturite(org.aestia.map.Map.getObjResist(MP, cellTest, item));
						}
						if (this._sexe == 0) {
							this.SereniteMale();
						} else {
							this.SereniteFemelle();
						}
						this.aumFatige();
						break;
					}
					break;
				}
			} else {
				if (!map.getCase(cellTest).isWalkable(false) || MP.getDoor() == cellTest
						|| map.cellSide(cell, cellTest)) {
					break;
				}
				cell = cellTest;
				path = String.valueOf(path) + direction + CryptManager.cellID_To_Code(cell);
				++i;
			}
		}
		if (cell == this._cellId) {
			this._orientation = CryptManager.getIntByHashedValue(direction);
			SocketManager.GAME_SEND_eD_PACKET_TO_MAP(map, this._id, this._orientation);
			SocketManager.SEND_GDE_FRAME_OBJECT_EXTERNAL(map, String.valueOf(cellTest) + ";4");
			SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(map, this._id, action);
			return;
		}
		if (random == 5) {
			action = 8;
		}
		if (MP.getListOfRaising().size() > 1) {
			for (final Integer pavo : MP.getListOfRaising()) {
				final Dragodinde dd = World.getDragoByID(pavo);
				if (dd._sexe != this._sexe && dd.getFecunda() != 0 && this.getFecunda() != 0 && dd.getCell() == cellTest
						&& dd._reprod < 20 && this._reprod < 20 && !dd.isCastrated() && !this.isCastrated()) {
					int aparearce = Formulas.getRandomValue(2, 4);
					if (dd._capacity.contains(6) || this._capacity.contains(6)) {
						aparearce = 3;
					}
					if (aparearce == 3) {
						if (dd._sexe == 1) {
							dd._fecundatedAgo = 1;
							dd._upFecundo.start();
							dd.setCouple(this._id);
							this.resAmour(7500);
							this.resResistence(7500);
						} else if (this._sexe == 1) {
							this._fecundatedAgo = 1;
							this._upFecundo.start();
							this._couple = dd.getId();
							dd.resAmour(7500);
							dd.resResistence(7500);
						}
						action = 4;
						break;
					}
					continue;
				}
			}
		}
		SocketManager.GAME_SEND_GA_ACTION_TO_MAP(map, "0", 1, new StringBuilder(String.valueOf(this._id)).toString(),
				"a" + CryptManager.cellID_To_Code(this._cellId) + path);
		this._cellId = cell;
		this._orientation = CryptManager.getIntByHashedValue(direction);
		final int ID = this._id;
		final int cellule = cellTest;
		this.waiter.addNext(new Runnable() {
			@Override
			public void run() {
				SocketManager.SEND_GDE_FRAME_OBJECT_EXTERNAL(map, String.valueOf(cellule) + ";4");
			}
		}, 1250L);
		final int _action = action;
		this.waiter.addNext(new Runnable() {
			@Override
			public void run() {
				SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(map, ID, _action);
			}
		}, 500L);
	}

	public int minCalving() {
		if (this._reprod == 0) {
			return 1;
		}
		if (this._reprod < 5) {
			return 2;
		}
		if (this._reprod < 11) {
			return 3;
		}
		if (this._reprod <= 20) {
			return 4;
		}
		return 1;
	}

	public int get_podsActuels() {
		int pods = 0;
		synchronized (this._items) {
			for (final org.aestia.object.Object value : this._items.values()) {
				pods += value.getTemplate().getPod() * value.getQuantity();
			}
		}
		// monitorexit(this._items)
		return pods;
	}

	public int getMaxPod() {
		return this.getTotalPod();
	}

	public int getTotalPod() {
		int habilidad = 0;
		if (this._capacity.contains(2)) {
			habilidad = 20 * this._level;
		}
		return 10 * this._level + (100 * Constant.getGeneration(this._color) + habilidad);
	}

	public void addInDinde(final int guid, final int qua, final Player P) {
		if (qua <= 0) {
			return;
		}
		final org.aestia.object.Object PersoObj = World.getObjet(guid);
		if (PersoObj == null) {
			return;
		}
		if (P.getItems().get(guid) == null) {
			return;
		}
		String str = "";
		if (PersoObj.getPosition() != -1) {
			return;
		}
		org.aestia.object.Object TrunkObj = this.getSimilarDindeItem(PersoObj);
		final int newQua = PersoObj.getQuantity() - qua;
		if (TrunkObj == null) {
			if (newQua <= 0) {
				P.removeItem(PersoObj.getGuid());
				synchronized (this._items) {
					this._items.put(PersoObj.getGuid(), PersoObj);
				}
				// monitorexit(this._items)
				str = "O+" + PersoObj.getGuid() + "|" + PersoObj.getQuantity() + "|" + PersoObj.getTemplate().getId()
						+ "|" + PersoObj.parseStatsString();
				SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(P, guid);
			} else {
				PersoObj.setQuantity(newQua);
				TrunkObj = org.aestia.object.Object.getCloneObjet(PersoObj, qua);
				World.addObjet(TrunkObj, true);
				synchronized (this._items) {
					this._items.put(TrunkObj.getGuid(), TrunkObj);
				}
				// monitorexit(this._items)
				str = "O+" + TrunkObj.getGuid() + "|" + TrunkObj.getQuantity() + "|" + TrunkObj.getTemplate().getId()
						+ "|" + TrunkObj.parseStatsString();
				SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(P, PersoObj);
			}
		} else if (newQua <= 0) {
			P.removeItem(PersoObj.getGuid());
			World.removeItem(PersoObj.getGuid());
			TrunkObj.setQuantity(TrunkObj.getQuantity() + PersoObj.getQuantity());
			str = "O+" + TrunkObj.getGuid() + "|" + TrunkObj.getQuantity() + "|" + TrunkObj.getTemplate().getId() + "|"
					+ TrunkObj.parseStatsString();
			SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(P, guid);
		} else {
			PersoObj.setQuantity(newQua);
			TrunkObj.setQuantity(TrunkObj.getQuantity() + qua);
			str = "O+" + TrunkObj.getGuid() + "|" + TrunkObj.getQuantity() + "|" + TrunkObj.getTemplate().getId() + "|"
					+ TrunkObj.parseStatsString();
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(P, PersoObj);
		}
		for (final Player perso : P.getCurMap().getPersos()) {
			if (perso.getInTrunk() != null && this._id == perso.getInTrunk().getId()) {
				SocketManager.GAME_SEND_EsK_PACKET(perso, str);
			}
		}
		SocketManager.GAME_SEND_Ow_PACKET(P);
		SocketManager.GAME_SEND_Ew_PACKET(P, this.get_podsActuels(), this.getTotalPod());
		SocketManager.GAME_SEND_EL_MOUNT_PACKET(P, this);
		Database.getStatique().getMounts_dataData().update(this, true);
	}

	public void removeFromDinde(final int guid, final int qua, final Player P) {
		if (qua <= 0) {
			return;
		}
		final org.aestia.object.Object TrunkObj = World.getObjet(guid);
		synchronized (this._items) {
			if (this._items.get(guid) == null) {
				// monitorexit(this._items)
				return;
			}
		}
		// monitorexit(this._items)
		org.aestia.object.Object PersoObj = P.getSimilarItem(TrunkObj);
		String str = "";
		final int newQua = TrunkObj.getQuantity() - qua;
		if (PersoObj == null) {
			if (newQua <= 0) {
				synchronized (this._items) {
					this._items.remove(guid);
				}
				// monitorexit(this._items)
				synchronized (P.getItems()) {
					P.getItems().put(guid, TrunkObj);
				}
				// monitorexit(P.getItems())
				SocketManager.GAME_SEND_OAKO_PACKET(P, TrunkObj);
				SocketManager.GAME_SEND_Ew_PACKET(P, this.get_podsActuels(), this.getTotalPod());
				str = "O-" + guid;
			} else {
				PersoObj = org.aestia.object.Object.getCloneObjet(TrunkObj, qua);
				World.addObjet(PersoObj, true);
				TrunkObj.setQuantity(newQua);
				synchronized (P.getItems()) {
					P.getItems().put(PersoObj.getGuid(), PersoObj);
				}
				// monitorexit(P.getItems())
				SocketManager.GAME_SEND_OAKO_PACKET(P, PersoObj);
				SocketManager.GAME_SEND_Ew_PACKET(P, this.get_podsActuels(), this.getTotalPod());
				str = "O+" + TrunkObj.getGuid() + "|" + TrunkObj.getQuantity() + "|" + TrunkObj.getTemplate().getId()
						+ "|" + TrunkObj.parseStatsString();
			}
		} else if (newQua <= 0) {
			synchronized (this._items) {
				this._items.remove(TrunkObj.getGuid());
			}
			// monitorexit(this._items)
			World.removeItem(TrunkObj.getGuid());
			PersoObj.setQuantity(PersoObj.getQuantity() + TrunkObj.getQuantity());
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(P, PersoObj);
			SocketManager.GAME_SEND_Ew_PACKET(P, this.get_podsActuels(), this.getTotalPod());
			str = "O-" + guid;
		} else {
			TrunkObj.setQuantity(newQua);
			PersoObj.setQuantity(PersoObj.getQuantity() + qua);
			SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(P, PersoObj);
			SocketManager.GAME_SEND_Ew_PACKET(P, this.get_podsActuels(), this.getTotalPod());
			str = "O+" + TrunkObj.getGuid() + "|" + TrunkObj.getQuantity() + "|" + TrunkObj.getTemplate().getId() + "|"
					+ TrunkObj.parseStatsString();
		}
		SocketManager.GAME_SEND_EsK_PACKET(P, str);
		SocketManager.GAME_SEND_Ow_PACKET(P);
	}

	private org.aestia.object.Object getSimilarDindeItem(final org.aestia.object.Object obj) {
		synchronized (this._items) {
			for (final org.aestia.object.Object value : this._items.values()) {
				if (value.getTemplate().getType() == 85) {
					continue;
				}
				if (value.getTemplate().getId() == obj.getTemplate().getId()
						&& value.getStats().isSameStats(obj.getStats())) {
					// monitorexit(this._items)
					return value;
				}
			}
		}
		// monitorexit(this._items)
		return null;
	}

	public String parseToDindeItem() {
		final StringBuilder packet = new StringBuilder();
		synchronized (this._items) {
			for (final org.aestia.object.Object obj : this._items.values()) {
				packet.append("O").append(obj.parseItem()).append(";");
			}
		}
		// monitorexit(this._items)
		return packet.toString();
	}

	private double getBonusFatigue(final int energie) {
		if (energie > 160 && energie <= 170) {
			return 1.15;
		}
		if (energie > 170 && energie <= 180) {
			return 1.3;
		}
		if (energie > 180 && energie <= 200) {
			return 1.5;
		}
		if (energie > 200 && energie <= 210) {
			return 1.8;
		}
		if (energie > 210 && energie <= 220) {
			return 2.1;
		}
		if (energie > 220 && energie <= 230) {
			return 2.5;
		}
		if (energie > 230 && energie <= 239) {
			return 3.0;
		}
		return 1.0;
	}

	static /* synthetic */ void access$1(final Dragodinde dragodinde, final int fecundatedAgo) {
		dragodinde._fecundatedAgo = fecundatedAgo;
	}

	static /* synthetic */ void access$5(final Dragodinde dragodinde, final int fatigue) {
		dragodinde._fatigue = fatigue;
	}
}

// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.map;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.aestia.common.Formulas;
import org.aestia.entity.Dragodinde;
import org.aestia.game.scheduler.TimerWaiter;
import org.aestia.game.world.World;
import org.aestia.other.Guild;

public class MountPark {
	private int owner;
	private int size;
	private Guild guild;
	private Map map;
	private int cell;
	private int price;
	private int placeOfSpawn;
	private int maxObject;
	private int door;
	private ArrayList<Integer> cellOfObject;
	private java.util.Map<Integer, Integer> cellAndObject;
	private java.util.Map<Integer, java.util.Map<Integer, Integer>> objDurab;
	private java.util.Map<Integer, java.util.Map<Integer, Integer>> breedingObject;
	private CopyOnWriteArrayList<Integer> raising;
	private ArrayList<Dragodinde> etable;
	private TimerWaiter waiter;

	public MountPark(final Map map, final int cell, final int size, final int placeOfSpawn, final int door,
			final String cellOfObject, final int maxObject) {
		this.cell = -1;
		this.cellOfObject = new ArrayList<Integer>();
		this.cellAndObject = new TreeMap<Integer, Integer>();
		this.objDurab = new TreeMap<Integer, java.util.Map<Integer, Integer>>();
		this.breedingObject = new TreeMap<Integer, java.util.Map<Integer, Integer>>();
		this.raising = new CopyOnWriteArrayList<Integer>();
		this.etable = new ArrayList<Dragodinde>();
		this.waiter = new TimerWaiter();
		this.size = size;
		this.map = map;
		this.cell = cell;
		this.placeOfSpawn = placeOfSpawn;
		this.door = door;
		this.maxObject = maxObject;
		if (!cellOfObject.isEmpty()) {
			String[] split;
			for (int length = (split = cellOfObject.split(";")).length, i = 0; i < length; ++i) {
				final String cases = split[i];
				final int cellId = Integer.parseInt(cases);
				if (cellId > 0) {
					this.cellOfObject.add(cellId);
				}
			}
		}
		if (this.map != null) {
			this.map.setMountPark(this);
		}
	}

	public void setDataParse(final String data) {
		this.etable.clear();
		if (!data.equalsIgnoreCase("")) {
			String[] split;
			for (int length = (split = data.split(";")).length, j = 0; j < length; ++j) {
				final String i = split[j];
				try {
					final Dragodinde DD = World.getDragoByID(Integer.parseInt(i));
					if (DD != null) {
						this.etable.add(DD);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setDurabiliteParse(final String durabilite) {
		this.objDurab.clear();
		if (!durabilite.isEmpty()) {
			String[] split;
			for (int length = (split = durabilite.split("\\|")).length, i = 0; i < length; ++i) {
				final String object = split[i];
				final String[] infos = object.split(";");
				final int cellId = Integer.parseInt(infos[0]);
				final int durability = Integer.parseInt(infos[1]);
				final int durabilityMax = Integer.parseInt(infos[2]);
				final java.util.Map<Integer, Integer> inDurab = new TreeMap<Integer, Integer>();
				inDurab.put(durability, durabilityMax);
				this.objDurab.put(cellId, inDurab);
			}
		}
	}

	public void setObjetPlacerParse(final String objetPlacer) {
		this.cellAndObject.clear();
		this.breedingObject.clear();
		if (!objetPlacer.isEmpty()) {
			String[] split;
			for (int length = (split = objetPlacer.split("\\|")).length, i = 0; i < length; ++i) {
				final String object = split[i];
				final String[] infos = object.split(";");
				final int cellId = Integer.parseInt(infos[0]);
				final int objectId = Integer.parseInt(infos[1]);
				final int proprietor = Integer.parseInt(infos[2]);
				final java.util.Map<Integer, Integer> other = new TreeMap<Integer, Integer>();
				other.put(objectId, proprietor);
				this.cellAndObject.put(cellId, objectId);
				this.breedingObject.put(cellId, other);
			}
		}
	}

	public void setEnclosParse(final String enclos) {
		this.raising.clear();
		if (!enclos.isEmpty() && !enclos.contains(",")) {
			String[] split;
			for (int length = (split = enclos.split(";")).length, i = 0; i < length; ++i) {
				final String dd = split[i];
				try {
					this.raising.add(Integer.parseInt(dd));
					World.getDragoByID(Integer.parseInt(dd)).setMapCell(this.map.getId(), this.getPlaceOfSpawn());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (!enclos.equalsIgnoreCase("")) {
			String[] split2;
			for (int length2 = (split2 = enclos.split(";")).length, j = 0; j < length2; ++j) {
				final String firstCut = split2[j];
				if (!firstCut.equalsIgnoreCase("")) {
					try {
						final String[] secondCut = firstCut.split(",");
						final Dragodinde DD = World.getDragoByID(Integer.parseInt(secondCut[1]));
						if (DD != null) {
							this.raising.add(Integer.parseInt(secondCut[1]), Integer.parseInt(secondCut[0]));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void setDoor(final int id) {
		this.door = id;
	}

	public int getMountcell() {
		return this.placeOfSpawn;
	}

	public void setMountCell(final int id) {
		this.placeOfSpawn = id;
	}

	public void setCellObject(final ArrayList<Integer> array) {
		this.cellOfObject = (ArrayList<Integer>) array.clone();
	}

	public String getCellObjectParse() {
		String cellObjet = "";
		if (!this.cellOfObject.isEmpty()) {
			for (final Integer cell : this.cellOfObject) {
				if (cellObjet.equalsIgnoreCase("")) {
					cellObjet = String.valueOf(cellObjet) + cell;
				} else {
					cellObjet = String.valueOf(cellObjet) + ";" + cell;
				}
			}
		}
		return cellObjet;
	}

	public void setInfos(final Map map, final int cell, final int size, final int placeOfSpawn, final int door,
			final String cellOfObject, final int maxObject) {
		this.size = size;
		this.map.setMountPark(null);
		this.map = map;
		if (this.map != null) {
			this.map.setMountPark(this);
		}
		this.cell = cell;
		this.placeOfSpawn = placeOfSpawn;
		this.door = door;
		this.maxObject = maxObject;
		this.cellOfObject.clear();
		if (!cellOfObject.isEmpty()) {
			String[] split;
			for (int length = (split = cellOfObject.split(";")).length, i = 0; i < length; ++i) {
				final String cases = split[i];
				final int cellId = Integer.parseInt(cases);
				if (cellId > 0) {
					this.cellOfObject.add(cellId);
				}
			}
		}
	}

	public int getOwner() {
		return this.owner;
	}

	public void setOwner(final int owner) {
		this.owner = owner;
	}

	public int getSize() {
		return this.size;
	}

	public Guild getGuild() {
		return this.guild;
	}

	public void setGuild(final Guild guild) {
		this.guild = guild;
	}

	public Map getMap() {
		return this.map;
	}

	public int getCell() {
		return this.cell;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(final int price) {
		this.price = price;
	}

	public int getPlaceOfSpawn() {
		return this.placeOfSpawn;
	}

	public void addPlaceOfSpawn(final int placeOfSpawn) {
		this.placeOfSpawn = placeOfSpawn;
	}

	public int getMaxObject() {
		return this.maxObject;
	}

	public int getDoor() {
		return this.door;
	}

	public ArrayList<Integer> getCellOfObject() {
		return this.cellOfObject;
	}

	public void addCellObject(final int cell) {
		if (this.cellOfObject.contains(cell)) {
			return;
		}
		if (cell <= 0) {
			return;
		}
		this.cellOfObject.add(cell);
	}

	public String parseStringCellObject() {
		String cell = "";
		boolean first = true;
		for (final Integer i : this.cellOfObject) {
			if (first) {
				cell = String.valueOf(cell) + i;
			} else {
				cell = String.valueOf(cell) + ";" + i;
			}
			first = false;
		}
		return cell;
	}

	public java.util.Map<Integer, Integer> getCellAndObject() {
		return this.cellAndObject;
	}

	public void addObject(final int cell, final int object, final int owner, final int durability,
			final int durabilityMax) {
		if (this.breedingObject.containsKey(cell)) {
			this.breedingObject.remove(cell);
			this.cellAndObject.remove(cell);
		}
		final java.util.Map<Integer, Integer> other = new TreeMap<Integer, Integer>();
		other.put(object, owner);
		final java.util.Map<Integer, Integer> inDurab = new TreeMap<Integer, Integer>();
		inDurab.put(durability, durabilityMax);
		this.cellAndObject.put(cell, object);
		this.breedingObject.put(cell, other);
		this.objDurab.put(cell, inDurab);
	}

	public boolean delObject(final int cell) {
		if (!this.breedingObject.containsKey(cell) && !this.objDurab.containsKey(cell)) {
			return false;
		}
		this.objDurab.remove(cell);
		this.breedingObject.remove(cell);
		this.cellAndObject.remove(cell);
		return true;
	}

	public java.util.Map<Integer, java.util.Map<Integer, Integer>> getObjDurab() {
		return this.objDurab;
	}

	public java.util.Map<Integer, java.util.Map<Integer, Integer>> getObject() {
		return this.breedingObject;
	}

	public void addRaising(final int id) {
		this.raising.add(id);
	}

	public void delRaising(final int id) {
		if (this.raising.contains(id)) {
			final int index = this.raising.indexOf(id);
			this.raising.remove(index);
		}
	}

	public CopyOnWriteArrayList<Integer> getListOfRaising() {
		return this.raising;
	}

	public ArrayList<Dragodinde> getEtable() {
		return this.etable;
	}

	public synchronized void startMoveMounts() {
		if (this.raising.size() > 0) {
			final char[] directions = { 'b', 'd', 'f', 'h' };
			for (final Integer id : this.raising) {
				final Dragodinde DD = World.getDragoByID(id);
				if (DD != null) {
					final char dir = directions[Formulas.getRandomValue(0, 3)];
					this.waiter.addNext(new Runnable() {
						@Override
						public void run() {
							DD.moveMountsAuto(dir, 3, false);
						}
					}, 300L);
				}
			}
		}
	}

	public String getStringObject() {
		String str = "";
		boolean first = false;
		if (this.breedingObject.size() == 0) {
			return str;
		}
		for (final java.util.Map.Entry<Integer, java.util.Map<Integer, Integer>> entry : this.breedingObject
				.entrySet()) {
			if (first) {
				str = String.valueOf(str) + "|";
			}
			str = String.valueOf(str) + entry.getKey();
			for (final java.util.Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
				str = String.valueOf(str) + ";" + entry2.getKey() + ";" + entry2.getValue();
			}
			first = true;
		}
		return str;
	}

	public String getStringObjDurab() {
		String str = "";
		boolean first = false;
		if (this.objDurab.size() == 0) {
			return str;
		}
		for (final java.util.Map.Entry<Integer, java.util.Map<Integer, Integer>> entry : this.objDurab.entrySet()) {
			if (first) {
				str = String.valueOf(str) + "|";
			}
			str = String.valueOf(str) + entry.getKey();
			for (final java.util.Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
				str = String.valueOf(str) + ";" + entry2.getKey() + ";" + entry2.getValue();
			}
			first = true;
		}
		return str;
	}

	public String getData() {
		String str = "";
		boolean first = true;
		if (this.raising.size() == 0) {
			return "";
		}
		for (final Integer id : this.raising) {
			if (!first) {
				str = String.valueOf(str) + ";";
			}
			str = String.valueOf(str) + id;
			first = false;
		}
		return str;
	}

	public String parseStringEtableId() {
		String str = "";
		for (final Dragodinde DD : this.etable) {
			if (!str.equalsIgnoreCase("")) {
				str = String.valueOf(str) + ";";
			}
			str = String.valueOf(str) + DD.getId();
		}
		return str;
	}
}

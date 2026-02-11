// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.entity.npc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.object.ObjectTemplate;
import org.aestia.quest.Quest;

public class NpcTemplate {
	private int _id;
	private int _bonusValue;
	private int _gfxID;
	private int _scaleX;
	private int _scaleY;
	private int _sex;
	private int _color1;
	private int _color2;
	private int _color3;
	private String _acces;
	private int _extraClip;
	private int _customArtWork;
	private Map<Integer, Integer> initQuestions;
	private ArrayList<ObjectTemplate> _ventes;
	private Map<Integer, Integer> _exchangesGet1;
	private Map<Integer, Integer> _exchangesGive1;
	private Map<Integer, Integer> _exchangesGet2;
	private Map<Integer, Integer> _exchangesGive2;
	private Map<Integer, Integer> _exchangesGet3;
	private Map<Integer, Integer> _exchangesGive3;
	public Quest quest;

	public NpcTemplate(final int _id, final int value, final int _gfxid, final int _scalex, final int _scaley,
			final int _sex, final int _color1, final int _color2, final int _color3, final String _acces,
			final int clip, final int artWork, final String questionID, final String ventes, final String quests,
			final String exchanges) {
		this.initQuestions = new TreeMap<Integer, Integer>();
		this._ventes = new ArrayList<ObjectTemplate>();
		this._exchangesGet1 = new TreeMap<Integer, Integer>();
		this._exchangesGive1 = new TreeMap<Integer, Integer>();
		this._exchangesGet2 = new TreeMap<Integer, Integer>();
		this._exchangesGive2 = new TreeMap<Integer, Integer>();
		this._exchangesGet3 = new TreeMap<Integer, Integer>();
		this._exchangesGive3 = new TreeMap<Integer, Integer>();
		this._id = _id;
		this._bonusValue = value;
		this._gfxID = _gfxid;
		this._scaleX = _scalex;
		this._scaleY = _scaley;
		this._sex = _sex;
		this._color1 = _color1;
		this._color2 = _color2;
		this._color3 = _color3;
		this._acces = _acces;
		this._extraClip = clip;
		this._customArtWork = artWork;
		if (quests.equalsIgnoreCase("")) {
			this.quest = null;
		} else {
			this.quest = Quest.getQuestById(Integer.parseInt(quests));
		}
		if (questionID.split("\\|").length > 1) {
			String[] split;
			for (int length = (split = questionID.split("\\|")).length, j = 0; j < length; ++j) {
				final String i = split[j];
				try {
					this.initQuestions.put(Integer.parseInt(i.split("\\,")[0]), Integer.parseInt(i.split("\\,")[1]));
				} catch (Exception e) {
					e.printStackTrace();
					Console.println("#1# Erreur sur une question id sur le PNJ d'id : " + _id, Console.Color.ERROR);
				}
			}
		} else if (questionID.equalsIgnoreCase("")) {
			this.initQuestions.put(-1, 0);
		} else {
			this.initQuestions.put(-1, Integer.parseInt(questionID));
		}
		if (!ventes.equals("")) {
			String[] split2;
			for (int length2 = (split2 = ventes.split("\\,")).length, k = 0; k < length2; ++k) {
				final String obj = split2[k];
				try {
					final int tempID = Integer.parseInt(obj);
					final ObjectTemplate temp = World.getObjTemplate(tempID);
					if (temp != null) {
						this._ventes.add(temp);
					}
				} catch (NumberFormatException e2) {
					e2.printStackTrace();
				}
			}
		}
		if (!exchanges.equals("")) {
			try {
				int nbr = 0;
				String[] split3;
				for (int length3 = (split3 = exchanges.split("\\~")).length, l = 0; l < length3; ++l) {
					final String part = split3[l];
					if (++nbr < 4) {
						switch (nbr) {
						case 1: {
							final String necc1 = part.split("\\|")[0];
							final String give1 = part.split("\\|")[1];
							String[] split4;
							for (int length4 = (split4 = necc1.split("\\,")).length, n = 0; n < length4; ++n) {
								final String obj2 = split4[n];
								final int id = Integer.parseInt(obj2.split("\\:")[0]);
								final int qua = Integer.parseInt(obj2.split("\\:")[1]);
								final ObjectTemplate objT = World.getObjTemplate(id);
								if (objT != null) {
									this._exchangesGet1.put(id, qua);
								}
							}
							String[] split5;
							for (int length5 = (split5 = give1.split("\\,")).length, n2 = 0; n2 < length5; ++n2) {
								final String obj2 = split5[n2];
								final int id = Integer.parseInt(obj2.split("\\:")[0]);
								final int qua = Integer.parseInt(obj2.split("\\:")[1]);
								final ObjectTemplate objT = World.getObjTemplate(id);
								if (objT != null) {
									this._exchangesGive1.put(id, qua);
								}
							}
							break;
						}
						case 2: {
							final String necc2 = part.split("\\|")[0];
							final String give2 = part.split("\\|")[1];
							String[] split6;
							for (int length6 = (split6 = necc2.split("\\,")).length, n3 = 0; n3 < length6; ++n3) {
								final String obj3 = split6[n3];
								final int id2 = Integer.parseInt(obj3.split("\\:")[0]);
								final int qua2 = Integer.parseInt(obj3.split("\\:")[1]);
								final ObjectTemplate objT2 = World.getObjTemplate(id2);
								if (objT2 != null) {
									this._exchangesGet2.put(id2, qua2);
								}
							}
							String[] split7;
							for (int length7 = (split7 = give2.split("\\,")).length, n4 = 0; n4 < length7; ++n4) {
								final String obj3 = split7[n4];
								final int id2 = Integer.parseInt(obj3.split("\\:")[0]);
								final int qua2 = Integer.parseInt(obj3.split("\\:")[1]);
								final ObjectTemplate objT2 = World.getObjTemplate(id2);
								if (objT2 != null) {
									this._exchangesGive2.put(id2, qua2);
								}
							}
							break;
						}
						case 3: {
							final String necc3 = part.split("\\|")[0];
							final String give3 = part.split("\\|")[1];
							String[] split8;
							for (int length8 = (split8 = necc3.split("\\,")).length, n5 = 0; n5 < length8; ++n5) {
								final String obj4 = split8[n5];
								final int id3 = Integer.parseInt(obj4.split("\\:")[0]);
								final int qua3 = Integer.parseInt(obj4.split("\\:")[1]);
								final ObjectTemplate objT3 = World.getObjTemplate(id3);
								if (objT3 != null) {
									this._exchangesGet3.put(id3, qua3);
								}
							}
							String[] split9;
							for (int length9 = (split9 = give3.split("\\,")).length, n6 = 0; n6 < length9; ++n6) {
								final String obj4 = split9[n6];
								final int id3 = Integer.parseInt(obj4.split("\\:")[0]);
								final int qua3 = Integer.parseInt(obj4.split("\\:")[1]);
								final ObjectTemplate objT3 = World.getObjTemplate(id3);
								if (objT3 != null) {
									this._exchangesGive3.put(id3, qua3);
								}
							}
							break;
						}
						}
					}
				}
			} catch (NumberFormatException e3) {
				e3.printStackTrace();
			}
		}
	}

	public void setInfos(final int id, final int bonusValue, final int gfxID, final int scaleX, final int scaleY,
			final int sex, final int color1, final int color2, final int color3, final String access,
			final int extraClip, final int customArtWork, final String questionID, final String ventes,
			final String quests, final String exchanges) {
		this._id = id;
		this._bonusValue = bonusValue;
		this._gfxID = gfxID;
		this._scaleX = scaleX;
		this._scaleY = scaleY;
		this._sex = sex;
		this._color1 = color1;
		this._color2 = color2;
		this._color3 = color3;
		this._acces = access;
		this._extraClip = extraClip;
		this._customArtWork = customArtWork;
		this.initQuestions.clear();
		if (quests.equalsIgnoreCase("")) {
			this.quest = null;
		} else {
			this.quest = Quest.getQuestById(Integer.parseInt(quests));
		}
		if (questionID.split("\\|").length > 1) {
			String[] split;
			for (int length = (split = questionID.split("\\|")).length, j = 0; j < length; ++j) {
				final String i = split[j];
				try {
					this.initQuestions.put(Integer.parseInt(i.split("\\,")[0]), Integer.parseInt(i.split("\\,")[1]));
				} catch (Exception e) {
					e.printStackTrace();
					Console.println("#2# Erreur sur une question id sur le PNJ d'id : " + this._id,
							Console.Color.ERROR);
				}
			}
		} else {
			this.initQuestions.put(-1, Integer.parseInt(questionID));
		}
		this._ventes.clear();
		if (!ventes.equals("")) {
			String[] split2;
			for (int length2 = (split2 = ventes.split("\\,")).length, k = 0; k < length2; ++k) {
				final String obj = split2[k];
				try {
					final int tempID = Integer.parseInt(obj);
					final ObjectTemplate temp = World.getObjTemplate(tempID);
					if (temp != null) {
						this._ventes.add(temp);
					}
				} catch (NumberFormatException e2) {
					e2.printStackTrace();
				}
			}
		}
		if (!exchanges.equals("")) {
			try {
				int nbr = 0;
				String[] split3;
				for (int length3 = (split3 = exchanges.split("\\~")).length, l = 0; l < length3; ++l) {
					final String part = split3[l];
					if (exchanges.split("~").length == 1 && nbr == 1) {
						return;
					}
					if (exchanges.split("~").length == 2 && nbr == 3) {
						return;
					}
					if (exchanges.split("~").length == 3 && nbr == 3) {
						return;
					}
					if (exchanges.split("~").length == 4 && nbr == 4) {
						return;
					}
					switch (++nbr) {
					case 1: {
						final String necc1 = part.split("\\|")[0];
						final String give1 = part.split("\\|")[1];
						this._exchangesGet1.clear();
						this._exchangesGive1.clear();
						String[] split4;
						for (int length4 = (split4 = necc1.split("\\,")).length, n = 0; n < length4; ++n) {
							final String obj2 = split4[n];
							final int id2 = Integer.parseInt(obj2.split("\\:")[0]);
							final int qua = Integer.parseInt(obj2.split("\\:")[1]);
							final ObjectTemplate objT = World.getObjTemplate(id2);
							if (objT != null) {
								this._exchangesGet1.put(id2, qua);
							}
						}
						String[] split5;
						for (int length5 = (split5 = give1.split("\\,")).length, n2 = 0; n2 < length5; ++n2) {
							final String obj2 = split5[n2];
							final int id2 = Integer.parseInt(obj2.split("\\:")[0]);
							final int qua = Integer.parseInt(obj2.split("\\:")[1]);
							final ObjectTemplate objT = World.getObjTemplate(id2);
							if (objT != null) {
								this._exchangesGive1.put(id2, qua);
							}
						}
						break;
					}
					case 2: {
						final String necc2 = part.split("\\|")[0];
						final String give2 = part.split("\\|")[1];
						this._exchangesGet2.clear();
						this._exchangesGive2.clear();
						String[] split6;
						for (int length6 = (split6 = necc2.split("\\,")).length, n3 = 0; n3 < length6; ++n3) {
							final String obj3 = split6[n3];
							final int id3 = Integer.parseInt(obj3.split("\\:")[0]);
							final int qua2 = Integer.parseInt(obj3.split("\\:")[1]);
							final ObjectTemplate objT2 = World.getObjTemplate(id3);
							if (objT2 != null) {
								this._exchangesGet2.put(id3, qua2);
							}
						}
						String[] split7;
						for (int length7 = (split7 = give2.split("\\,")).length, n4 = 0; n4 < length7; ++n4) {
							final String obj3 = split7[n4];
							final int id3 = Integer.parseInt(obj3.split("\\:")[0]);
							final int qua2 = Integer.parseInt(obj3.split("\\:")[1]);
							final ObjectTemplate objT2 = World.getObjTemplate(id3);
							if (objT2 != null) {
								this._exchangesGive2.put(id3, qua2);
							}
						}
						break;
					}
					case 3: {
						final String necc3 = part.split("\\|")[0];
						final String give3 = part.split("\\|")[1];
						this._exchangesGet3.clear();
						this._exchangesGive3.clear();
						String[] split8;
						for (int length8 = (split8 = necc3.split("\\,")).length, n5 = 0; n5 < length8; ++n5) {
							final String obj4 = split8[n5];
							final int id4 = Integer.parseInt(obj4.split("\\:")[0]);
							final int qua3 = Integer.parseInt(obj4.split("\\:")[1]);
							final ObjectTemplate objT3 = World.getObjTemplate(id4);
							if (objT3 != null) {
								this._exchangesGet3.put(id4, qua3);
							}
						}
						String[] split9;
						for (int length9 = (split9 = give3.split("\\,")).length, n6 = 0; n6 < length9; ++n6) {
							final String obj4 = split9[n6];
							final int id4 = Integer.parseInt(obj4.split("\\:")[0]);
							final int qua3 = Integer.parseInt(obj4.split("\\:")[1]);
							final ObjectTemplate objT3 = World.getObjTemplate(id4);
							if (objT3 != null) {
								this._exchangesGive3.put(id4, qua3);
							}
						}
						break;
					}
					}
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
	}

	public int get_id() {
		return this._id;
	}

	public void setQuest(final Quest q) {
		this.quest = q;
	}

	public Quest getQuest() {
		return this.quest;
	}

	public boolean verifItemGet(final int id) {
		return this._exchangesGet1.containsKey(id) && this._exchangesGet2.containsKey(id)
				&& this._exchangesGet3.containsKey(id);
	}

	public boolean haveGet1Object(final int id) {
		return this._exchangesGet1.containsKey(id);
	}

	public boolean haveGet2Object(final int id) {
		return this._exchangesGet2.containsKey(id);
	}

	public boolean haveGet3Object(final int id) {
		return this._exchangesGet3.containsKey(id);
	}

	public int haveAllGetObject(final ArrayList<World.Couple<Integer, Integer>> items1) {
		boolean verif1 = true;
		boolean verif2 = true;
		boolean verif3 = true;
		if (items1.size() <= 0) {
			return 0;
		}
		for (final World.Couple<Integer, Integer> i : items1) {
			if (!this.haveGet1Object(World.getObjet(i.first).getTemplate().getId())) {
				verif1 = false;
			} else if (this._exchangesGet1.get(World.getObjet(i.first).getTemplate().getId()) != i.second) {
				verif1 = false;
			} else if (items1.size() != this._exchangesGet1.size()) {
				verif1 = false;
			}
			if (!this.haveGet2Object(World.getObjet(i.first).getTemplate().getId())) {
				verif2 = false;
			} else {
				if (this._exchangesGet2.get(World.getObjet(i.first).getTemplate().getId()) != i.second) {
					verif2 = false;
				}
				if (items1.size() != this._exchangesGet2.size()) {
					verif2 = false;
				}
			}
			if (!this.haveGet3Object(World.getObjet(i.first).getTemplate().getId())) {
				verif3 = false;
			} else {
				if (this._exchangesGet3.get(World.getObjet(i.first).getTemplate().getId()) != i.second) {
					verif3 = false;
				}
				if (items1.size() == this._exchangesGet3.size()) {
					continue;
				}
				verif3 = false;
			}
		}
		if (verif1) {
			return 1;
		}
		if (verif2) {
			return 2;
		}
		if (verif3) {
			return 3;
		}
		return 0;
	}

	public int getQuantityOfGive1(final int id) {
		return this._exchangesGive1.get(id);
	}

	public Map<Integer, Integer> getGiveItem1() {
		return this._exchangesGive1;
	}

	public int getQuantityOfGive2(final int id) {
		return this._exchangesGive2.get(id);
	}

	public Map<Integer, Integer> getGiveItem2() {
		return this._exchangesGive2;
	}

	public int getQuantityOfGive3(final int id) {
		return this._exchangesGive3.get(id);
	}

	public Map<Integer, Integer> getGiveItem3() {
		return this._exchangesGive3;
	}

	public int get_bonusValue() {
		return this._bonusValue;
	}

	public int get_gfxID() {
		return this._gfxID;
	}

	public int get_scaleX() {
		return this._scaleX;
	}

	public int get_scaleY() {
		return this._scaleY;
	}

	public int get_sex() {
		return this._sex;
	}

	public int get_color1() {
		return this._color1;
	}

	public int get_color2() {
		return this._color2;
	}

	public int get_color3() {
		return this._color3;
	}

	public String get_acces() {
		return this._acces;
	}

	public int get_extraClip() {
		return this._extraClip;
	}

	public void setExtraClip(final int i) {
		this._extraClip = i;
	}

	public int get_customArtWork() {
		return this._customArtWork;
	}

	public int getInitQuestionId(final int mapid) {
		if (this.initQuestions.get(mapid) == null) {
			final Iterator<Map.Entry<Integer, Integer>> iterator = this.initQuestions.entrySet().iterator();
			if (iterator.hasNext()) {
				final Map.Entry<Integer, Integer> entry = iterator.next();
				return entry.getValue();
			}
		}
		return this.initQuestions.get(mapid);
	}

	public String getItemVendorList() {
		final StringBuilder items = new StringBuilder();
		if (this._ventes.isEmpty()) {
			return "";
		}
		for (final ObjectTemplate obj : this._ventes) {
			items.append(obj.parseItemTemplateStats()).append("|");
		}
		return items.toString();
	}

	public ArrayList<ObjectTemplate> getAllItem() {
		return this._ventes;
	}

	public boolean addItemVendor(final ObjectTemplate T) {
		if (this._ventes.contains(T)) {
			return false;
		}
		this._ventes.add(T);
		return true;
	}

	public boolean delItemVendor(final int tID) {
		final ArrayList<ObjectTemplate> newVentes = new ArrayList<ObjectTemplate>();
		boolean remove = false;
		for (final ObjectTemplate T : this._ventes) {
			if (T.getId() == tID) {
				remove = true;
			} else {
				newVentes.add(T);
			}
		}
		this._ventes = newVentes;
		return remove;
	}

	public boolean haveItem(final int templateID) {
		for (final ObjectTemplate curTemp : this._ventes) {
			if (curTemp.getId() == templateID) {
				return true;
			}
		}
		return false;
	}
}

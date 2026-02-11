// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.hdv;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.aestia.client.Account;
import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.game.world.World;
import org.aestia.object.ObjectTemplate;

public class Hdv {
	private int hdvId;
	private float taxe;
	private short sellTime;
	private short maxAccountItem;
	private String strCategory;
	private short lvlMax;
	private Map<Integer, HdvCategory> categorys;
	private Map<Integer, World.Couple<Integer, Integer>> path;
	private DecimalFormat pattern;

	public Hdv(final int hdvID, final float taxe, final short sellTime, final short maxItemCompte, final short lvlMax,
			final String strCategory) {
		this.categorys = new HashMap<Integer, HdvCategory>();
		this.path = new HashMap<Integer, World.Couple<Integer, Integer>>();
		this.pattern = new DecimalFormat("0.0");
		this.hdvId = hdvID;
		this.taxe = taxe;
		this.maxAccountItem = maxItemCompte;
		this.strCategory = strCategory;
		this.lvlMax = lvlMax;
		String[] split;
		for (int length = (split = strCategory.split(",")).length, i = 0; i < length; ++i) {
			final String strCategID = split[i];
			final int categId = Integer.parseInt(strCategID);
			this.categorys.put(categId, new HdvCategory(categId));
		}
	}

	public int getHdvId() {
		return this.hdvId;
	}

	public float getTaxe() {
		return this.taxe;
	}

	public short getSellTime() {
		return this.sellTime;
	}

	public short getMaxAccountItem() {
		return this.maxAccountItem;
	}

	public String getStrCategory() {
		return this.strCategory;
	}

	public short getLvlMax() {
		return this.lvlMax;
	}

	public Map<Integer, HdvCategory> getCategorys() {
		return this.categorys;
	}

	public boolean haveCategory(final int categ) {
		return this.categorys.containsKey(categ);
	}

	public HdvLine getLine(final int lineId) {
		if (this.path == null) {
			return null;
		}
		try {
			final int categoryId = this.path.get(lineId).first;
			final int templateId = this.path.get(lineId).second;
			return this.getCategorys().get(categoryId).getTemplate(templateId).getLine(lineId);
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void addEntry(final HdvEntry toAdd, final boolean load) {
		toAdd.setHdvId(this.getHdvId());
		final int categoryId = toAdd.getObject().getTemplate().getType();
		final int templateId = toAdd.getObject().getTemplate().getId();
		if (this.getCategorys().get(categoryId) != null) {
			this.getCategorys().get(categoryId).addEntry(toAdd);
			this.path.put(toAdd.getLineId(), new World.Couple<Integer, Integer>(categoryId, templateId));
			if (!load) {
				Database.getGame().getHdvs_itemData().add(toAdd);
			}
			World.addHdvItem(toAdd.getOwner(), this.getHdvId(), toAdd);
			return;
		}
		if (World.getPersonnage(toAdd.getOwner()) != null) {
			World.getPersonnage(toAdd.getOwner()).itemLost.add(toAdd.getObject().getGuid());
		}
	}

	public boolean delEntry(final HdvEntry toDel) {
		final boolean toReturn = this.getCategorys().get(toDel.getObject().getTemplate().getType()).delEntry(toDel);
		if (toReturn) {
			this.path.remove(toDel.getLineId());
			World.removeHdvItem(toDel.getOwner(), toDel.getHdvId(), toDel);
		}
		return toReturn;
	}

	public ArrayList<HdvEntry> getAllEntry() {
		final ArrayList<HdvEntry> toReturn = new ArrayList<HdvEntry>();
		for (final HdvCategory curCat : this.getCategorys().values()) {
			toReturn.addAll(curCat.getAllEntry());
		}
		return toReturn;
	}

	public synchronized boolean buyItem(final int ligneID, final byte amount, final int price, final Player newOwner) {
		boolean toReturn = true;
		try {
			if (newOwner.get_kamas() < price) {
				return false;
			}
			final HdvLine ligne = this.getLine(ligneID);
			HdvEntry toBuy = ligne.doYouHave(amount, price);
			if (toBuy.buy) {
				return false;
			}
			toBuy.buy = true;
			newOwner.addKamas(price * -1);
			if (toBuy.getOwner() != -1) {
				final Account C = World.getCompte(toBuy.getOwner());
				if (C != null) {
					C.setBankKamas(C.getBankKamas() + toBuy.getPrice());
				}
			}
			SocketManager.GAME_SEND_STATS_PACKET(newOwner);
			toBuy.getObject().setPosition(-1);
			newOwner.addObjet(toBuy.getObject(), true);
			toBuy.getObject().getTemplate().newSold(toBuy.getAmount(true), price);
			this.delEntry(toBuy);
			Database.getGame().getHdvs_itemData().delete(toBuy.getObject().getGuid());
			if (World.getCompte(toBuy.getOwner()) != null && World.getCompte(toBuy.getOwner()).getCurPerso() != null) {
				SocketManager.GAME_SEND_Im_PACKET(World.getCompte(toBuy.getOwner()).getCurPerso(),
						"065;" + price + "~" + toBuy.getObject().getTemplate().getId() + "~"
								+ toBuy.getObject().getTemplate().getId() + "~1");
			}
			if (toBuy.getOwner() == -1) {
				Database.getStatique().getItemData().save(toBuy.getObject(), false);
			}
			Database.getStatique().getPlayerData().update(newOwner, true);
			toBuy = null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			toReturn = false;
		}
		return toReturn;
	}

	public String parseToEHl(final int templateID) {
		try {
			final ObjectTemplate OT = World.getObjTemplate(templateID);
			final HdvCategory Hdv = this.getCategorys().get(OT.getType());
			final HdvTemplate HdvT = Hdv.getTemplate(templateID);
			if (HdvT == null) {
				return "";
			}
			return HdvT.parseToEHl();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String parseTemplate(final int categID) {
		return this.getCategorys().get(categID).parseTemplate();
	}

	public String parseTaxe() {
		return this.pattern.format(this.getTaxe()).replace(",", ".");
	}
}

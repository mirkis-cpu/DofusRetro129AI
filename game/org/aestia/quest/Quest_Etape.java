// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.quest;

import java.util.Map;
import java.util.TreeMap;

import org.aestia.entity.npc.NpcTemplate;
import org.aestia.game.world.World;

public class Quest_Etape {
	public static Map<Integer, Quest_Etape> questEtapeList;
	private int id;
	private short type;
	private int objectif;
	private Quest quest;
	private Map<Integer, Integer> itemNecessary;
	private NpcTemplate npc;
	private int monsterId;
	private short qua;
	private String condition;
	private int validationType;

	static {
		Quest_Etape.questEtapeList = new TreeMap<Integer, Quest_Etape>();
	}

	public Quest_Etape(final int aId, final int aType, final int aObjectif, final String itemN, final int aNpc,
			final String aMonster, final String aCondition, final int validationType) {
		this.quest = null;
		this.itemNecessary = new TreeMap<Integer, Integer>();
		this.npc = null;
		this.condition = null;
		this.id = aId;
		this.type = (short) aType;
		this.objectif = aObjectif;
		try {
			if (!itemN.equalsIgnoreCase("")) {
				final String[] split = itemN.split(";");
				if (split != null && split.length > 0) {
					String[] array;
					for (int length = (array = split).length, i = 0; i < length; ++i) {
						final String infos = array[i];
						final String[] loc1 = infos.split(",");
						this.itemNecessary.put(Integer.parseInt(loc1[0]), Integer.parseInt(loc1[1]));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.npc = World.getNPCTemplate(aNpc);
		try {
			if (aMonster.contains(",") && !aMonster.equals(0)) {
				final String[] loc2 = aMonster.split(",");
				this.setMonsterId(Integer.parseInt(loc2[0]));
				this.setQua(Short.parseShort(loc2[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setValidationType(validationType);
		this.condition = aCondition;
		try {
			final Quest.Quest_Objectif qo = Quest.Quest_Objectif.getQuestObjectifById(this.objectif);
			if (qo != null) {
				qo.setEtape(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return this.id;
	}

	public short getType() {
		return this.type;
	}

	public int getObjectif() {
		return this.objectif;
	}

	public Quest getQuestData() {
		return this.quest;
	}

	public void setQuestData(final Quest aQuest) {
		this.quest = aQuest;
	}

	public Map<Integer, Integer> getItemNecessaryList() {
		return this.itemNecessary;
	}

	public NpcTemplate getNpc() {
		return this.npc;
	}

	public String getCondition() {
		return this.condition;
	}

	public int getMonsterId() {
		return this.monsterId;
	}

	public void setMonsterId(final int monsterId) {
		this.monsterId = monsterId;
	}

	public short getQua() {
		return this.qua;
	}

	public void setQua(final short qua) {
		this.qua = qua;
	}

	public int getValidationType() {
		return this.validationType;
	}

	public void setValidationType(final int aValidationType) {
		this.validationType = aValidationType;
	}

	public static Map<Integer, Quest_Etape> getQuestEtapeList() {
		return Quest_Etape.questEtapeList;
	}

	public static Quest_Etape getQuestEtapeById(final int id) {
		return Quest_Etape.questEtapeList.get(id);
	}

	public static void setQuestEtape(final Quest_Etape qEtape) {
		Quest_Etape.questEtapeList.put(qEtape.getId(), qEtape);
	}
}

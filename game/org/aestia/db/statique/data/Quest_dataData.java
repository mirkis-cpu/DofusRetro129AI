/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 */
package org.aestia.db.statique.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.db.statique.AbstractDAO;
import org.aestia.quest.Quest;

public class Quest_dataData extends AbstractDAO<Quest> {
	public Quest_dataData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Quest obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM quest_data");

				Quest.questDataList.clear();
				while (RS.next()) {
					Quest quest = new Quest(RS.getInt("id"), RS.getString("etapes"), RS.getString("objectif"),
							RS.getInt("npc"), RS.getString("action"), RS.getString("args"),
							RS.getInt("deleteFinish") == 1, RS.getString("condition"));
					if (quest.getNpc_Tmpl() != null) {
						quest.getNpc_Tmpl().quest = quest;
						quest.getNpc_Tmpl().setExtraClip(4);
					}
					Quest.setQuestInList(quest);
				}
			} catch (SQLException e) {
				super.sendError("Quest_dataData load", e);
			}
		} finally {
			this.close(RS);
		}
	}
}

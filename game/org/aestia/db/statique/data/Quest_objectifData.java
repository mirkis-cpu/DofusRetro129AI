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

public class Quest_objectifData extends AbstractDAO<Quest.Quest_Objectif> {
	public Quest_objectifData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Quest.Quest_Objectif obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM quest_objectifs");
				Quest.Quest_Objectif.questObjectifList.clear();
				while (RS.next()) {
					Quest.Quest_Objectif qObjectif = new Quest.Quest_Objectif(RS.getInt("id"), RS.getInt("xp"),
							RS.getInt("kamas"), RS.getString("item"), RS.getString("action"));
					Quest.Quest_Objectif.setQuest_Objectif(qObjectif);
				}
			} catch (SQLException e) {
				super.sendError("Quest_objectifData load", e);
			}
		} finally {
			this.close(RS);
		}
	}
}

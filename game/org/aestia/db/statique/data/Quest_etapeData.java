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
import org.aestia.quest.Quest_Etape;

public class Quest_etapeData extends AbstractDAO<Quest_Etape> {
	public Quest_etapeData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(Quest_Etape obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * FROM quest_etapes");
				Quest_Etape.questEtapeList.clear();
				while (RS.next()) {
					Quest_Etape QE = new Quest_Etape(RS.getInt("id"), RS.getInt("type"), RS.getInt("objectif"),
							RS.getString("item"), RS.getInt("npc"), RS.getString("monster"),
							RS.getString("conditions"), RS.getInt("validationType"));
					Quest_Etape.setQuestEtape(QE);
				}
			} catch (SQLException e) {
				super.sendError("Quest_etapeData load", e);
			}
		} finally {
			this.close(RS);
		}
	}
}

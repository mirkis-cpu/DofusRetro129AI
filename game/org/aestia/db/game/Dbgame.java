/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ch.qos.logback.classic.Level
 *  ch.qos.logback.classic.Logger
 *  com.zaxxer.hikari.HikariConfig
 *  com.zaxxer.hikari.HikariDataSource
 *  org.slf4j.LoggerFactory
 */
package org.aestia.db.game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.aestia.db.game.data.Area_dataData;
import org.aestia.db.game.data.BanditData;
import org.aestia.db.game.data.CoffreData;
import org.aestia.db.game.data.GuildData;
import org.aestia.db.game.data.Guild_memberData;
import org.aestia.db.game.data.Hdvs_itemData;
import org.aestia.db.game.data.HouseData;
import org.aestia.db.game.data.Mountpark_dataData;
import org.aestia.db.game.data.PercepteurData;
import org.aestia.db.game.data.PrismeData;
import org.aestia.db.game.data.Subarea_dataData;
import org.aestia.kernel.Console;
import org.aestia.kernel.Main;

public class Dbgame {
	private Connection connection;

	private Area_dataData area_dataData;
	private BanditData banditData;
	private CoffreData coffreData;
	private Guild_memberData guild_memberData;
	private GuildData guildData;
	private Hdvs_itemData hdvs_itemData;
	private HouseData houseData;
	private Mountpark_dataData mountpark_dataData;
	private PercepteurData percepteurData;
	private PrismeData prismeData;
	private Subarea_dataData subarea_dataData;

	public void initializeData() {
		this.area_dataData = new Area_dataData(this.connection);
		this.banditData = new BanditData(this.connection);
		this.coffreData = new CoffreData(this.connection);
		this.guild_memberData = new Guild_memberData(this.connection);
		this.guildData = new GuildData(this.connection);
		this.hdvs_itemData = new Hdvs_itemData(this.connection);
		this.houseData = new HouseData(this.connection);
		this.mountpark_dataData = new Mountpark_dataData(this.connection);
		this.percepteurData = new PercepteurData(this.connection);
		this.prismeData = new PrismeData(this.connection);
		this.subarea_dataData = new Subarea_dataData(this.connection);
	}

	public boolean initializeConnection() {
		try {
			String url = "jdbc:mysql://" + Main.hostDB + "/" + Main.nameDB;
			this.connection = DriverManager.getConnection(url, Main.userDB, Main.passDB);
			if (!connection.isValid(1000)) {
				System.err.println("Pleaz check your username and password and database connection");
				Main.stop();
			}
			System.out.println("Database connection established");
			this.initializeData();
		} catch (SQLException e) {
			e.printStackTrace();
			Console.println("Erreur sql : " + e.getMessage(), Console.Color.ERROR);
		}
		return true;
	}

	public Area_dataData getArea_dataData() {
		return this.area_dataData;
	}

	public BanditData getBanditData() {
		return this.banditData;
	}

	public CoffreData getCoffreData() {
		return this.coffreData;
	}

	public Guild_memberData getGuild_memberData() {
		return this.guild_memberData;
	}

	public GuildData getGuildData() {
		return this.guildData;
	}

	public Hdvs_itemData getHdvs_itemData() {
		return this.hdvs_itemData;
	}

	public HouseData getHouseData() {
		return this.houseData;
	}

	public Mountpark_dataData getMountpark_dataData() {
		return this.mountpark_dataData;
	}

	public PercepteurData getPercepteurData() {
		return this.percepteurData;
	}

	public PrismeData getPrismeData() {
		return this.prismeData;
	}

	public Subarea_dataData getSubarea_dataData() {
		return this.subarea_dataData;
	}
}

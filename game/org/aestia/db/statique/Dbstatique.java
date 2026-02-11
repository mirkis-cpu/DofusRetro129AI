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
package org.aestia.db.statique;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.aestia.db.statique.data.AccountData;
import org.aestia.db.statique.data.AnimationData;
import org.aestia.db.statique.data.Area_dataData;
import org.aestia.db.statique.data.BanipData;
import org.aestia.db.statique.data.ChallengeData;
import org.aestia.db.statique.data.CoffreData;
import org.aestia.db.statique.data.CommandeData;
import org.aestia.db.statique.data.CraftData;
import org.aestia.db.statique.data.DonjonData;
import org.aestia.db.statique.data.DropData;
import org.aestia.db.statique.data.Endfight_actionData;
import org.aestia.db.statique.data.ExperienceData;
import org.aestia.db.statique.data.Extra_monsterData;
import org.aestia.db.statique.data.Full_morphData;
import org.aestia.db.statique.data.GiftData;
import org.aestia.db.statique.data.GroupeData;
import org.aestia.db.statique.data.HdvData;
import org.aestia.db.statique.data.HouseData;
import org.aestia.db.statique.data.Interactive_objects_dataData;
import org.aestia.db.statique.data.ItemData;
import org.aestia.db.statique.data.Item_templateData;
import org.aestia.db.statique.data.ItemsetData;
import org.aestia.db.statique.data.Jobs_dataData;
import org.aestia.db.statique.data.MapData;
import org.aestia.db.statique.data.Mobgroups_fixData;
import org.aestia.db.statique.data.MonsterData;
import org.aestia.db.statique.data.Mountpark_dataData;
import org.aestia.db.statique.data.Mounts_dataData;
import org.aestia.db.statique.data.NpcData;
import org.aestia.db.statique.data.Npc_questionData;
import org.aestia.db.statique.data.Npc_reponses_actionData;
import org.aestia.db.statique.data.Npc_templateData;
import org.aestia.db.statique.data.ObjectsactionData;
import org.aestia.db.statique.data.ParoliData;
import org.aestia.db.statique.data.PetData;
import org.aestia.db.statique.data.Pets_dataData;
import org.aestia.db.statique.data.PlayerData;
import org.aestia.db.statique.data.Quest_dataData;
import org.aestia.db.statique.data.Quest_etapeData;
import org.aestia.db.statique.data.Quest_objectifData;
import org.aestia.db.statique.data.Quest_persoData;
import org.aestia.db.statique.data.RuneData;
import org.aestia.db.statique.data.SchemafightData;
import org.aestia.db.statique.data.Scripted_cellData;
import org.aestia.db.statique.data.ServerData;
import org.aestia.db.statique.data.SortData;
import org.aestia.db.statique.data.Subarea_dataData;
import org.aestia.db.statique.data.TutorielData;
import org.aestia.db.statique.data.ZaapData;
import org.aestia.db.statique.data.ZaapiData;
import org.aestia.kernel.Console;
import org.aestia.kernel.Main;

public class Dbstatique {
	private Connection connection;

	private AccountData accountData;
	private AnimationData animationData;
	private Area_dataData area_dataData;
	private BanipData banipData;
	private ChallengeData challengeData;
	private CommandeData commandeData;
	private CoffreData coffreData;
	private CraftData craftData;
	private DonjonData donjonData;
	private DropData dropData;
	private Endfight_actionData endfight_actionData;
	private ExperienceData experienceData;
	private Extra_monsterData extra_monsterData;
	private Full_morphData full_morphData;
	private GiftData giftData;
	private GroupeData groupeData;
	private HdvData hdvData;
	private HouseData houseData;
	private Interactive_objects_dataData interactive_objects_dataData;
	private Item_templateData item_templateData;
	private ItemData itemData;
	private ItemsetData itemsetData;
	private Jobs_dataData jobs_dataData;
	private MapData mapData;
	private Mobgroups_fixData mobgroups_fixData;
	private MonsterData monsterData;
	private Mountpark_dataData mountpark_dataData;
	private Mounts_dataData mounts_dataData;
	private Npc_questionData npc_questionData;
	private Npc_reponses_actionData npc_reponses_actionData;
	private Npc_templateData npc_templateData;
	private NpcData npcData;
	private ObjectsactionData objectsactionData;
	private ParoliData paroliData;
	private PetData petData;
	private Pets_dataData pets_dataData;
	private PlayerData playerData;
	private Quest_dataData quest_dataData;
	private Quest_etapeData quest_etapeData;
	private Quest_objectifData quest_objectifData;
	private Quest_persoData quest_persoData;
	private RuneData runeData;
	private SchemafightData schemafightData;
	private Scripted_cellData scripted_cellData;
	private ServerData serverData;
	private Subarea_dataData subarea_dataData;
	private SortData sortData;
	private TutorielData tutorielData;
	private ZaapData zaapData;
	private ZaapiData zaapiData;

	public void initializeData() {
		this.accountData = new AccountData(this.connection);
		this.animationData = new AnimationData(this.connection);
		this.area_dataData = new Area_dataData(this.connection);
		this.banipData = new BanipData(this.connection);
		this.challengeData = new ChallengeData(this.connection);
		this.commandeData = new CommandeData(this.connection);
		this.coffreData = new CoffreData(this.connection);
		this.craftData = new CraftData(this.connection);
		this.donjonData = new DonjonData(this.connection);
		this.dropData = new DropData(this.connection);
		this.endfight_actionData = new Endfight_actionData(this.connection);
		this.experienceData = new ExperienceData(this.connection);
		this.extra_monsterData = new Extra_monsterData(this.connection);
		this.full_morphData = new Full_morphData(this.connection);
		this.giftData = new GiftData(this.connection);
		this.groupeData = new GroupeData(this.connection);
		this.hdvData = new HdvData(this.connection);
		this.houseData = new HouseData(this.connection);
		this.interactive_objects_dataData = new Interactive_objects_dataData(this.connection);
		this.item_templateData = new Item_templateData(this.connection);
		this.itemData = new ItemData(this.connection);
		this.itemsetData = new ItemsetData(this.connection);
		this.jobs_dataData = new Jobs_dataData(this.connection);
		this.mapData = new MapData(this.connection);
		this.mobgroups_fixData = new Mobgroups_fixData(this.connection);
		this.monsterData = new MonsterData(this.connection);
		this.mountpark_dataData = new Mountpark_dataData(this.connection);
		this.mounts_dataData = new Mounts_dataData(this.connection);
		this.npc_questionData = new Npc_questionData(this.connection);
		this.npc_reponses_actionData = new Npc_reponses_actionData(this.connection);
		this.npc_templateData = new Npc_templateData(this.connection);
		this.npcData = new NpcData(this.connection);
		this.objectsactionData = new ObjectsactionData(this.connection);
		this.paroliData = new ParoliData(this.connection);
		this.petData = new PetData(this.connection);
		this.pets_dataData = new Pets_dataData(this.connection);
		this.playerData = new PlayerData(this.connection);
		this.quest_dataData = new Quest_dataData(this.connection);
		this.quest_etapeData = new Quest_etapeData(this.connection);
		this.quest_objectifData = new Quest_objectifData(this.connection);
		this.quest_persoData = new Quest_persoData(this.connection);
		this.runeData = new RuneData(this.connection);
		this.schemafightData = new SchemafightData(this.connection);
		this.scripted_cellData = new Scripted_cellData(this.connection);
		this.serverData = new ServerData(this.connection);
		this.subarea_dataData = new Subarea_dataData(this.connection);
		this.sortData = new SortData(this.connection);
		this.tutorielData = new TutorielData(this.connection);
		this.zaapData = new ZaapData(this.connection);
		this.zaapiData = new ZaapiData(this.connection);
	}

	public boolean initializeConnection() {
		try {
			String url = "jdbc:mysql://" + Main.loginHostDB + "/" + Main.loginNameDB;
			this.connection = DriverManager.getConnection(url, Main.loginUserDB, Main.loginPassDB);
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

	public AccountData getAccountData() {
		return this.accountData;
	}

	public PlayerData getPlayerData() {
		return this.playerData;
	}

	public Area_dataData getArea_dataData() {
		return this.area_dataData;
	}

	public AnimationData getAnimationData() {
		return this.animationData;
	}

	public BanipData getBanipData() {
		return this.banipData;
	}

	public ChallengeData getChallengeData() {
		return this.challengeData;
	}

	public CommandeData getCommandeData() {
		return this.commandeData;
	}

	public CoffreData getCoffreData() {
		return this.coffreData;
	}

	public CraftData getCraftData() {
		return this.craftData;
	}

	public DonjonData getDonjonData() {
		return this.donjonData;
	}

	public DropData getDropData() {
		return this.dropData;
	}

	public Endfight_actionData getEndfight_actionData() {
		return this.endfight_actionData;
	}

	public ExperienceData getExperienceData() {
		return this.experienceData;
	}

	public Extra_monsterData getExtra_monsterData() {
		return this.extra_monsterData;
	}

	public Full_morphData getFull_morphData() {
		return this.full_morphData;
	}

	public GiftData getGiftData() {
		return this.giftData;
	}

	public GroupeData getGroupeData() {
		return this.groupeData;
	}

	public HdvData getHdvData() {
		return this.hdvData;
	}

	public HouseData getHouseData() {
		return this.houseData;
	}

	public Interactive_objects_dataData getInteractive_objects_dataData() {
		return this.interactive_objects_dataData;
	}

	public Item_templateData getItem_templateData() {
		return this.item_templateData;
	}

	public ItemData getItemData() {
		return this.itemData;
	}

	public ItemsetData getItemsetData() {
		return this.itemsetData;
	}

	public Jobs_dataData getJobs_dataData() {
		return this.jobs_dataData;
	}

	public MapData getMapData() {
		return this.mapData;
	}

	public Mobgroups_fixData getMobgroups_fixData() {
		return this.mobgroups_fixData;
	}

	public MonsterData getMonsterData() {
		return this.monsterData;
	}

	public Mountpark_dataData getMountpark_dataData() {
		return this.mountpark_dataData;
	}

	public Mounts_dataData getMounts_dataData() {
		return this.mounts_dataData;
	}

	public Npc_questionData getNpc_questionData() {
		return this.npc_questionData;
	}

	public Npc_reponses_actionData getNpc_reponses_actionData() {
		return this.npc_reponses_actionData;
	}

	public Npc_templateData getNpc_templateData() {
		return this.npc_templateData;
	}

	public NpcData getNpcData() {
		return this.npcData;
	}

	public ObjectsactionData getObjectsactionData() {
		return this.objectsactionData;
	}

	public ParoliData getParoliData() {
		return this.paroliData;
	}

	public PetData getPetData() {
		return this.petData;
	}

	public Pets_dataData getPets_dataData() {
		return this.pets_dataData;
	}

	public Quest_dataData getQuest_dataData() {
		return this.quest_dataData;
	}

	public Quest_etapeData getQuest_etapeData() {
		return this.quest_etapeData;
	}

	public Quest_objectifData getQuest_objectifData() {
		return this.quest_objectifData;
	}

	public Quest_persoData getQuest_persoData() {
		return this.quest_persoData;
	}

	public RuneData getRuneData() {
		return this.runeData;
	}

	public SchemafightData getSchemafightData() {
		return this.schemafightData;
	}

	public Scripted_cellData getScripted_cellData() {
		return this.scripted_cellData;
	}

	public ServerData getServerData() {
		return this.serverData;
	}

	public Subarea_dataData getSubarea_dataData() {
		return this.subarea_dataData;
	}

	public SortData getSortData() {
		return this.sortData;
	}

	public TutorielData getTutorielData() {
		return this.tutorielData;
	}

	public ZaapData getZaapData() {
		return this.zaapData;
	}

	public ZaapiData getZaapiData() {
		return this.zaapiData;
	}
}

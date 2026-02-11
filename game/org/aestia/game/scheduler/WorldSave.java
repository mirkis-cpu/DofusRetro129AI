// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game.scheduler;

import java.util.concurrent.TimeUnit;

import org.aestia.client.Account;
import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.entity.Collector;
import org.aestia.entity.Dragodinde;
import org.aestia.entity.Prism;
import org.aestia.game.GameServer;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.kernel.Main;
import org.aestia.map.MountPark;
import org.aestia.other.Guild;
import org.aestia.other.House;
import org.aestia.other.Trunk;

public class WorldSave extends Manageable {
	@Override
	public void run() {
		if (!Main.isSaving) {
			cast(0);
		}
	}

	@Override
	public void launch() {
		GlobalManager.worldSheduler.scheduleWithFixedDelay(this, 30L, 30L, TimeUnit.MINUTES);
	}

	public static void cast(final int trys) {
		GameServer.setState(2);
		Label_0913: {
			try {
				Console.println("Lancement de la sauvegarde :", Console.Color.GAME);
				SocketManager.GAME_SEND_Im_PACKET_TO_ALL("1164;");
				Main.isSaving = true;
				Console.println("-> des comptes.", Console.Color.GAME);
				for (final Account account : World.getAccounts().values()) {
					if (account == null) {
						continue;
					}
					Database.getStatique().getAccountData().update(account);
				}
				Console.println("-> des personnages.", Console.Color.GAME);
				for (final Player player : World.getPlayers().values()) {
					if (player == null) {
						continue;
					}
					if (!player.isOnline()) {
						continue;
					}
					Database.getStatique().getPlayerData().update(player, true);
				}
				Console.println("-> des prismes.", Console.Color.GAME);
				for (final Prism prism : World.getPrisms().values()) {
					if (World.getMap(prism.getMap()).getSubArea().getPrismId() != prism.getId()) {
						Database.getGame().getPrismeData().delete(prism.getId());
					} else {
						Database.getGame().getPrismeData().update(prism);
					}
				}
				Console.println("-> des dragodindes.", Console.Color.GAME);
				for (final Dragodinde mount : World.getMounts().values()) {
					if (mount.getId() > 0) {
						Database.getStatique().getMounts_dataData().update(mount, true);
					}
				}
				Console.println("-> des guildes.", Console.Color.GAME);
				for (final Guild guilde : World.getGuilds().values()) {
					Database.getGame().getGuildData().update(guilde);
				}
				Console.println("-> des membres de guilde.", Console.Color.GAME);
				for (final Player player : World.getPlayers().values()) {
					if (player.isOnline() && player.getGuildMember() != null) {
						Database.getGame().getGuild_memberData().update(player);
					}
				}
				Console.println("-> des percepteurs.", Console.Color.GAME);
				for (final Collector collector : World.getCollectors().values()) {
					if (collector.getInFight() <= 0) {
						Database.getGame().getPercepteurData().update(collector);
					}
				}
				Console.println("-> des maisons.", Console.Color.GAME);
				for (final House house : World.getHouses().values()) {
					if (house.getOwnerId() > 0) {
						Database.getGame().getHouseData().update(house);
					}
				}
				Console.println("-> des coffres.", Console.Color.GAME);
				for (final Trunk trunk : World.getTrunks().values()) {
					Database.getGame().getCoffreData().update(trunk);
				}
				Console.println("-> des enclos.", Console.Color.GAME);
				for (final MountPark mp : World.getMountparks().values()) {
					if (mp.getOwner() > 0 || mp.getOwner() == -1) {
						Database.getGame().getMountpark_dataData().update(mp);
					}
				}
				Console.println("-> des zones.", Console.Color.GAME);
				for (final World.Area area : World.getAreas().values()) {
					Database.getGame().getArea_dataData().update(area);
				}
				for (final World.SubArea subarea : World.getSubAreas().values()) {
					Database.getGame().getSubarea_dataData().update(subarea);
				}
				Console.println("Sauvegarde effectuee !", Console.Color.GAME);
				SocketManager.GAME_SEND_Im_PACKET_TO_ALL("1165;");
			} catch (Exception exception) {
				exception.printStackTrace();
				Console.println("Erreur de sauvegarde : " + exception.getMessage(), Console.Color.ERROR);
				if (trys < 10) {
					Console.println("Echec de la sauvegarde. Relancement numero " + (trys + 1) + ".",
							Console.Color.ERROR);
					cast(trys + 1);
					return;
				}
				Console.println("Trop d echec de sauvegarde.", Console.Color.ERROR);
				Main.isSaving = false;
				break Label_0913;
			} finally {
				Main.isSaving = false;
			}
			Main.isSaving = false;
		}
		GameServer.setState(1);
		Console.refreshTitle();
	}
}

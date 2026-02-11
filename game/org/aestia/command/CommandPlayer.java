// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.command;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.game.GameClient;
import org.aestia.game.world.World;
import org.aestia.kernel.Boutique;
import org.aestia.kernel.Config;
import org.aestia.kernel.Main;

public class CommandPlayer {
	private static String canal;

	static {
		CommandPlayer.canal = "Taverne";
	}

	public static boolean analyse(final Player perso, final String msg) {
		if (msg.charAt(0) != '.' || msg.charAt(1) == '.') {
			return false;
		}
		if(msg.length() > 7 && msg.substring(1, 7).equalsIgnoreCase("parcho")) {
			int points = perso.getAccount().getPoints() - 250;
			if(points < 0) {
				perso.sendMessage("Il vous manque <b>" + (250 - perso.getAccount().getPoints()) + "</b> points boutique pour effectuer cet achat");
				return true;
			}
			
			Player player = perso;
			player.getStatsParcho().getMap().clear();
			player.getStatsParcho().addOneStat(125, 101);
			player.getStatsParcho().addOneStat(124, 101);
			player.getStatsParcho().addOneStat(118, 101);
			player.getStatsParcho().addOneStat(126, 101);
			player.getStatsParcho().addOneStat(119, 101);
			player.getStatsParcho().addOneStat(123, 101);
			
			SocketManager.GAME_SEND_STATS_PACKET(player);
			player.sendMessage("Vous êtes maintenant parchoté 101 dans tous les éléments");
			perso.getAccount().setPoints(points);
			return true;
		} 
		
		if(msg.length() > 7 && msg.substring(1, 7).equalsIgnoreCase("restat")) {
			int points = perso.getAccount().getPoints() - 180;
			if(points < 0) {
				perso.sendMessage("Il vous manque <b>" + (180 - perso.getAccount().getPoints()) + "</b> points boutique pour effectuer cet achat");
				return true;
			}
			
			perso.getStatsParcho().getMap().clear();
			perso.getStats().addOneStat(125,-perso.getStats().getEffect(125));
			perso.getStats().addOneStat(124,-perso.getStats().getEffect(124));
			perso.getStats().addOneStat(118,-perso.getStats().getEffect(118));
			perso.getStats().addOneStat(123,-perso.getStats().getEffect(123));
			perso.getStats().addOneStat(119,-perso.getStats().getEffect(119));
			perso.getStats().addOneStat(126,-perso.getStats().getEffect(126));
			perso.setCapital((perso.getLevel() * 5) - 5);
			SocketManager.GAME_SEND_STATS_PACKET(perso);
			SocketManager.GAME_SEND_Im_PACKET(perso,"023;" + (perso.getLevel() * 5 - 5));
			return true;
		} 
		
		if(msg.length() > 4 && msg.substring(1, 5).equalsIgnoreCase("obvi")) {
			int points = perso.getAccount().getPoints() - 210;
			if(points < 0) {
				perso.sendMessage("Il vous manque <b>" + (210 - perso.getAccount().getPoints()) + "</b> points boutique pour effectuer cet achat");
			} else {
				org.aestia.object.Object object = World.getObjTemplate(9233).createNewItem(1, false);
				org.aestia.object.Object object2 = World.getObjTemplate(9234).createNewItem(1, false);
				if(perso.addObjet(object,true))
					World.addObjet(object, true);
				
				if(perso.addObjet(object2,true))
					World.addObjet(object2, true);
				perso.sendMessage("Vous venez de recevoir vos objets vivants avec succès");
				perso.getAccount().setPoints(points);
			}
			return true;
		} 
		
		
		
		if(msg.length() > 6 && msg.substring(1, 7).equalsIgnoreCase("points")) {
			perso.sendMessage("Vous avez <b>" + perso.getAccount().getPoints() + "</b> points boutique");
			return true;
		} 
		
		
		if(msg.length() > 8 && msg.substring(1, 9).equalsIgnoreCase("boutique")) {
			Boutique.open(perso);
			return true;
		} 
		if (msg.length() > 3 && msg.substring(1, 4).equalsIgnoreCase("all")) {
			if (perso.noall) {
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Votre canal " + CommandPlayer.canal + " est d\u00e9sactiv\u00e9.", "C35617");
				return true;
			}
			final String prefix = "[" + new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()))
					+ "] (" + CommandPlayer.canal + ") <b><a href='asfunction:onHref,ShowPlayerPopupMenu,"
					+ perso.getName() + "'>" + perso.getName() + "</a></b> : ";
			for (final Player p : World.getOnlinePersos()) {
				if (!p.noall) {
					SocketManager.GAME_SEND_MESSAGE(p, String.valueOf(prefix) + msg.substring(5, msg.length() - 1),
							"C35617");
				}
			}
			return true;
		} else {
			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("noall")) {
				if (perso.noall) {
					perso.noall = false;
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Vous avez activ\u00e9 le canal " + CommandPlayer.canal + ".", "C35617");
				} else {
					perso.noall = true;
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Vous avez d\u00e9sactiv\u00e9 le canal " + CommandPlayer.canal + ".", "C35617");
				}
				return true;
			}
			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("staff")) {
				String message = "Liste des membres du staff connect\u00e9s :";
				boolean vide = true;
				for (final GameClient client : Main.gameServer.getClients().values()) {
					final Player player = client.getPersonnage();
					if (player == null) {
						continue;
					}
					if (player.getGroupe() == null) {
						continue;
					}
					if (player.isInvisible()) {
						continue;
					}
					message = String.valueOf(message) + "\n- <b><a href='asfunction:onHref,ShowPlayerPopupMenu,"
							+ player.getName() + "'>[" + player.getGroupe().getNom() + "] " + player.getName()
							+ "</a></b>";
					vide = false;
				}
				if (vide) {
					message = "Il n'y a aucun membre du staff connect\u00e9. Vous pouvez tout de m\u00eame allez voir sur notre <a href='http://aestia.fr/ts3.php'><b>TeamSpeak</b></a>.";
				}
				SocketManager.GAME_SEND_MESSAGE(perso, message);
				return true;
			}
			if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("deblo")) {
				if (perso.isInPrison()) {
					return true;
				}
				if (perso.cantTP()) {
					return true;
				}
				if (System.currentTimeMillis() - perso.restriction.timeDeblo < Config.getInstance().delayDeblo) {
					SocketManager.GAME_SEND_MESSAGE(perso,
							"Il faut que tu attendes encore "
									+ (Config.getInstance().delayDeblo / 60000L
											- (System.currentTimeMillis() - perso.restriction.timeDeblo) / 60L / 1000L)
									+ " minute(s).");
					return true;
				}
				if (perso.get_fight() != null) {
					return true;
				}
				perso.setTimeDeblo(System.currentTimeMillis());
				perso.teleport((short) 7411, 311);
				return true;
			} else {
				if (msg.length() > 5 && msg.substring(1, 6).equalsIgnoreCase("infos")) {
					long uptime = System.currentTimeMillis() - Config.getInstance().startTime;
					final int jour = (int) (uptime / 86400000L);
					uptime %= 86400000L;
					final int hour = (int) (uptime / 3600000L);
					uptime %= 3600000L;
					final int min = (int) (uptime / 60000L);
					uptime %= 60000L;
					final int sec = (int) (uptime / 1000L);
					final int nbPlayer = Main.gameServer.getPlayerNumber();
					final int nbPlayerIp = Main.gameServer.getPlayerNumberByIp();
					final int maxPlayer = Main.gameServer.getMaxPlayer();
					String mess = "<b>" + Config.getInstance().NAME + "</b>\n" + "Uptime : " + jour + "j " + hour + "h "
							+ min + "m " + sec + "s.";
					if (nbPlayer > 0) {
						mess = String.valueOf(mess) + "\nJoueurs en ligne : " + nbPlayer;
					}
					if (nbPlayerIp > 0) {
						mess = String.valueOf(mess) + "\nJoueurs uniques en ligne : " + nbPlayerIp;
					}
					if (maxPlayer > 0) {
						mess = String.valueOf(mess) + "\nRecord de connexion : " + maxPlayer;
					}
					SocketManager.GAME_SEND_MESSAGE(perso, mess);
					return true;
				}
				SocketManager.GAME_SEND_MESSAGE(perso,
						"Les commandes disponnible sont :\n<b>.infos</b> - Permet d'obtenir des informations sur le serveur.\n<b>.deblo</b> - Permet de vous d\u00e9bloquer en vous t\u00e9l\u00e9portant au zaap d'Astrub.\n<b>.staff</b> - Permet de voir les membres du staff connect\u00e9s.\n<b>.all</b> - Permet d'envoyer un message \u00e0 tous les joueurs.\n<b>.noall</b> - Permet de ne plus recevoir les messages du canal .all.");
				return true;
			}
		}
	}
}

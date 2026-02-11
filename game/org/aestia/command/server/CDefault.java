// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.command.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.aestia.client.Account;
import org.aestia.client.Player;
import org.aestia.command.Command;
import org.aestia.common.CryptManager;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.entity.PetEntry;
import org.aestia.entity.monster.Monster;
import org.aestia.entity.npc.Npc;
import org.aestia.entity.npc.NpcTemplate;
import org.aestia.fight.Fight;
import org.aestia.game.GameClient;
import org.aestia.game.GameServer;
import org.aestia.game.scheduler.WorldSave;
import org.aestia.game.world.World;
import org.aestia.job.JobStat;
import org.aestia.kernel.Config;
import org.aestia.kernel.Console;
import org.aestia.kernel.Main;
import org.aestia.map.Case;
import org.aestia.map.MountPark;
import org.aestia.map.SchemaFight;
import org.aestia.object.ObjectSet;
import org.aestia.object.ObjectTemplate;
import org.aestia.quest.Quest;
import org.aestia.quest.Quest_Etape;

public class CDefault extends Command {
	public CDefault(final Player player) {
		super(player);
	}

	@Override
	public void apply(final String packet) {
		final String msg = packet.substring(2);
		final String[] infos = msg.split(" ");
		if (infos.length == 0) {
			return;
		}
		final String command = infos[0];
		try {
			final Groupes groupe = this.getPlayer().getGroupe();
			if (groupe == null) {
				this.getClient().kick();
				return;
			}
			if (!groupe.haveCommande(command)) {
				SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "Commande invalide !");
				return;
			}
			this.command(command, infos, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void command(final String command, String[] infos, final String msg) {
		if (command.equalsIgnoreCase("LOG")) {
			Main.modDebug = !Main.modDebug;
			SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
					"Les logs console sont : " + (Main.modDebug ? "activ\u00e9" : "d\u00e9sactiv\u00e9"));
			return;
		}
		if (command.equalsIgnoreCase("HELP")) {
			String cmd = "";
			try {
				cmd = infos[1];
			} catch (Exception ex) {
			}
			if (cmd.equalsIgnoreCase("")) {
				SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
						"\nVous avez actuellement le groupe GM " + this.getPlayer().getGroupe().getNom()
								+ ".\nCommandes disponibles :\n");
				for (final Commandes commande : this.getPlayer().getGroupe().getCommandes()) {
					final String args = (commande.getArguments().get(1) != null
							&& !commande.getArguments().get(1).equalsIgnoreCase(""))
									? (" + " + commande.getArguments().get(1)) : "";
					final String desc = (commande.getArguments().get(2) != null
							&& !commande.getArguments().get(2).equalsIgnoreCase("")) ? commande.getArguments().get(2)
									: "";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
							"<u>" + commande.getArguments().get(0) + args + "</u> - " + desc);
				}
				return;
			}
			SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "\nVous avez actuellement le groupe GM "
					+ this.getPlayer().getGroupe().getNom() + ".\nCommandes recherch\u00e9s :\n");
			for (final Commandes commande : this.getPlayer().getGroupe().getCommandes()) {
				if (commande.getArguments().get(0).contains(cmd.toUpperCase())) {
					final String args = (commande.getArguments().get(1) != null
							&& !commande.getArguments().get(1).equalsIgnoreCase(""))
									? (" + " + commande.getArguments().get(1)) : "";
					final String desc = (commande.getArguments().get(2) != null
							&& !commande.getArguments().get(2).equalsIgnoreCase("")) ? commande.getArguments().get(2)
									: "";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
							"<u>" + commande.getArguments().get(0) + args + "</u> - " + desc);
				}
			}
		} else {
			if (command.equalsIgnoreCase("ONLINE")) {
				Player perso = this.getPlayer();
				if (infos.length > 1) {
					try {
						perso = World.getPersoByName(infos[1]);
					} catch (Exception ex2) {
					}
					if (perso == null) {
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
								"Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9");
						return;
					}
				}
				if (perso.getGameClient() != null) {
					perso.getGameClient().kick();
				}
				perso.set_Online(false);
				perso.resetVars();
				Database.getStatique().getPlayerData().update(perso, true);
				SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso.getCurMap(), perso.getId());
				World.unloadPerso(perso);
				final String str = "Le joueur " + perso.getName()
						+ " a \u00e9t\u00e9 r\u00e9initialis\u00e9 de ces variables.";
				SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
				return;
			}
			if (command.equalsIgnoreCase("ANAME")) {
				infos = msg.split(" ", 2);
				final String prefix = "<b><a href='asfunction:onHref,ShowPlayerPopupMenu," + this.getPlayer().getName()
						+ "'>[" + this.getPlayer().getGroupe().getNom() + "] " + this.getPlayer().getName()
						+ "</a></b>";
				String suffix = infos[1];
				if (suffix.contains("<") && (!suffix.contains(">") || !suffix.contains("</"))) {
					suffix = suffix.replace("<", "").replace(">", "");
				}
				if (suffix.contains("<") && suffix.contains(">") && !suffix.contains("</")) {
					suffix = suffix.replace("<", "").replace(">", "");
				}
				SocketManager.GAME_SEND_Im_PACKET_TO_ALL("116;" + prefix + "~" + suffix);
				return;
			}
			if (command.equalsIgnoreCase("GONAME") || command.equalsIgnoreCase("JOIN")
					|| command.equalsIgnoreCase("GON")) {
				final Player P = World.getPersoByName(infos[1]);
				if (P == null) {
					final String str = "Le personnage de destination n'\u00e9xiste pas.";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
					return;
				}
				final short mapID = P.getCurMap().getId();
				final int cellID = P.getCurCell().getId();
				Player perso2 = this.getPlayer();
				if (infos.length > 2) {
					perso2 = World.getPersoByName(infos[2]);
					if (perso2 == null) {
						final String str2 = "Le personnage \u00e0 t\u00e9l\u00e9porter n'a pas \u00e9t\u00e9 trouv\u00e9.";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str2);
						return;
					}
					if (perso2.get_fight() != null) {
						final String str2 = "La cible \u00e0 t\u00e9l\u00e9porter est en combat.";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str2);
						return;
					}
				}
				perso2.teleport(mapID, cellID);
				final String str2 = "Le joueur " + perso2.getName() + " a \u00e9t\u00e9 teleport\u00e9 vers "
						+ P.getName() + ".";
				SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str2);
			} else if (command.equalsIgnoreCase("KICKFIGHT")) {
				final Player P = World.getPersoByName(infos[1]);
				if (P == null || P.get_fight() == null) {
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
							"Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9 ou il n'est pas en combat.");
					return;
				}
				SocketManager.GAME_SEND_GV_PACKET(P);
				if (P.get_fight() != null) {
					P.get_fight().leftFight(P, null);
					P.set_fight(null);
				}
				SocketManager.GAME_SEND_GV_PACKET(P);
				SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
						"Le personnage " + P.getName() + " a \u00e9t\u00e9 expuls\u00e9 de son combat.");
			} else if (command.equalsIgnoreCase("DEBUG")) {
				Player perso = this.getPlayer();
				if (infos.length <= 1) {
					return;
				}
				perso = World.getPersoByName(infos[1]);
				if (perso == null) {
					final String str = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
					return;
				}
				if (perso.get_fight() != null) {
					final String str = "La cible est en combat.";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
					return;
				}
				perso.warpToSavePos();
				final String str = "Le joueur " + perso.getName()
						+ " a \u00e9t\u00e9 teleport\u00e9 \u00e0 son point de sauvegarde.";
				SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
			} else {
				if (command.equalsIgnoreCase("JOBLEFT")) {
					Player perso = this.getPlayer();
					try {
						perso = World.getPersoByName(infos[1]);
					} catch (Exception ex3) {
					}
					if (perso == null) {
						perso = this.getPlayer();
					}
					perso.setDoAction(false);
					perso.setCurJobAction(null);
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
							"L'action de m\u00e9tier \u00e0 \u00e9t\u00e9 annul\u00e9.");
					return;
				}
				if (command.equalsIgnoreCase("WHO")) {
					String mess = "\n<u>Liste des joueurs en ligne :</u>";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
					int i = 0;
					for (final GameClient client : Main.gameServer.getClients().values()) {
						if (i == 30) {
							break;
						}
						++i;
						final Player player = client.getPersonnage();
						if (player == null) {
							continue;
						}
						mess = String.valueOf(player.getName()) + " (" + player.getId() + ") ";
						mess = String.valueOf(mess) + HDefault.returnClasse(player.getClasse());
						mess = String.valueOf(mess) + " ";
						mess = String.valueOf(mess) + ((player.getSexe() == 0) ? "M" : "F") + " ";
						mess = String.valueOf(mess) + player.getLevel() + " ";
						mess = String.valueOf(mess) + player.getCurMap().getId() + "(" + player.getCurMap().getX() + "/"
								+ player.getCurMap().getY() + ") ";
						mess = String.valueOf(mess) + ((player.get_fight() == null) ? "" : "Combat ");
						mess = String.valueOf(mess) + player.getAccount().getCurIP();
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
					}
					if (Main.gameServer.getClients().size() - 30 > 0) {
						mess = "Et " + (Main.gameServer.getClients().size() - 30) + " autres personnages";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
					}
					mess = "\n";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
					return;
				}
				if (command.equalsIgnoreCase("WHOALL")) {
					String mess = "\n<u>Liste des joueurs en ligne :</u>";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
					for (final GameClient client2 : Main.gameServer.getClients().values()) {
						final Player player2 = client2.getPersonnage();
						if (player2 == null) {
							continue;
						}
						mess = String.valueOf(player2.getName()) + " (" + player2.getId() + ") ";
						mess = String.valueOf(mess) + HDefault.returnClasse(player2.getClasse());
						mess = String.valueOf(mess) + " ";
						mess = String.valueOf(mess) + ((player2.getSexe() == 0) ? "M" : "F") + " ";
						mess = String.valueOf(mess) + player2.getLevel() + " ";
						mess = String.valueOf(mess) + player2.getCurMap().getId() + "(" + player2.getCurMap().getX()
								+ "/" + player2.getCurMap().getY() + ") ";
						mess = String.valueOf(mess) + ((player2.get_fight() == null) ? "" : "Combat ");
						mess = String.valueOf(mess) + player2.getAccount().getCurIP();
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
					}
					mess = "\n";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
					return;
				}
				if (command.equalsIgnoreCase("WHOFIGHT")) {
					String mess = "";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
							"\n<u>Liste des joueurs en ligne et en combat :</u>");
					for (final GameClient client2 : Main.gameServer.getClients().values()) {
						final Player player2 = client2.getPersonnage();
						if (player2 == null) {
							continue;
						}
						if (player2.get_fight() == null) {
							continue;
						}
						mess = String.valueOf(player2.getName()) + " (" + player2.getId() + ") ";
						mess = String.valueOf(mess) + HDefault.returnClasse(player2.getClasse());
						mess = String.valueOf(mess) + " ";
						mess = String.valueOf(mess) + ((player2.getSexe() == 0) ? "M" : "F") + " ";
						mess = String.valueOf(mess) + player2.getLevel() + " ";
						mess = String.valueOf(mess) + player2.getCurMap().getId() + "(" + player2.getCurMap().getX()
								+ "/" + player2.getCurMap().getY() + ") ";
						mess = String.valueOf(mess) + ((player2.get_fight() == null) ? "" : "Combat ");
						mess = String.valueOf(mess) + player2.getAccount().getCurIP();
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
					}
					if (mess.equalsIgnoreCase("")) {
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "Aucun joueur en combat.");
					} else {
						mess = "\n";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
					}
					return;
				}
				if (command.equalsIgnoreCase("NAMEGO") || command.equalsIgnoreCase("NGO")) {
					final Player perso = World.getPersoByName(infos[1]);
					if (perso == null) {
						final String str = "Le personnage \u00e0 t\u00e9l\u00e9porter n'\u00e9xiste pas.";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
						return;
					}
					if (perso.get_fight() != null) {
						final String str = "Le personnage \u00e0 t\u00e9l\u00e9porter est en combat.";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
						return;
					}
					Player P2 = this.getPlayer();
					if (infos.length > 2) {
						P2 = World.getPersoByName(infos[2]);
						if (P2 == null) {
							final String str3 = "Le personnage de destination n'a pas \u00e9t\u00e9 trouv\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
							return;
						}
					}
					if (P2.isOnline()) {
						final short mapID2 = P2.getCurMap().getId();
						final int cellID2 = P2.getCurCell().getId();
						perso.teleport(mapID2, cellID2);
						final String str2 = "Le joueur " + perso.getName() + " a \u00e9t\u00e9 teleport\u00e9 vers "
								+ P2.getName() + ".";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str2);
					} else {
						final String str3 = "Le joueur " + P2.getName() + " n'est pas en ligne.";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
					}
				} else if (command.equalsIgnoreCase("TP")) {
					short mapID3 = -1;
					int cellID3 = -1;
					try {
						mapID3 = Short.parseShort(infos[1]);
						cellID3 = Integer.parseInt(infos[2]);
					} catch (Exception ex4) {
					}
					if (mapID3 == -1 || cellID3 == -1 || World.getMap(mapID3) == null) {
						String str3 = "";
						if (mapID3 == -1 || World.getMap(mapID3) == null) {
							str3 = "MapID invalide.";
						} else {
							str3 = "cellID invalide.";
						}
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
						return;
					}
					if (World.getMap(mapID3).getCase(cellID3) == null) {
						final String str3 = "cellID invalide.";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
						return;
					}
					Player perso3 = this.getPlayer();
					if (infos.length > 3) {
						perso3 = World.getPersoByName(infos[3]);
						if (perso3 == null || perso3.get_fight() != null) {
							final String str4 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9 ou est en combat";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str4);
							return;
						}
					}
					perso3.teleport(mapID3, cellID3);
					final String str4 = "Le joueur " + perso3.getName() + " a \u00e9t\u00e9 teleport\u00e9.";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str4);
				} else if (command.equalsIgnoreCase("SIZE")) {
					int size = -1;
					try {
						size = Integer.parseInt(infos[1]);
					} catch (Exception ex5) {
					}
					if (size == -1) {
						final String str = "Taille invalide.";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
						return;
					}
					Player perso4 = this.getPlayer();
					if (infos.length > 2) {
						perso4 = World.getPersoByName(infos[2]);
						if (perso4 == null) {
							final String str3 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
							return;
						}
					}
					perso4.set_size(size);
					SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(perso4.getCurMap(), perso4.getId());
					SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(perso4.getCurMap(), perso4);
					final String str3 = "La taille du joueur " + perso4.getName() + " a \u00e9t\u00e9 modifi\u00e9e.";
					SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
				} else if (command.equalsIgnoreCase("FREEZE")) {
					Player perso = this.getPlayer();
					if (infos.length > 1) {
						perso = World.getPersoByName(infos[1]);
					}
					if (perso == null) {
						final String mess2 = "Le personnage n'existe pas.";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess2);
						return;
					}
					if (perso.getBlockMovement()) {
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
								"Le joueur n'est plus bloqu\u00e9.");
					} else {
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "Le joueur est bloqu\u00e9.");
					}
					perso.setBlockMovement(!perso.getBlockMovement());
				} else {
					if (command.equalsIgnoreCase("BLOCKMAP")) {
						int j = -1;
						try {
							j = Short.parseShort(infos[1]);
						} catch (Exception ex6) {
						}
						if (j == 0) {
							Main.mapAsBlocked = false;
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "Map d\u00e9block\u00e9.");
						} else if (j == 1) {
							Main.mapAsBlocked = true;
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "Map block\u00e9.");
						} else {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "Aucune information.");
						}
						return;
					}
					if (command.equalsIgnoreCase("BLOCKFIGHT")) {
						int j = -1;
						try {
							j = Short.parseShort(infos[1]);
						} catch (Exception ex7) {
						}
						if (j == 0) {
							Main.fightAsBlocked = false;
							SocketManager.PACKET_POPUP_ALL("Il est maintenant possible de rentrer en combat !");
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Les combats ont \u00e9t\u00e9s d\u00e9bloqu\u00e9s.");
						} else if (j == 1) {
							Main.fightAsBlocked = true;
							SocketManager.PACKET_POPUP_ALL("Il est maintenant impossible de rentrer en combat !");
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Les combats ont \u00e9t\u00e9s bloqu\u00e9s.");
						} else {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "Aucune information.");
						}
						return;
					}
					if (command.equalsIgnoreCase("MUTE")) {
						Player perso = this.getPlayer();
						String message = "";
						String name = null;
						int time = 0;
						try {
							name = infos[1];
							time = Integer.parseInt(infos[2]);
							if (infos.length >= 4) {
								message = msg.split(" ", 4)[3];
							}
						} catch (Exception ex8) {
						}
						perso = World.getPersoByName(name);
						if (perso == null || time <= 0) {
							final String mess3 = "Le personnage n'existe pas ou la dur\u00e9e est invalide.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess3);
							return;
						}
						String mess3 = "Vous avez mut\u00e9 " + perso.getName() + " pour " + time + " minutes";
						if (perso.getAccount() == null) {
							mess3 = "Le personnage " + perso.getName() + " n'\u00e9tait pas connect\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess3);
							return;
						}
						perso.getAccount().mute(time, message, this.getPlayer().getName());
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess3);
						if (!perso.isOnline()) {
							mess3 = "Le personnage " + perso.getName() + " n'\u00e9tait pas connect\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess3);
						}
					} else if (command.equalsIgnoreCase("MUTEIP")) {
						Player perso = this.getPlayer();
						String message = "";
						String mess4 = "";
						String name2 = null;
						int time2 = 0;
						try {
							name2 = infos[1];
							time2 = Integer.parseInt(infos[2]);
							if (infos.length >= 4) {
								message = msg.split(" ", 4)[3];
							}
						} catch (Exception ex9) {
						}
						perso = World.getPersoByName(name2);
						if (perso == null || time2 <= 0) {
							mess4 = "Le personnage n'existe pas ou la dur\u00e9e est invalide.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
							return;
						}
						if (perso.getAccount() == null) {
							mess4 = "Le personnage " + perso.getName() + " n'\u00e9tait pas connect\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
							return;
						}
						final String IP = perso.getAccount().getLastIP();
						if (IP.equalsIgnoreCase("")) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "L'IP est invalide.");
							return;
						}
						for (final Map.Entry<Integer, Account> entry : World.getAccountsByIp(IP).entrySet()) {
							final Account a = entry.getValue();
							if (a == null) {
								continue;
							}
							if (!a.getLastIP().equalsIgnoreCase(IP)) {
								continue;
							}
							a.mute(time2, message, this.getPlayer().getName());
							if (a.getCurPerso() == null) {
								continue;
							}
							mess4 = "Vous avez mut\u00e9 " + a.getCurPerso().getName() + " pour " + time2 + " minutes";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
						}
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "Fin de la commande MUTEIP");
					} else if (command.equalsIgnoreCase("UNMUTEIP")) {
						Player perso = this.getPlayer();
						String mess2 = "";
						String name = null;
						try {
							name = infos[1];
						} catch (Exception ex10) {
						}
						perso = World.getPersoByName(name);
						if (perso == null) {
							mess2 = "Le personnage n'existe pas.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess2);
							return;
						}
						if (perso.getAccount() == null) {
							mess2 = "Le personnage " + perso.getName() + " n'\u00e9tait pas connect\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess2);
							return;
						}
						final String IP2 = perso.getAccount().getLastIP();
						if (IP2.equalsIgnoreCase("")) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "L'IP est invalide.");
							return;
						}
						for (final Map.Entry<Integer, Account> entry2 : World.getAccountsByIp(IP2).entrySet()) {
							final Account a2 = entry2.getValue();
							if (a2 == null) {
								continue;
							}
							if (!a2.getLastIP().equalsIgnoreCase(IP2)) {
								continue;
							}
							a2.unMute();
							if (a2.getCurPerso() == null) {
								continue;
							}
							mess2 = "Vous avez d\u00e9mut\u00e9 " + a2.getCurPerso().getName() + ".";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess2);
						}
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "Fin de la commande UNMUTEIP");
					} else if (command.equalsIgnoreCase("UNMUTE")) {
						Player perso = this.getPlayer();
						String name3 = null;
						try {
							name3 = infos[1];
						} catch (Exception ex11) {
						}
						perso = World.getPersoByName(name3);
						if (perso == null) {
							final String mess4 = "Le personnage n'existe pas.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
							return;
						}
						perso.getAccount().unMute();
						String mess4 = "Vous avez d\u00e9mut\u00e9 " + perso.getName();
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
						if (!perso.isOnline()) {
							mess4 = "Le personnage " + perso.getName() + " n'\u00e9tait pas connect\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
						}
					} else if (command.equalsIgnoreCase("MUTEMAP")) {
						if (this.getPlayer().getCurMap() == null) {
							return;
						}
						this.getPlayer().getCurMap().mute();
						String mess = "";
						if (this.getPlayer().getCurMap().isMute()) {
							mess = "Vous venez de muter la MAP.";
						} else {
							mess = "Vous venez de d\u00e9muter la MAP.";
						}
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
					} else if (command.equalsIgnoreCase("KICK")) {
						Player perso = this.getPlayer();
						String name3 = null;
						String razon = "";
						try {
							name3 = infos[1];
						} catch (Exception ex12) {
						}
						try {
							razon = msg.substring(infos[0].length() + infos[1].length() + 1);
						} catch (Exception ex13) {
						}
						perso = World.getPersoByName(name3);
						if (perso == null) {
							final String mess5 = "Le personnage n'\u00e9xiste pas.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess5);
							return;
						}
						if (perso.isOnline()) {
							final Player P3 = World.getPersoByName(infos[1]);
							if (razon != "") {
								final String msj = "Vous avez \u00e9t\u00e9 kick\u00e9 par la raison suivante : \n"
										+ razon + "\n\n" + this.getPlayer().getName();
								SocketManager.SEND_MESSAGE_DECO(P3, 18, msj);
							} else {
								final String msj = "Vous avez \u00e9t\u00e9 kick\u00e9.\n" + this.getPlayer().getName();
								SocketManager.SEND_MESSAGE_DECO(P3, 18, msj);
							}
							P3.getGameClient().kick();
							final String mess3 = "Vous avez kick\u00e9 " + perso.getName() + ".";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess3);
						} else {
							final String mess5 = "Le personnage " + perso.getName() + " n'est pas connect\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess5);
						}
					} else if (command.equalsIgnoreCase("JAIL")) {
						final short mapID3 = 666;
						final int cellID3 = HDefault.getCellJail();
						if (mapID3 == -1 || cellID3 == -1 || World.getMap(mapID3) == null) {
							String str3 = "MapID ou cellID invalide.";
							if (cellID3 == -1) {
								str3 = "cellID invalide.";
							} else {
								str3 = "MapID invalide.";
							}
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
							return;
						}
						if (World.getMap(mapID3).getCase(cellID3) == null) {
							final String str3 = "cellID invalide.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
							return;
						}
						try {
							if (infos.length > 1) {
								final Player perso3 = World.getPersoByName(infos[1]);
								if (perso3.getGroupe() != null) {
									final String str4 = "Il est interdit d'emprisonner un personnage ayant des droits.";
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str4);
									return;
								}
								if (perso3 == null || perso3.get_fight() != null) {
									final String str4 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9 ou est en combat.";
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str4);
									return;
								}
								if (perso3.isOnline()) {
									perso3.teleport(mapID3, cellID3);
								} else {
									perso3.teleportD(mapID3, cellID3);
								}
								final String str4 = "Le joueur " + perso3.getName()
										+ " a \u00e9t\u00e9 teleport\u00e9 emprisonn\u00e9.";
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str4);
							}
						} catch (Exception e4) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "Introuvable.");
						}
					} else if (command.equalsIgnoreCase("UNJAIL")) {
						final Player perso = World.getPersoByName(infos[1]);
						if (perso == null || perso.get_fight() != null) {
							final String str = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9 ou est en combat.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
							return;
						}
						if (infos.length > 1 && perso.isInPrison()) {
							perso.warpToSavePos();
							final String str = "Le joueur " + perso.getName()
									+ " a \u00e9t\u00e9 teleport\u00e9 \u00e0 son point de sauvegarde.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
						}
					} else if (command.equalsIgnoreCase("BAN")) {
						final Player P = World.getPersoByName(infos[1]);
						if (P == null) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.");
							return;
						}
						if (P.getAccount() == null) {
							Database.getStatique().getAccountData().load(P.getAccID());
						}
						if (P.getAccount() == null) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Le personnage n'a pas de compte.");
							return;
						}
						P.getAccount().setBanned(true);
						Database.getStatique().getAccountData().update(P.getAccount());
						if (P.get_fight() == null) {
							if (P.getGameClient() != null) {
								P.getGameClient().kick();
							}
						} else {
							SocketManager.send(P, "Im1201;" + this.getPlayer().getName());
						}
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
								"Vous avez banni " + P.getName() + ".");
					} else if (command.equalsIgnoreCase("BANACCOUNT")) {
						String mess = "Le compte est introuvable";
						String A = "";
						try {
							A = infos[1];
						} catch (Exception ex14) {
						}
						if (A.equalsIgnoreCase("")) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Il faut le nom de compte.");
							return;
						}
						for (final Map.Entry<Integer, Account> entry3 : World.getAccounts().entrySet()) {
							final Account a3 = entry3.getValue();
							if (a3 == null) {
								continue;
							}
							if (!a3.getName().equalsIgnoreCase(A)) {
								continue;
							}
							a3.setBanned(true);
							Database.getStatique().getAccountData().update(a3);
							mess = "Vous avez banni le compte " + A;
							final Player p = a3.getCurPerso();
							if (p == null || !p.isOnline()) {
								continue;
							}
							mess = String.valueOf(mess) + " dont le joueur est " + p.getName();
							if (p.get_fight() == null) {
								if (p.getGameClient() == null) {
									continue;
								}
								p.getGameClient().kick();
							} else {
								SocketManager.send(p, "Im1201;" + this.getPlayer().getName());
							}
						}
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), String.valueOf(mess) + ".");
					} else if (command.equalsIgnoreCase("BANBYID")) {
						int ID = -1;
						String mess2 = "Aucun personnage n'a \u00e9t\u00e9 trouv\u00e9.";
						try {
							ID = Integer.parseInt(infos[1]);
						} catch (Exception ex15) {
						}
						if (ID <= 0) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Une IP est n\u00e9c\u00e9ssaire.");
							return;
						}
						for (final Map.Entry<Integer, Player> entry4 : World.getPlayers().entrySet()) {
							final Player p2 = entry4.getValue();
							if (p2 == null) {
								continue;
							}
							if (p2.getId() != ID) {
								continue;
							}
							if (p2.getAccount() == null) {
								Database.getStatique().getAccountData().load(p2.getAccID());
							}
							if (p2.getAccount() == null) {
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
										"Le personnage n'a pas de compte.");
								if (p2.getGameClient() != null) {
									p2.getGameClient().kick();
								}
								return;
							}
							p2.getAccount().setBanned(true);
							Database.getStatique().getAccountData().update(p2.getAccount());
							if (p2.get_fight() == null) {
								if (p2.getGameClient() != null) {
									p2.getGameClient().kick();
								}
							} else {
								SocketManager.send(p2, "Im1201;" + this.getPlayer().getName());
							}
							mess2 = "Vous avez banni " + p2.getName() + ".";
						}
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess2);
					} else if (command.equalsIgnoreCase("BANBYIP")) {
						String IP3 = "";
						try {
							IP3 = infos[1];
						} catch (Exception ex16) {
						}
						if (IP3.equalsIgnoreCase("")) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Une IP est n\u00e9c\u00e9ssaire.");
							return;
						}
						for (final Map.Entry<Integer, Account> entry5 : World.getAccountsByIp(IP3).entrySet()) {
							final Account a4 = entry5.getValue();
							if (a4 == null) {
								continue;
							}
							if (!a4.getLastIP().equalsIgnoreCase(IP3)) {
								continue;
							}
							a4.setBanned(true);
							Database.getStatique().getAccountData().update(a4);
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Le compte " + a4.getName() + " a \u00e9t\u00e9 banni.");
							if (!a4.isOnline()) {
								continue;
							}
							final GameClient gc = a4.getGameClient();
							if (gc == null) {
								continue;
							}
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Le joueur " + gc.getPersonnage().getName() + " a \u00e9t\u00e9 kick.");
							gc.kick();
						}
						Main.exchangeClient.send("SB" + IP3);
						if (Database.getStatique().getBanipData().add(IP3)) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"L'IP " + IP3 + " a ete banni.");
						}
					} else if (command.equalsIgnoreCase("BANIP")) {
						Player P = null;
						try {
							P = World.getPersoByName(infos[1]);
						} catch (Exception ex17) {
						}
						if (P == null) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.");
							return;
						}
						final String IP4 = P.getAccount().getLastIP();
						if (IP4.equalsIgnoreCase("")) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "L'IP est invalide.");
							return;
						}
						Database.getStatique().getBanipData().add(IP4);
						for (final Map.Entry<Integer, Account> entry3 : World.getAccountsByIp(IP4).entrySet()) {
							final Account a3 = entry3.getValue();
							if (a3 == null) {
								continue;
							}
							if (!a3.getLastIP().equalsIgnoreCase(IP4)) {
								continue;
							}
							a3.setBanned(true);
							Database.getStatique().getAccountData().update(a3);
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Le compte " + a3.getName() + " a \u00e9t\u00e9 banni.");
							if (!a3.isOnline()) {
								continue;
							}
							final GameClient gc2 = a3.getGameClient();
							if (gc2 == null) {
								continue;
							}
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Le joueur " + gc2.getPersonnage().getName() + " a \u00e9t\u00e9 kick.");
							gc2.kick();
						}
						Main.exchangeClient.send("SB" + IP4);
						if (Database.getStatique().getBanipData().add(IP4)) {
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"L'IP " + IP4 + " a ete banni.");
						}
					} else if (command.equalsIgnoreCase("SHOWITEM")) {
						Player perso = this.getPlayer();
						String name3 = null;
						try {
							name3 = infos[1];
						} catch (Exception ex18) {
						}
						perso = World.getPersoByName(name3);
						if (perso == null) {
							final String mess4 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
							return;
						}
						String mess4 = "==========\nListe d'items sur le personnage :\n";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
						synchronized (perso.getItems()) {
							for (final Map.Entry<Integer, org.aestia.object.Object> entry6 : perso.getItems()
									.entrySet()) {
								mess4 = String.valueOf(entry6.getValue().getGuid()) + " || "
										+ entry6.getValue().getTemplate().getName() + " || "
										+ entry6.getValue().getQuantity();
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
							}
						}
						// monitorexit(perso.getItems())
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
								"Le personnage poss\u00e8de : " + perso.get_kamas() + " Kamas.\n");
						mess4 = "==========";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
					} else if (command.equalsIgnoreCase("SHOWBANK")) {
						Player perso = this.getPlayer();
						String name3 = null;
						try {
							name3 = infos[1];
						} catch (Exception ex19) {
						}
						perso = World.getPersoByName(name3);
						if (perso == null) {
							final String mess4 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
							return;
						}
						final Account cBank = perso.getAccount();
						String mess5 = "==========\nListe d'items dans la banque :";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess5);
						for (final Map.Entry<Integer, org.aestia.object.Object> entry6 : cBank.getBank().entrySet()) {
							mess5 = String.valueOf(entry6.getValue().getGuid()) + " || "
									+ entry6.getValue().getTemplate().getName() + " || "
									+ entry6.getValue().getQuantity();
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess5);
						}
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
								"Le personnage poss\u00e8de : " + cBank.getBankKamas() + " Kamas en banque.");
						mess5 = "==========";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess5);
					} else if (command.equalsIgnoreCase("SHOWSTORE")) {
						Player perso = this.getPlayer();
						String name3 = null;
						try {
							name3 = infos[1];
						} catch (Exception ex20) {
						}
						perso = World.getPersoByName(name3);
						if (perso == null) {
							final String mess4 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
							return;
						}
						String mess4 = "==========\nListe d'items dans le Store :";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
						synchronized (perso.getStoreItems()) {
							for (final Map.Entry<Integer, Integer> obj : perso.getStoreItems().entrySet()) {
								final org.aestia.object.Object entry7 = World.getObjet(obj.getKey());
								mess4 = String.valueOf(entry7.getGuid()) + " || " + entry7.getTemplate().getName()
										+ " || " + entry7.getQuantity();
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
							}
						}
						// monitorexit(perso.getStoreItems())
						mess4 = "==========";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
					} else if (command.equalsIgnoreCase("SHOWMOUNT")) {
						Player perso = this.getPlayer();
						String name3 = null;
						try {
							name3 = infos[1];
						} catch (Exception ex21) {
						}
						perso = World.getPersoByName(name3);
						if (perso == null) {
							final String mess4 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
							return;
						}
						String mess4 = "==========\nListe d'items dans la banque :";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
						synchronized (perso.getMount().getItems()) {
							for (final Map.Entry<Integer, org.aestia.object.Object> entry6 : perso.getMount().getItems()
									.entrySet()) {
								mess4 = String.valueOf(entry6.getValue().getGuid()) + " || "
										+ entry6.getValue().getTemplate().getName() + " || "
										+ entry6.getValue().getQuantity();
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
							}
						}
						// monitorexit(perso.getMount().getItems())
						mess4 = "==========";
						SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
					} else {
						if (command.equalsIgnoreCase("BLOCKTRADE")) {
							int j = -1;
							try {
								j = Short.parseShort(infos[1]);
							} catch (Exception ex22) {
							}
							if (j == 0) {
								Main.tradeAsBlocked = false;
								SocketManager.PACKET_POPUP_ALL(
										"Il est maintenant possible d'effetu\u00e9 des actions, dans les coffres, poubelles, banques, \u00e9changes, .. !");
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
										"Les \u00e9changes ont \u00e9t\u00e9s d\u00e9bloqu\u00e9s.");
							} else if (j == 1) {
								Main.tradeAsBlocked = true;
								SocketManager.PACKET_POPUP_ALL(
										"Il est maintenant impossible d'effetu\u00e9 des actions, dans les coffres, poubelles, banques, \u00e9changes, .. !");
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
										"Tous les \u00e9changes sont bloqu\u00e9s.");
							} else {
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), "Aucune information.");
							}
							return;
						}
						if (command.equalsIgnoreCase("ERASEALLMAP")) {
							for (final org.aestia.map.Map map : World.getAllMaps()) {
								map.delAllDropItem();
							}
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Tous les objets sur toutes les maps ont \u00e9t\u00e9s supprim\u00e9s.");
							return;
						}
						if (command.equalsIgnoreCase("ERASEMAP")) {
							this.getPlayer().getCurMap().delAllDropItem();
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Les objets de la map ont \u00e9t\u00e9s supprim\u00e9s.");
							return;
						}
						if (command.equalsIgnoreCase("MORPH")) {
							int morphID = -9;
							try {
								morphID = Integer.parseInt(infos[1]);
							} catch (Exception ex23) {
							}
							if (morphID == -9) {
								final String str = "MorphID invalide.";
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
								return;
							}
							Player target = this.getPlayer();
							if (infos.length > 2) {
								target = World.getPersoByName(infos[2]);
								if (target == null) {
									final String str3 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
									return;
								}
							}
							if (morphID == -1) {
								morphID = target.getClasse() * 10 + target.getSexe();
								target.set_gfxID(morphID);
								SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(target.getCurMap(), target.getId());
								SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(target.getCurMap(), target);
								final String str3 = "Le joueur " + target.getName() + " a son apparence originale.";
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
								return;
							}
							target.set_gfxID(morphID);
							SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(target.getCurMap(), target.getId());
							SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(target.getCurMap(), target);
							final String str3 = "Le joueur " + target.getName() + " a \u00e9t\u00e9 transform\u00e9.";
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
						} else if (command.equalsIgnoreCase("POP")) {
							Player perso = null;
							infos = msg.split(" ", 3);
							try {
								perso = World.getPersoByName(infos[1]);
							} catch (Exception ex24) {
							}
							if (perso == null) {
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
										"Le nom du personnage est incorrect.");
								return;
							}
							if (infos[2] != "") {
								SocketManager.PACKET_POPUP(perso, infos[2]);
							}
							SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
									"Le popup \u00e0 \u00e9t\u00e9 envoy\u00e9 \u00e0 " + perso.getName() + ".");
						} else {
							if (command.equalsIgnoreCase("DEMORPHALL")) {
								for (final Player player3 : World.getOnlinePersos()) {
									player3.set_gfxID(player3.getClasse() * 10 + player3.getSexe());
								}
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
										"Tous les joueurs connect\u00e9s ont leur apparence originale.");
								return;
							}
							if (command.equalsIgnoreCase("ADDHONOR")) {
								int honor = 0;
								try {
									honor = Integer.parseInt(infos[1]);
								} catch (Exception ex25) {
								}
								Player perso4 = this.getPlayer();
								if (infos.length > 2) {
									perso4 = World.getPersoByName(infos[2]);
									if (perso4 == null) {
										final String str3 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
										return;
									}
								}
								String str3 = "Vous avez ajout\u00e9 " + honor + " points d'honneur \u00e0 "
										+ perso4.getName() + ".";
								if (perso4.get_align() != 3) {
									str3 = "Le joueur n'est pas mercenaire ... l'action a \u00e9t\u00e9 annul\u00e9e.";
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
									return;
								}
								perso4.addHonor(honor);
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
							} else if (command.equalsIgnoreCase("HONOR")) {
								int honor = 0;
								try {
									honor = Integer.parseInt(infos[1]);
								} catch (Exception ex26) {
								}
								Player perso4 = this.getPlayer();
								if (infos.length > 2) {
									perso4 = World.getPersoByName(infos[2]);
									if (perso4 == null) {
										final String str3 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9";
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
										return;
									}
								}
								String str3 = "Vous avez ajout\u00e9 " + honor + " points d'honneur \u00e0 "
										+ perso4.getName() + ".";
								if (perso4.get_align() == -1) {
									str3 = "Le joueur est neutre ... l'action a \u00e9t\u00e9 annul\u00e9e.";
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
									return;
								}
								perso4.addHonor(honor);
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
							} else if (command.equalsIgnoreCase("NOAGRO")) {
								Player perso = this.getPlayer();
								String name3 = null;
								try {
									name3 = infos[1];
								} catch (Exception ex27) {
								}
								perso = World.getPersoByName(name3);
								if (perso == null) {
									final String mess4 = "Le personnage n'\u00e9xiste pas.";
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
									return;
								}
								perso.setCanAggro(!perso.canAggro());
								String mess4 = perso.getName();
								if (perso.canAggro()) {
									mess4 = String.valueOf(mess4) + " peut maintenant \u00eatre aggress\u00e9.";
								} else {
									mess4 = String.valueOf(mess4) + " ne peut plus \u00eatre agress\u00e9.";
								}
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
								if (!perso.isOnline()) {
									mess4 = "Le personnage " + perso.getName() + " n'\u00e9tait pas connect\u00e9.";
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
								}
							} else if (command.equalsIgnoreCase("WHOIS")) {
								String name4 = "";
								Player perso4 = null;
								try {
									name4 = infos[1];
								} catch (Exception ex28) {
								}
								if (name4 == "") {
									return;
								}
								perso4 = World.getPersoByName(name4);
								if (perso4 == null) {
									final String mess4 = "Le personnage n'\u00e9xiste pas.";
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
									return;
								}
								if (perso4.getAccount().getLastIP().equalsIgnoreCase("")) {
									final String mess4 = "Aucune IP.";
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess4);
									return;
								}
								final Map<Integer, Account> accounts = World
										.getAccountsByIp(perso4.getAccount().getLastIP());
								String mess5 = "Whois sur le joueur : " + name4 + "\n";
								mess5 = String.valueOf(mess5) + "Derni\u00e8re IP : " + perso4.getAccount().getLastIP()
										+ "\n";
								int k = 1;
								for (final Map.Entry<Integer, Account> entry8 : accounts.entrySet()) {
									String persos = "";
									final Account a = entry8.getValue();
									if (a == null) {
										continue;
									}
									for (final Map.Entry<Integer, Player> entry9 : a.getPersos().entrySet()) {
										perso4 = entry9.getValue();
										if (perso4 != null) {
											if (persos.equalsIgnoreCase("")) {
												persos = String.valueOf(persos) + perso4.getName()
														+ ((perso4.getGroupe() != null)
																? (":" + perso4.getGroupe().getNom()) : "");
											} else {
												persos = String.valueOf(persos) + ", " + perso4.getName()
														+ ((perso4.getGroupe() != null)
																? (":" + perso4.getGroupe().getNom()) : "");
											}
										}
									}
									if (persos.equalsIgnoreCase("")) {
										continue;
									}
									mess5 = String.valueOf(mess5) + "[" + k + "] " + a.getName() + " - " + persos
											+ (a.isBanned() ? " : banni" : "") + "\n";
									++k;
								}
								SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess5);
							} else {
								if (command.equalsIgnoreCase("CLEANFIGHT")) {
									this.getPlayer().getCurMap().getFights().clear();
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
											"Tous les combats de la map ont \u00e9t\u00e9s supprim\u00e9s.");
									return;
								}
								if (command.equalsIgnoreCase("ETATSERVER")) {
									int etat = 1;
									try {
										etat = Integer.parseInt(infos[1]);
									} catch (Exception ex29) {
									}
									GameServer.setState(etat);
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
											"Vous avez chang\u00e9 l'\u00e9tat du serveur en " + etat + ".");
									return;
								}
								if (command.equalsIgnoreCase("MPTOTP")) {
									this.getPlayer().mpToTp = !this.getPlayer().mpToTp;
									String mess = "";
									if (this.getPlayer().mpToTp) {
										mess = "Vous venez d'activer le MP to TP.";
									} else {
										mess = "Vous venez de d\u00e9sactiver le MP to TP.";
									}
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
									return;
								}
								if (command.equalsIgnoreCase("RETURNTP")) {
									for (final Player perso : World.getOnlinePersos()) {
										if (perso.thatMap != -1) {
											if (perso.get_fight() != null) {
												continue;
											}
											perso.teleport((short) perso.thatMap, perso.thatCell);
											perso.thatMap = -1;
											perso.thatCell = -1;
										}
									}
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
											"Vous venez de renvoyer tous les joueurs \u00e0 leur ancienne position.");
									return;
								}
								if (command.equalsIgnoreCase("GETCASES")) {
									if (this.getPlayer().getCases) {
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
												"Le getCases viens d'\u00eatre d\u00e9sactiv\u00e9 :");
										String l = "";
										for (final Integer c : this.getPlayer().thisCases) {
											l = String.valueOf(l) + ";" + c;
										}
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
												l.substring(1));
										this.getPlayer().thisCases.clear();
									} else {
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
												"Le getCases viens d'\u00eatre activ\u00e9. D\u00e9placez-vous sur la map pour capturer les cellules.");
									}
									this.getPlayer().getCases = !this.getPlayer().getCases;
									return;
								}
								if (command.equalsIgnoreCase("WALKFAST")) {
									if (this.getPlayer().walkFast) {
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
												"La marche instantann\u00e9 viens d'\u00eatre d\u00e9sactiv\u00e9.");
									} else {
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
												"La marche instantann\u00e9 viens d'\u00eatre activ\u00e9.");
									}
									this.getPlayer().walkFast = !this.getPlayer().walkFast;
									return;
								}
								if (command.equalsIgnoreCase("LISTMAP")) {
									String data = "";
									final ArrayList<org.aestia.map.Map> m = World.getMapByPosInArray(
											this.getPlayer().getCurMap().getX(), this.getPlayer().getCurMap().getY());
									for (final org.aestia.map.Map map2 : m) {
										data = String.valueOf(data) + map2.getId() + " | ";
									}
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), data);
									return;
								}
								if (command.equalsIgnoreCase("DELINVENTORY")) {
									Player perso = null;
									infos = msg.split(" ", 3);
									try {
										perso = World.getPersoByName(infos[1]);
									} catch (Exception ex30) {
									}
									if (perso == null) {
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
												"Le nom du personnage est incorrect.");
										return;
									}
									int i = 0;
									final ArrayList<org.aestia.object.Object> list = new ArrayList<org.aestia.object.Object>();
									synchronized (perso.getItems()) {
										list.addAll(perso.getItems().values());
										for (final org.aestia.object.Object obj2 : list) {
											final int guid = obj2.getGuid();
											SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(perso, guid);
											perso.deleteItem(guid);
											++i;
										}
									}
									// monitorexit(perso.getItems())
									SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
											"Vous venez de supprimer " + i + " objets au joueur " + perso.getName()
													+ ".");
								} else {
									if (command.equalsIgnoreCase("RMOBS")) {
										this.getPlayer().getCurMap().refreshSpawns();
										final String mess = "Les spawns de monstres sur la map ont \u00e9t\u00e9s rafraichit.";
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), mess);
										return;
									}
									if (command.equalsIgnoreCase("DELJOB")) {
										Player perso = this.getPlayer();
										infos = msg.split(" ", 3);
										int job = -1;
										try {
											job = Integer.parseInt(infos[1]);
										} catch (Exception ex31) {
										}
										try {
											perso = World.getPersoByName(infos[2]);
										} catch (Exception ex32) {
										}
										if (perso == null) {
											SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
													"Le nom du personnage est incorrect.");
											return;
										}
										if (job < 1) {
											return;
										}
										final JobStat jobStats = perso.getMetierByID(job);
										if (jobStats == null) {
											return;
										}
										perso.unlearnJob(jobStats.getId());
										SocketManager.GAME_SEND_STATS_PACKET(perso);
										Database.getStatique().getPlayerData().update(perso, false);
										SocketManager.GAME_SEND_MESSAGE(perso,
												"Vous venez de d\u00e9sapprendre un m\u00e9tier, veuillez vous reconnecter.");
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
												"Vous avez supprim\u00e9 le m\u00e9tier " + job + " sur le personnage "
														+ perso.getName() + ".");
									} else if (command.equalsIgnoreCase("ADDTRIGGER")) {
										String args2 = "";
										try {
											args2 = infos[1];
										} catch (Exception ex33) {
										}
										if (args2.equals("")) {
											final String str = "Valeur invalide.";
											SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
											return;
										}
										this.getPlayer().getCurCell().addOnCellStopAction(0, args2, "-1", null);
										final boolean success = Database.getStatique().getScripted_cellData().update(
												this.getPlayer().getCurMap().getId(),
												this.getPlayer().getCurCell().getId(), 0, 1, args2, "-1");
										String str3 = "";
										if (success) {
											str3 = "Le trigger a \u00e9t\u00e9 ajout\u00e9.";
										} else {
											str3 = "Le trigger n'a pas \u00e9t\u00e9 ajout\u00e9.";
										}
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
									} else if (command.equalsIgnoreCase("DELTRIGGER")) {
										int cellID4 = -1;
										try {
											cellID4 = Integer.parseInt(infos[1]);
										} catch (Exception ex34) {
										}
										if (cellID4 == -1 || this.getPlayer().getCurMap().getCase(cellID4) == null) {
											final String str = "CellID invalide.";
											SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str);
											return;
										}
										this.getPlayer().getCurMap().getCase(cellID4).clearOnCellAction();
										final boolean success = Database.getStatique().getScripted_cellData()
												.delete(this.getPlayer().getCurMap().getId(), cellID4);
										String str3 = "";
										if (success) {
											str3 = "Le trigger a \u00e9t\u00e9 retir\u00e9.";
										} else {
											str3 = "Le trigger n'a pas \u00e9t\u00e9 retir\u00e9.";
										}
										SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(), str3);
									} else {
										if (command.equalsIgnoreCase("SAVETHAT")) {
											this.getPlayer().thatMap = this.getPlayer().getCurMap().getId();
											this.getPlayer().thatCell = this.getPlayer().getCurCell().getId();
											SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
													"Vous avez sauvegard\u00e9 la map " + this.getPlayer().thatMap
															+ " et la cellule " + this.getPlayer().thatCell + ".");
											return;
										}
										if (command.equalsIgnoreCase("APPLYTHAT")) {
											if (this.getPlayer().thatMap == -1 || this.getPlayer().thatCell == -1) {
												SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
														"Impossible d'ajouter le trigger, veuillez utiliser la commande SAVETHAT avant.");
												return;
											}
											this.getPlayer().getCurCell().addOnCellStopAction(0,
													String.valueOf(this.getPlayer().thatMap) + ","
															+ this.getPlayer().thatCell,
													"-1", null);
											Database.getStatique().getScripted_cellData().update(
													this.getPlayer().getCurMap().getId(),
													this.getPlayer().getCurCell().getId(), 0, 1,
													String.valueOf(this.getPlayer().thatMap) + ","
															+ this.getPlayer().thatCell,
													"-1");
											this.getPlayer().thatMap = -1;
											this.getPlayer().thatCell = -1;
											SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
													"Vous avez appliqu\u00e9 le trigger.");
										} else {
											if (command.equalsIgnoreCase("STRIGGER")) {
												this.getPlayer().thatMap = this.getPlayer().getCurMap().getId();
												this.getPlayer().thatCell = this.getPlayer().getCurCell().getId();
												SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
														"Vous avez sauvegard\u00e9 la map " + this.getPlayer().thatMap
																+ " et la cellule " + this.getPlayer().thatCell + ".");
												return;
											}
											if (command.equalsIgnoreCase("APTRIGGER")) {
												if (this.getPlayer().thatMap == -1 || this.getPlayer().thatCell == -1) {
													SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
															"Impossible d'ajouter le trigger, veuillez utiliser la commande STRIGGER avant.");
													return;
												}
												World.getMap((short) this.getPlayer().thatMap)
														.getCase(this.getPlayer().thatCell).addOnCellStopAction(0,
																String.valueOf(this.getPlayer().getCurMap().getId())
																		+ "," + this.getPlayer().getCurCell().getId(),
																"-1", null);
												Database.getStatique().getScripted_cellData()
														.update(this.getPlayer().thatMap, this.getPlayer().thatCell, 0,
																1,
																String.valueOf(this.getPlayer().getCurMap().getId())
																		+ "," + this.getPlayer().getCurCell().getId(),
																"-1");
												this.getPlayer().thatMap = -1;
												this.getPlayer().thatCell = -1;
												SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
														"Vous avez appliqu\u00e9 le trigger.");
											} else {
												if (command.equalsIgnoreCase("INFOS")) {
													long uptime = System.currentTimeMillis()
															- Config.getInstance().startTime;
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
													String mess6 = "===========\nUptime : " + jour + "j " + hour + "h "
															+ min + "m " + sec + "s.\n";
													if (nbPlayer > 0) {
														mess6 = String.valueOf(mess6) + "Joueurs en ligne : " + nbPlayer
																+ "\n";
													}
													if (nbPlayerIp > 0) {
														mess6 = String.valueOf(mess6) + "Joueurs uniques en ligne : "
																+ nbPlayerIp + "\n";
													}
													if (maxPlayer > 0) {
														mess6 = String.valueOf(mess6) + "Record de connexion : "
																+ maxPlayer + "\n";
													}
													mess6 = String.valueOf(mess6) + "===========";
													SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
															mess6);
													return;
												}
												if (command.equalsIgnoreCase("STARTFIGHT")) {
													if (this.getPlayer().get_fight() == null) {
														SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
																"Vous devez \u00eatre dans un combat.");
														return;
													}
													this.getPlayer().get_fight().startFight();
													SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
															"Le combat a \u00e9t\u00e9 d\u00e9marr\u00e9.");
												} else if (command.equalsIgnoreCase("ENDFIGHT")) {
													if (this.getPlayer().get_fight() == null) {
														SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
																"Le combat n'existe pas.");
														return;
													}
													int j = -1;
													try {
														j = Short.parseShort(infos[1]);
													} catch (Exception ex35) {
													}
													if (j == 0) {
														this.getPlayer().get_fight().endFight(false);
														SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
																"L'\u00e9quipe des joueurs meurent !");
													} else if (j == 1) {
														this.getPlayer().get_fight().endFight(true);
														SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
																"L'\u00e9quipe des monstres meurent !");
													} else {
														SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
																"Aucune information.");
													}
												} else {
													if (command.equalsIgnoreCase("ENDFIGHTALL")) {
														try {
															for (final GameClient client3 : Main.gameServer.getClients()
																	.values()) {
																final Player player4 = client3.getPersonnage();
																if (player4 == null) {
																	continue;
																}
																final Fight f = player4.get_fight();
																if (f == null) {
																	continue;
																}
																try {
																	if (f.getLaunchTime() > 1L) {
																		continue;
																	}
																	f.endFight(true);
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(),
																			"Le combat de " + player4.getName()
																					+ " a \u00e9t\u00e9 termin\u00e9.");
																} catch (Exception e5) {
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(),
																			"Le combat de " + player4.getName()
																					+ " a d\u00e9j\u00e0 \u00e9t\u00e9 termin\u00e9.");
																}
															}
														} catch (Exception e) {
															e.printStackTrace();
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(),
																	"Erreur lors de la commande endfightall : "
																			+ e.getMessage() + ".");
															return;
														} finally {
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(),
																	"Tous les combats ont \u00e9t\u00e9 termin\u00e9s.");
														}
														SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
																"Tous les combats ont \u00e9t\u00e9 termin\u00e9s.");
														return;
													}
													if (command.equalsIgnoreCase("MAPINFO")) {
														String mess = "==========\nListe des PNJs de la Map :";
														SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
																mess);
														final org.aestia.map.Map map3 = this.getPlayer().getCurMap();
														for (final Map.Entry<Integer, Npc> entry10 : map3.getNpcs()
																.entrySet()) {
															mess = entry10.getKey() + " | "
																	+ entry10.getValue().getTemplate().get_id() + " | "
																	+ entry10.getValue().getCellid() + " | "
																	+ entry10.getValue().getTemplate()
																			.getInitQuestionId(this.getPlayer()
																					.getCurMap().getId());
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), mess);
														}
														mess = "Liste des groupes de monstres :";
														SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
																mess);
														for (final Map.Entry<Integer, Monster.MobGroup> entry11 : map3
																.getMobGroups().entrySet()) {
															mess = entry11.getKey() + " | "
																	+ entry11.getValue().getCellId() + " | "
																	+ entry11.getValue().getAlignement() + " | "
																	+ entry11.getValue().getMobs().size();
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), mess);
														}
														mess = "==========";
														SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
																mess);
														return;
													}
													if (command.equalsIgnoreCase("UNBANIP")) {
														Player perso = null;
														try {
															perso = World.getPersoByName(infos[1]);
														} catch (Exception ex36) {
														}
														if (perso == null) {
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(),
																	"Le nom du personnage n'est pas bon.");
															return;
														}
														if (Database.getStatique().getBanipData()
																.delete(perso.getAccount().getCurIP())) {
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(),
																	"L'IP a \u00e9t\u00e9 d\u00e9banni.");
														}
													} else if (command.equalsIgnoreCase("UNBAN")) {
														final Player P = World.getPersoByName(infos[1]);
														if (P == null) {
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), "Personnage non trouv\u00e9.");
															return;
														}
														if (P.getAccount() == null) {
															Database.getStatique().getAccountData().load(P.getAccID());
														}
														if (P.getAccount() == null) {
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(),
																	"Le personnage n'a pas de compte.");
															return;
														}
														P.getAccount().setBanned(false);
														Database.getStatique().getAccountData().update(P.getAccount());
														SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(this.getClient(),
																"Vous avez d\u00e9banni " + P.getName() + ".");
													} else {
														if (command.equalsIgnoreCase("EXIT")) {
															GameServer.setState(0);
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(),
																	"Tout le monde va \u00eatre kick\u00e9.");
															Main.gameServer.kickAll(true);
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), "Sauvegarde lanc\u00e9e !");
															WorldSave.cast(0);
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), "Lancement du reboot.");
															Main.stop();
															return;
														}
														if (command.equalsIgnoreCase("SAVE") && !Main.isSaving) {
															WorldSave.cast(0);
															final String mess = "Sauvegarde lanc\u00e9e!";
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), mess);
															return;
														}
														if (command.equalsIgnoreCase("LEVEL")) {
															int count = 0;
															try {
																count = Integer.parseInt(infos[1]);
																if (count < 1) {
																	count = 1;
																}
																if (count > World.getExpLevelSize()) {
																	count = World.getExpLevelSize();
																}
																Player perso4 = this.getPlayer();
																if (infos.length == 3) {
																	final String name = infos[2];
																	perso4 = World.getPersoByName(name);
																	if (perso4 == null) {
																		perso4 = this.getPlayer();
																	}
																}
																if (perso4.getLevel() < count) {
																	while (perso4.getLevel() < count) {
																		perso4.levelUp(false, true);
																	}
																	if (perso4.isOnline()) {
																		SocketManager.GAME_SEND_SPELL_LIST(perso4);
																		SocketManager.GAME_SEND_NEW_LVL_PACKET(
																				perso4.getGameClient(),
																				perso4.getLevel());
																		SocketManager.GAME_SEND_STATS_PACKET(perso4);
																	}
																}
																final String mess4 = "Vous avez fix\u00e9 le niveau de "
																		+ perso4.getName() + " \u00e0 " + count + ".";
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), mess4);
															} catch (Exception e6) {
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), "Valeur incorecte.");
															}
															return;
														}
														if (command.equalsIgnoreCase("KAMAS")) {
															int count = 0;
															try {
																count = Integer.parseInt(infos[1]);
															} catch (Exception e6) {
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), "Valeur incorecte.");
																return;
															}
															if (count == 0) {
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), "Valeur inutile.");
																return;
															}
															Player perso4 = this.getPlayer();
															if (infos.length == 3) {
																final String name = infos[2];
																perso4 = World.getPersoByName(name);
																if (perso4 == null) {
																	perso4 = this.getPlayer();
																}
															}
															final long curKamas = perso4.get_kamas();
															long newKamas = curKamas + count;
															if (newKamas < 0L) {
																newKamas = 0L;
															}
															if (newKamas > 1000000000L) {
																newKamas = 1000000000L;
															}
															perso4.set_kamas(newKamas);
															if (perso4.isOnline()) {
																SocketManager.GAME_SEND_STATS_PACKET(perso4);
															}
															String mess7 = "Vous avez ";
															mess7 = String.valueOf(mess7)
																	+ ((count < 0) ? "retir\u00e9" : "ajout\u00e9")
																	+ " ";
															mess7 = String.valueOf(mess7) + Math.abs(count)
																	+ " kamas \u00e0 " + perso4.getName() + ".";
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), mess7);
														} else if (command.equalsIgnoreCase("ITEMSET")) {
															int tID = 0;
															try {
																tID = Integer.parseInt(infos[1]);
															} catch (Exception ex37) {
															}
															final ObjectSet IS = World.getItemSet(tID);
															if (tID == 0 || IS == null) {
																final String mess4 = "La panoplie " + tID
																		+ " n'existe pas.";
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), mess4);
																return;
															}
															boolean useMax = false;
															if (infos.length == 3) {
																useMax = infos[2].equals("MAX");
															}
															for (final ObjectTemplate t : IS.getItemTemplates()) {
																final org.aestia.object.Object obj3 = t.createNewItem(1,
																		useMax);
																if (this.getPlayer().addObjet(obj3, true)) {
																	World.addObjet(obj3, true);
																}
															}
															String str4 = "Creation de la panoplie " + tID
																	+ " r\u00e9ussie";
															if (useMax) {
																str4 = String.valueOf(str4)
																		+ " avec des stats maximums";
															}
															str4 = String.valueOf(str4) + ".";
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), str4);
														} else if (command.equalsIgnoreCase("ITEM")) {
															int tID = 0;
															try {
																tID = Integer.parseInt(infos[1]);
															} catch (Exception ex38) {
															}
															if (tID == 0) {
																final String mess2 = "Le template " + tID
																		+ " n'existe pas.";
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), mess2);
																return;
															}
															int qua = 1;
															if (infos.length == 3) {
																try {
																	qua = Integer.parseInt(infos[2]);
																} catch (Exception ex39) {
																}
															}
															boolean useMax = false;
															if (infos.length == 4 && infos[3].equalsIgnoreCase("MAX")) {
																useMax = true;
															}
															final ObjectTemplate t = World.getObjTemplate(tID);
															if (t == null) {
																final String mess3 = "Le template " + tID
																		+ " n'existe pas.";
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), mess3);
																return;
															}
															if (t.getType() == 93 && (t.getStrTemplate().isEmpty()
																	|| t.getStrTemplate().equalsIgnoreCase(""))) {
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(),
																		"Impossible de cr\u00e9er l'item d'\u00e9levage. Le StrTemplate ("
																				+ tID + ") est vide.");
																return;
															}
															if (qua < 1) {
																qua = 1;
															}
															final org.aestia.object.Object obj2 = t.createNewItem(qua,
																	useMax);
															if (this.getPlayer().addObjet(obj2, true)) {
																World.addObjet(obj2, true);
															}
															String str5 = "Creation de l'item " + tID + " r\u00e9ussie";
															if (useMax) {
																str5 = String.valueOf(str5)
																		+ " avec des stats maximums";
															}
															str5 = String.valueOf(str5) + ".";
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), str5);
															SocketManager.GAME_SEND_Ow_PACKET(this.getPlayer());
														} else if (command.equalsIgnoreCase("SPELLPOINT")) {
															int pts = -1;
															try {
																pts = Integer.parseInt(infos[1]);
															} catch (Exception ex40) {
															}
															if (pts == -1) {
																final String str = "Valeur invalide.";
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), str);
																return;
															}
															Player perso4 = this.getPlayer();
															if (infos.length > 2) {
																perso4 = World.getPersoByName(infos[2]);
																if (perso4 == null) {
																	final String str3 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(), str3);
																	return;
																}
															}
															perso4.addSpellPoint(pts);
															SocketManager.GAME_SEND_STATS_PACKET(perso4);
															final String str3 = "Vous avez ajout\u00e9 " + pts
																	+ " points de sorts \u00e0 " + perso4.getName()
																	+ ".";
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), str3);
														} else if (command.equalsIgnoreCase("LSPELL")) {
															int spell = -1;
															try {
																spell = Integer.parseInt(infos[1]);
															} catch (Exception ex41) {
															}
															if (spell == -1) {
																final String str = "Valeur invalide.";
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), str);
																return;
															}
															Player perso4 = this.getPlayer();
															if (infos.length > 2) {
																perso4 = World.getPersoByName(infos[2]);
																if (perso4 == null) {
																	final String str3 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(), str3);
																	return;
																}
															}
															perso4.learnSpell(spell, 1, true, true, true);
															final String str3 = "Le sort " + spell
																	+ " a \u00e9t\u00e9 appris \u00e0 "
																	+ perso4.getName() + ".";
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), str3);
														} else if (command.equalsIgnoreCase("CAPITAL")) {
															int pts = -1;
															try {
																pts = Integer.parseInt(infos[1]);
															} catch (Exception ex42) {
															}
															if (pts == -1) {
																final String str = "Valeur invalide.";
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), str);
																return;
															}
															Player perso4 = this.getPlayer();
															if (infos.length > 2) {
																perso4 = World.getPersoByName(infos[2]);
																if (perso4 == null) {
																	final String str3 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(), str3);
																	return;
																}
															}
															perso4.addCapital(pts);
															SocketManager.GAME_SEND_STATS_PACKET(perso4);
															final String str3 = "Vous avez ajout\u00e9 " + pts
																	+ " points de capital \u00e0 " + perso4.getName()
																	+ ".";
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), str3);
														} else if (command.equalsIgnoreCase("ALIGN")) {
															byte align = -1;
															try {
																align = Byte.parseByte(infos[1]);
															} catch (Exception ex43) {
															}
															if (align < -1 || align > 3) {
																final String str = "Valeur invalide.";
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), str);
																return;
															}
															Player perso4 = this.getPlayer();
															if (infos.length > 2) {
																perso4 = World.getPersoByName(infos[2]);
																if (perso4 == null) {
																	final String str3 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(), str3);
																	return;
																}
															}
															perso4.modifAlignement(align);
															String a5 = "";
															if (align == 0) {
																a5 = "neutre";
															} else if (align == 1) {
																a5 = "bontarien";
															} else if (align == 2) {
																a5 = "brakmarien";
															} else if (align == 3) {
																a5 = "serianne";
															}
															final String str4 = "L'alignement du joueur a \u00e9t\u00e9 modifi\u00e9 en "
																	+ a5 + ".";
															SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																	this.getClient(), str4);
														} else {
															if (command.equalsIgnoreCase("LIFE")) {
																int count = 0;
																try {
																	count = Integer.parseInt(infos[1]);
																	if (count < 0) {
																		count = 0;
																	}
																	if (count > 100) {
																		count = 100;
																	}
																	Player perso4 = this.getPlayer();
																	if (infos.length == 3) {
																		final String name = infos[2];
																		perso4 = World.getPersoByName(name);
																		if (perso4 == null) {
																			perso4 = this.getPlayer();
																		}
																	}
																	final int newPDV = perso4.getMaxPdv() * count / 100;
																	perso4.setPdv(newPDV);
																	if (perso4.isOnline()) {
																		SocketManager.GAME_SEND_STATS_PACKET(perso4);
																	}
																	final String mess5 = "Vous avez fix\u00e9 le pourcentage de vitalit\u00e9 de "
																			+ perso4.getName() + " \u00e0 " + count
																			+ "%.";
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(), mess5);
																} catch (Exception e6) {
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(), "Valeur incorecte.");
																}
																return;
															}
															if (command.equalsIgnoreCase("XPJOB")) {
																int job2 = -1;
																int xp = -1;
																try {
																	job2 = Integer.parseInt(infos[1]);
																	xp = Integer.parseInt(infos[2]);
																} catch (Exception ex44) {
																}
																if (job2 == -1 || xp < 0) {
																	final String str3 = "Valeurs invalides.";
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(), str3);
																	return;
																}
																Player perso3 = this.getPlayer();
																if (infos.length > 3) {
																	perso3 = World.getPersoByName(infos[3]);
																	if (perso3 == null) {
																		final String str4 = "Le personnage n'a pas \u00e9t\u00e9 trouv.\u00e9";
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(), str4);
																		return;
																	}
																}
																final JobStat SM = perso3.getMetierByID(job2);
																if (SM == null) {
																	final String str2 = "Le joueur ne poss\u00e8de pas le metier demand\u00e9.";
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(), str2);
																	return;
																}
																SM.addXp(perso3, xp, false);
																final ArrayList<JobStat> SMs = new ArrayList<JobStat>();
																SMs.add(SM);
																SocketManager.GAME_SEND_JX_PACKET(perso3, SMs);
																final String str5 = "Vous avez ajout\u00e9 " + xp
																		+ " points d'exp\u00e9rience au m\u00e9tier "
																		+ job2 + " de " + perso3.getName() + ".";
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), str5);
															} else if (command.equalsIgnoreCase("LJOB")) {
																int job2 = -1;
																try {
																	job2 = Integer.parseInt(infos[1]);
																} catch (Exception ex45) {
																}
																if (job2 == -1 || World.getMetier(job2) == null) {
																	final String str = "Valeur invalide.";
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(), str);
																	return;
																}
																Player perso4 = this.getPlayer();
																if (infos.length > 2) {
																	perso4 = World.getPersoByName(infos[2]);
																	if (perso4 == null) {
																		final String str3 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(), str3);
																		return;
																	}
																}
																perso4.learnJob(World.getMetier(job2));
																final String str3 = "Le metier " + job2
																		+ " a \u00e9t\u00e9 appris \u00e0 "
																		+ perso4.getName() + ".";
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(), str3);
															} else if (command.equalsIgnoreCase("SPAWN")) {
																String Mob = null;
																try {
																	Mob = infos[1];
																} catch (Exception ex46) {
																}
																if (Mob == null) {
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(),
																			"Les param\u00e8tres sont invalides.");
																	return;
																}
																this.getPlayer().getCurMap().spawnGroupOnCommand(
																		this.getPlayer().getCurCell().getId(), Mob,
																		true);
																SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																		this.getClient(),
																		"Vous avez ajout\u00e9 un groupe de monstres.");
															} else {
																if (command.equalsIgnoreCase("SHUTDOWN")) {
																	int time3 = 30;
																	int OffOn = 0;
																	try {
																		OffOn = Integer.parseInt(infos[1]);
																		time3 = Integer.parseInt(infos[2]);
																	} catch (Exception ex47) {
																	}
																	if (OffOn == 1 && this.isTimerStart()) {
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(),
																				"Un reboot est d\u00e9j\u00e0 programm\u00e9.");
																	} else if (OffOn == 1 && !this.isTimerStart()) {
																		if (time3 <= 15) {
																			SocketManager.PACKET_POPUP_ALL(
																					"Pour \u00e9viter tout probl\u00e8me, les combats sont temporairement bloqu\u00e9s !");
																			Main.fightAsBlocked = true;
																		}
																		String timeMSG = "minutes";
																		if (time3 <= 1) {
																			timeMSG = "minute";
																		}
																		SocketManager.GAME_SEND_Im_PACKET_TO_ALL(
																				"115;" + time3 + " " + timeMSG);
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(),
																				"Reboot programm\u00e9.");
																	} else if (OffOn == 0 && this.isTimerStart()) {
																		SocketManager.PACKET_POPUP_ALL(
																				"Le reboot programm\u00e9 \u00e0 \u00e9t\u00e9 stopp\u00e9. Les combats sont d\u00e9bloqu\u00e9s.");
																		Main.fightAsBlocked = true;
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(),
																				"Reboot arr\u00eat\u00e9.");
																	} else if (OffOn == 0 && !this.isTimerStart()) {
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(),
																				"Aucun reboot n'est lanc\u00e9.");
																	}
																	return;
																}
																if (command.equalsIgnoreCase("POPALL")) {
																	infos = msg.split(" ", 2);
																	SocketManager.PACKET_POPUP_ALL(infos[1]);
																	SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																			this.getClient(),
																			"Vous avez envoy\u00e9 un popup \u00e0 tout les connect\u00e9s.");
																	return;
																}
																if (command.equalsIgnoreCase("ENERGIE")) {
																	try {
																		Player perso = this.getPlayer();
																		String name3 = null;
																		name3 = infos[2];
																		perso = World.getPersoByName(name3);
																		final int jet = Integer.parseInt(infos[1]);
																		int EnergyTotal = perso.getEnergy() + jet;
																		if (EnergyTotal > 10000) {
																			EnergyTotal = 10000;
																		}
																		perso.setEnergy(EnergyTotal);
																		SocketManager.GAME_SEND_STATS_PACKET(perso);
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(),
																				"Vous avez fix\u00e9 l'\u00e9nergie de "
																						+ perso.getName() + " \u00e0 "
																						+ EnergyTotal + ".");
																		return;
																	} catch (Exception e) {
																		GameServer.addToLog(e.getMessage());
																		return;
																	}
																}
																if (command.equalsIgnoreCase("RES")) {
																	Player perso = this.getPlayer();
																	perso = World.getPersoByName(infos[1]);
																	if (perso == null) {
																		final String mess2 = "Le personnage n'existe pas.";
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(), mess2);
																		return;
																	}
																	if (perso.get_fight() != null) {
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(),
																				"Le personnage est en combat.");
																		return;
																	}
																	if (perso.isOnline()) {
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(),
																				"Vous avez ramen\u00e9 \u00e0 la vie "
																						+ perso.getName() + ".");
																		perso.set_Alive();
																	} else {
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(),
																				"Le personnage n'est pas connect\u00e9.");
																	}
																} else {
																	if (command.equalsIgnoreCase("KICKALL")) {
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(),
																				"Tout le monde va \u00eatre kick\u00e9.");
																		Main.gameServer.kickAll(true);
																		return;
																	}
																	if (command.equalsIgnoreCase("RESET")) {
																		Player perso = this.getPlayer();
																		if (infos.length > 1) {
																			perso = World.getPersoByName(infos[1]);
																		}
																		if (perso == null) {
																			final String mess2 = "Le personnage n'existe pas.";
																			SocketManager
																					.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																							this.getClient(), mess2);
																			return;
																		}
																		perso.getStats().addOneStat(125,
																				-perso.getStats().getEffect(125));
																		perso.getStats().addOneStat(124,
																				-perso.getStats().getEffect(124));
																		perso.getStats().addOneStat(118,
																				-perso.getStats().getEffect(118));
																		perso.getStats().addOneStat(123,
																				-perso.getStats().getEffect(123));
																		perso.getStats().addOneStat(119,
																				-perso.getStats().getEffect(119));
																		perso.getStats().addOneStat(126,
																				-perso.getStats().getEffect(126));
																		perso.getStatsParcho().getMap().clear();
																		perso.addCapital((perso.getLevel() - 1) * 5
																				- perso.get_capital());
																		SocketManager.GAME_SEND_STATS_PACKET(perso);
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(), "Vous avez restat "
																						+ perso.getName() + ".");
																	} else if (command
																			.equalsIgnoreCase("RENAMEPERSO")) {
																		Player perso = this.getPlayer();
																		if (infos.length > 1) {
																			perso = World.getPersoByName(infos[1]);
																		}
																		if (perso == null) {
																			final String mess2 = "Le personnage n'existe pas.";
																			SocketManager
																					.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																							this.getClient(), mess2);
																			return;
																		}
																		final String name3 = perso.getName();
																		perso.setName(infos[2]);
																		Database.getStatique().getPlayerData()
																				.update(perso, false);
																		SocketManager.GAME_SEND_STATS_PACKET(perso);
																		SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(
																				perso.getCurMap(), perso.getId());
																		SocketManager.GAME_SEND_ADD_PLAYER_TO_MAP(
																				perso.getCurMap(), perso);
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(),
																				"Vous avez renomm\u00e9 " + name3
																						+ " en " + perso.getName()
																						+ ".");
																	} else if (command
																			.equalsIgnoreCase("RENAMEGUILDE")) {
																		String ancName = "";
																		String newName = "";
																		int idGuild = -1;
																		if (infos.length > 1) {
																			ancName = infos[1];
																		}
																		newName = infos[2];
																		idGuild = World.getGuildByName(ancName);
																		if (idGuild == -1) {
																			final String mess5 = "La guilde n'existe pas.";
																			SocketManager
																					.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																							this.getClient(), mess5);
																			return;
																		}
																		World.getGuild(idGuild).setName(newName);
																		SocketManager.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																				this.getClient(),
																				"Vous avez renomm\u00e9 la guilde en "
																						+ newName + ".");
																	} else {
																		if (command.equalsIgnoreCase("A")) {
																			infos = msg.split(" ", 2);
																			final String prefix = "<b>Serveur</b>";
																			SocketManager.GAME_SEND_Im_PACKET_TO_ALL(
																					"116;" + prefix + "~" + infos[1]);
																			SocketManager
																					.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																							this.getClient(),
																							"Vous avez envoy\u00e9 un message \u00e0 tout le serveur.");
																			return;
																		}
																		if (command.equalsIgnoreCase("MOVENPC")) {
																			this.getPlayer().getCurMap()
																					.onMapNpcDisplacement(true);
																			SocketManager
																					.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																							this.getClient(),
																							"Vous avez d\u00e9plac\u00e9 les NPCs.");
																			return;
																		}
																		if (command.equalsIgnoreCase("MOVEMOB")) {
																			this.getPlayer().getCurMap()
																					.onMapMonstersDisplacement();
																			SocketManager
																					.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																							this.getClient(),
																							"Vous avez d\u00e9plac\u00e9 un groupe de monstres.");
																			return;
																		}
																		if (command.equalsIgnoreCase("ALLGIFTS")) {
																			int template = -1;
																			int quantity = 0;
																			int jp = 0;
																			try {
																				template = Integer.parseInt(infos[1]);
																				quantity = Integer.parseInt(infos[2]);
																				jp = Integer.parseInt(infos[3]);
																			} catch (Exception e7) {
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"Param\u00e8tre incorrect : ALLGIFTS [templateid] [quantity] [jp= 1 ou 0]");
																				return;
																			}
																			final String gift = String.valueOf(template)
																					+ "," + quantity + "," + jp;
																			for (final Account account : World
																					.getAccounts().values()) {
																				final String gifts = Database
																						.getStatique().getGiftData()
																						.getByAccount(
																								account.getGuid());
																				if (gifts.isEmpty()) {
																					Database.getStatique().getGiftData()
																							.update(account.getGuid(),
																									gift);
																				} else {
																					Database.getStatique().getGiftData()
																							.update(account.getGuid(),
																									String.valueOf(
																											gifts) + ";"
																											+ gift);
																				}
																			}
																			SocketManager
																					.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																							this.getClient(),
																							String.valueOf(
																									World.getAccounts()
																											.size())
																									+ " ont re\u00e7u le cadeau : "
																									+ gift + ".");
																			return;
																		}
																		if (command.equalsIgnoreCase("GIFTS")) {
																			String name4 = "";
																			int template2 = -1;
																			int quantity2 = 0;
																			int jp2 = 0;
																			try {
																				name4 = infos[1];
																				template2 = Integer.parseInt(infos[2]);
																				quantity2 = Integer.parseInt(infos[3]);
																				jp2 = Integer.parseInt(infos[4]);
																			} catch (Exception e5) {
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"Param\u00e8tre incorrect : GIFTS [account] [templateid] [quantity] [jp= 1 ou 0]");
																				return;
																			}
																			final Player player = World
																					.getPersoByName(name4);
																			if (player == null) {
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"Personnage inexistant.");
																				return;
																			}
																			final String gift2 = String
																					.valueOf(template2) + ","
																					+ quantity2 + "," + jp2;
																			final String gifts = Database.getStatique()
																					.getGiftData().getByAccount(player
																							.getAccount().getGuid());
																			if (gifts.isEmpty()) {
																				Database.getStatique()
																						.getGiftData().update(
																								player.getAccount()
																										.getGuid(),
																								gift2);
																			} else {
																				Database.getStatique().getGiftData()
																						.update(player.getAccount()
																								.getGuid(),
																								String.valueOf(gifts)
																										+ ";" + gift2);
																			}
																			SocketManager
																					.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																							this.getClient(),
																							String.valueOf(name4)
																									+ " a re\u00e7u le cadeau : "
																									+ gift2 + ".");
																		} else if (command
																				.equalsIgnoreCase("SHOWPOINTS")) {
																			Player perso = this.getPlayer();
																			if (infos.length > 1) {
																				perso = World.getPersoByName(infos[1]);
																			}
																			if (perso == null) {
																				final String mess2 = "Le personnage n'existe pas.";
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								mess2);
																				return;
																			}
																			SocketManager
																					.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																							this.getClient(),
																							String.valueOf(
																									perso.getName())
																									+ " poss\u00e8de "
																									+ perso.getAccount()
																											.getPoints()
																									+ " points boutique.");
																		} else if (command.equalsIgnoreCase("ADDNPC")) {
																			int id = 0;
																			try {
																				id = Integer.parseInt(infos[1]);
																			} catch (Exception ex48) {
																			}
																			if (id == 0 || World
																					.getNPCTemplate(id) == null) {
																				final String str = "NpcID invalide.";
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(), str);
																				return;
																			}
																			final Npc npc = this.getPlayer().getCurMap()
																					.addNpc(id, this.getPlayer()
																							.getCurCell().getId(),
																							this.getPlayer()
																									.get_orientation(),
																							false);
																			SocketManager.GAME_SEND_ADD_NPC_TO_MAP(
																					this.getPlayer().getCurMap(), npc);
																			String str3 = "Le PNJ a \u00e9t\u00e9 ajout\u00e9";
																			if (this.getPlayer().get_orientation() == 0
																					|| this.getPlayer()
																							.get_orientation() == 2
																					|| this.getPlayer()
																							.get_orientation() == 4
																					|| this.getPlayer()
																							.get_orientation() == 6) {
																				str3 = String.valueOf(str3)
																						+ " mais est invisible (orientation diagonale invalide)";
																			}
																			str3 = String.valueOf(str3) + ".";
																			if (Database.getStatique().getNpcData()
																					.addOnMap(
																							this.getPlayer().getCurMap()
																									.getId(),
																							id,
																							this.getPlayer()
																									.getCurCell()
																									.getId(),
																							this.getPlayer()
																									.get_orientation(),
																							false)) {
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(), str3);
																			} else {
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"Erreur lors de la sauvegarde de la position.");
																			}
																		} else if (command.equalsIgnoreCase("DELNPC")) {
																			int id = 0;
																			try {
																				id = Integer.parseInt(infos[1]);
																			} catch (Exception ex49) {
																			}
																			final Npc npc = this.getPlayer().getCurMap()
																					.getNpc(id);
																			if (id == 0 || npc == null) {
																				final String str3 = "Npc GUID invalide.";
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(), str3);
																				return;
																			}
																			final int exC = npc.getCellid();
																			SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(
																					this.getPlayer().getCurMap(), id);
																			this.getPlayer().getCurMap()
																					.removeNpcOrMobGroup(id);
																			final String str4 = "Le PNJ a \u00e9t\u00e9 supprim\u00e9.";
																			if (Database.getStatique().getNpcData()
																					.delete(this.getPlayer().getCurMap()
																							.getId(), exC)) {
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(), str4);
																			} else {
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"Erreur lors de la sauvegarde de la position.");
																			}
																		} else if (command
																				.equalsIgnoreCase("SETSTATS")) {
																			int obj4 = -1;
																			String stats = "";
																			try {
																				obj4 = Integer.parseInt(infos[1]);
																				stats = infos[2];
																			} catch (Exception ex50) {
																			}
																			if (obj4 == -1 || stats.equals("")) {
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"Les param\u00e8tres sont invalides.");
																				return;
																			}
																			final org.aestia.object.Object object = World
																					.getObjet(obj4);
																			if (object == null) {
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"L'objet n'existe pas.");
																				return;
																			}
																			if (stats.equals("-1")) {
																				object.clearStats();
																				SocketManager.GAME_SEND_UPDATE_ITEM(
																						this.getPlayer(), object);
																			} else {
																				object.refreshStatsObjet(stats);
																				SocketManager.GAME_SEND_UPDATE_ITEM(
																						this.getPlayer(), object);
																			}
																			SocketManager
																					.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																							this.getClient(),
																							"L'objet a \u00e9t\u00e9 modifi\u00e9 avec succ\u00e8s.");
																		} else if (command
																				.equalsIgnoreCase("ADDCELLPARK")) {
																			if (this.getPlayer().getCurMap()
																					.getMountPark() == null) {
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"Pas d'enclos sur votre map.");
																				return;
																			}
																			this.getPlayer().getCurMap().getMountPark()
																					.addCellObject(this.getPlayer()
																							.getCurCell().getId());
																			Database.getStatique()
																					.getMountpark_dataData()
																					.update(this.getPlayer().getCurMap()
																							.getMountPark());
																			SocketManager
																					.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																							this.getClient(),
																							"Vous avez ajout\u00e9 la cellule \u00e0 l'enclos.");
																		} else {
																			if (command.equalsIgnoreCase("O")) {
																				final MountPark mp = this.getPlayer()
																						.getCurMap().getMountPark();
																				for (final Case c2 : this.getPlayer()
																						.getCurMap().getCases()
																						.values()) {
																					if (c2.getObject() != null) {
																						switch (c2.getObject()
																								.getTemplate()
																								.getId()) {
																						case 6763:
																						case 6766:
																						case 6767:
																						case 6772: {
																							mp.setDoor(c2.getId());
																							SocketManager
																									.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																											this.getClient(),
																											"Vous avez ajout\u00e9 une porte \u00e0 l'enclos.");
																							return;
																						}
																						default: {
																							continue;
																						}
																						}
																					}
																				}
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"Vous ne vous situez pas sur la porte.");
																			} else if (command.equalsIgnoreCase("A1")) {
																				this.getPlayer().getCurMap()
																						.getMountPark()
																						.setMountCell(this.getPlayer()
																								.getCurCell().getId());
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"Vous avez modifi\u00e9 la cellule de spawn de l'enclos.");
																			} else if (command.equalsIgnoreCase("B1")) {
																				this.getPlayer().getCases = true;
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"Vous avez activ\u00e9 le getCases.");
																			} else if (command.equalsIgnoreCase("C1")) {
																				this.getPlayer().getCases = false;
																				this.getPlayer().getCurMap()
																						.getMountPark()
																						.setCellObject(this
																								.getPlayer().thisCases);
																				this.getPlayer().thisCases.clear();
																				Database.getStatique()
																						.getMountpark_dataData()
																						.update(this.getPlayer()
																								.getCurMap()
																								.getMountPark());
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								"Vous avez appliqu\u00e9 les nouvelles cases \u00e0 l'enclos.");
																			} else {
																				if (command.equalsIgnoreCase(
																						"RELOADDROP")) {
																					World.reloadDrops();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des drops a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADENDFIGHT")) {
																					World.reloadEndFightActions();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des endfights a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADHOUSE")) {
																					World.reloadHouses();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des maisons a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADCOFFRE")) {
																					World.reloadTrunks();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des coffres a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADACTION")) {
																					World.reloadObjectsActions();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des actions a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADMAP")) {
																					World.reloadMaps();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des maps a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADMOUNTPARK")) {
																					final int j = Integer
																							.parseInt(infos[1]);
																					World.reloadMountParks(j);
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement de l'enclos "
																											+ j
																											+ " a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADNPC")) {
																					World.reloadNpcs();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des Npcs a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADSPELL")) {
																					World.reloadSpells();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des sorts a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADITEM")) {
																					World.reloadItems();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des items a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADMONSTER")) {
																					World.reloadMonsters();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des monstres a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADQUEST")) {
																					World.reloadQuests();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des qu\u00eates a \u00e9t\u00e9 \u00e9ffectu\u00e9.");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"RELOADADMIN")) {
																					Commandes.reload();
																					Groupes.reload();
																					World.reloadPlayerGroup();
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Le rechargement des commandes et des groupes ont \u00e9t\u00e9s \u00e9ffectu\u00e9s.");
																					return;
																				}
																				if (command
																						.equalsIgnoreCase("CONVERT")) {
																					try {
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										Integer.toHexString(
																												Integer.parseInt(
																														infos[1])));
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										new StringBuilder(
																												String.valueOf(
																														Integer.parseInt(
																																infos[1],
																																16))).toString());
																					} catch (Exception e) {
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										new StringBuilder(
																												String.valueOf(
																														Integer.parseInt(
																																infos[1],
																																16))).toString());
																					}
																					return;
																				}
																				if (command
																						.equalsIgnoreCase("LISTTYPE")) {
																					String s = "";
																					for (final ObjectTemplate obj5 : World
																							.getObjTemplates()) {
																						if (obj5.getType() == Integer
																								.parseInt(infos[1])) {
																							s = String.valueOf(s)
																									+ obj5.getId()
																									+ ",";
																						}
																					}
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									s);
																					return;
																				}
																				if (command.equalsIgnoreCase("EMOTE")) {
																					Player perso = this.getPlayer();
																					byte emoteId = 0;
																					try {
																						emoteId = Byte
																								.parseByte(infos[1]);
																						perso = World.getPersoByName(
																								infos[2]);
																					} catch (Exception ex51) {
																					}
																					if (perso == null) {
																						perso = this.getPlayer();
																					}
																					this.getPlayer()
																							.addStaticEmote(emoteId);
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"L'\u00e9mote "
																											+ emoteId
																											+ " a \u00e9t\u00e9 ajout\u00e9 au joueur "
																											+ perso.getName()
																											+ ".");
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"DELNPCITEM")) {
																					int npcGUID = 0;
																					int itmID = -1;
																					try {
																						npcGUID = Integer
																								.parseInt(infos[1]);
																						itmID = Integer
																								.parseInt(infos[2]);
																					} catch (Exception ex52) {
																					}
																					final org.aestia.map.Map map2 = this
																							.getPlayer().getCurMap();
																					final Npc npc2 = map2
																							.getNpc(npcGUID);
																					NpcTemplate npcTemplate = null;
																					if (npc2 == null) {
																						npcTemplate = World
																								.getNPCTemplate(
																										npcGUID);
																					} else {
																						npcTemplate = npc2
																								.getTemplate();
																					}
																					if (npcGUID == 0 || itmID == -1
																							|| npcTemplate == null) {
																						final String str5 = "NpcGUID ou itemID invalide.";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										str5);
																						return;
																					}
																					String str5 = "";
																					if (npcTemplate
																							.delItemVendor(itmID)) {
																						str5 = "L'objet a \u00e9t\u00e9 retir\u00e9.";
																					} else {
																						str5 = "L'objet n'a pas \u00e9t\u00e9 retir\u00e9.";
																					}
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									str5);
																					Database.getStatique()
																							.getNpc_templateData()
																							.update(npcTemplate);
																					return;
																				} else if (command.equalsIgnoreCase(
																						"ADDNPCITEM")) {
																					int npcGUID = 0;
																					int itmID = -1;
																					try {
																						npcGUID = Integer
																								.parseInt(infos[1]);
																						itmID = Integer
																								.parseInt(infos[2]);
																					} catch (Exception ex53) {
																					}
																					final org.aestia.map.Map map2 = this
																							.getPlayer().getCurMap();
																					final Npc npc2 = map2
																							.getNpc(npcGUID);
																					NpcTemplate npcTemplate = null;
																					if (npc2 == null) {
																						npcTemplate = World
																								.getNPCTemplate(
																										npcGUID);
																					} else {
																						npcTemplate = npc2
																								.getTemplate();
																					}
																					final ObjectTemplate item = World
																							.getObjTemplate(itmID);
																					if (npcGUID == 0 || itmID == -1
																							|| npcTemplate == null
																							|| item == null) {
																						final String str6 = "NpcGUID ou itemID invalide.";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										str6);
																						return;
																					}
																					String str6 = "";
																					if (npcTemplate
																							.addItemVendor(item)) {
																						str6 = "L'objet a \u00e9t\u00e9 rajout\u00e9.";
																					} else {
																						str6 = "L'objet n'a pas \u00e9t\u00e9 rajout\u00e9.";
																					}
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									str6);
																					Database.getStatique()
																							.getNpc_templateData()
																							.update(npcTemplate);
																					return;
																				} else {
																					if (command.equalsIgnoreCase(
																							"LISTEXTRA")) {
																						String mess = "Liste des Extra Monstres :";
																						for (final Map.Entry<Integer, org.aestia.map.Map> i2 : World
																								.getExtraMonsterOnMap()
																								.entrySet()) {
																							mess = String.valueOf(mess)
																									+ "\n- "
																									+ i2.getKey()
																									+ " est sur la map : "
																									+ i2.getValue()
																											.getId();
																						}
																						if (World.getExtraMonsterOnMap()
																								.size() <= 0) {
																							mess = "Aucun Extra Monstres existe.";
																						}
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess);
																						return;
																					}
																					if (command.equalsIgnoreCase(
																							"CREATEGUILD")) {
																						Player perso = this.getPlayer();
																						if (infos.length > 1) {
																							perso = World
																									.getPersoByName(
																											infos[1]);
																						}
																						if (perso == null) {
																							final String mess2 = "Le personnage n'existe pas.";
																							SocketManager
																									.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																											this.getClient(),
																											mess2);
																							return;
																						}
																						if (!perso.isOnline()) {
																							final String mess2 = "Le personnage "
																									+ perso.getName()
																									+ " n'est pas connect\u00e9.";
																							SocketManager
																									.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																											this.getClient(),
																											mess2);
																							return;
																						}
																						if (perso.get_guild() != null
																								|| perso.getGuildMember() != null) {
																							final String mess2 = "Le personnage "
																									+ perso.getName()
																									+ " poss\u00e8de d\u00e9j\u00e0 une guilde.";
																							SocketManager
																									.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																											this.getClient(),
																											mess2);
																							return;
																						}
																						SocketManager
																								.GAME_SEND_gn_PACKET(
																										perso);
																						final String mess2 = String
																								.valueOf(
																										perso.getName())
																								+ ": Panneau de creation de guilde ouvert.";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess2);
																						return;
																					} else {
																						if (command.equalsIgnoreCase(
																								"SEND")) {
																							SocketManager.send(
																									this.getClient(),
																									msg.substring(5));
																							SocketManager
																									.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																											this.getClient(),
																											"Le paquet a \u00e9t\u00e9 envoy\u00e9 : "
																													+ msg.substring(
																															5));
																							return;
																						}
																						if (command.equalsIgnoreCase(
																								"SENDTOMAP")) {
																							SocketManager
																									.sendPacketToMap(
																											this.getPlayer()
																													.getCurMap(),
																											infos[1]);
																							SocketManager
																									.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																											this.getClient(),
																											"Le paquet a \u00e9t\u00e9 envoy\u00e9 : "
																													+ infos[1]);
																							return;
																						}
																						if (command.equalsIgnoreCase(
																								"SENDTO")) {
																							Player perso = null;
																							infos = msg.split(" ", 3);
																							try {
																								perso = World
																										.getPersoByName(
																												infos[1]);
																							} catch (Exception ex54) {
																							}
																							if (perso == null) {
																								SocketManager
																										.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																												this.getClient(),
																												"Le nom du personnage est incorrect.");
																								return;
																							}
																							SocketManager.send(
																									World.getPersoByName(
																											infos[1]),
																									infos[2]);
																							SocketManager
																									.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																											this.getClient(),
																											"Le paquet a \u00e9t\u00e9 envoy\u00e9 : "
																													+ infos[1]
																													+ " \u00e0 "
																													+ infos[1]
																													+ ".");
																							return;
																						} else {
																							if (command
																									.equalsIgnoreCase(
																											"TITRE")) {
																								Player perso = this
																										.getPlayer();
																								byte TitleID = 0;
																								try {
																									TitleID = Byte
																											.parseByte(
																													infos[1]);
																									perso = World
																											.getPersoByName(
																													infos[2]);
																								} catch (Exception ex55) {
																								}
																								if (perso == null) {
																									perso = this
																											.getPlayer();
																								}
																								perso.set_title(
																										TitleID);
																								SocketManager
																										.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																												this.getClient(),
																												"Vous avez modifi\u00e9 le titre de "
																														+ perso.getName()
																														+ ".");
																								Database.getStatique()
																										.getPlayerData()
																										.update(perso,
																												false);
																								if (perso
																										.get_fight() == null) {
																									SocketManager
																											.GAME_SEND_ALTER_GM_PACKET(
																													perso.getCurMap(),
																													perso);
																								}
																								return;
																							}
																							if (command
																									.equalsIgnoreCase(
																											"POINTS")) {
																								int count = 0;
																								try {
																									count = Integer
																											.parseInt(
																													infos[1]);
																								} catch (Exception e6) {
																									SocketManager
																											.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																													this.getClient(),
																													"Valeur incorrecte.");
																									return;
																								}
																								if (count == 0) {
																									SocketManager
																											.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																													this.getClient(),
																													"Valeur inutile.");
																									return;
																								}
																								Player perso4 = this
																										.getPlayer();
																								if (infos.length == 3) {
																									final String name = infos[2];
																									perso4 = World
																											.getPersoByName(
																													name);
																									if (perso4 == null) {
																										perso4 = this
																												.getPlayer();
																									}
																								}
																								int pointtotal = perso4
																										.getAccount()
																										.getPoints()
																										+ count;
																								if (pointtotal < 0) {
																									pointtotal = 0;
																								}
																								if (pointtotal > 50000) {
																									pointtotal = 50000;
																								}
																								perso4.getAccount()
																										.setPoints(
																												pointtotal);
																								if (perso4.isOnline()) {
																									SocketManager
																											.GAME_SEND_STATS_PACKET(
																													perso4);
																								}
																								final String mess5 = "Vous venez de donner "
																										+ count
																										+ " points boutique \u00e0 "
																										+ perso4.getName()
																										+ ".";
																								SocketManager
																										.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																												this.getClient(),
																												mess5);
																								return;
																							} else {
																								if (command
																										.equalsIgnoreCase(
																												"ITEMTYPE")) {
																									int type = 0;
																									try {
																										type = Integer
																												.parseInt(
																														infos[1]);
																									} catch (Exception ex56) {
																									}
																									for (final ObjectTemplate obj5 : World
																											.getObjTemplates()) {
																										if (obj5.getType() == type) {
																											final org.aestia.object.Object addObj = obj5
																													.createNewItem(
																															1,
																															true);
																											if (!this
																													.getPlayer()
																													.addObjet(
																															addObj,
																															true)) {
																												continue;
																											}
																											World.addObjet(
																													addObj,
																													true);
																										}
																									}
																									SocketManager
																											.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																													this.getClient(),
																													"Vous avez tous les objets de type "
																															+ type
																															+ " dans votre inventaire.");
																									return;
																								}
																								if (command
																										.equalsIgnoreCase(
																												"FULLMORPH")) {
																									this.getPlayer()
																											.setFullMorph(
																													10,
																													false,
																													false);
																									SocketManager
																											.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																													this.getClient(),
																													"Vous avez \u00e9t\u00e9 transform\u00e9 en crocoburio.");
																									return;
																								}
																								if (command
																										.equalsIgnoreCase(
																												"UNFULLMORPH")) {
																									String pseudo = "";
																									try {
																										pseudo = infos[1];
																									} catch (Exception ex57) {
																									}
																									Player p3 = World
																											.getPersoByName(
																													pseudo);
																									if (p3 == null) {
																										p3 = this
																												.getPlayer();
																									}
																									p3.unsetFullMorph();
																									SocketManager
																											.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																													this.getClient(),
																													"Vous avez transform\u00e9 dans la forme originale "
																															+ p3.getName()
																															+ ".");
																									return;
																								}
																								if (command
																										.equalsIgnoreCase(
																												"PETSRES")) {
																									int objID = 1;
																									try {
																										objID = Integer
																												.parseInt(
																														infos[1]);
																									} catch (Exception ex58) {
																									}
																									final PetEntry p4 = World
																											.getPetsEntry(
																													objID);
																									if (p4 == null) {
																										SocketManager
																												.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																														this.getClient(),
																														"Le familier n'existe pas.");
																										return;
																									}
																									p4.resurrection();
																									SocketManager
																											.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																													this.getClient(),
																													"Vous avez r\u00e9ssuscit\u00e9 le familier.");
																									SocketManager
																											.GAME_SEND_UPDATE_OBJECT_DISPLAY_PACKET(
																													this.getPlayer(),
																													World.getObjet(
																															objID));
																									return;
																								} else {
																									if (command
																											.equalsIgnoreCase(
																													"SETGROUPE")) {
																										int groupe = -1;
																										try {
																											groupe = Integer
																													.parseInt(
																															infos[1]);
																										} catch (Exception ex59) {
																										}
																										Groupes g = null;
																										if (groupe > 0) {
																											g = Groupes
																													.getGroupeById(
																															groupe);
																										}
																										Player perso3 = null;
																										if (infos.length > 2) {
																											perso3 = World
																													.getPersoByName(
																															infos[2]);
																											if (perso3 == null) {
																												final String str4 = "Le personnage n'a pas \u00e9t\u00e9 trouv\u00e9.";
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																str4);
																												return;
																											}
																										}
																										perso3.setGroupe(
																												g,
																												true);
																										Database.getStatique()
																												.getAccountData()
																												.update(perso3
																														.getAccount());
																										final String str4 = "Le groupe du joueur "
																												+ perso3.getName()
																												+ " a \u00e9t\u00e9 modifi\u00e9.";
																										SocketManager
																												.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																														this.getClient(),
																														str4);
																										return;
																									}
																									if (command
																											.equalsIgnoreCase(
																													"SHOWRIGHTGROUPE")) {
																										int groupe = -1;
																										String cmd2 = "";
																										try {
																											groupe = Integer
																													.parseInt(
																															infos[1]);
																											cmd2 = infos[2];
																										} catch (Exception ex60) {
																										}
																										Groupes g2 = null;
																										if (groupe > 0) {
																											g2 = Groupes
																													.getGroupeById(
																															groupe);
																										}
																										if (g2 == null) {
																											final String str4 = "Le groupe est invalide.";
																											SocketManager
																													.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																															this.getClient(),
																															str4);
																											return;
																										}
																										final ArrayList<Commandes> c3 = g2
																												.getCommandes();
																										if (cmd2.equalsIgnoreCase(
																												"")) {
																											SocketManager
																													.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																															this.getClient(),
																															"\nCommandes disponibles pour le groupe "
																																	+ g2.getNom()
																																	+ " :\n");
																											for (final Commandes co : c3) {
																												final String args3 = (co
																														.getArguments()
																														.get(1) != null
																														&& !co.getArguments()
																																.get(1)
																																.equalsIgnoreCase(
																																		"")) ? (" + "
																																				+ co.getArguments()
																																						.get(1))
																																				: "";
																												final String desc2 = (co
																														.getArguments()
																														.get(2) != null
																														&& !co.getArguments()
																																.get(2)
																																.equalsIgnoreCase(
																																		"")) ? co
																																				.getArguments()
																																				.get(2)
																																				: "";
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"<u>" + co
																																		.getArguments()
																																		.get(0)
																																		+ args3
																																		+ "</u> - "
																																		+ desc2);
																											}
																										} else {
																											SocketManager
																													.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																															this.getClient(),
																															"\nCommandes recherch\u00e9s pour le groupe "
																																	+ g2.getNom()
																																	+ " :\n");
																											for (final Commandes co : c3) {
																												if (co.getArguments()
																														.get(0)
																														.contains(
																																cmd2.toUpperCase())) {
																													final String args3 = (co
																															.getArguments()
																															.get(1) != null
																															&& !co.getArguments()
																																	.get(1)
																																	.equalsIgnoreCase(
																																			"")) ? (" + "
																																					+ co.getArguments()
																																							.get(1))
																																					: "";
																													final String desc2 = (co
																															.getArguments()
																															.get(2) != null
																															&& !co.getArguments()
																																	.get(2)
																																	.equalsIgnoreCase(
																																			"")) ? co
																																					.getArguments()
																																					.get(2)
																																					: "";
																													SocketManager
																															.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																	this.getClient(),
																																	"<u>" + co
																																			.getArguments()
																																			.get(0)
																																			+ args3
																																			+ "</u> - "
																																			+ desc2);
																												}
																											}
																										}
																										return;
																									} else {
																										if (command
																												.equalsIgnoreCase(
																														"INV")) {
																											final int size = this
																													.getPlayer()
																													.get_size();
																											final Player perso4 = this
																													.getPlayer();
																											if (size == 0) {
																												if (perso4
																														.get_gfxID() == 8008) {
																													perso4.set_size(
																															150);
																												} else {
																													perso4.set_size(
																															100);
																												}
																												perso4.setInvisible(
																														false);
																												SocketManager
																														.GAME_SEND_ERASE_ON_MAP_TO_MAP(
																																perso4.getCurMap(),
																																perso4.getId());
																												SocketManager
																														.GAME_SEND_ADD_PLAYER_TO_MAP(
																																perso4.getCurMap(),
																																perso4);
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Vous \u00eates visible.");
																											} else {
																												perso4.setInvisible(
																														true);
																												perso4.set_size(
																														0);
																												SocketManager
																														.GAME_SEND_ERASE_ON_MAP_TO_MAP(
																																perso4.getCurMap(),
																																perso4.getId());
																												SocketManager
																														.GAME_SEND_ADD_PLAYER_TO_MAP(
																																perso4.getCurMap(),
																																perso4);
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Vous \u00eates invisible.");
																											}
																											return;
																										}
																										if (command
																												.equalsIgnoreCase(
																														"INCARNAM")) {
																											final Player perso = this
																													.getPlayer();
																											perso.teleport(
																													(short) 10292,
																													284);
																											SocketManager
																													.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																															this.getClient(),
																															"Vous avez \u00e9t\u00e9 t\u00e9l\u00e9port\u00e9 \u00e0 Incarnam.");
																											return;
																										}
																										if (command
																												.equalsIgnoreCase(
																														"ASTRUB")) {
																											final Player perso = this
																													.getPlayer();
																											perso.teleport(
																													(short) 7411,
																													311);
																											SocketManager
																													.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																															this.getClient(),
																															"Vous avez \u00e9t\u00e9 t\u00e9l\u00e9port\u00e9 \u00e0 Astrub.");
																											return;
																										}
																										if (command
																												.equalsIgnoreCase(
																														"DELQUEST")) {
																											int id = -1;
																											String perso5 = "";
																											try {
																												id = Integer
																														.parseInt(
																																infos[1]);
																												perso5 = infos[2];
																											} catch (Exception ex61) {
																											}
																											if (id == -1
																													|| perso5
																															.equalsIgnoreCase(
																																	"")) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Un des param\u00e8tres est invalide.");
																												return;
																											}
																											final Player p5 = World
																													.getPersoByName(
																															perso5);
																											final Quest q = Quest
																													.getQuestById(
																															id);
																											if (p5 == null
																													|| q == null) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"La qu\u00eate ou le joueur est introuvable.");
																												return;
																											}
																											final Quest.Quest_Perso qp = p5
																													.getQuestPersoByQuest(
																															q);
																											if (qp == null) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Le personnage n'a pas la qu\u00eate.");
																												return;
																											}
																											p5.delQuestPerso(
																													qp.getId());
																											if (qp.deleteQuestPerso()) {
																												Database.getStatique()
																														.getPlayerData()
																														.update(p5,
																																false);
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"La qu\u00eate a \u00e9t\u00e9 supprim\u00e9 sur le personnage "
																																		+ perso5
																																		+ ".");
																											} else {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Un probl\u00e8me est survenu.");
																											}
																											return;
																										} else if (command
																												.equalsIgnoreCase(
																														"ADDQUEST")) {
																											int id = -1;
																											String perso5 = "";
																											try {
																												id = Integer
																														.parseInt(
																																infos[1]);
																												perso5 = infos[2];
																											} catch (Exception ex62) {
																											}
																											if (id == -1
																													|| perso5
																															.equalsIgnoreCase(
																																	"")) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Un des param\u00e8tres est invalide.");
																												return;
																											}
																											final Player p5 = World
																													.getPersoByName(
																															perso5);
																											final Quest q = Quest
																													.getQuestById(
																															id);
																											if (p5 == null
																													|| q == null) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"La qu\u00eate ou le joueur est introuvable.");
																												return;
																											}
																											Quest.Quest_Perso qp = p5
																													.getQuestPersoByQuest(
																															q);
																											if (qp != null) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Le personnage a d\u00e9j\u00e0 la qu\u00eate.");
																												return;
																											}
																											q.applyQuest(
																													p5);
																											qp = p5.getQuestPersoByQuest(
																													q);
																											if (qp == null) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Une erreur est survenue.");
																												return;
																											}
																											SocketManager
																													.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																															this.getClient(),
																															"La qu\u00eate a \u00e9t\u00e9 ajout\u00e9 sur le personnage "
																																	+ perso5
																																	+ ".");
																											return;
																										} else if (command
																												.equalsIgnoreCase(
																														"FINISHQUEST")) {
																											int id = -1;
																											String perso5 = "";
																											try {
																												id = Integer
																														.parseInt(
																																infos[1]);
																												perso5 = infos[2];
																											} catch (Exception ex63) {
																											}
																											if (id == -1
																													|| perso5
																															.equalsIgnoreCase(
																																	"")) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Un des param\u00e8tres est invalide.");
																												return;
																											}
																											final Player p5 = World
																													.getPersoByName(
																															perso5);
																											final Quest q = Quest
																													.getQuestById(
																															id);
																											if (p5 == null
																													|| q == null) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"La qu\u00eate ou le joueur est introuvable.");
																												return;
																											}
																											final Quest.Quest_Perso qp = p5
																													.getQuestPersoByQuest(
																															q);
																											if (qp == null) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Le personnage n'a pas la qu\u00eate.");
																												return;
																											}
																											for (final Quest_Etape e2 : q
																													.getQuestEtapeList()) {
																												q.updateQuestData(
																														p5,
																														true,
																														e2.getValidationType());
																											}
																											Database.getStatique()
																													.getPlayerData()
																													.update(p5,
																															false);
																											SocketManager
																													.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																															this.getClient(),
																															"La qu\u00eate a \u00e9t\u00e9 termin\u00e9 sur le personnage "
																																	+ perso5
																																	+ ".");
																											return;
																										} else if (command
																												.equalsIgnoreCase(
																														"SKIPQUEST")) {
																											int id = -1;
																											String perso5 = "";
																											try {
																												id = Integer
																														.parseInt(
																																infos[1]);
																												perso5 = infos[2];
																											} catch (Exception ex64) {
																											}
																											if (id == -1
																													|| perso5
																															.equalsIgnoreCase(
																																	"")) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Un des param\u00e8tres est invalide.");
																												return;
																											}
																											final Player p5 = World
																													.getPersoByName(
																															perso5);
																											final Quest q = Quest
																													.getQuestById(
																															id);
																											if (p5 == null
																													|| q == null) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"La qu\u00eate ou le joueur est introuvable.");
																												return;
																											}
																											final Quest.Quest_Perso qp = p5
																													.getQuestPersoByQuest(
																															q);
																											if (qp == null) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Le personnage n'a pas la qu\u00eate.");
																												return;
																											}
																											for (final Quest_Etape e2 : q
																													.getQuestEtapeList()) {
																												if (qp.isQuestEtapeIsValidate(
																														e2)) {
																													continue;
																												}
																												q.updateQuestData(
																														p5,
																														true,
																														e2.getValidationType());
																												break;
																											}
																											Database.getStatique()
																													.getPlayerData()
																													.update(p5,
																															false);
																											SocketManager
																													.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																															this.getClient(),
																															"La qu\u00eate est pass\u00e9 \u00e0 l'\u00e9tape suivante sur le personnage "
																																	+ perso5
																																	+ ".");
																											return;
																										} else if (command
																												.equalsIgnoreCase(
																														"ITEMQUEST")) {
																											int id = -1;
																											try {
																												id = Integer
																														.parseInt(
																																infos[1]);
																											} catch (Exception ex65) {
																											}
																											if (id == -1) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"Le param\u00e8tre est invalide.");
																												return;
																											}
																											final Quest q2 = Quest
																													.getQuestById(
																															id);
																											if (q2 == null) {
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																"La qu\u00eate est introuvable.");
																												return;
																											}
																											for (final Quest_Etape e3 : q2
																													.getQuestEtapeList()) {
																												for (final Map.Entry<Integer, Integer> entry12 : e3
																														.getItemNecessaryList()
																														.entrySet()) {
																													final ObjectTemplate objT = World
																															.getObjTemplate(
																																	entry12.getKey());
																													final int qua2 = entry12
																															.getValue();
																													final org.aestia.object.Object obj6 = objT
																															.createNewItem(
																																	qua2,
																																	false);
																													if (this.getPlayer()
																															.addObjet(
																																	obj6,
																																	true)) {
																														World.addObjet(
																																obj6,
																																true);
																													}
																													SocketManager
																															.GAME_SEND_Im_PACKET(
																																	this.getPlayer(),
																																	"021;" + qua2
																																			+ "~"
																																			+ objT.getId());
																													if (objT.getType() == 32) {
																														this.getPlayer()
																																.setMascotte(
																																		entry12.getKey());
																													}
																												}
																											}
																											SocketManager
																													.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																															this.getClient(),
																															"Vous avez re\u00e7u tous les items n\u00e9c\u00e9ssaire \u00e0 la qu\u00eate.");
																											return;
																										} else if (command
																												.equalsIgnoreCase(
																														"ADDSF")) {
																											final Player perso = this
																													.getPlayer();
																											final org.aestia.map.Map map3 = perso
																													.getCurMap();
																											SchemaFight sf = World
																													.getSchemaFight(
																															map3.getPlaces());
																											String mess5 = "";
																											if (sf != null) {
																												mess5 = "Le SchemaFight existe d\u00e9j\u00e0. Son id est "
																														+ sf.getId()
																														+ ".";
																												SocketManager
																														.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																																this.getClient(),
																																mess5);
																												return;
																											}
																											sf = new SchemaFight(
																													map3.getPlaces());
																											if (World
																													.newSchemaFight(
																															sf)) {
																												mess5 = "Le SchemaFight a \u00e9t\u00e9 ajout\u00e9. Son id est "
																														+ sf.getId()
																														+ ".";
																											} else {
																												mess5 = "Le SchemaFight n'a pas \u00e9t\u00e9 ajout\u00e9.";
																											}
																											SocketManager
																													.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																															this.getClient(),
																															mess5);
																											return;
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																			if (command.equalsIgnoreCase("RSF")) {
																				final Player perso = this.getPlayer();
																				final org.aestia.map.Map map3 = perso
																						.getCurMap();
																				final SchemaFight sf = World
																						.getSchemaFight(
																								map3.getPlaces());
																				String mess5 = "";
																				if (sf == null) {
																					mess5 = "Le SchemaFight n'existe pas.";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess5);
																					return;
																				}
																				final int id2 = sf.getId();
																				final boolean b = World
																						.delSchemaFight(sf);
																				if (b) {
																					mess5 = "Le SchemaFight a \u00e9t\u00e9 supprim\u00e9. Son id \u00e9tait "
																							+ id2 + ".";
																				} else {
																					mess5 = "Le SchemaFight n'a pas \u00e9t\u00e9 supprim\u00e9. Son id est "
																							+ id2 + ".";
																				}
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								mess5);
																			} else if (command.equalsIgnoreCase("MSF")
																					&& (Config.config == 1
																							|| Config.config == 5)) {
																				String id3 = "";
																				String id4 = "";
																				String mess4 = "";
																				if (infos.length <= 2) {
																					mess4 = "Il manque les arguments.";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess4);
																					return;
																				}
																				id3 = infos[1];
																				id4 = infos[2];
																				final SchemaFight sf2 = World
																						.getSchemaFight(
																								Integer.parseInt(id3));
																				final SchemaFight sf3 = World
																						.getSchemaFight(
																								Integer.parseInt(id4));
																				if (sf2 == null) {
																					mess4 = "L'identifiant du premier SchemaFight est inexistant.";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess4);
																					return;
																				}
																				if (sf3 == null) {
																					mess4 = "L'identifiant du second SchemaFight est inexistant.";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess4);
																					return;
																				}
																				final Map<Short, org.aestia.map.Map> Maps = World
																						.getMaps();
																				for (final Map.Entry<Short, org.aestia.map.Map> entry13 : Maps
																						.entrySet()) {
																					final org.aestia.map.Map map4 = entry13
																							.getValue();
																					if (map4.getPlaces()
																							.equalsIgnoreCase(
																									sf2.getPlacesStr())
																							&& World.updateMapPlaces(
																									sf3,
																									map4.getId())) {
																						map4.setPlaces(
																								sf3.getPlacesStr());
																						mess4 = "La map d'id "
																								+ map4.getId()
																								+ " a d\u00e9sormais le sch\u00e9ma d'id "
																								+ sf3.getId() + ".";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess4);
																					}
																				}
																			} else if (command.equalsIgnoreCase("SF")) {
																				String id5 = "";
																				String mess2 = "";
																				if (infos.length <= 1) {
																					mess2 = "Il manque le premier argument.";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess2);
																					return;
																				}
																				id5 = infos[1];
																				final SchemaFight sf = World
																						.getSchemaFight(
																								Integer.parseInt(id5));
																				if (sf == null) {
																					mess2 = "L'identifiant du SchemaFight est inexistant.";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess2);
																					return;
																				}
																				final Player perso2 = this.getPlayer();
																				final org.aestia.map.Map map5 = perso2
																						.getCurMap();
																				final String oldSF = map5.getPlaces();
																				map5.setPlaces(sf.getPlacesStr());
																				final ArrayList<Case> case0 = CryptManager
																						.parseStartCell(map5, 0);
																				final ArrayList<Case> case2 = CryptManager
																						.parseStartCell(map5, 1);
																				for (final Case c4 : case0) {
																					if (!c4.isWalkableInFight()) {
																						map5.setPlaces(oldSF);
																						mess2 = "Le SchemaFight ne peux pas \u00eatre appliqu\u00e9 sur cette map : casebug.";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess2);
																						return;
																					}
																				}
																				for (final Case c4 : case2) {
																					if (!c4.isWalkableInFight()) {
																						map5.setPlaces(oldSF);
																						mess2 = "Le SchemaFight ne peux pas \u00eatre appliqu\u00e9 sur cette map : casebug.";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess2);
																						return;
																					}
																				}
																				if (World.updateMapPlaces(sf,
																						map5.getId())) {
																					mess2 = "Le SchemaFight de la map a \u00e9t\u00e9 modifi\u00e9.";
																				} else {
																					map5.setPlaces(oldSF);
																					mess2 = "Impossible de modifier le SchemaFight de la map.";
																				}
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								mess2);
																			} else if (command
																					.equalsIgnoreCase("ISSF")) {
																				String id5 = "";
																				String mess2 = "";
																				if (infos.length <= 1) {
																					mess2 = "Il manque le premier argument";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess2);
																					return;
																				}
																				id5 = infos[1];
																				final SchemaFight sf = World
																						.getSchemaFight(
																								Integer.parseInt(id5));
																				if (sf == null) {
																					mess2 = "L'identifiant du SchemaFight est inexistant.";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess2);
																					return;
																				}
																				final Player perso2 = this.getPlayer();
																				final org.aestia.map.Map map5 = perso2
																						.getCurMap();
																				final String oldSF = map5.getPlaces();
																				map5.setPlaces(sf.getPlacesStr());
																				final ArrayList<Case> case0 = CryptManager
																						.parseStartCell(map5, 0);
																				final ArrayList<Case> case2 = CryptManager
																						.parseStartCell(map5, 1);
																				for (final Case c4 : case0) {
																					if (!c4.isWalkableInFight()) {
																						map5.setPlaces(oldSF);
																						mess2 = "Le SchemaFight ne peux pas \u00eatre appliqu\u00e9 sur cette map : casebug.";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess2);
																						return;
																					}
																				}
																				for (final Case c4 : case2) {
																					if (!c4.isWalkableInFight()) {
																						map5.setPlaces(oldSF);
																						mess2 = "Le SchemaFight ne peux pas \u00eatre appliqu\u00e9 sur cette map : casebug.";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess2);
																						return;
																					}
																				}
																				map5.setPlaces(oldSF);
																				mess2 = "Il est possible de mettre ce SchemaFight sur la map.";
																				SocketManager
																						.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																								this.getClient(),
																								mess2);
																			} else {
																				if (command
																						.equalsIgnoreCase("SHOWSF")) {
																					final Map<Short, SchemaFight> SchemaFights = World
																							.getSchemaFights();
																					int i = 0;
																					String mess4 = "";
																					for (final Map.Entry<Short, SchemaFight> entry14 : SchemaFights
																							.entrySet()) {
																						mess4 = "[" + entry14.getKey()
																								+ "] "
																								+ entry14.getValue()
																										.getPlacesStr();
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess4);
																						++i;
																					}
																					if (i == 0) {
																						mess4 = "Il n'y a aucun SchemaFight.";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess4);
																					}
																					return;
																				}
																				if (command
																						.equalsIgnoreCase("HAVESF")) {
																					final Player perso = this
																							.getPlayer();
																					final org.aestia.map.Map map3 = perso
																							.getCurMap();
																					String mess4 = "";
																					final SchemaFight sf2 = World
																							.getSchemaFight(
																									map3.getPlaces());
																					if (sf2 == null) {
																						mess4 = "La map ne poss\u00e8de aucun SchemaFight.";
																					} else {
																						mess4 = "La map poss\u00e8de le SchemaFight d'id "
																								+ sf2.getId() + ".";
																					}
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess4);
																					return;
																				}
																				if (command
																						.equalsIgnoreCase("DALLSFBUG")
																						&& (Config.config == 1
																								|| Config.config == 5)) {
																					final Map<Short, org.aestia.map.Map> Maps2 = World
																							.getMaps();
																					int i = 0;
																					ArrayList<Case> case3 = null;
																					ArrayList<Case> case4 = null;
																					String mess3 = "";
																					for (final Map.Entry<Short, org.aestia.map.Map> entry15 : Maps2
																							.entrySet()) {
																						final org.aestia.map.Map map6 = entry15
																								.getValue();
																						if ((map6.getPlaces()
																								.endsWith("|")
																								|| map6.getPlaces()
																										.startsWith(
																												"|"))
																								&& !map6.getPlaces()
																										.equalsIgnoreCase(
																												"|")
																								&& !map6.getMobGroups()
																										.isEmpty()) {
																							final SchemaFight sf4 = new SchemaFight(
																									"|");
																							if (!World.updateMapPlaces(
																									sf4,
																									map6.getId())) {
																								continue;
																							}
																							map6.setPlaces(
																									sf4.getPlacesStr());
																							mess3 = "Le sch\u00e9ma de la map d'id "
																									+ map6.getId()
																									+ " a \u00e9t\u00e9 supprim\u00e9.";
																							SocketManager
																									.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																											this.getClient(),
																											mess3);
																							++i;
																						} else {
																							if (map6.getId() == 2110
																									|| map6.getW() != 15
																									|| map6.getH() != 17
																									|| map6.getMobGroups()
																											.isEmpty()
																									|| map6.getPlaces()
																											.equalsIgnoreCase(
																													"|")
																									|| map6.getPlaces()
																											.isEmpty()) {
																								continue;
																							}
																							case3 = CryptManager
																									.parseStartCell(
																											map6, 0);
																							case4 = CryptManager
																									.parseStartCell(
																											map6, 1);
																							boolean b2 = false;
																							for (final Case c5 : case3) {
																								if (!c5.isWalkableInFight()) {
																									b2 = true;
																									break;
																								}
																							}
																							if (!b2) {
																								for (final Case c5 : case4) {
																									if (!c5.isWalkableInFight()) {
																										b2 = true;
																										break;
																									}
																								}
																							}
																							if (!b2) {
																								continue;
																							}
																							final SchemaFight sf5 = new SchemaFight(
																									"|");
																							if (!World.updateMapPlaces(
																									sf5,
																									map6.getId())) {
																								continue;
																							}
																							map6.setPlaces(
																									sf5.getPlacesStr());
																							mess3 = "Le sch\u00e9ma de la map d'id "
																									+ map6.getId()
																									+ " a \u00e9t\u00e9 supprim\u00e9.";
																							SocketManager
																									.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																											this.getClient(),
																											mess3);
																							++i;
																						}
																					}
																					mess3 = "Au total, " + i
																							+ "sch\u00e9mas de map ont \u00e9t\u00e9s supprim\u00e9s. Fin de la commande.";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess3);
																					return;
																				}
																				if (command.equalsIgnoreCase(
																						"SHOWFIGHTPOS")) {
																					String mess = "Liste des StartCell [teamID][cellID]:";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess);
																					final String places = this
																							.getPlayer().getCurMap()
																							.getPlaces();
																					if (places.indexOf(124) == -1
																							|| places.length() < 2) {
																						mess = "Les places n'ont pas ete definies";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess);
																						return;
																					}
																					String team0 = "";
																					String team2 = "";
																					final String[] p6 = places
																							.split("\\|");
																					try {
																						team0 = p6[0];
																					} catch (Exception ex66) {
																					}
																					try {
																						team2 = p6[1];
																					} catch (Exception ex67) {
																					}
																					mess = "Team 0 : ";
																					for (int a6 = 0; a6 <= team0
																							.length() - 2; a6 += 2) {
																						final String code = team0
																								.substring(a6, a6 + 2);
																						mess = String.valueOf(mess)
																								+ CryptManager
																										.cellCode_To_ID(
																												code)
																								+ ",";
																					}
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess);
																					mess = "Team 1 : ";
																					for (int a6 = 0; a6 <= team2
																							.length() - 2; a6 += 2) {
																						final String code = team2
																								.substring(a6, a6 + 2);
																						mess = String.valueOf(mess)
																								+ CryptManager
																										.cellCode_To_ID(
																												code)
																								+ ",";
																					}
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess);
																				} else if (command.equalsIgnoreCase(
																						"ADDFIGHTPOS")) {
																					int team3 = -1;
																					int cell = -1;
																					try {
																						team3 = Integer
																								.parseInt(infos[1]);
																						cell = Integer
																								.parseInt(infos[2]);
																					} catch (Exception ex68) {
																					}
																					if (team3 < 0 || team3 > 1) {
																						final String str3 = "Team ou cellID incorects";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										str3);
																						return;
																					}
																					if (cell < 0
																							|| this.getPlayer()
																									.getCurMap()
																									.getCase(
																											cell) == null
																							|| !this.getPlayer()
																									.getCurMap()
																									.getCase(cell)
																									.isWalkable(true)) {
																						cell = this.getPlayer()
																								.getCurCell().getId();
																					}
																					final String places2 = this
																							.getPlayer().getCurMap()
																							.getPlaces();
																					final String[] p7 = places2
																							.split("\\|");
																					boolean already = false;
																					String team4 = "";
																					String team5 = "";
																					try {
																						team4 = p7[0];
																					} catch (Exception ex69) {
																					}
																					try {
																						team5 = p7[1];
																					} catch (Exception ex70) {
																					}
																					for (int a7 = 0; a7 <= team4
																							.length() - 2; a7 += 2) {
																						if (cell == CryptManager
																								.cellCode_To_ID(team4
																										.substring(a7,
																												a7 + 2))) {
																							already = true;
																						}
																					}
																					for (int a7 = 0; a7 <= team5
																							.length() - 2; a7 += 2) {
																						if (cell == CryptManager
																								.cellCode_To_ID(team5
																										.substring(a7,
																												a7 + 2))) {
																							already = true;
																						}
																					}
																					if (already) {
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										"La case est deja dans la liste");
																						return;
																					}
																					if (team3 == 0) {
																						team4 = String.valueOf(team4)
																								+ CryptManager
																										.cellID_To_Code(
																												cell);
																					} else if (team3 == 1) {
																						team5 = String.valueOf(team5)
																								+ CryptManager
																										.cellID_To_Code(
																												cell);
																					}
																					final String newPlaces = String
																							.valueOf(team4) + "|"
																							+ team5;
																					this.getPlayer().getCurMap()
																							.setPlaces(newPlaces);
																					if (!Database.getStatique()
																							.getMapData()
																							.update(this.getPlayer()
																									.getCurMap())) {
																						return;
																					}
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Les places ont ete modifiees ("
																											+ newPlaces
																											+ ")");
																				} else if (command.equalsIgnoreCase(
																						"DELFIGHTPOS")) {
																					int cell2 = -1;
																					try {
																						cell2 = Integer
																								.parseInt(infos[2]);
																					} catch (Exception ex71) {
																					}
																					if (cell2 < 0 || this.getPlayer()
																							.getCurMap()
																							.getCase(cell2) == null) {
																						cell2 = this.getPlayer()
																								.getCurCell().getId();
																					}
																					final String places = this
																							.getPlayer().getCurMap()
																							.getPlaces();
																					final String[] p8 = places
																							.split("\\|");
																					String newPlaces2 = "";
																					String team6 = "";
																					String team7 = "";
																					try {
																						team6 = p8[0];
																					} catch (Exception ex72) {
																					}
																					try {
																						team7 = p8[1];
																					} catch (Exception ex73) {
																					}
																					for (int a8 = 0; a8 <= team6
																							.length() - 2; a8 += 2) {
																						final String c6 = p8[0]
																								.substring(a8, a8 + 2);
																						if (cell2 != CryptManager
																								.cellCode_To_ID(c6)) {
																							newPlaces2 = String.valueOf(
																									newPlaces2) + c6;
																						}
																					}
																					newPlaces2 = String
																							.valueOf(newPlaces2) + "|";
																					for (int a8 = 0; a8 <= team7
																							.length() - 2; a8 += 2) {
																						final String c6 = p8[1]
																								.substring(a8, a8 + 2);
																						if (cell2 != CryptManager
																								.cellCode_To_ID(c6)) {
																							newPlaces2 = String.valueOf(
																									newPlaces2) + c6;
																						}
																					}
																					this.getPlayer().getCurMap()
																							.setPlaces(newPlaces2);
																					if (!Database.getStatique()
																							.getMapData()
																							.update(this.getPlayer()
																									.getCurMap())) {
																						return;
																					}
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Les places ont ete modifiees ("
																											+ newPlaces2
																											+ ")");
																				} else if (command.equalsIgnoreCase(
																						"DELALLFIGHTPOS")) {
																					this.getPlayer().getCurMap()
																							.setPlaces("");
																					if (!Database.getStatique()
																							.getMapData()
																							.update(this.getPlayer()
																									.getCurMap())) {
																						return;
																					}
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Les places ont ete mis a z\u00e9ro !");
																				} else if (command
																						.equalsIgnoreCase("APPLYALEASF")
																						&& (Config.config == 1
																								|| Config.config == 5)) {
																					final Map<Short, org.aestia.map.Map> Maps2 = World
																							.getMaps();
																					final Map<Short, SchemaFight> SchemaFights2 = World
																							.getSchemaFights();
																					String mess4 = "";
																					int i3 = 0;
																					int y = 0;
																					if (SchemaFights2.size() < 2) {
																						mess4 = "Il n'y a pas assez de SchemaFight pour appliquer cette commande.";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess4);
																						return;
																					}
																					final Random random = new Random();
																					SchemaFight sf6 = null;
																					String oldSF2 = "";
																					String listmap = "";
																					ArrayList<Case> case5 = null;
																					ArrayList<Case> case6 = null;
																					ArrayList<Short> noSF = null;
																					for (final Map.Entry<Short, org.aestia.map.Map> entry16 : Maps2
																							.entrySet()) {
																						final org.aestia.map.Map map7 = entry16
																								.getValue();
																						if (map7.getW() == 15
																								&& map7.getH() == 17
																								&& !map7.getMobGroups()
																										.isEmpty()) {
																							noSF = new ArrayList<Short>();
																							if (!map7.getPlaces()
																									.equalsIgnoreCase(
																											"|")
																									&& !map7.getPlaces()
																											.isEmpty()) {
																								continue;
																							}
																							boolean b3 = false;
																							while (!b3) {
																								if (noSF.size() == SchemaFights2
																										.size() - 1) {
																									mess4 = "Il n'y a aucun SchemaFight applicable sur la map d'id "
																											+ map7.getId()
																											+ ".";
																									SocketManager
																											.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																													this.getClient(),
																													mess4);
																									listmap = String
																											.valueOf(
																													listmap)
																											+ map7.getId()
																											+ ", ";
																									++y;
																									break;
																								}
																								final List<Short> keys = new ArrayList<Short>(
																										SchemaFights2
																												.keySet());
																								Short randomKey;
																								for (randomKey = keys
																										.get(random
																												.nextInt(
																														keys.size())), sf6 = World
																																.getSchemaFight(
																																		randomKey); sf6 == null
																																				|| noSF.contains(
																																						sf6.getId()); sf6 = World
																																								.getSchemaFight(
																																										randomKey)) {
																									randomKey = keys
																											.get(random
																													.nextInt(
																															keys.size()));
																								}
																								oldSF2 = map7
																										.getPlaces();
																								map7.setPlaces(sf6
																										.getPlacesStr());
																								try {
																									case5 = CryptManager
																											.parseStartCell(
																													map7,
																													0);
																									case6 = CryptManager
																											.parseStartCell(
																													map7,
																													1);
																									for (final Case c7 : case5) {
																										if (!c7.isWalkableInFight()) {
																											b3 = true;
																											break;
																										}
																									}
																									if (!b3) {
																										for (final Case c7 : case6) {
																											if (!c7.isWalkableInFight()) {
																												b3 = true;
																												break;
																											}
																										}
																									}
																								} catch (Exception e8) {
																									b3 = true;
																								}
																								if (!b3) {
																									if (World
																											.updateMapPlaces(
																													sf6,
																													map7.getId())) {
																										b3 = true;
																										mess4 = "Le SchemaFight d'id "
																												+ sf6.getId()
																												+ " a \u00e9t\u00e9 appliqu\u00e9 sur la map d'id "
																												+ map7.getId()
																												+ ".";
																										SocketManager
																												.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																														this.getClient(),
																														mess4);
																										++i3;
																									} else {
																										map7.setPlaces(
																												oldSF2);
																										noSF.add(sf6
																												.getId());
																									}
																								} else {
																									b3 = false;
																									map7.setPlaces(
																											oldSF2);
																									noSF.add(sf6
																											.getId());
																								}
																							}
																						}
																					}
																					mess4 = String.valueOf(i3)
																							+ " SchemaFight ont \u00e9t\u00e9 appliqu\u00e9s. "
																							+ y
																							+ " maps n'ont pas eu de SchemaFight. Fin de la commande.";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess4);
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									listmap);
																				} else if (command.equalsIgnoreCase(
																						"ADDMOBSUBAREA")) {
																					String monsters = "";
																					String mess2 = "";
																					if (infos.length > 1) {
																						monsters = infos[1];
																						final Player perso3 = this
																								.getPlayer();
																						final org.aestia.map.Map map8 = perso3
																								.getCurMap();
																						final World.SubArea subArea = map8
																								.getSubArea();
																						final ArrayList<org.aestia.map.Map> maps = subArea
																								.getMaps();
																						int i4 = 0;
																						int y2 = 0;
																						for (final org.aestia.map.Map m2 : maps) {
																							if (m2.getPlaces()
																									.equalsIgnoreCase(
																											"")
																									|| m2.getPlaces()
																											.equalsIgnoreCase(
																													"|")) {
																								m2.setMobPossibles("");
																								Database.getStatique()
																										.getMapData()
																										.updateMonster(
																												m2, "");
																								++y2;
																							} else {
																								m2.setMobPossibles(
																										monsters);
																								Database.getStatique()
																										.getMapData()
																										.updateMonster(
																												m2,
																												monsters);
																								++i4;
																							}
																							m2.refreshSpawns();
																						}
																						mess2 = String.valueOf(i4)
																								+ " maps ont \u00e9t\u00e9s modifi\u00e9s et refresh. "
																								+ y2
																								+ "maps ont \u00e9t\u00e9s modifi\u00e9s sans monstres et refresh.";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess2);
																						return;
																					}
																					mess2 = "Il manque le premier argument.";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess2);
																				} else if (command.equalsIgnoreCase(
																						"GSMOBSUBAREA")) {
																					byte maxGroup = 0;
																					byte minSize = 0;
																					byte fixSize = 0;
																					byte maxSize = 0;
																					final byte def = -1;
																					String mess8 = "";
																					if (infos.length > 4) {
																						maxGroup = Byte
																								.parseByte(infos[1]);
																						minSize = Byte
																								.parseByte(infos[2]);
																						fixSize = Byte
																								.parseByte(infos[3]);
																						maxSize = Byte
																								.parseByte(infos[4]);
																						final Player perso6 = this
																								.getPlayer();
																						final org.aestia.map.Map map6 = perso6
																								.getCurMap();
																						final World.SubArea subArea2 = map6
																								.getSubArea();
																						final ArrayList<org.aestia.map.Map> maps2 = subArea2
																								.getMaps();
																						int i5 = 0;
																						int y3 = 0;
																						for (final org.aestia.map.Map m3 : maps2) {
																							if (m3.getPlaces()
																									.equalsIgnoreCase(
																											"")
																									|| m3.getPlaces()
																											.equalsIgnoreCase(
																													"|")) {
																								m3.setGs(def, def, def,
																										def);
																								Database.getStatique()
																										.getMapData()
																										.updateGs(m3);
																								++y3;
																							} else {
																								m3.setGs(maxGroup,
																										minSize,
																										fixSize,
																										maxSize);
																								Database.getStatique()
																										.getMapData()
																										.updateGs(m3);
																								++i5;
																							}
																							m3.refreshSpawns();
																						}
																						mess8 = String.valueOf(i5)
																								+ " maps ont \u00e9t\u00e9s modifi\u00e9s et refresh. "
																								+ y3
																								+ " maps ont \u00e9t\u00e9s modifi\u00e9s \u00e0 -1 partout et refresh.";
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										mess8);
																						return;
																					}
																					mess8 = "Il manque les arguments.";
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									mess8);
																				} else {
																					if (command.equalsIgnoreCase(
																							"FINDEXTRAMONSTER")) {
																						final Map<Integer, Map<String, Map<String, Integer>>> extras = World
																								.getExtraMonsters();
																						final Map<Short, org.aestia.map.Map> maps3 = World
																								.getMaps();
																						for (final Map.Entry<Integer, Map<String, Map<String, Integer>>> entry17 : extras
																								.entrySet()) {
																							final Integer idMob = entry17
																									.getKey();
																							for (final Map.Entry<Short, org.aestia.map.Map> entry18 : maps3
																									.entrySet()) {
																								final org.aestia.map.Map map6 = entry18
																										.getValue();
																								for (final Monster.MobGrade mob : map6
																										.getMobPossibles()) {
																									if (mob.getTemplate()
																											.getId() == idMob) {
																										Console.println(
																												"Map avec extraMonster : "
																														+ map6.getId()
																														+ " -> "
																														+ idMob
																														+ ".",
																												Console.Color.YELLOW);
																									}
																								}
																							}
																						}
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										"Recherche termin\u00e9 et affich\u00e9 en console.");
																						return;
																					}
																					if (command.equalsIgnoreCase(
																							"GETAREA")) {
																						int subArea3 = -1;
																						int area = -1;
																						int superArea = -1;
																						try {
																							subArea3 = this.getPlayer()
																									.getCurMap()
																									.getSubArea()
																									.getId();
																							area = this.getPlayer()
																									.getCurMap()
																									.getSubArea()
																									.getArea().get_id();
																							superArea = this.getPlayer()
																									.getCurMap()
																									.getSubArea()
																									.getArea()
																									.get_superArea()
																									.get_id();
																						} catch (Exception ex74) {
																						}
																						SocketManager
																								.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																										this.getClient(),
																										"subArea : "
																												+ subArea3
																												+ "\nArea : "
																												+ area
																												+ "\nsuperArea : "
																												+ superArea);
																						return;
																					}
																					SocketManager
																							.GAME_SEND_CONSOLE_MESSAGE_PACKET(
																									this.getClient(),
																									"Commande invalide !");
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}

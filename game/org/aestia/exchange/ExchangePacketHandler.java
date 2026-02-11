// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.exchange;

import org.aestia.client.Account;
import org.aestia.db.Database;
import org.aestia.dynamic.Migration;
import org.aestia.game.GameClient;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.kernel.Main;

public class ExchangePacketHandler {
	public static void parser(final String packet) {
		Label_0849: {
			switch (packet.charAt(0)) {
			case 'F': {
				switch (packet.charAt(1)) {
				case '?': {
					final int i = 50000 - Main.gameServer.getPlayerNumber();
					Main.exchangeClient.send("F" + i);
					break;
				}
				}
				break;
			}
			case 'S': {
				switch (packet.charAt(1)) {
				case 'H': {
					switch (packet.charAt(2)) {
					case 'K': {
						Console.println("Le serveur login valide la connexion avec succes.", Console.Color.SUCCESS);
						break;
					}
					}
					break;
				}
				case 'K': {
					switch (packet.charAt(2)) {
					case '?': {
						final int i = 50000 - Main.gameServer.getPlayerNumber();
						Main.exchangeClient.send("SK" + Main.serverId + ";" + Main.key + ";" + i);
						break;
					}
					case 'K': {
						Console.println("Le serveur login a accepte la connexion !", Console.Color.SUCCESS);
						Main.exchangeClient.send("SH" + Main.Ip + ";" + Main.gamePort);
						break;
					}
					case 'R': {
						Console.println("Le serveur login a refuse la connexion !", Console.Color.ERROR);
						Main.stop();
						break;
					}
					}
					break;
				}
				}
				break;
			}
			case 'W': {
				if (packet.split("W").length > 2) {
					final String[] packets = packet.split("W");
					String[] array;
					for (int length = (array = packets).length, j = 0; j < length; ++j) {
						final String p = array[j];
						if (!p.equalsIgnoreCase("")) {
							parser("W" + p);
						}
					}
					break;
				}
				switch (packet.charAt(1)) {
				case 'A': {
					final int id = Integer.parseInt(packet.substring(2));
					Database.getStatique().getAccountData().load(id);
					if (World.getCompte(id).getCurPerso() != null) {
						World.getCompte(id).getGameClient().kick();
					}
					Main.gameServer.addWaitingCompte(World.getCompte(id));
					break;
				}
				case 'K': {
					final int id = Integer.parseInt(packet.substring(2));
					Console.println("Reception d'un WK" + id + ".", Console.Color.INFORMATION);
					Database.getStatique().getPlayerData().updateAllLogged(id, 0);
					Database.getStatique().getAccountData().setLogged(id, 0);
					final Account a = World.getCompte(id);
					if (a == null) {
						break;
					}
					final GameClient gc = a.getGameClient();
					if (gc != null) {
						gc.kick();
						break;
					}
					break;
				}
				}
				break;
			}
			case 'M': {
				switch (packet.charAt(1)) {
				case 'G': {
					final String[] split = packet.substring(2).split("\\|");
					final String account = split[0];
					final String server = split[1];
					final StringBuilder alks = new StringBuilder("MT" + account + "|" + server);
					String[] split2;
					for (int length2 = (split2 = split[2].split("\\,")).length, k = 0; k < length2; ++k) {
						final String id2 = split2[k];
						alks.append(World.getPersonnage(Integer.parseInt(id2)).parseALK());
					}
					Main.exchangeClient.send(alks.toString());
					break;
				}
				case 'F': {
					final String[] split = packet.substring(2).split("\\|");
					final int id3 = Integer.parseInt(split[0]);
					final int sender = Integer.parseInt(split[1]);
					final String players = packet.substring(packet.indexOf("|", packet.indexOf("|") + 1) + 1);
					Migration.migrations.get(id3).add(sender, "|" + players);
					break;
				}
				case 'D': {
					final int player = Integer.parseInt(packet.substring(2));
					World.deletePerso(World.getPersonnage(player));
					break;
				}
				}
				break;
			}
			}
		}
	}
}

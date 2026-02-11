// 

// 

package org.aestia.login.packet;

import org.aestia.kernel.Main;
import org.aestia.login.LoginClient;
import org.aestia.login.LoginServer;
import org.aestia.object.Account;

public class PacketHandler {
	public static void parser(final LoginClient client, final String packet) {
		switch (client.getStatus()) {
		case WAIT_VERSION: {
			client.setStatus(LoginClient.Status.WAIT_ACCOUNT);
			break;
		}
		case WAIT_ACCOUNT: {
				if (!verifyAccountName(client, packet.split("\n")[0]) || !verifyPassword(client, packet.split("\n")[1])) {
					client.send("AlEf");
				}
			break;
		}
		case WAIT_NICKNAME: {
			ChooseNickName.verify(client, packet);
			break;
		}
		case SERVER: {
			switch (packet.substring(0, 2)) {
			case "AF": {
				FriendServerList.get(client, packet.substring(2));
				break;
			}
			case "AX": {
				ServerSelected.get(client, packet.substring(2));
				break;
			}
			case "Af": {
				AccountQueue.verify(client);
				break;
			}
			case "Ax": {
				ServerList.get(client);
				break;
			}
			default:
				client.kick();
				break;
			}

			break;
		}
		default:
			client.kick();

		}
	}

	public static boolean verifyAccountName(final LoginClient client, final String name) {
		try {
			Account account = Main.database.getAccountData().load(name.toLowerCase(), client);
			if(account == null)
				return false;
			client.setAccount(account);
			client.getAccount().setClient(client);
		} catch (Exception e) {
			return false;
		}

		if (client.getAccount() == null) {
			return false;
		}
		client.setStatus(LoginClient.Status.WAIT_PASSWORD);
		return true;
	}

	public static boolean verifyPassword(final LoginClient client, final String pass) {
		if (!decryptPassword(pass.substring(2), client.getKey()).equals(client.getAccount().getPass())) {
			return false;
		}
		client.setStatus(LoginClient.Status.SERVER);
		return true;
	}

	public static String decryptPassword(String pass, final String key) {
		final String Chaine = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
		String decrypted = "";
		for (int i = 0; i < pass.length(); i += 2) {
			final char PKey = key.charAt(i / 2);
			final int ANB = Chaine.indexOf(pass.charAt(i));
			final int ANB2 = Chaine.indexOf(pass.charAt(i + 1));
			final int somme1 = ANB + Chaine.length();
			final int somme2 = ANB2 + Chaine.length();
			int APass = somme1 - PKey;
			if (APass < 0) {
				APass += 64;
			}
			APass *= 16;
			int AKey = somme2 - PKey;
			if (AKey < 0) {
				AKey += 64;
			}
			final char PPass = (char) (APass + AKey);
			decrypted = String.valueOf(decrypted) + PPass;
		}
		return decrypted;
	}
}

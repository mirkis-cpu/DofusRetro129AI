// 

// 

package org.aestia.login.packet;

import org.aestia.object.Account;
import org.aestia.object.Server;
import org.aestia.kernel.Main;
import org.aestia.login.LoginClient;

public class AccountQueue {

	public static void verify(final LoginClient client) {
		final Account account = client.getAccount();
		if (Main.database.getAccountData().isBanned(client.getIoSession().getRemoteAddress().toString().replace("/", "").split(":")[0])|| account.isBanned()) {
			System.out.println("[" + client.getIoSession().getId() + "] Le compte est banni.");
			client.send("AlEb");
			client.kick();
			return;
		}
		sendInformation(client,account);
	}

	public static void sendInformation(final LoginClient client,Account account) {
		if (account == null) {
			System.out.println("[" + client.getIoSession().getId()
					+ "] Le compte n'existe pas. Le client va \u00eatre kick\u00e9.");
			client.send("AlEf");
			client.kick();
			return;
		}
		if (account.getPseudo().isEmpty()) {
			System.out.println("[" + client.getIoSession().getId() + "] Le compte " + account.getName()
					+ " n'a pas de pseudo. Envoie des informations pour mettre le pseudo et status Nickname.");
			client.send("AlEr");
			client.setStatus(LoginClient.Status.WAIT_NICKNAME);
			return;
		}
		System.out.println("[" + client.getIoSession().getId() + "] Envoi des informations de connexion au compte "
				+ account.getName() + ".");
		Main.database.getPlayerData().load(account);
		client.send("Af0|0|0|1|-1");
		client.send("Ad" + account.getPseudo());
		client.send("Ac0");
		client.send(Server.getHostList());
		client.send("AlK" + (account.isMj() ? 1 : 0));
		client.send("AQ" + account.getQuestion());
	}
}

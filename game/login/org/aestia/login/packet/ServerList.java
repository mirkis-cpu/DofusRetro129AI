// 

// 

package org.aestia.login.packet;

import org.aestia.object.Player;

import org.aestia.object.Server;
import org.aestia.object.Account;
import org.aestia.login.LoginClient;

public class ServerList
{
    public static void get(final LoginClient client) {
        client.send("AxK" + serverList(client.getAccount()));
    }
    
    public static String serverList(final Account account) {
        final StringBuilder sb = new StringBuilder(new StringBuilder(String.valueOf(account.getSubscribeRemaining())).toString());
        for (final Server server : Server.servers.values()) {
            final int i = characterNumber(account, server.getId());
            if (i == 0) {
                continue;
            }
            sb.append("|").append(server.getId()).append(",").append(i);
        }
        System.out.println("[" + account.getClient().getIoSession().getId() + "] Envoi de la liste des serveurs pour le compte " + account.getName() + ". Liste : '" + sb.toString() + "'");
        return sb.toString();
    }
    
    public static int characterNumber(final Account account, final int server) {
        int i = 0;
        for (final Player character : account.getPlayers().values()) {
            if (character.getServer() == server) {
                ++i;
            }
        }
        return i;
    }
}

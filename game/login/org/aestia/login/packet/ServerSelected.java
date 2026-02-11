package org.aestia.login.packet;


import org.aestia.exchange.ExchangeClient;
import java.util.Map;
import org.aestia.object.Account;
import org.aestia.kernel.Main;
import org.aestia.object.Server;
import org.aestia.login.LoginClient;

public class ServerSelected
{
    public static void get(final LoginClient client, final String packet) {
        Server server = null;
        final Account account = client.getAccount();
        try {
            final int i = Integer.parseInt(packet);
            server = Server.get(i);
            System.out.println("[" + client.getIoSession().getId() + "] S\u00e9lection du serveur " + i + " pour le compte " + account.getName() + ".");
     
        if (server == null) {
            System.out.println("[" + client.getIoSession().getId() + "] Le serveur s\u00e9lectionner n'existe pas pour le compte " + account.getName() + ".");
            client.send("AXEr");
            return;
        }
        if (server.getState() != 1) {
            System.out.println("[" + client.getIoSession().getId() + "] L'\u00e9tat du serveur s\u00e9lectionn\u00e9 est indisponible pour le compte " + account.getName() + ".");
            client.send("AXEd");
            return;
        }
        if (account.getSubscribeRemaining() == 0L && server.getSub() == 1) {
            System.out.println("[" + client.getIoSession().getId() + "] Le serveur s\u00e9lectionn\u00e9 est plein ou il faut \u00eatre abonn\u00e9 pour le compte " + account.getName() + ".");
            client.send(getFreeServer());
            return;
        }
        account.setServer(server.getId());
        server.send("WA" + account.getUUID());
        final StringBuilder sb = new StringBuilder();
        final String ip = client.getIoSession().getLocalAddress().toString().replace("/", "").split("\\:")[0];
        sb.append("AYK").append(ip.equals("127.0.0.1") ? "127.0.0.1" : server.getIp()).append(":").append(server.getPort()).append(";").append(account.getUUID());
        client.send(sb.toString());
        client.getAccount().setState(0);
        System.out.println("[" + client.getIoSession().getId() + "] Le compte " + account.getName() + " a bien choissi son serveur : le Game prend le relai.");
        //LoginServer.connected.put(client.getAccount().getUUID(),server.getId());
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("[" + client.getIoSession().getId() + "] La s\u00e9lection du serveur a \u00e9chou\u00e9 pour le compte " + account.getName() + ".");
            client.send("AXEr");
            client.kick();
            return;
        }
    }
    
    private static String getFreeServer() {
        final StringBuilder sb = new StringBuilder("AXEf");
        for (final Map.Entry<Long, ExchangeClient> entry : Main.config.getExchangeServer().getClients().entrySet()) {
            final ExchangeClient client = entry.getValue();
            if (client == null) {
                continue;
            }
            final Server server = client.getServer();
            if (server == null) {
                continue;
            }
            if (server.getSub() != 0 || server.getFreePlaces() > 0) {
                continue;
            }
            sb.append(server.getId()).append("|");
        }
        return sb.toString();
    }
}

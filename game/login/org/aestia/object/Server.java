// 

// 

package org.aestia.object;


import java.util.ArrayList;
import org.aestia.login.LoginHandler;
import java.util.HashMap;
import java.util.Map;
import org.aestia.exchange.ExchangeClient;

public class Server
{
    private int id;
    private int port;
    private int state;
    private int pop;
    private int sub;
    private int freePlaces;
    private String ip;
    private String key;
    private ExchangeClient client;
    public static Map<Integer, Server> servers;
    
    static {
        Server.servers = new HashMap<Integer, Server>();
    }
    
    public Server(final int id, final String key, final int pop, final int sub) {
        this.id = id;
        this.key = key;
        this.state = 0;
        this.pop = pop;
        this.sub = sub;
        Server.servers.put(this.id, this);
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public boolean isSubscribe() {
        return this.sub == 1;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    public String getIp() {
        return this.ip;
    }
    
    public void setIp(final String ip) {
        this.ip = ip;
    }
    
    public int getState() {
        return this.state;
    }
    
    public void setState(final int state) {
        this.state = state;
        sendHostListToAll();
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public int getPop() {
        return this.pop;
    }
    
    public void setPop(final int pop) {
        this.pop = pop;
    }
    
    public int getSub() {
        return this.sub;
    }
    
    public void setSub(final int sub) {
        this.sub = sub;
    }
    
    public ExchangeClient getClient() {
        return this.client;
    }
    
    public void setClient(final ExchangeClient client) {
        this.client = client;
    }
    
    public int getFreePlaces() {
        return this.freePlaces;
    }
    
    public void setFreePlaces(final int freePlaces) {
        this.freePlaces = freePlaces;
    }
    
    public static Server get(final int id) {
        if (!Server.servers.containsKey(id)) {
            return null;
        }
        return Server.servers.get(id);
    }
    
    public void send(final Object arg0) {
        if (arg0 instanceof String) {
            this.getClient().send((String)arg0);
        }
        else {
            this.getClient().getIoSession().write(arg0);
        }
    }
    
    public static void sendHostListToAll() {
        LoginHandler.sendToAll(getHostList());
    }
    
    public static String getHostList() {
        final StringBuilder sb = new StringBuilder("AH");
        final ArrayList<Server> list = new ArrayList<Server>();
        list.addAll(Server.servers.values());
        for (final Server server : list) {
            sb.append(server.getId()).append(";").append((server == null) ? 0 : server.getState()).append(";110;1|");
        }
        return sb.toString();
    }
}

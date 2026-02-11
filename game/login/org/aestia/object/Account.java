package org.aestia.object;

import org.aestia.kernel.Main;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aestia.login.LoginClient;

public class Account
{
    private int UUID;
    private int server;
    private String name;
    private String pass;
    private String pseudo;
    private String question;
    private byte state;
    private LoginClient client;
    private long subscribe;
    private boolean banned;
    private boolean isMj;
    private Map<Integer, Player> players;
    
    public Account(final int UUID, final String name, final String pass, final String pseudo, final String question, final byte state, final long subscribe, final byte banned) {
        this.banned = false;
        this.isMj = false;
        this.players = new HashMap<Integer, Player>();
        this.UUID = UUID;
        this.name = name;
        this.pass = pass;
        this.pseudo = pseudo;
        this.question = question;
        this.state = state;
        this.subscribe = subscribe;
        this.banned = (banned != 0);
    }
    
    public int getUUID() {
        return this.UUID;
    }
    
    public void setUUID(final int UUID) {
        this.UUID = UUID;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getPass() {
        return this.pass;
    }
    
    public void setPass(final String pass) {
        this.pass = pass;
    }
    
    public String getPseudo() {
        return this.pseudo;
    }
    
    public void setPseudo(final String pseudo) {
        this.pseudo = pseudo;
    }
    
    public String getQuestion() {
        return this.question;
    }
    
    public void setQuestion(final String question) {
        this.question = question;
    }
    
    public boolean isMj() {
        return this.isMj;
    }
    
    public LoginClient getClient() {
        return this.client;
    }
    
    public void setClient(final LoginClient client) {
        this.client = client;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public void setState(final int state) {
        this.state = (byte)state;
        Main.database.getAccountData().update(this);
    }
    
    public int getServer() {
        return this.server;
    }
    
    public void setServer(final int server) {
        this.server = server;
    }
    
    public long getSubscribeRemaining() {
        final long remaining = this.subscribe - System.currentTimeMillis();
        return (remaining <= 0L) ? 0L : remaining;
    }
    
    public long getSubscribe() {
        final long remaining = this.subscribe - System.currentTimeMillis();
        return (remaining <= 0L) ? 0L : this.subscribe;
    }
    
    public void setSubscribe(final long subscribe) {
        this.subscribe = subscribe;
    }
    
    public boolean isSubscribes() {
        final Server s = Server.get(this.server);
        return !s.isSubscribe() || this.getSubscribeRemaining() != 0L;
    }
    
    public void setBanned(final boolean banned) {
        this.banned = banned;
    }
    
    public boolean isBanned() {
        return this.banned;
    }
    
    public void addPlayer(final Player player) {
        if (this.players.containsKey(player.getId())) {
            return;
        }
        this.players.put(player.getId(), player);
        if (!this.isMj && player.getGroupe() > 0) {
            this.isMj = true;
        }
    }
    
    public void delPlayer(final Player player) {
        if (!this.players.containsKey(player.getId())) 
            return;
        this.players.remove(player.getId());
    }
    
    public Map<Integer, Player> getPlayers() {
        return this.players;
    }
}

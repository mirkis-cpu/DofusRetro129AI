// 

// 

package org.aestia.exchange;

import java.util.ArrayList;
import java.util.Map;

import org.aestia.kernel.Main;
import org.apache.mina.core.buffer.IoBuffer;
import org.aestia.object.Server;
import org.apache.mina.core.session.IoSession;

public class ExchangeClient
{
    private long id;
    private IoSession ioSession;
    private Server server;
    
    public ExchangeClient(final long id, final IoSession ioSession) {
        this.id = id;
        this.ioSession = ioSession;
    }
    
    public void send(final String s) {
        final IoBuffer ioBuffer = IoBuffer.allocate(2048);
        ioBuffer.put(s.getBytes());
        this.ioSession.write((Object)ioBuffer.flip());
    }
    
    public void kick() {
        this.ioSession.close(true);
    }
    
    public long getId() {
        return this.id;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public IoSession getIoSession() {
        return this.ioSession;
    }
    
    public void setIoSession(final IoSession ioSession) {
        this.ioSession = ioSession;
    }
    
    public Server getServer() {
        return this.server;
    }
    
    public void setServer(final Server server) {
        this.server = server;
    }
    
    public void parse(final String packet) {
        try {
            switch (packet.charAt(0)) {
                case 'F': {
                    final int freePlaces = Integer.parseInt(packet.substring(1));
                    this.getServer().setFreePlaces(freePlaces);
                    break;
                }
                case 'S': {
                    switch (packet.charAt(1)) {
                        case 'H': {
                            final Server server = this.getServer();
                            final String[] s = packet.substring(2).split("\\;");
                            server.setIp(s[0]);
                            server.setPort(Integer.parseInt(s[1]));
                            server.setState(1);
                            this.send("SHK");
                            break;
                        }
                        case 'K': {
                            final String[] s = packet.substring(2).split("\\;");
                            final int id = Integer.parseInt(s[0]);
                            final String key = s[1];
                            final int freePlaces = Integer.parseInt(s[2]);
                            final Server server = Server.get(id);
                            if (!server.getKey().equals(key)) {
                                this.send("SKR");
                                this.kick();
                            }
                            server.setClient(this);
                            this.setServer(server);
                            server.setFreePlaces(freePlaces);
                            this.send("SKK");
                            break;
                        }
                        case 'S': {
                            if (this.getServer() == null) {
                                return;
                            }
                            final int statut = Integer.parseInt(packet.substring(2));
                            this.getServer().setState(statut);
                            break;
                        }
                    }
                    break;
                }
                case 'M': {
                    switch (packet.charAt(1)) {
                        case 'P': {
                            final int id2 = Integer.parseInt(packet.substring(2));
                            final Map<Server, ArrayList<Integer>> map = Main.database.getPlayerData().loadAllPlayersByAccountId(this.getServer().getId(), id2);
                            for (final Map.Entry<Server, ArrayList<Integer>> entry : map.entrySet()) {
                                String players = "";
                                for (final Integer i : entry.getValue()) {
                                    players = String.valueOf(players) + (players.isEmpty() ? String.valueOf(i) : ("," + i));
                                }
                                entry.getKey().send("MG" + id2 + "|" + this.getServer().getId() + "|" + players);
                            }
                            break;
                        }
                        case 'T': {
                            final String[] split = packet.substring(2).split("\\|");
                            final String account = split[0];
                            final String players = packet.substring(packet.indexOf("|", packet.indexOf("|") + 1) + 1);
                            final Server server2 = Server.get(Integer.parseInt(split[1]));
                            if (server2 == null) {
                                return;
                            }
                            server2.send("MF" + account + "|" + this.getServer().getId() + "|" + players);
                            break;
                        }
                        case 'D': {
                            final String[] split = packet.substring(2).split("\\|");
                            final Server server2 = Server.get(Integer.parseInt(split[1]));
                            if (server2 == null) {
                                return;
                            }
                            server2.send("MD" + split[0]);
                            break;
                        }
                        case 'O': {
                            final String[] split = packet.substring(2).split("\\|");
                            final Server server2 = Server.get(Integer.parseInt(split[1]));
                            if (server2 == null) {
                                return;
                            }
                            server2.send("MO" + split[0] + "|" + this.getServer().getId());
                            break;
                        }
                    }
                    break;
                }
                default: {
                    this.send("Packet undefined\"" + packet + "\"");
                    this.kick();
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.kick();
        }
    }
}

package org.aestia.exchange;

import java.util.HashMap;
import java.util.Map;
import org.apache.mina.core.session.IoSession;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import org.aestia.kernel.Main;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.SocketAcceptor;

public class ExchangeServer
{
    private SocketAcceptor acceptor;
    
    public ExchangeServer() {
        (this.acceptor = (SocketAcceptor)new NioSocketAcceptor()).setReuseAddress(true);
        this.acceptor.setHandler((IoHandler)new ExchangeHandler());
    }
    
    public void start() {
        if (this.acceptor.isActive()) {
            return;
        }
        try {
            this.acceptor.bind((SocketAddress)new InetSocketAddress(Main.config.getExchangeIp(), Main.config.getExchangePort()));
        }
        catch (IOException e) {
            System.out.println(e.toString());
            System.out.println("Fail to bind acceptor : " + e);
            return;
        }
        finally {
            System.out.println(" > Exchange server started on port " + Main.config.getExchangePort());
        }
    }
    
    public void stop() {
        if (!this.acceptor.isActive()) {
            return;
        }
        this.acceptor.unbind();
        for (final IoSession session : this.acceptor.getManagedSessions().values()) {
            if (session.isConnected() || !session.isClosing()) {
                session.close(true);
            }
        }
        this.acceptor.dispose();
        System.out.println("Exchange server stoped");
    }
    
    public Map<Long, ExchangeClient> getClients() {
        final Map<Long, ExchangeClient> clients = new HashMap<Long, ExchangeClient>();
        try {
            for (final Map.Entry<Long, IoSession> entry : this.acceptor.getManagedSessions().entrySet()) {
                final Long id = entry.getKey();
                final IoSession session = entry.getValue();
                if (session == null) {
                    continue;
                }
                if (!(session.getAttribute((Object)"client") instanceof ExchangeClient)) {
                    continue;
                }
                final ExchangeClient client = (ExchangeClient)session.getAttribute((Object)"client");
                if (client == null) {
                    continue;
                }
                clients.put(id, client);
            }
        }
        catch (Exception ex) {}
        return clients;
    }
}

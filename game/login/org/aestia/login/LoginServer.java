// 

// 

package org.aestia.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.aestia.kernel.Main;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import java.nio.charset.Charset;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class LoginServer
{
    private NioSocketAcceptor acceptor;
    
    
    public LoginServer() {
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter((ProtocolCodecFactory)new TextLineCodecFactory(Charset.forName("UTF8"), LineDelimiter.NUL, new LineDelimiter("\n\u0000"))));
        acceptor.setHandler(new LoginHandler());
    }
    
    public void start() {
        if (acceptor.isActive()) {
            return;
        }
        try {
            acceptor.bind(new InetSocketAddress(Main.config.getLoginPort()));
        }
        catch (IOException e) {
            System.out.println(e.toString());
            System.out.println("Fail to bind acceptor : " + e);
            return;
        }
        finally {
            System.out.println(" > Login server started on port " + Main.config.getLoginPort());
        }
    }
    
    public void stop() {
        if (!acceptor.isActive()) {
            return;
        }
        acceptor.unbind();
        for (final IoSession session : acceptor.getManagedSessions().values()) {
            if (session.isConnected() || !session.isClosing()) {
                session.close(true);
            }
        }
        acceptor.dispose();
        System.out.println("Login server stoped");
    }
    
    public List<LoginClient> getClients() {
        List<LoginClient> clients=  new ArrayList<>();
        for(IoSession session : acceptor.getManagedSessions().values()) 
        	clients.add((LoginClient)session.getAttribute("client"));
        return clients;
    }
    
    public LoginClient getClient(IoSession session) {
        return (LoginClient) session.getAttribute("client");
    }
}

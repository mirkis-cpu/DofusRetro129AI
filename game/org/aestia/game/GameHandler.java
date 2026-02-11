// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class GameHandler implements IoHandler {
	public static final Map<Long, GameClient> clients = new ConcurrentHashMap<>();

	@Override
	public void sessionCreated(final IoSession arg0) throws Exception {
		clients.put(arg0.getId(), new GameClient(arg0));
	}

	@Override
	public void messageReceived(final IoSession arg0, final Object arg1) throws Exception {
		System.err.println("Recv <" + arg1);
		clients.get(arg0.getId()).parsePacket((String) arg1);
	}

	@Override
	public void sessionClosed(final IoSession arg0) throws Exception {
		final GameClient client = clients.get(arg0.getId());
		if (client != null) {
			client.kick();
			clients.remove(arg0.getId());
		}
	}

	@Override
	public void exceptionCaught(final IoSession arg0, final Throwable arg1) throws Exception {
		arg1.printStackTrace();
	}

	@Override
	public void messageSent(final IoSession arg0, final Object arg1) throws Exception {
		final GameClient client = clients.get(arg0.getId());
		System.err.println("messageSent --> "
				+ ((client.getPersonnage() == null) ? "" : client.getPersonnage().getName()) + " : " + arg1);
	}

	@Override
	public void sessionIdle(final IoSession arg0, final IdleStatus arg1) throws Exception {
	}

	@Override
	public void sessionOpened(final IoSession arg0) throws Exception {
	}

	@Override
	public void inputClosed(final IoSession arg0) throws Exception {
		arg0.close(true);
	}
}

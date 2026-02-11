package org.aestia.login;

import java.util.Random;
import org.aestia.kernel.Main;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.service.IoHandler;

public class LoginHandler implements IoHandler {

	public void exceptionCaught(final IoSession arg0, final Throwable arg1) throws Exception {
		arg1.printStackTrace();
	} 

	public void messageReceived(final IoSession arg0, final Object arg1) throws Exception {
		Main.config.getLoginServer().getClient(arg0).parser((String)arg1);
		System.out.println("session " + arg0.getId() + " : recv < " + arg1);
	}

	public void messageSent(final IoSession arg0, final Object arg1) throws Exception {
		System.out.println("session " + arg0.getId() + " : sent > " + arg1.toString());
	}

	public void sessionClosed(final IoSession arg0) throws Exception {
		System.out.println("session " + arg0.getId() + " closed");
	}

	public void sessionCreated(final IoSession arg0) throws Exception {
		arg0.setAttribute("client", new LoginClient(arg0, this.genKey()));
		System.out.println("session " + arg0.getId() + " created");
	}

	public void sessionIdle(final IoSession arg0, final IdleStatus arg1) throws Exception {
		System.out.println("session " + arg0.getId() + " idle");
	}

	public void sessionOpened(final IoSession arg0) throws Exception {
		System.out.println("session " + arg0.getId() + " oppened");
	}

	public static synchronized void sendToAll(final String packet) {
		for(LoginClient client: Main.config.getLoginServer().getClients()) 
			client.send(packet);
	}

	public String genKey() {
		final String alphabet = "abcdefghijklmnopqrstuvwxyz";
		final StringBuilder hashKey = new StringBuilder();
		final Random rand = new Random();
		for (int i = 0; i < 32; ++i) {
			hashKey.append(alphabet.charAt(rand.nextInt(alphabet.length())));
		}
		return hashKey.toString();
	}

	public void inputClosed(final IoSession arg0) throws Exception {
		arg0.close(true);
	}
}

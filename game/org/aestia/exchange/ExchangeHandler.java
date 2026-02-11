// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.exchange;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.aestia.kernel.Console;
import org.aestia.kernel.Main;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ExchangeHandler extends IoHandlerAdapter {
	private IoSession session;
	private boolean use;

	public ExchangeHandler() {
		this.use = true;
	}

	@Override
	public void sessionCreated(final IoSession arg0) throws Exception {
		if (Main.modDebug) {
			Console.println("Connexion avec le serveur de login ouverte.", Console.Color.EXCHANGE);
		}
		this.session = arg0;
	}

	@Override
	public void messageReceived(final IoSession arg0, final Object arg1) throws Exception {
		final String packet = ioBufferToString(arg1);
		if (Main.modDebug) {
			Console.println("messageReceived <-- " + packet, Console.Color.EXCHANGE);
		}
		ExchangePacketHandler.parser(packet);
	}

	@Override
	public void messageSent(final IoSession arg0, final Object arg1) throws Exception {
		final String packet = ioBufferToString(arg1);
		if (Main.modDebug) {
			Console.println("messageSent --> " + packet, Console.Color.EXCHANGE);
		}
	}

	@Override
	public void sessionClosed(final IoSession arg0) throws Exception {
		if (Main.modDebug) {
			Console.println("Fermeture de la connexion avec le serveur de login !", Console.Color.EXCHANGE);
		}
		Console.println("La connexion a \u00e9t\u00e9 perdu avec le serveur de connexion !", Console.Color.ERROR);
		if (this.use) {
			Main.exchangeClient.restart();
		}
	}

	@Override
	public void exceptionCaught(final IoSession arg0, final Throwable arg1) throws Exception {
		Console.println("Erreur avec le serveur de login. Exception : " + arg1.getMessage(), Console.Color.ERROR);
		arg1.printStackTrace();
	}

	@Override
	public void inputClosed(final IoSession arg0) throws Exception {
		arg0.close(true);
	}

	public static String ioBufferToString(final Object o) {
		final IoBuffer ioBuffer = IoBuffer.allocate(2048);
		ioBuffer.put((IoBuffer) o);
		ioBuffer.flip();
		final CharsetDecoder charsetDecoder = Charset.forName("UTF-8").newDecoder();
		try {
			return ioBuffer.getString(charsetDecoder);
		} catch (CharacterCodingException e) {
			e.printStackTrace();
			return "undefined";
		}
	}

	public IoSession getSession() {
		return this.session;
	}

	public void end() {
		this.use = false;
	}
}

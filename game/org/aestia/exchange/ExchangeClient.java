// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.exchange;

import java.net.InetSocketAddress;

import org.aestia.game.scheduler.TimerWaiter;
import org.aestia.kernel.Console;
import org.aestia.kernel.Main;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ExchangeClient {
	private IoConnector ioConnector;
	public ConnectFuture connectFuture;
	private boolean restart;
	private TimerWaiter waiter;

	public ExchangeClient() {
		this.ioConnector = new NioSocketConnector();
		this.waiter = new TimerWaiter();
		Main.exchangeClient = this;
		this.ioConnector.setHandler(new ExchangeHandler());
		this.restart = false;
	}

	public void initialize() {
		try {
			this.connectFuture = this.ioConnector.connect(new InetSocketAddress(Main.exchangeIp, Main.exchangePort));
		} catch (Exception e) {
			Console.println("Le serveur Game ne trouve pas le serveur Login. Exception : " + e.getMessage(),
					Console.Color.ERROR);
			this.waiter.addNext(new Runnable() {
				@Override
				public void run() {
					ExchangeClient.this.restart();
				}
			}, 2000L);
			e.printStackTrace();
			return;
		}
		this.waiter.addNext(new Runnable() {
			@Override
			public void run() {
				if (ExchangeClient.this.ioConnector.isActive()) {
					ExchangeClient.access$1(ExchangeClient.this, false);
					Console.println("Le serveur Game a ete demarre avec succes sur l'ip suivante : " + Main.exchangeIp
							+ ":" + Main.exchangePort, Console.Color.SUCCESS);
					return;
				}
				if (!Main.isRunning) {
					return;
				}
				Console.println("Try to connect..", Console.Color.WAITING);
				ExchangeClient.this.restart();
			}
		}, 3000L);
	}

	public void restart() {
		this.restart = true;
		if (!Main.isRunning) {
			return;
		}
		Console.println("Login server not found..", Console.Color.ERROR);
		((ExchangeHandler) this.ioConnector.getHandler()).end();
		this.stop();
		this.connectFuture = null;
		(this.ioConnector = new NioSocketConnector()).setHandler(new ExchangeHandler());
		this.initialize();
	}

	public void stop() {
		this.ioConnector.dispose();
		this.connectFuture.cancel();
		Console.println("Exchange server stopped !", Console.Color.INFORMATION);
	}

	public void send(final String packet) {
		((ExchangeHandler) this.ioConnector.getHandler()).getSession().write(StringToIoBuffer(packet));
	}

	public static IoBuffer StringToIoBuffer(final String s) {
		final IoBuffer ioBuffer = IoBuffer.allocate(2048);
		ioBuffer.put(s.getBytes());
		return ioBuffer.flip();
	}

	public boolean isRestart() {
		return this.restart;
	}

	static /* synthetic */ void access$1(final ExchangeClient exchangeClient, final boolean restart) {
		exchangeClient.restart = restart;
	}
}

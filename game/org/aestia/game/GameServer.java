// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.game;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.aestia.client.Account;
import org.aestia.client.Player;
import org.aestia.common.SocketManager;
import org.aestia.db.Database;
import org.aestia.game.scheduler.GlobalManager;
import org.aestia.game.world.World;
import org.aestia.kernel.Console;
import org.aestia.kernel.Main;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class GameServer {
	private ArrayList<Account> waitingClients;
	private int maxConnections;
	private IoAcceptor acceptor;

	public GameServer() {
		this.waitingClients = new ArrayList<Account>();
		this.maxConnections = 0;
		try {
			Main.gameServer = this;
			this.acceptor = new NioSocketAcceptor();
			this.acceptor.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF8"), LineDelimiter.NUL,
							new LineDelimiter("\n\u0000"))));
			this.acceptor.setHandler(new GameHandler());
			this.acceptor.setCloseOnDeactivation(true);
			GlobalManager.loadWorldScheduler();
		} catch (Exception e) {
			this.sendError(e);
		}
	}

	public void initialize() {
		try {
			if (this.acceptor.isActive()) {
				Console.println("Impossible d'initialiser le GameServer car il est deja actif.", Console.Color.ERROR);
				return;
			}
			this.acceptor.bind(new InetSocketAddress(Main.gamePort));
			Console.println("Le serveur est ligne sur l'adresse suivante : " + Main.Ip + ":" + Main.gamePort,
					Console.Color.SUCCESS);
		} catch (IOException e) {
			this.sendError(e);
			Main.stop();
		}
	}

	public void close() {
		try {
			this.acceptor.unbind();
			this.acceptor.getManagedSessions().values().stream()
					.filter(session -> session.isConnected() || !session.isClosing()).forEach(session -> {
						session.close(true);
					});
			this.acceptor.dispose();
		} catch (Exception e) {
			this.sendError(e);
			Main.stop();
		}
	}

	public static String getServerDate() {
		final Date actDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd");
		String jour;
		for (jour = new StringBuilder(String.valueOf(Integer.parseInt(dateFormat.format(actDate)))).toString(); jour
				.length() < 2; jour = "0" + jour) {
		}
		dateFormat = new SimpleDateFormat("MM");
		String mois;
		for (mois = new StringBuilder(String.valueOf(Integer.parseInt(dateFormat.format(actDate)) - 1)).toString(); mois
				.length() < 2; mois = "0" + mois) {
		}
		dateFormat = new SimpleDateFormat("yyyy");
		final String annee = new StringBuilder(String.valueOf(Integer.parseInt(dateFormat.format(actDate)) - 1370))
				.toString();
		return "BD" + annee + "|" + mois + "|" + jour;
	}

	public int getMaxPlayer() {
		return this.maxConnections;
	}

	public void newClient() {
		if (this.acceptor.getManagedSessionCount() > this.maxConnections) {
			this.maxConnections = this.acceptor.getManagedSessionCount();
		}
	}

	public int getPlayerNumber() {
		return this.acceptor.getManagedSessionCount();
	}

	public int getPlayerNumberByIp() {
		try {
			final ArrayList<String> IPs = new ArrayList<String>();
			for (final Map.Entry<Long, IoSession> entry : this.acceptor.getManagedSessions().entrySet()) {
				final IoSession actuel = entry.getValue();
				if (actuel == null) {
					continue;
				}
				if (!(actuel.getAttribute("client") instanceof GameClient)) {
					continue;
				}
				final GameClient client = (GameClient) actuel.getAttribute("client");
				if (client == null) {
					continue;
				}
				final Account a = client.getAccount();
				if (a == null) {
					continue;
				}
				final String IP = a.getCurIP();
				if (IP.equalsIgnoreCase("")) {
					continue;
				}
				if (IPs.contains(IP)) {
					continue;
				}
				IPs.add(IP);
			}
			return IPs.size();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public Map<Long, GameClient> getClients() {
		return GameHandler.clients;
	}

	public synchronized Account getWaitingCompte(final int guid) {
		for (int i = 0; i < this.waitingClients.size(); ++i) {
			if (this.waitingClients.get(i).getGuid() == guid) {
				return this.waitingClients.get(i);
			}
		}
		return null;
	}

	public synchronized void delWaitingCompte(final Account account) {
		this.waitingClients.remove(account);
	}

	public synchronized void addWaitingCompte(final Account account) {
		this.waitingClients.add(account);
	}

	public static String getServerTime() {
		final Date actDate = new Date();
		return "BT" + (actDate.getTime() + 3600000L);
	}

	public static void addToLog(final String arg0) {
		if (Main.modDebug) {
			Console.println("addToLog : " + arg0, Console.Color.INFORMATION);
		}
	}

	public static void setState(final int state) {
		if (Main.exchangeClient == null || Main.exchangeClient.isRestart() || Main.exchangeClient.connectFuture == null
				|| Main.exchangeClient.connectFuture.isCanceled() || !Main.exchangeClient.connectFuture.isConnected()) {
			return;
		}
		switch (state) {
		case 0: {
			Main.exchangeClient.send("SS0");
			break;
		}
		case 1: {
			Main.exchangeClient.send("SS1");
			break;
		}
		case 2: {
			Main.exchangeClient.send("SS2");
			break;
		}
		}
	}

	public void kickAll(final boolean kickGm) {
		SocketManager.PACKET_POPUP_ALL(
				"Vous allez \u00eatre kick\u00e9 du serveur pour une cause de maintenance.<br>Veuillez nous excuser de la g\u00eane occasion\u00e9.<br><br>Cordialement.");
		SocketManager.SEND_MESSAGE_DECO_ALL(4, "");
		for (final Player player : World.getOnlinePersos()) {
			if (player != null) {
				Database.getStatique().getPlayerData().update(player, true);
				if (player.getGroupe() != null && !player.getGroupe().isPlayer() && kickGm) {
					continue;
				}
				try {
					player.getGameClient().getSession().close(true);
				} catch (Exception ex) {
				}
			}
		}

	}

	private void sendError(final Exception e) {
		e.printStackTrace();
		Console.println("Erreur GameServer : " + e.getMessage(), Console.Color.ERROR);
	}
}

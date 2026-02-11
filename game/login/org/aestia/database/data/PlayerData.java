// 

// 

package org.aestia.database.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import org.aestia.object.Server;
import java.util.Map;
import java.sql.ResultSet;
import org.aestia.object.Account;
import com.zaxxer.hikari.HikariDataSource;
import org.aestia.object.Player;
import org.aestia.database.AbstractDAO;

public class PlayerData extends AbstractDAO<Player> {
	public PlayerData(final HikariDataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Player load(final Object obj) {
		try {
			if (obj instanceof Account) {
				final Account account = (Account) obj;
				final Result result = this.getData("SELECT * FROM players WHERE account = " + account.getUUID());
				final ResultSet resultSet = result.resultSet;
				while (resultSet.next()) {
					account.addPlayer(
							new Player(resultSet.getInt("id"), resultSet.getInt("server"), resultSet.getInt("groupe")));
				}
				this.close(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean update(final Player obj) {
		return false;
	}

	public Map<Server, ArrayList<Integer>> loadAllPlayersByAccountId(final int notServer, final int account) {
		final Map<Server, ArrayList<Integer>> maps = new HashMap<Server, ArrayList<Integer>>();
		try {
			final Result result = this.getData("SELECT id,server FROM players WHERE account = '" + account
					+ "' AND NOT server = '" + notServer + "';");
			final ResultSet resultSet = result.resultSet;
			while (resultSet.next()) {
				final Server server = Server.servers.get(resultSet.getInt("server"));
				final int guid = resultSet.getInt("id");
				if (maps.get(server) == null) {
					final ArrayList<Integer> array = new ArrayList<Integer>();
					array.add(guid);
					maps.put(server, array);
				} else {
					maps.get(server).add(guid);
				}
			}
			this.close(result);
		} catch (SQLException e) {
			 e.printStackTrace();
		}
		return maps;
	}

	public int isLogged(final Account account) {
		int logged = 0;
		try {
			final Result result = this.getData("SELECT * FROM players WHERE account = " + account.getUUID());
			final ResultSet resultSet = result.resultSet;
			while (resultSet.next()) {
				if (resultSet.getInt("logged") == 1) {
					logged = resultSet.getInt("server");
				}
			}
			this.close(result);
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return logged;
	}

	public void setState(final int state, final Account a) {
		try {
			final String baseQuery = "UPDATE players SET logged = '" + state + "' WHERE account = '" + a.getUUID()
					+ "';";
			final PreparedStatement statement = this.getPreparedStatement(baseQuery);
			this.execute(statement);
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
}

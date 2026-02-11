package org.aestia.database.data;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import com.zaxxer.hikari.HikariDataSource;
import org.aestia.object.Account;
import org.aestia.object.Server;
import org.aestia.database.AbstractDAO;
import org.aestia.login.LoginClient;
import org.aestia.login.LoginServer;

public class AccountData extends AbstractDAO<Account> {
	public Map<Integer, Account> accounts = new ConcurrentHashMap<>();

	public AccountData(final HikariDataSource source) {
		super(source);
	}

	@Override
	public Account load(final Object id) {
		Account account = null;
		try {
			String query = "SELECT * FROM accounts WHERE guid = " + id;
			final Result result = super.getData(query);
			account = this.loadFromResultSet(result.resultSet);
			this.close(result);
			if (account != null) {
				query = "UPDATE accounts SET reload_needed = 0 WHERE guid = " + id;
				super.execute(query);
			}
		} catch (Exception e) {
			System.out.println("Can't load account with guid" + id);
		}
		return account;
	}

	public Account load(final String name) {
		Account account = null;
		try {
			String query = "SELECT * FROM accounts WHERE account = '" + name + "';";
			final Result result = super.getData(query);
			account = this.loadFromResultSet(result.resultSet);

			this.close(result);
			if (account != null) {
				query = "UPDATE accounts SET reload_needed = 0 WHERE guid = '" + account.getUUID() + "';";
				super.execute(query);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}

	public Account load(final String name, LoginClient client) {
		Account account = null;
		try {
			String query = "SELECT * FROM accounts WHERE account = '" + name + "';";
			final Result result = super.getData(query);
			checkAccount(account = this.loadFromResultSet(result.resultSet));
			this.close(result);
			if (account != null) {
				query = "UPDATE accounts SET reload_needed = 0 WHERE guid = '" + account.getUUID() + "';";
				super.execute(query);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}

	public void checkAccount(Account account) {
		if(account == null)
			return;
		int id = account.getUUID();
		if (accounts.get(id) != null) {
			accounts.get(id).getClient().send("AlEa");
			accounts.get(id).getClient().kick();
		}
		
		accounts.put(id, account);

		//if (LoginServer.connected.get(id) != null) {
			//Server.get(LoginServer.connected.get(id)).send("WK" + id);
		//	LoginServer.connected.remove(id);
		//}
		
	}

	@Override
	public boolean update(final Account obj) {
		try {
			final String baseQuery = "UPDATE accounts SET account = '" + obj.getName() + "'," + " pass = '"
					+ obj.getPass() + "'," + " pseudo = '" + obj.getPseudo() + "'," + " question = '"
					+ obj.getQuestion() + "'," + " logged = '" + obj.getState() + "'," + " subscribe = '"
					+ obj.getSubscribe() + "'" + " WHERE guid = '" + obj.getUUID() + "';";
			final PreparedStatement statement = this.getPreparedStatement(baseQuery);
			this.execute(statement);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String exist(final String nickname) {
		String name = null;
		try {
			final String query = "SELECT * FROM accounts WHERE pseudo = '" + nickname + "';";
			final Result result = super.getData(query);
			if (result.resultSet.next())
				name = result.resultSet.getString("account");
			this.close(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	public boolean isBanned(final String ip) {
		boolean banned = false;
		try {
			final String query = "SELECT * FROM banip WHERE 'ip' LIKE '" + ip + "';";
			final Result result = super.getData(query);
			final ResultSet resultSet = result.resultSet;
			if (resultSet.next()) {
				banned = true;
			}
			this.close(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return banned;
	}

	protected Account loadFromResultSet(final ResultSet resultSet) throws SQLException {
		if (resultSet.next()) {
			final Account account = new Account(resultSet.getInt("guid"), resultSet.getString("account").toLowerCase(),
					resultSet.getString("pass"), resultSet.getString("pseudo"), resultSet.getString("question"),
					resultSet.getByte("logged"), resultSet.getLong("subscribe"), resultSet.getByte("banned"));
			return account;
		}
		return null;
	}
}

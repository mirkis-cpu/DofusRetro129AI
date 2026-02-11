package org.aestia.database;

import java.sql.ResultSet;
import org.aestia.kernel.Main;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariDataSource;

public abstract class AbstractDAO<T> implements DAO<T> {
	protected HikariDataSource dataSource;

	public AbstractDAO(final HikariDataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected void execute(final String query) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = this.dataSource.getConnection();
			statement = connection.createStatement();
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} finally {
			this.close(statement);
			this.close(connection);
		}
	}

	protected void execute(final PreparedStatement statement) {
		Connection connection = null;
		try {
			connection = statement.getConnection();
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(statement);
			this.close(connection);
		}
	}

	protected Result getData(String query) {
		Connection connection = null;
		try {
			if (!query.endsWith(";"))
				query = String.valueOf(query) + ";";
			connection = this.dataSource.getConnection();
			final Result result = new Result(connection, connection.createStatement().executeQuery(query));
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (connection != null) 
					connection.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			return null;
		}
	}

	protected PreparedStatement getPreparedStatement(final String query) throws SQLException {
		try {
			final Connection connection = this.dataSource.getConnection();
			return connection.prepareStatement(query);
		} catch (SQLException e) {
			System.out.println("Can't get datasource connection");
			this.dataSource.close();
			if (!Main.database.initializeConnection()) {
				System.exit(0);
			}
			return null;
		}
	}

	protected void close(final PreparedStatement statement) {
		if (statement == null) {
			return;
		}
		try {
			statement.clearParameters();
			statement.close();
		} catch (Exception e) {
			System.out.println("Can't close statement");
		}
	}

	protected void close(final Connection connection) {
		if (connection == null) {
			return;
		}
		try {
			connection.close();
		} catch (Exception e) {
			System.out.println("Can't close connection");
		}
	}

	protected void close(final Statement statement) {
		if (statement == null) {
			return;
		}
		try {
			statement.close();
		} catch (Exception e) {
			System.out.println("Can't close statement");
		}
	}

	protected void close(final Result result) {
		if (result != null) {
			try {
				if (result.resultSet != null) {
					result.resultSet.close();
				}
				if (result.connection != null) {
					result.connection.close();
				}
			} catch (SQLException e) {
				System.out.println("Can't close result");
			}
		}
	}

	protected class Result {
		public final Connection connection;
		public final ResultSet resultSet;

		protected Result(final Connection connection, final ResultSet resultSet) {
			this.connection = connection;
			this.resultSet = resultSet;
		}
	}
}

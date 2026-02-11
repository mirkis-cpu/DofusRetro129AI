/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  ch.qos.logback.classic.Level
 *  ch.qos.logback.classic.Logger
 *  com.zaxxer.hikari.HikariDataSource
 *  org.slf4j.LoggerFactory
 */
package org.aestia.db.statique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractDAO<T> implements DAO<T> {
	private final Connection connection;

	public AbstractDAO(Connection connection) {
		this.connection = connection;
	}

	protected void execute(String query) {
		try {
			Statement statement = connection.createStatement();
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void execute(PreparedStatement statement) {
		try {
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected ResultSet getData(String query) {
		ResultSet resultSet = null;
		try {
			if (!query.endsWith(";"))
				query = query + ";";
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	protected PreparedStatement getPreparedStatement(String query) throws SQLException {
		try {
			return connection.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected void close(PreparedStatement statement) {
		if (statement == null) {
			return;
		}
		try {
			statement.clearParameters();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void close(Statement statement) {
		if (statement == null) {
			return;
		}
		try {
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void close(ResultSet resultSet) {
		if (resultSet == null) {
			return;
		}
		try {
			resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void sendError(String error,Exception e) {
		e.printStackTrace();
	}
}

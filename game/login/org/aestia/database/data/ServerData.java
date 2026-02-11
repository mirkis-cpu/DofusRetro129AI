package org.aestia.database.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariDataSource;
import org.aestia.object.Server;
import org.aestia.database.AbstractDAO;

public class ServerData extends AbstractDAO<Server>
{
    public ServerData(final HikariDataSource dataSource) {
        super(dataSource);
    }
    
    @Override
    public Server load(final Object obj) {
        try {
            final Result result = super.getData("SELECT * FROM servers;");
            final ResultSet resultSet = result.resultSet;
            while (resultSet.next()) 
                new Server(resultSet.getInt("id"), resultSet.getString("key"), resultSet.getInt("population"), resultSet.getInt("isSubscriberServer"));
            this.close(result);
        }
        catch (SQLException e) {
            System.out.println("Can't load server");
        }
        return null;
    }
    
    @Override
    public boolean update(final Server obj) {
        return false;
    }
}

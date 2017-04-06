package org.iii.nmi.air.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBHandlerIn
{
	public Connection getConnection() throws SQLException;

	public void releaseConnection(Connection connection);

	public boolean disconnectDb();

	public boolean resetConnectionDB();
}

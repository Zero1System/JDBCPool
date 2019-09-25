package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
	List<Connection> connections = new ArrayList<Connection>();
	int size;
	
	public ConnectionPool(int size) {
		this.size = size;
		init();
	}
	
	public void init() {
		try {
			Class.forName("com.mysql.cj.Driver");
			for (int i = 0; i < size; i++) {
				Connection connection = DriverManager.getConnection("","","");
				connections.add(connection);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized Connection getConnection() {
		while(connections.isEmpty()) {
			try {
				this.wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		Connection connection = connections.remove(0);
		return connection;
	}
	
	public synchronized void returnConnection(Connection connection) {
		connections.add(connection);
		this.notifyAll();
	}
}

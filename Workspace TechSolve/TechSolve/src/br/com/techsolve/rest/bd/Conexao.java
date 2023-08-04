package br.com.techsolve.rest.bd;

import java.sql.Connection;

public class Conexao {
	
	private Connection conn;
	
	public Connection openConn() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = java.sql.DriverManager.
					getConnection("jdbc:mysql://localhost/db_techsolve?"
							+ "user=root&password=root&useTimezone=true&serverTimezone=UTC");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
	public void closeConn() {
		try {
			conn.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}

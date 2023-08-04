package br.com.techsolve.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;

import br.com.techsolve.modelo.User;
import br.com.techsolve.servlet.login.LoginUserData;

public class JDBCAuthenticationDAO {
	
	private Connection conexao;
	
	public JDBCAuthenticationDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	public boolean consultUserLogin(LoginUserData authData) {
		
		try {
			
			String command = "SELECT * FROM funcionarios WHERE nome = '"+ authData.getName() +"' "
			+"AND senha = '" + authData.getPassword() +"'";
			
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
			
		}catch(Exception e) {
			return false;
		}
		
	}
	
	public User getUserInfo(LoginUserData authData) {
		
		User userInfo = new User();
		int idPermission = -1;
		try {
			
			String command = "SELECT * FROM funcionarios WHERE nome = '"+ authData.getName() +"'";
			
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(command);

			while(rs.next()) {
				userInfo.setIdPermission(rs.getInt("idpermissao"));
				userInfo.setIdEmployee(rs.getInt("idfuncionario"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return userInfo;
	}

}

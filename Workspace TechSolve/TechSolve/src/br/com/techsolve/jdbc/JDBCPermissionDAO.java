package br.com.techsolve.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.techsolve.modelo.Permission;

public class JDBCPermissionDAO {
	
	private Connection conn;
	
	public JDBCPermissionDAO(Connection conn) {
		this.conn = conn;
	}
	
	public int register(Permission permission) {
		
		String sqlQuery = "INSERT INTO permissoes (idpermissoes, descricao) VALUES (?, ?)";
		String sqlQueryVerify = "SELECT * FROM permissoes WHERE descricao=?";
		
		PreparedStatement ps, psv;
		ResultSet result;
		
		try {
			
			psv = conn.prepareStatement(sqlQueryVerify);
			psv.setString(1, permission.getDescription());
			result = psv.executeQuery();
			
			if(result.getFetchSize()==0) {
				ps = conn.prepareStatement(sqlQuery);
				ps.setString(1, null);
				ps.setString(2, permission.getDescription());
				ps.execute();
				return 1;
			}else {
				return 3;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return 4;
		
	}
	
	public List<JsonObject> getAll() {
		
		List<JsonObject> listPermissions = new ArrayList<JsonObject>();
		
		String sqlQuery = "SELECT * FROM permissoes ORDER BY idpermissoes";
		
		PreparedStatement ps;
		ResultSet result;
		JsonObject permission = null;
		
		try {
			
			ps = conn.prepareStatement(sqlQuery);
			result = ps.executeQuery();
			
			while(result.next()) {
				
				int id = result.getInt("idpermissoes");
				String description = result.getString("descricao");
				
				permission = new JsonObject();
				permission.addProperty("id", id);
				permission.addProperty("descricao", description);
				
				listPermissions.add(permission);
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return listPermissions;
	}

	public int delete(int id) {
		
		String command = "DELETE FROM permissoes WHERE idpermissoes = ?";
		String commandVerify = "SELECT * FROM permissoes WHERE idpermissoes = ?";
		
		try {
			
			PreparedStatement p, pv;
			ResultSet result;
			
			pv = this.conn.prepareStatement(commandVerify);
			pv.setInt(1, id);
			result = pv.executeQuery();
			
			if(result.getFetchSize()==0) {
				p = conn.prepareStatement(command);
				p.setInt(1, id);
				p.execute();
				return 1;
			}else {
				return 2;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return 3;
		}
		
	}

	public int edit(Permission permission) {
		
		String command = "UPDATE permissoes "
				+ "SET descricao= ? "
				+ "WHERE idpermissoes = ?";
		String commandVerify = "SELECT * FROM permissoes WHERE descricao=?";
		PreparedStatement p, pv;
		ResultSet result;
		
		try {
			
			pv = this.conn.prepareStatement(commandVerify);
			pv.setString(1, permission.getDescription());
			result = pv.executeQuery();
			
			while(result.next()) {

				String description = result.getString("descricao");
				
				if(description.equals(permission.getDescription())){
					return 2;
				}
				
			}
			
			p = this.conn.prepareStatement(command);
			p.setString(1, permission.getDescription());
			p.setInt(2, permission.getIdPermission());
			p.execute();
			return 1;
			
		}catch(Exception e) {
			e.printStackTrace();
			return 3;
		}
		
	}

	public Permission getbyid(int id) {
		
		String command = "SELECT * FROM permissoes WHERE idpermissoes= ?";
		Permission permission = new Permission();
		
		try {
			PreparedStatement p = this.conn.prepareStatement(command);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				
				String description = rs.getString("descricao");
				int idpermission = rs.getInt("idpermissoes");
				
				permission.setDescription(description);
				permission.setIdPermission(idpermission);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return permission;
	}

}

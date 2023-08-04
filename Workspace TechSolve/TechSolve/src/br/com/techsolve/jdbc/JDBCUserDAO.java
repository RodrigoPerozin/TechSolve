package br.com.techsolve.jdbc;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.techsolve.jdbcinterface.JDBCinterfaceUser;
import br.com.techsolve.modelo.Permission;
import br.com.techsolve.modelo.User;

public class JDBCUserDAO implements JDBCinterfaceUser{
	
	private Connection conn;
	
	public JDBCUserDAO(Connection conn) {
		this.conn = conn;
	}
	
	public int register(User user) {
		
		String sqlQuery = 
				"call insertFuncionario(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		String sqlQueryVerify = "SELECT * FROM funcionarios WHERE cpf=? OR nome=?";
		
		String sqlQueryIdEmployee = "SELECT * FROM funcionarios WHERE cpf=? AND nome=?";
		
		String sqlQueryUpdatePass = "";
		
		PreparedStatement ps, psv, pspe, psup;
		ResultSet result, resultEmployee;
		
		try {
			
			psv = conn.prepareStatement(sqlQueryVerify);
			psv.setLong(1, user.getCpf());
			psv.setString(2, user.getName());
			result = psv.executeQuery();
			String todayDate = "";
			if(!result.next()) {
				ps = conn.prepareStatement(sqlQuery);
				ps.setString(1, user.getName());
				ps.setString(2, user.getDateBorn());
				
				todayDate = ""+
					(new GregorianCalendar().getTime().getYear()+1900)+
					"-"+
					(new GregorianCalendar().getTime().getMonth()+1)+
					"-"+
					new GregorianCalendar().getTime().getDate();
				
				ps.setString(3, todayDate);
				ps.setLong(4, user.getCpf());
				ps.setString(5, user.getEmail());
				ps.setLong(6, user.getPhone());
				ps.setInt(7, 1);
				
				String senhaTxt = new String(Base64.getDecoder().decode(user.getPassword()));
		    	String senMd5 = "";
		    	MessageDigest md = null;
		    	
		    	String salt = 
		    			Integer.toString(user.getPassword().length())
		    			+ senhaTxt.length() + senhaTxt.charAt(senhaTxt.length()-1) 
		    			+ user.getName().length();
		    	
		    	user.setPassword(user.getPassword()+salt);
		    	
		    	try {
		    		md = MessageDigest.getInstance("MD5");
		    	}catch(NoSuchAlgorithmException e) {
		    		e.printStackTrace();
		    	}
		    	
		    	BigInteger hash = new BigInteger(1, md.digest(user.getPassword().getBytes()));
		    	senMd5 = hash.toString(16);
				
		    	user.setPassword(senMd5);
				ps.setString(8, user.getPassword());
				ps.setInt(9, user.getIdPermission());
				ps.executeUpdate();
				
				return 1;
			}else {
				return 2;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return 3;
		
	}

	public List<JsonObject> getAll() {
		
		List<JsonObject> listUsers = new ArrayList<JsonObject>();
		
		String sqlQuery = "SELECT * FROM funcionarios";
		
		PreparedStatement ps;
		ResultSet result;
		JsonObject user = null;
		
		try {
			
			ps = conn.prepareStatement(sqlQuery);
			result = ps.executeQuery();
			
			while(result.next()) {
				
				int idemployee = result.getInt("idfuncionario");
				String name = result.getString("nome");
				String date_born = result.getString("data_nasc");
				String date_register = result.getString("data_cadastro");
				long cpf = result.getLong("cpf");
				String email = result.getString("email");
				long phone = result.getLong("telefone");
				int active = result.getInt("ativado");
				String password = result.getString("senha");
				int idpermission = result.getInt("idpermissao");
				
				user = new JsonObject();
				user.addProperty("idfuncionario", idemployee);
				user.addProperty("nome", name);
				user.addProperty("data_nasc", date_born.toString());
				user.addProperty("data_registro", date_register);
				user.addProperty("cpf", cpf);
				user.addProperty("email", email);
				user.addProperty("telefone", phone);
				user.addProperty("ativado", active);
				user.addProperty("senha", password);
				user.addProperty("idpermissao", idpermission);
				
				listUsers.add(user);
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return listUsers;
	}

	public int delete(int id) {
		
		String command = "DELETE FROM funcionarios WHERE idfuncionario = ?";
		String commandVerify = "SELECT * FROM funcionarios WHERE idfuncionario = ?";
		
		try {
			
			PreparedStatement p, pv;
			ResultSet result;
			
			pv = this.conn.prepareStatement(commandVerify);
			pv.setInt(1, id);
			result = pv.executeQuery();
			
			if(result.next()) {
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

	public int edit(User user) {
		
		String command = "UPDATE funcionarios ";
		command += "SET nome= ? ";
		command += ", email= ? ";
		command	+= ", telefone= ? ";
		command	+= ", senha= ? ";
		command	+= ", idpermissao = ? ";
		command	+= "WHERE idfuncionario = ?";
		
		String commandGetBase = "SELECT * FROM funcionarios WHERE idfuncionario=?";
		
		String commandVerify = "SELECT * FROM funcionarios WHERE (idfuncionario!=? "
				+"AND nome= ?) OR (email= ? AND idfuncionario!=?)";
		
		PreparedStatement p, pv, pb;
		ResultSet result, resultBase;
		
		try {
			
			pb = this.conn.prepareStatement(commandGetBase);
			pb.setInt(1, user.getIdEmployee());
			resultBase = pb.executeQuery();
			
			while(resultBase.next()) {
				
				pv = this.conn.prepareStatement(commandVerify);
				pv.setInt(1, user.getIdEmployee());
				pv.setString(2, user.getName());
				pv.setString(3, user.getEmail());
				pv.setInt(4, user.getIdEmployee());
				result = pv.executeQuery();
				
				while(result.next()) {
					return 2;
				}
				
				String nomeBase="", emailBase="", senhaBase="", userName="";
				int idpermissaoBase=0;
				long telefoneBase=0;
				
				if(user.getName().isBlank()) {
					userName = resultBase.getString("nome");
				}else {
					userName = user.getName();
				}
				
				if(user.getEmail().isBlank()) {
					emailBase = resultBase.getString("email");
				}
				
				if((Long.toString((long) user.getPhone()).length()==0)) {
					telefoneBase = resultBase.getLong("telefone");
				}
				
				if(user.getPassword().isBlank()) {
					senhaBase = resultBase.getString("senha");
				}
				
				if(user.getIdPermission()==0) {
					idpermissaoBase = resultBase.getInt("idpermissao");
				}
				
				p = this.conn.prepareStatement(command);
				
				if(nomeBase.isBlank()) {
					p.setString(1, user.getName());
				}else {
					p.setString(1, nomeBase);
				}
				
				if(emailBase.isBlank()) {
					p.setString(2, user.getEmail());
				}else {
					p.setString(2, emailBase);
				}
				
				if(telefoneBase==0) {
					p.setLong(3, user.getPhone());
				}else {
					p.setLong(3, telefoneBase);
				}
				
				String senhaCrip = null;
				String senMd5 = "";
				if(!senhaBase.isBlank()){
					senhaCrip = senhaBase;
				}else {
					
					senhaCrip = user.getPassword();
					
					String senhaTxt = new String(Base64.getDecoder().decode(senhaCrip));
			    	MessageDigest md = null;
			    	
			    	String salt = 
			    			Integer.toString(senhaCrip.length())
			    			+ senhaTxt.length() + senhaTxt.charAt(senhaTxt.length()-1) 
			    			+ userName.length();
			    
			    	try {
			    		md = MessageDigest.getInstance("MD5");
			    	}catch(NoSuchAlgorithmException e) {
			    		e.printStackTrace();
			    	}
			    	
			    	BigInteger hash = new BigInteger(1, md.digest((senhaCrip+salt).getBytes()));
			    	senMd5 = hash.toString(16);
			    	
				}
				
				p.setString(4, senMd5);
				
				if(idpermissaoBase==0) {
					p.setInt(5, user.getIdPermission());
				}else {
					p.setInt(5, idpermissaoBase);
				}
				
				p.setInt(6, user.getIdEmployee());
				p.execute();	
				
				return 1;
					
			}
			return 3;
		}catch(Exception e) {
			e.printStackTrace();
			return 3;
		}
		
	}

	public User getbyid(int id) {
		
		String command = "SELECT * FROM funcionarios WHERE idfuncionario= ?";
		User user = new User();
		
		try {
			PreparedStatement p = this.conn.prepareStatement(command);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				
				int idEmployee = rs.getInt("idfuncionario");
				String name = rs.getString("nome");
				String dateBorn = rs.getString("data_nasc");
				String date_register = rs.getString("data_cadastro");
				long cpf = rs.getLong("cpf");
				String email = rs.getString("email");
				long phone = rs.getLong("telefone");
				int active = rs.getInt("ativado");
				String password = rs.getString("senha");
				int idpermission = rs.getInt("idpermissao");
				
				user.setIdEmployee(idEmployee);
				user.setName(name);
				user.setDateBorn(dateBorn);
				user.setDate_register(date_register);
				user.setCpf(cpf);
				user.setEmail(email);
				user.setPhone(phone);
				user.setActive(active);
				user.setPassword(password);
				user.setIdPermission(idpermission);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}

	public List<JsonObject> getByidPermission(int idPermission) {
		
		List<JsonObject> listUsers = new ArrayList<JsonObject>();
		
		String sqlQuery = "SELECT * FROM funcionarios WHERE idpermissao=?";
		
		PreparedStatement ps;
		ResultSet result;
		JsonObject user = null;
		
		try {
			
			ps = conn.prepareStatement(sqlQuery);
			ps.setInt(1, idPermission);
			result = ps.executeQuery();
			
			while(result.next()) {
				
				int idemployee = result.getInt("idfuncionario");
				String name = result.getString("nome");
				String date_born = result.getString("data_nasc");
				String date_register = result.getString("data_cadastro");
				long cpf = result.getLong("cpf");
				String email = result.getString("email");
				long phone = result.getLong("telefone");
				int active = result.getInt("ativado");
				String password = result.getString("senha");
				int idpermission = result.getInt("idpermissao");
				
				user = new JsonObject();
				user.addProperty("idfuncionario", idemployee);
				user.addProperty("nome", name);
				user.addProperty("data_nasc", date_born.toString());
				user.addProperty("data_registro", date_register);
				user.addProperty("cpf", cpf);
				user.addProperty("email", email);
				user.addProperty("telefone", phone);
				user.addProperty("ativado", active);
				user.addProperty("senha", password);
				user.addProperty("idpermissao", idpermission);
				
				listUsers.add(user);
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return listUsers;
	}
	
}

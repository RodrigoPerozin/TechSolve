package br.com.techsolve.jdbc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.techsolve.jdbcinterface.JDBCinterfaceClient;
import br.com.techsolve.modelo.Client;

public class JDBCClientDAO implements JDBCinterfaceClient{

	private Connection conn;
	
	public JDBCClientDAO(Connection conn) {
		this.conn = conn;
	}

	public int register(Client client) {
		
		String sqlQuery = 
				"call insertCliente(?, ?, ?, ?, ?)";
		
		String sqlQueryVerify = "SELECT * FROM clientes WHERE nome=? OR cpf=?";
		
		PreparedStatement ps, psv;
		ResultSet result;
		
		try {
			
			psv = conn.prepareStatement(sqlQueryVerify);
			psv.setString(1, client.getName());
			psv.setLong(2, client.getCpf());
			result = psv.executeQuery();
			String todayDate = "";
			if(!result.next()) {
				ps = conn.prepareStatement(sqlQuery);
				ps.setString(1, client.getName());
				ps.setLong(2, client.getPhone());
				ps.setLong(3, client.getCpf());
				ps.setString(4, client.getEmail());
				
				todayDate = ""+
						(new GregorianCalendar().getTime().getYear()+1900)+
						"-"+
						(new GregorianCalendar().getTime().getMonth()+1)+
						"-"+
						new GregorianCalendar().getTime().getDate();
				
				ps.setString(5, todayDate);
				ps.execute();
				return 1; //success
			}else {
				return 2; //client already exist
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return 3; //internal error
		}
		
	}

	public List<JsonObject> getAll() {
	
		List<JsonObject> listClients = new ArrayList<JsonObject>();
		
		String sqlQuery = "SELECT * FROM clientes LIMIT 10";
		
		PreparedStatement ps;
		
		ResultSet result;
		JsonObject client = null;
		
		try {
			
			ps = conn.prepareStatement(sqlQuery);
			result = ps.executeQuery();
			
			while(result.next()) {
				
				int idclient = result.getInt("idcliente");
				String name = result.getString("nome");
				String email = result.getString("email");
				long cpf = result.getLong("cpf");
				long phone = result.getLong("telefone");
				
				client = new JsonObject();
				client.addProperty("idcliente", idclient);
				client.addProperty("nome", name);
				client.addProperty("email", email);
				client.addProperty("cpf", cpf);
				client.addProperty("telefone", phone);
				
				listClients.add(client);
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return listClients;
	}

	public int delete(int id) {
		
		String command = "DELETE FROM clientes WHERE idcliente = ?";
		String commandVerify = "SELECT * FROM clientes WHERE idcliente = ?";
		
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
	
	public Client getbyid(int id) {
		
		String command = "SELECT * FROM clientes WHERE idcliente= ?";
		Client client = new Client();
		
		try {
			PreparedStatement p = this.conn.prepareStatement(command);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				
				int idClient = rs.getInt("idcliente");
				String name = rs.getString("nome");
				String email = rs.getString("email");
				long cpf = rs.getLong("cpf");
				long phone = rs.getLong("telefone");
				
				client.setIdClient(idClient);
				client.setName(name);
				client.setEmail(email);
				client.setCpf(cpf);
				client.setPhone(phone);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return client;
	}

	
	public int edit(Client client) {
		
		String command = "UPDATE clientes ";
		command += "SET nome= ? ";
		command += ", email= ? ";
		command	+= ", telefone= ? ";
		command	+= "WHERE idcliente = ?";
		
		String commandGetBase = "SELECT * FROM clientes WHERE idcliente=?";
		
		String commandVerify = "SELECT * FROM clientes WHERE idcliente!=? "
				+"AND nome=?";
		
		PreparedStatement p, pv, pb;
		ResultSet result, resultBase;
		
		try {
			
			pb = this.conn.prepareStatement(commandGetBase);
			pb.setLong(1, client.getIdClient());
			resultBase = pb.executeQuery();
			
			while(resultBase.next()) {
				
				pv = this.conn.prepareStatement(commandVerify);
				pv.setLong(1, client.getIdClient());
				pv.setString(2, client.getName());
				result = pv.executeQuery();
				
				while(result.next()) {
					return 2;
				}
				
				String nomeBase="", emailBase="";
				long telefoneBase=0;
				
				if(client.getName().isBlank()) {
					nomeBase = resultBase.getString("nome");
				}
				
				if(client.getEmail().isBlank()) {
					emailBase = resultBase.getString("email");
				}
				
				if((Long.toString((long) client.getPhone()).length()==0)) {
					telefoneBase = resultBase.getLong("telefone");
				}
				
				p = this.conn.prepareStatement(command);
				
				if(nomeBase.isBlank()) {
					p.setString(1, client.getName());
				}else {
					p.setString(1, nomeBase);
				}
				
				if(emailBase.isBlank()) {
					p.setString(2, client.getEmail());
				}else {
					p.setString(2, emailBase);
				}
				
				if(telefoneBase==0) {
					p.setLong(3, client.getPhone());
				}else {
					p.setLong(3, telefoneBase);
				}
				
				p.setLong(4, client.getIdClient());
				
				p.execute();
				
				return 1;
					
			}
			return 3;
		}catch(Exception e) {
			e.printStackTrace();
			return 4;
		}

	}

	public List<JsonObject> filterBy(String filterTxt) {
		
		List<JsonObject> listClients = new ArrayList<JsonObject>();
		
		String sqlQuery = "SELECT * FROM clientes WHERE "
				+ "idcliente LIKE '%"+ filterTxt +"%' "
				+ "OR data_cadastro LIKE '%"+ filterTxt +"%' "
				+ "OR nome LIKE '%"+ filterTxt +"%' "
				+ "OR email LIKE '%"+ filterTxt +"%' "
				+ "OR cpf LIKE '%"+ filterTxt +"%' "
				+ "OR telefone LIKE '%"+ filterTxt +"%' "
				+ "LIMIT 10";
		
		PreparedStatement ps;
		
		ResultSet result;
		JsonObject client = null;
		
		try {
			
			ps = conn.prepareStatement(sqlQuery);
			
			result = ps.executeQuery();
			
			while(result.next()) {
				
				int idclient = result.getInt("idcliente");
				String name = result.getString("nome");
				String email = result.getString("email");
				long cpf = result.getLong("cpf");
				long phone = result.getLong("telefone");
				
				client = new JsonObject();
				client.addProperty("idcliente", idclient);
				client.addProperty("nome", name);
				client.addProperty("email", email);
				client.addProperty("cpf", cpf);
				client.addProperty("telefone", phone);
				
				listClients.add(client);
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return listClients;
	}
	
}

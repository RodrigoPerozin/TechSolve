package br.com.techsolve.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.techsolve.modelo.Client;

public interface JDBCinterfaceClient {
	
	public List<JsonObject> getAll();
	public int delete(int id);
	public int register(Client client);
	public Client getbyid(int id);
	public int edit(Client client);
	
}

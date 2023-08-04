package br.com.techsolve.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.techsolve.modelo.User;

public interface JDBCinterfaceUser {
	
	public List<JsonObject> getAll();
	public int delete(int id);
	public int register(User user);
	public User getbyid(int id);
	public int edit(User user);
	
}

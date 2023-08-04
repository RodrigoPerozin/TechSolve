package br.com.techsolve.rest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import br.com.techsolve.rest.bd.Conexao;
import br.com.techsolve.jdbc.JDBCPermissionDAO;
import br.com.techsolve.jdbc.JDBCUserDAO;
import br.com.techsolve.modelo.Permission;
import br.com.techsolve.modelo.User;

@Path("/user")
public class UserRest extends UtilRest {
	
	@POST
	@Path("/register")
	@Consumes("application/*")
	public Response register(String userParam) {
		
		String msg="";
		try {
			
			User user = new Gson().fromJson(userParam, User.class);
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCUserDAO jdbcUser = new JDBCUserDAO(conn);
			int result = jdbcUser.register(user);
			if(result==1) {
				msg = "Usuário registrado com sucesso!";
			}else if(result==2){
				msg = "Este usuário já existe!";
			}else {
				msg= "Erro inesperado!";
			}
			
			con.closeConn();
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
			
		}
		
	}
	
	@GET
	@Path("/getall")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		
		try {
			
			List<JsonObject> listUsers = new ArrayList<JsonObject>();
			
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCUserDAO jdbcUser = new JDBCUserDAO(conn);
			listUsers = jdbcUser.getAll();
			
			
			con.closeConn();
			return this.buildResponse(listUsers);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
			
		}
		
	}
	
	@GET
	@Path("/getbyidpermission")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByidPermission(@QueryParam("idPermission") int idPermission) {
		
		try {
			
			List<JsonObject> listUsers = new ArrayList<JsonObject>();
			
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCUserDAO jdbcUser = new JDBCUserDAO(conn);
			listUsers = jdbcUser.getByidPermission(idPermission);
			
			
			con.closeConn();
			return this.buildResponse(listUsers);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
			
		}
		
	}
	
	@GET
	@Path("/getbyid")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getbyid(@QueryParam("id") int id) {
		
		try {
			
			User user;
			
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCUserDAO jdbcUser = new JDBCUserDAO(conn);
			user = jdbcUser.getbyid(id);
			
			
			con.closeConn();
			return this.buildResponse(user);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
			
		}
		
	}
	
	@PUT
	@Path("/edit")
	@Consumes("application/*")
	public Response alter(String userJSON) {
		
		try {
			
			User user = new Gson().fromJson(userJSON, User.class);
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCUserDAO jdbcUser = new JDBCUserDAO(conn);
			
			int response = jdbcUser.edit(user);
			
			String msg = "";
			if(response==1) {
				msg="Informações do usuário alteradas com sucesso!";
			}else if(response==2){
				msg="Esse nome já pertence a um usuário!";
			}else {
				msg="Houve algum erro inesperado!";
			}
			
			con.closeConn();
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@DELETE
	@Path("/delete/{idfuncionario}")
	@Consumes("application/*")
	public Response excluir(@PathParam("idfuncionario") int idfuncionario) {
		
		try {
			
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCUserDAO jdbcUser = new JDBCUserDAO(conn);
			
			int response = jdbcUser.delete(idfuncionario);
			
			String msg = "";
			if(response==1) {
				msg="Usuário excluído com sucesso!";
			}else if(response==2){
				msg="Erro ao excluír usuário! O usuário já foi excluido!";
				return this.buildErrorResponse(msg);
			}else {
				msg="Ops! Houve algum erro inesperado!";
			}
			
			con.closeConn();
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}

}


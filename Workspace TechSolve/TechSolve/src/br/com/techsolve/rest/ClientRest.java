package br.com.techsolve.rest;

import javax.ws.rs.Path;
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

import br.com.techsolve.jdbc.JDBCClientDAO;
import br.com.techsolve.jdbc.JDBCUserDAO;
import br.com.techsolve.modelo.Client;
import br.com.techsolve.modelo.User;
import br.com.techsolve.rest.bd.Conexao;

@Path("/client")
public class ClientRest extends UtilRest{

	@POST
	@Path("/register")
	@Consumes("application/*")
	public Response register(String clientParam) {
		
		String msg="";
		try {
			
			Client client = new Gson().fromJson(clientParam, Client.class);
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCClientDAO jdbcUser = new JDBCClientDAO(conn);
			int result = jdbcUser.register(client);
			if(result==1) {
				msg = "Cliente registrado com sucesso!";
			}else if(result==2){
				msg = "Este cliente já existe!";
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
	public Response getall() {
		
		try {
			
			List<JsonObject> listClients = new ArrayList<JsonObject>();
			
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCClientDAO jdbcClient = new JDBCClientDAO(conn);
			listClients = jdbcClient.getAll();
			
			
			con.closeConn();
			return this.buildResponse(listClients);
			
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
			
			Client client;
			
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCClientDAO jdbcClient = new JDBCClientDAO(conn);
			client = jdbcClient.getbyid(id);
			
			
			con.closeConn();
			return this.buildResponse(client);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
			
		}
		
	}
	
	@GET
	@Path("/filterby")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response filterby(@QueryParam("filterTxt") String filterTxt) {
		
		try {
			
			List<JsonObject> listClients = new ArrayList<JsonObject>();
			
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCClientDAO jdbcClient = new JDBCClientDAO(conn);
			listClients = jdbcClient.filterBy(filterTxt);
			
			
			con.closeConn();
			return this.buildResponse(listClients);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
			
		}
		
	}
	
	@DELETE
	@Path("/delete/{idcliente}")
	@Consumes("application/*")
	public Response delete(@PathParam("idcliente") int idcliente) {
		
		try {
			
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCClientDAO jdbcClient = new JDBCClientDAO(conn);
			
			int response = jdbcClient.delete(idcliente);
			
			String msg = "";
			if(response==1) {
				msg="Cliente excluído com sucesso!";
			}else if(response==2){
				msg="Erro ao excluír cliente! O cliente já foi excluido!";
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
	
	@PUT
	@Path("/edit")
	@Consumes("application/*")
	public Response alter(String clientJSON) {
		
		try {
			
			Client client = new Gson().fromJson(clientJSON, Client.class);
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCClientDAO jdbcClient = new JDBCClientDAO(conn);
			
			int response = jdbcClient.edit(client);
			
			String msg = "";
			if(response==1) {
				msg="Informações do usuário alteradas com sucesso!";
			}else if(response==2){
				msg="Esse nome já pertence a um cliente!";
			}else if(response==3){
				msg="O usuário editado não existe mais!";
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
	
}

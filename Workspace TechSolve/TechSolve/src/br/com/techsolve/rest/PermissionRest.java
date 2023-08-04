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

@Path("/permission")
public class PermissionRest extends UtilRest {
	
	@GET
	@Path("/getall")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		
		try {
			
			List<JsonObject> listPermissions = new ArrayList<JsonObject>();
			
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCPermissionDAO jdbcPermission = new JDBCPermissionDAO(conn);
			listPermissions = jdbcPermission.getAll();
			
			
			con.closeConn();
			return this.buildResponse(listPermissions);
			
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
			
			Permission permission;
			
			Conexao con = new Conexao();
			Connection conn = con.openConn();
			JDBCPermissionDAO jdbcPermission = new JDBCPermissionDAO(conn);
			permission = jdbcPermission.getbyid(id);
			
			
			con.closeConn();
			return this.buildResponse(permission);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
			
		}
		
	}
	
//	@POST
//	@Path("/register")
//	@Consumes("application/*")
//	public Response register(String permissionParam) {
//		
//		String msg="";
//		try {
//			
//			Permission permission = new Gson().fromJson(permissionParam, Permission.class);
//			Conexao con = new Conexao();
//			Connection conn = con.openConn();
//			JDBCPermissionDAO jdbcPermission = new JDBCPermissionDAO(conn);
//			int result = jdbcPermission.register(permission);
//			if(result==1) {
//				msg = "Nível de permissão registrado com sucesso!";
//			}else if(result==3){
//				msg = "Já existe um nível de permissão com esse nome!";
//			}else {
//				msg= "Erro inesperado!";
//			}
//			
//			con.closeConn();
//			return this.buildResponse(msg);
//			
//		}catch(Exception e) {
//			
//			e.printStackTrace();
//			return this.buildErrorResponse(e.getMessage());
//			
//		}
//		
//	}
	
//	@DELETE
//	@Path("/delete/{id}")
//	@Consumes("application/*")
//	public Response delete(@PathParam("id") int id) {
//		
//		try {
//			
//			Conexao con = new Conexao();
//			Connection conn = con.openConn();
//			JDBCPermissionDAO jdbcPermission = new JDBCPermissionDAO(conn);
//			
//			int response = jdbcPermission.delete(id);
//			
//			String msg = "";
//			if(response==1) {
//				msg="Permissão excluído com sucesso!";
//			}else if(response==2){
//				msg="Erro ao excluír permissão! O usuário já foi excluido!";
//				return this.buildErrorResponse(msg);
//			}else {
//				msg="Ops! Houve algum erro inesperado!";
//			}
//			
//			con.closeConn();
//			return this.buildResponse(msg);
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//			return this.buildErrorResponse(e.getMessage());
//		}
//		
//	}
	
//	@PUT
//	@Path("/edit")
//	@Consumes("application/*")
//	public Response alter(String permissionJSON) {
//		
//		try {
//			
//			Permission permission = new Gson().fromJson(permissionJSON, Permission.class);
//			Conexao con = new Conexao();
//			Connection conn = con.openConn();
//			JDBCPermissionDAO jdbcPermission = new JDBCPermissionDAO(conn);
//			
//			int response = jdbcPermission.edit(permission);
//			
//			String msg = "";
//			if(response==1) {
//				msg="Permissão alterada com sucesso!";
//			}else if(response==2){
//				msg="A permissão já existe!";
//				return this.buildErrorResponse(msg);
//			}else {
//				msg="Houve algum erro inesperado!";
//			}
//			
//			con.closeConn();
//			return this.buildResponse(msg);
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//			return this.buildErrorResponse(e.getMessage());
//		}
//		
//	}

}

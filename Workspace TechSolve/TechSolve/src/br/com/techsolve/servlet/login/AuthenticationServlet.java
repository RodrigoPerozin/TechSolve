package br.com.techsolve.servlet.login;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.Session;

import br.com.techsolve.jdbc.JDBCAuthenticationDAO;
import br.com.techsolve.rest.bd.Conexao;


@WebServlet("/AuthenticationServlet")
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AuthenticationServlet() {
        super();
    }
    
    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    	LoginUserData authData = new LoginUserData();
    	authData.setName((request.getParameter("usuario"))==null ? "" : request.getParameter("usuario"));
    	
    	String senhaTxt = (new String(Base64.getDecoder().decode(request.getParameter("senha")))) == null ? "" : 
    		(new String(Base64.getDecoder().decode(request.getParameter("senha"))));
    	
    	String senMd5 = "";
    	MessageDigest md = null;
    	String salt;
    	
    	try {
    		salt = 
    			Integer.toString(request.getParameter("senha").length())
    			+ senhaTxt.length() + senhaTxt.charAt(senhaTxt.length()-1) 
    			+ authData.getName().length();
    	}catch(Exception e) {
    		salt = "";
    	}
    	
    	try {
    		md = MessageDigest.getInstance("MD5");
    	}catch(NoSuchAlgorithmException e) {
    		e.printStackTrace();
    	}
    	
    	BigInteger hash = new BigInteger(1, md.digest((request.getParameter("senha")+salt).getBytes()));
    	senMd5 = hash.toString(16);
    	
    	authData.setPassword(senMd5);
    	
    	Conexao conec = new Conexao();
    	Connection conexao = (Connection)conec.openConn();
    	
    	JDBCAuthenticationDAO jdbcAuth = new JDBCAuthenticationDAO(conexao);
    	
    	boolean returnAuth = jdbcAuth.consultUserLogin(authData);
    	
    	if(returnAuth) {
    		
    		HttpSession session = request.getSession();
    		session.setAttribute("login", request.getParameter("usuario"));
    		session.setAttribute("idPermissao", jdbcAuth.getUserInfo(authData).getIdPermission());
    		session.setAttribute("userId", jdbcAuth.getUserInfo(authData).getIdEmployee());
    		int idPermissaoSession = Integer.parseInt(session.getAttribute("idPermissao").toString());
    		if(idPermissaoSession==1) {;
    			response.sendRedirect("acess/pages/attendant");
    		}else if(idPermissaoSession==2) {
    			response.sendRedirect("acess/pages/manager");
    		}else {
    			response.sendRedirect("acess/pages/technician");
    		}
    		
    	}else {
    		
    		response.sendRedirect("error.html");
    		
    	}
    	
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}

package br.com.techsolve.filter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ServletFilter implements Filter{

	public void destroy() {
		
	}

	//Filter every /acess/* url requested.
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		String context = request.getServletContext().getContextPath();
		String contextURI = ((HttpServletRequest)request).getRequestURI();
		
		try {
			HttpSession session = ((HttpServletRequest)request).getSession();
			String user = null;
			int idpermission = -1;
			
			try {
				idpermission = Integer.parseInt(session.getAttribute("idPermissao").toString());
			}catch(Exception e) {
				//JUST CONTINUE
			}
			
			boolean valid = false;
			
			if(session!=null && idpermission!=-1) {
				user = (String)session.getAttribute("login");
			}
			
			if((user==null && idpermission==-1) || (user==null && !contextURI.contains("/login"))) {
				((HttpServletResponse)response).sendRedirect("/TechSolve/error.html");
				valid = false;
			}
			
			if(user!=null && idpermission==1 && contextURI.contains("/attendant")){
				valid = true;
			}
			
			if(user!=null && idpermission==2 && contextURI.contains("/manager")){
				valid = true;
			}
			
			if(user!=null && idpermission==3 && contextURI.contains("/technician")){
				valid = true;
			}
			
			if(user!=null && contextURI.contains("/rest/")) {
				valid = true;
			}
			
			if(user!=null && contextURI.contains("/TechSolve/acess/logout")) {
				valid = true;
			}
			
			if(valid) {
				filterChain.doFilter(request, response);
			}else if(user!=null && !valid){
				((HttpServletResponse)response).sendRedirect("/TechSolve/acessError.html");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

	
}

package br.com.techsolve.modelo;

import java.io.Serializable;

public class Permission implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int idpermission;
	private String description;
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}

	public int getIdPermission() {
		return idpermission;
	}

	public void setIdPermission(int idpermission) {
		this.idpermission = idpermission;
	}
	
}

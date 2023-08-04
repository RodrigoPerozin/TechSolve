package br.com.techsolve.modelo;

import java.io.Serializable;
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int idemployee;
	private String name;
	private String date_born;
	private String date_register;
	private long cpf;
	private String email;
	private long phone;
	private int active;
	private String password;
	private int idpermission;
	
	public int getIdEmployee() {
		return idemployee;
	}
	public void setIdEmployee(int idemployee) {
		this.idemployee = idemployee;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDateBorn() {
		return date_born;
	}
	public void setDateBorn(String date_born) {
		this.date_born = date_born;
	}
	public long getCpf() {
		return cpf;
	}
	public void setCpf(long cpf) {
		this.cpf = cpf;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getIdPermission() {
		return idpermission;
	}
	public void setIdPermission(int idpermission) {
		this.idpermission = idpermission;
	}
	public String getDate_register() {
		return date_register;
	}
	public void setDate_register(String date_register) {
		this.date_register = date_register;
	}
	
}

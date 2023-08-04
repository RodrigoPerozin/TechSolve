package br.com.techsolve.modelo;

public class Order {

	private int orderId;
	private int statusOS;
	private String creationDate;
	private String finishDate;
	private int clientId;
	private int attendantRespId;
	private int technicianRespId;
	private String budget;
	private double totalPrice;
	private String problemDesc;
	private String imei;
	private String model;
	private String color;
	private short newProblem;
	private String cancelReason;
	private String justTransResp;
	private String infoNewProblem;
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getStatusOS() {
		return statusOS;
	}
	public void setStatusOS(int statusOS) {
		this.statusOS = statusOS;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getAttendantRespId() {
		return attendantRespId;
	}
	public void setAttendantRespId(int attendantRespId) {
		this.attendantRespId = attendantRespId;
	}
	public int getTechnicianRespId() {
		return technicianRespId;
	}
	public void setTechnicianRespId(int technicianRespId) {
		this.technicianRespId = technicianRespId;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getProblemDesc() {
		return problemDesc;
	}
	public void setProblemDesc(String problemDesc) {
		this.problemDesc = problemDesc;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public short getNewProblem() {
		return newProblem;
	}
	public void setNewProblem(short newProblem) {
		this.newProblem = newProblem;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getJustTransResp() {
		return justTransResp;
	}
	public void setJustTransResp(String justTransResp) {
		this.justTransResp = justTransResp;
	}
	public String getInfoNewProblem() {
		return infoNewProblem;
	}
	public void setInfoNewProblem(String infoNewProblem) {
		this.infoNewProblem = infoNewProblem;
	}
	
	
}

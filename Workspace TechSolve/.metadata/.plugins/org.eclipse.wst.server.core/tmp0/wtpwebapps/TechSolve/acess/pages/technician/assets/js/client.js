TECHSOLVE.client.getall = function(){

	$.ajax({
		type: "GET",
		url: TECHSOLVE.RESTPATH + "client/getall",
		success: function(data){
			var dataObj = JSON.parse(data);
			var tableHTML="";
			tableHTML = 
			"<table class='table table-bordered border-secondary' name='clientTable' id='clientTable'>"+
				"<thead>"+
					"<tr>"+
						"<th scope='col'><b>ID</b></th>"+
						"<th scope='col'>Nome</th>"+
						"<th scope='col'>Telefone</th>"+
						"<th scope='col'>CPF</th>"+
						"<th scope='col'>E-mail</th>"+
						"<th scope='col'>Ações</th>"+
					"</tr>"+
				"</thead>"+
				"<tbody>";
					
					if(dataObj.length==0){
						tableHTML +=
						"</tbody>"+
						"<tfoot>"+
						"<tr>"+
						"<td colspan='6' style='text-align: center;'>Não há clientes registrados</td>"+
						"</tr>"+
						"</tfoot>"+
						"</table>";
					}else{
						var cpf_formated;
						for(var i=0;i<dataObj.length;i++){	
							cpf_formated = dataObj[i].cpf.toString().slice(0, 3)+"."+dataObj[i].cpf.toString().slice(3, 6)+"."+dataObj[i].cpf.toString().slice(6, 9)+"-"+dataObj[i].cpf.toString().slice(9, 11);
							telefone_formated = "("+dataObj[i].telefone.toString().slice(0, 2)+")"+" "+dataObj[i].telefone.toString().slice(2, 7)+"-"+dataObj[i].telefone.toString().slice(7, 11);
							tableHTML +=
							"<tr>"+
								"<th scope='row'><b>"+dataObj[i].idcliente +"</b></th>"+
								"<td>"+dataObj[i].nome +"</td>"+
								"<td>"+telefone_formated +"</td>"+
								"<td>"+cpf_formated +"</td>"+
								"<td>"+dataObj[i].email +"</td>"+
								"<td>"+
									"<a href='javascript:TECHSOLVE.client.showedit("+ dataObj[i].idcliente +")'><img class='iconaction' src='" + TECHSOLVE.IMAGESPATH + "edit.png" + "' alt='Botão editar'></a>"+
									"<a href='javascript:TECHSOLVE.client.delete("+ dataObj[i].idcliente +")'><img class='iconaction' src='" + TECHSOLVE.IMAGESPATH + "delete.png" + "' alt='Botão excluir'></a>"+
								"</td>"+
							"</tr>";
						
						}
						
							tableHTML +=	
							"</tbody>"+
						"</table>";
					}
		
			
			if(document.getElementById("tableClientsdiv")==undefined){
				console.log(data)
			}else{
			document.getElementById("tableClientsdiv").innerHTML = tableHTML;
			}
			
		},
		error: function(info){
			TECHSOLVE.showPopUp("Mensagem", "Erro ao buscar os clientes: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
		}
	});
	
}

TECHSOLVE.client.register = function(){
	
	var name = document.frmRegister.name.value;
	var expRegName = new RegExp("^[A-zÀ-ü]{3,}([ ]{1}[A-zÀ-ü]{2,})+$");
	
	var phone = document.frmRegister.phone.value;
	var expRegPhone = new RegExp("^[(]{1}[0 -9]{2}[)]{1}[0-9]{4,5}[-]{1}[0-9]{4}$");
	
	var email = document.frmRegister.email.value;
	var expRegEmail = new RegExp("^[A-zÀ-ü0-9.-]{2,}[@]{1}[a-z0-9]{2,}([.]{1}[a-z]{2,}){1,}$");
	
	var cpf = document.frmRegister.cpf.value;
	var expRegCpf = new RegExp("^[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}$");
	
	if(!expRegName.test(name)){
		
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'Nome' corretamente!", document.frmRegister.name.id);
		
		
	}else if(!expRegCpf.test(cpf)){
	
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'CPF' corretamente!", document.frmRegister.cpf.id);
		
	}else if(!expRegEmail.test(email)){
	
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'E-mail' corretamente!", document.frmRegister.email.id);
		
	}else if(!expRegPhone.test(phone)){
	
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'Telefone' corretamente!", document.frmRegister.phone.id);
		
	}else{
		
		var client = new Object();
		client.name = name;
		
		phone = phone.replace("(", "");
		phone = phone.replace(")", "");
		phone = phone.replace("-", "");
		
		client.phone = phone;
		client.email = email;
		
		cpf = cpf.replace(".", "");
		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");
		
		client.cpf = cpf;
		
		$.ajax({
			type: "POST",
			url: TECHSOLVE.RESTPATH + "client/register",
			data: JSON.stringify(client),
			success: function(msg){
				TECHSOLVE.client.getall();
				TECHSOLVE.showPopUp("Mensagem", msg, null);
			},
			error: function(info){
				TECHSOLVE.showPopUp("Mensagem", "Erro ao cadastrar cliente: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
			}
		});
		
	}
}

TECHSOLVE.client.delete = function(idcliente){
	$.ajax({
			type: "DELETE",
			url: TECHSOLVE.RESTPATH + "client/delete/"+idcliente,
			success: function(msg){
				TECHSOLVE.client.getall();
				TECHSOLVE.showPopUp("Mensagem", msg, null);
			},
			error: function(info){
				TECHSOLVE.showPopUp("Mensagem", "Erro ao deletar cliente: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
			}
		});
}

TECHSOLVE.client.showedit = function(idcliente){
	
	$.ajax({
		type: "GET",
		url: TECHSOLVE.RESTPATH + "client/getbyid",
		data: "id="+idcliente,
		success: function(client){
			$("#modalWarning").html(
				"<input type='text' id='idClientToChange' name='idClientToChange' value='"+client.idClient+"' hidden>"
				+"<label for='name'><p>Nome Completo:</p></label>"
				+"<input oninput='verifyFormOk()' class='form-control' type='text' name='nameClientToChange' id='nameClientToChange' placeholder='Nome Cliente' value='"+ client.name +"' required><br>"
				+"<label for='email'><p>E-mail:</p></label>"
				+"<input oninput='verifyFormOk()' class='form-control' type='text' name='emailClientToChange' id='emailClientToChange' placeholder='usuariotal@techsolve.com.br' value='"+ client.email +"' required><br>"
				+"<label for='phone'><p>Telefone:</p></label>"
				+"<input oninput='verifyFormOk()' class='form-control' type='text' name='phoneClientToChange' id='phoneClientToChange' placeholder='(47)90000-0000' value='"+ client.phone +"' required>"
				+"<script>$('#phoneClientToChange').mask('(99)99999-9999');</script><br>");
			
			var modalAlter = {

				title: "Alterar cliente",
				height: 400,
				width: 400,
				modal: true,
				buttons: {
					"Salvar": function(){
						TECHSOLVE.client.edit();
					},
					"Cancelar": function(){
						$(this).dialog("close");
					}
				},
				close: function(){
					
				}
			}	

			$("#modalWarning").dialog(modalAlter);

		},
		error: function(info){
			TECHSOLVE.showPopUp("Mensagem", "Erro ao editar cliente: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
		}
	});
	
}

TECHSOLVE.client.edit = function(){

	var name = $("#nameClientToChange").val();
	var expRegName = new RegExp("^[A-zÀ-ü]{3,}([ ]{1}[A-zÀ-ü]{2,})+$");
	
	var email = $("#emailClientToChange").val();
	var expRegEmail = new RegExp("^[A-zÀ-ü0-9.-]{2,}[@]{1}[a-z0-9]{2,}([.]{1}[a-z]{2,}){1,}$");
	
	var phone = $("#phoneClientToChange").val();
	var expRegPhone = new RegExp("^[(]{1}[0 -9]{2}[)]{1}[0-9]{4,5}[-]{1}[0-9]{4}$");
	
	if(!expRegName.test(name)){
		
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'Nome' corretamente!", document.frmRegister.name.id);
		
		
	}else if(!expRegEmail.test(email)){
	
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'E-mail' corretamente!", document.frmRegister.email.id);
		
	}else if(!expRegPhone.test(phone)){
	
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'Telefone' corretamente!", document.frmRegister.phone.id);
		
	}
	
	var clientNew = new Object();
	clientNew.idClient = parseInt($("#idClientToChange").val());
	clientNew.name = $("#nameClientToChange").val();
	clientNew.email = $("#emailClientToChange").val();
	
	var phoneOk = $("#phoneClientToChange").val();
	
		phoneOk = phoneOk.replace("(", "");
		phoneOk = phoneOk.replace(")", "");
		phoneOk = phoneOk.replace("-", "");
	
	clientNew.phone = parseInt(phoneOk);
	
	$.ajax({
			type: "PUT",
			url: TECHSOLVE.RESTPATH + "client/edit",
			data: JSON.stringify(clientNew),
			success: function(msg){
				TECHSOLVE.client.getall();
				TECHSOLVE.showPopUp("Mensagem", msg, null);
			},
			error: function(info){
				TECHSOLVE.showPopUp("Mensagem", "Erro ao alterar cliente: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
			}
		});

}
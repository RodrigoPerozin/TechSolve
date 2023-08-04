TECHSOLVE.user.register = function(){
	
	var name = document.frmRegister.name.value;
	var expRegName = new RegExp("^[A-zÀ-ü]{3,}([ ]{1}[A-zÀ-ü]{2,})+$");
	
	var phone = document.frmRegister.phone.value;
	var expRegPhone = new RegExp("^[(]{1}[0 -9]{2}[)]{1}[0-9]{4,5}[-]{1}[0-9]{4}$");
	
	var email = document.frmRegister.email.value;
	var expRegEmail = new RegExp("^[A-zÀ-ü0-9.-]{2,}[@]{1}[a-z0-9]{2,}([.]{1}[a-z]{2,}){1,}$");
	
	var cpf = document.frmRegister.cpf.value;
	var expRegCpf = new RegExp("^[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}$");
	
	var dateborn = document.frmRegister.dateborn.value;
	var expRegDateborn = new RegExp("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
	
	var password = document.frmRegister.password.value;
	var permission = document.frmRegister.permission.value;
	
	if(!expRegName.test(name)){
		
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'Nome' corretamente!", document.frmRegister.name.id);
		
		
	}else if(!expRegDateborn.test(dateborn)){
		
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'Data de Nascimento' corretamente!", document.frmRegister.dateborn.id);
		
	}else if(!expRegCpf.test(cpf)){
	
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'CPF' corretamente!", document.frmRegister.cpf.id);
		
	}else if(!expRegEmail.test(email)){
	
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'E-mail' corretamente!", document.frmRegister.email.id);
		
	}else if(!expRegPhone.test(phone)){
	
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'Telefone' corretamente!", document.frmRegister.phone.id);
		
	}else if(password==""){
	
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'Senha' corretamente!", document.frmRegister.password.id);
		
	}else if(permission==""){
		
		TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'Nível de permissão' corretamente!", document.frmRegister.permission.id);
		
	}else{
		
		var user = new Object();
		user.name = name;
		
		phone = phone.replace("(", "");
		phone = phone.replace(")", "");
		phone = phone.replace("-", "");
		
		user.phone = phone;
		user.email = email;
		user.password = convertPassword64(password, "password");
		
		cpf = cpf.replace(".", "");
		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");
		
		user.cpf = cpf;
		
		user.date_born = dateborn;
		user.idpermission = permission;
		
		$.ajax({
			type: "POST",
			url: TECHSOLVE.RESTPATH + "user/register",
			data: JSON.stringify(user),
			success: function(msg){
				TECHSOLVE.user.getall();
				TECHSOLVE.showPopUp("Mensagem", msg, null);
			},
			error: function(info){
				TECHSOLVE.showPopUp("Mensagem", "Erro ao cadastrar usuário: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
			}
		});
		
	}
}

TECHSOLVE.user.getall = function(){
	$.ajax({
		type: "GET",
		url: TECHSOLVE.RESTPATH + "user/getall",
		success: function(data){
			var tableHTML="";
		tableHTML = 
		"<table class='table table-bordered border-secondary' name='userTable' id='userTable'>"+
			"<thead>"+
				"<tr>"+
					"<th scope='col'><b>ID</b></th>"+
					"<th scope='col'>Nome</th>"+
					"<th scope='col'>Data Nascimento</th>"+
					"<th scope='col'>CPF</th>"+
					"<th scope='col'>E-mail</th>"+
					"<th scope='col'>Telefone</th>"+
					"<th scope='col'>Permissão</th>"+
					"<th scope='col'>Ações</th>"+
				"</tr>"+
			"</thead>"+
			"<tbody>";
			
				if(data.length==0){
					tableHTML +=
						"</tbody>"+
						"<tfoot>"+
						"<tr>"+
						"<td colspan='8' style='text-align: center;'>Não há usuários registrados</td>"+
						"</tr>"+
						"</tfoot>"+
						"</table>";
				}else{
					for(var i=0;i<data.length;i++){
						
						var data_nsc_year = data[i].data_nasc.slice(0, 4);
						var data_nsc_month = data[i].data_nasc.slice(5, 7);
						var data_nsc_day = data[i].data_nasc.slice(8, 10);
						var data_nsc_formated = data_nsc_day + "/" + data_nsc_month + "/" + data_nsc_year;
						var cpf_formated = data[i].cpf.toString().slice(0, 3)+"."+data[i].cpf.toString().slice(3, 6)+"."+data[i].cpf.toString().slice(6, 9)+"-"+data[i].cpf.toString().slice(9, 11);
						var idPermissao = parseInt(data[i].idpermissao);
						var nivelPermissao;
						var telefone_formated = "("+data[i].telefone.toString().slice(0, 2)+")"+" "+data[i].telefone.toString().slice(2, 7)+"-"+data[i].telefone.toString().slice(7, 11);
						
						if(idPermissao==1){
							nivelPermissao="Atendente";
						}else if(idPermissao==2){
							nivelPermissao="Gerente";
						}else if(idPermissao==3){
							nivelPermissao="Técnico em Manutenção";
						}
						
						tableHTML +=
						"<tr>"+
							"<th scope='row'><b>"+data[i].idfuncionario +"</b></th>"+
							"<td>"+data[i].nome +"</td>"+
							"<td>"+data_nsc_formated +"</td>"+
							"<td>"+cpf_formated +"</td>"+
							"<td>"+data[i].email +"</td>"+
							"<td>"+telefone_formated +"</td>"+
							"<td>"+nivelPermissao +"</td>"+
							"<td>"+
								"<a href='javascript:TECHSOLVE.user.showedit("+ data[i].idfuncionario +");'><img class='iconaction' src='" + TECHSOLVE.IMAGESPATH + "edit.png" + "' alt='Botão editar'></a>"+
								"<a href='javascript:TECHSOLVE.user.delete("+ data[i].idfuncionario +")'><img class='iconaction' src='" + TECHSOLVE.IMAGESPATH + "delete.png" + "' alt='Botão excluir'></a>"+
							"</td>"+
						"</tr>";
					
					}
							tableHTML +=	
						"</tbody>"+
					"</table>";
				}
				
		if(document.getElementById("tableUsersdiv")!=undefined){
			document.getElementById("tableUsersdiv").innerHTML = tableHTML;
		}
		
		},
		error: function(info){
			TECHSOLVE.showPopUp("Mensagem", "Erro ao buscar usuários: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
		}
	});
	
}

TECHSOLVE.user.showedit = function(id){

	$.ajax({
		type: "GET",
		url: TECHSOLVE.RESTPATH + "user/getbyid",
		data: "id="+id,
		success: function(user){
			$("#modalWarning").html(
				"<input type='text' id='idUserToChange' name='idUserToChange' value='"+user.idemployee+"' hidden>"
				+"<label for='name'><p>Nome Completo:</p></label>"
				+"<input oninput='verifyFormOk()' class='form-control' type='text' name='nameUserToChange' id='nameUserToChange' placeholder='Nome Usuário' value='"+ user.name +"' required><br>"
				+"<label for='email'><p>E-mail:</p></label>"
				+"<input oninput='verifyFormOk()' class='form-control' type='text' name='emailUserToChange' id='emailUserToChange' placeholder='usuariotal@techsolve.com.br' value='"+ user.email +"' required><br>"
				+"<label for='phone'><p>Telefone:</p></label>"
				+"<input oninput='verifyFormOk()' class='form-control' type='text' name='phoneUserToChange' id='phoneUserToChange' placeholder='(47)90000-0000' value='"+ user.phone +"' required><br>"
				+"<label for='password'><p>Senha:</p></label>"
				+"<input oninput='verifyFormOk()' class='form-control' type='password' name='passwordUserToChange' id='passwordUserToChange' placeholder='*******' required><br>"
				+"<label for='permission'><p>Nível de permissão:</p></label>"
				+"<select oninput='verifyFormOk()' class='form-control' type='select' name='idpermissionUserToChange' id='idpermissionUserToChange' required><option value=''>Selecione uma opção</option>"
				+"<option value='1'>Atendente</option>"
				+"<option value='2'>Gerente</option>"
				+"<option value='3'>Técnico em Manutenção</option></select>"
				+"<script>"
					+"$('#phoneUserToChange').mask('(99)99999-9999');"
					+"$('#idpermissionUserToChange').val('"+ user.idpermission +"')"
				+"</script>");
			
			var modalAlter = {

				title: "Alterar usuário",
				height: 600,
				width: 400,
				modal: true,
				buttons: {
					"Salvar": function(){
						TECHSOLVE.user.edit();
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
			TECHSOLVE.showPopUp("Mensagem", "Erro ao editar usuário: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
		}
	});
	
}

TECHSOLVE.user.edit = function(){

	var userNew = new Object();
	userNew.idemployee = parseInt($("#idUserToChange").val());
	userNew.name = $("#nameUserToChange").val();
	userNew.email = $("#emailUserToChange").val();
	var phoneOk = $("#phoneUserToChange").val();
	
		phoneOk = phoneOk.replace("(", "");
		phoneOk = phoneOk.replace(")", "");
		phoneOk = phoneOk.replace("-", "");
	
	userNew.phone = parseInt(phoneOk);
	userNew.password = convertPassword64($("#passwordUserToChange").val(), "passwordUserToChange");
	userNew.idpermission = parseInt($("#idpermissionUserToChange").val());
	
	$.ajax({
			type: "PUT",
			url: TECHSOLVE.RESTPATH + "user/edit",
			data: JSON.stringify(userNew),
			success: function(msg){
				TECHSOLVE.user.getall();
				TECHSOLVE.showPopUp("Mensagem", msg, null);
			},
			error: function(info){
				TECHSOLVE.showPopUp("Mensagem", "Erro ao alterar usuário: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
			}
		});

}

TECHSOLVE.user.delete = function(idfuncionario){

	$.ajax({
		type: "DELETE",
		url: TECHSOLVE.RESTPATH + "user/delete/"+idfuncionario,
		success: function(msg){
			TECHSOLVE.user.getall();
			TECHSOLVE.showPopUp("Mensagem", msg, null);
		},
		error: function(info){
			TECHSOLVE.showPopUp("Mensagem", "Erro ao excluir usuário: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
		}
	});

}
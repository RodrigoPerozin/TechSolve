TECHSOLVE.order.getStatusAsText = (statusID) => {
	if(statusID==0){ //criada
		return "CRIADA";
	}else if(statusID==1){ //assumida
		return "ASSUMIDA";
	}else if(statusID==2){ //em avaliação
		return "EM AVALIAÇÃO"; 
	}else if(statusID==3){ //em aprovação
		return "ORÇAMENTO EM APROVAÇÃO";
	}else if(statusID==5){ //cancelada	
		return "CANCELADA";
	}else if(statusID==6){ //orçamento aprovado
		return "ORÇAMENTO APROVADO";
	}else if(statusID==4){ //orçamento reprovado
		return "ORÇAMENTO REPROVADO";
	}else if(statusID==7){ //em manutenção
		return "EM MANUTENÇÃO";
	}else if(statusID==8){ //em novo problema
		return "NOVO PROBLEMA";
	}else if(statusID==9){ //novo orçamento/problema aprovado
		return "ORÇAMENTO APROVADO";
	}else if(statusID==10){ //novo orçamento/problema reprovado
		return "ORÇAMENTO REPROVADO";
	}else if(statusID==11){ //aguardando resposta cliente
		return "AGUARDANDO CLIENTE";
	}else if(statusID==12){ //pagamento aprovado
		return "FINALIZADA";
	}else if(statusID==13){ //pagamento reprovado
		return "PAGAMENTO REPROVADO";
	}
	
}

TECHSOLVE.order.getStatusAsColor = (statusID) => {
	
	if(statusID==0){ //criada
		return "yellow";
	}else if(statusID==1){ //assumida
		return "blue";
	}else if(statusID==2){ //em avaliação
		return "orange";
	}else if(statusID==3){ //em aprovação
		return "purple";
	}else if(statusID==4){ //orçamento reprovado
		return "red";
	}else if(statusID==5){ //cancelada
		return "red";
	}else if(statusID==6){ //orçamento aprovado
		return "green";
	}else if(statusID==7){//em manutenção
		return "green";
	}else if(statusID==8){ //em novo problema
		return "pink";
	}else if(statusID==9){ //novo orçamento/problema aprovado
		return "green";
	}else if(statusID==10){ //novo orçamento/problema reprovado
		return "red";
	}else if(statusID==11){ //aguardando resposta cliente
		return "orange";
	}else if(statusID==12){ //pagamento aprovado
		return "red";
	}else if(statusID==13){ //pagamento reprovado
		return "red";
	}
	
}

TECHSOLVE.order.getall = () => {
	$("#order-boxes").remove();
	 $.ajax({
		type: "GET",
		url: TECHSOLVE.RESTPATH + "order/getall",
		success: async (data) => {
			
			document.getElementById("section").style.display = "flex";
			document.getElementById("section").style.flexFlow = "column";
			document.getElementById("section").style.height = "90vh";
			var dataObj = JSON.parse(data);
			htmlForm = "";
			var statusText = "";
			var clientName = "";
			
			htmlForm+="<div id='order-boxes'>";
			
			for(var i=0; i<dataObj.length;i++){
				
				var primColor;
				htmlForm+= "<div class='orderBox ";
				
				primColor = TECHSOLVE.order.getStatusAsColor(dataObj[i].status);
				statusText = TECHSOLVE.order.getStatusAsText(dataObj[i].status);
				
				htmlForm+=primColor;
				
				htmlForm+="'>";
				htmlForm+="<div class='persist-sizeBox'>";
				var dataFormated = dataObj[i].data_criacao;
				dataFormated = dataFormated.slice(8, 10) + "/" + dataFormated.slice(5, 7)+ "/" + dataFormated.slice(0, 4);
				
				htmlForm+="<h5><b>"+dataObj[i].idordem_servico+ "</b>" + " - " + dataFormated + " - " + dataObj[i].imei_aparelho + " <span>" + statusText; 
				
				if(dataObj[i].status==7){
					htmlForm+="<img src='"+ TECHSOLVE.ROOT +"assets/imgs/gear-icon.png' class='icon-gear'>";
				}		
						
				htmlForm+="</span></h5>";
				
				htmlForm+="<p>Descrição: " + dataObj[i].descricao_problema + "</p>";
				
				var clientN = await $.get(TECHSOLVE.RESTPATH + "client/getbyid", "id="+dataObj[i].idcliente);
				var clientNome = clientN.name;
				
				htmlForm+="<p>Cliente: " + clientNome + "</p>";
				
				if(dataObj[i].status!=5 && dataObj[i].status!=4 && dataObj[i].status!=10 && dataObj[i].status!=13 && dataObj[i].status!=12){
					htmlForm+=
					"<div>"
					+"<input type='button' value='Cancelar' onclick='TECHSOLVE.order.cancel("+ dataObj[i].idordem_servico +")' class='btn btn-danger inpt-btn-box'>"
					+"<input type='button' value='Transferir O.S.' onclick='TECHSOLVE.order.TrsfOS("+ dataObj[i].idordem_servico +")' class='btn btn-primary inpt-btn-box'>"
					+"</div>";
				}				
				
				htmlForm+="</div>";
				
				htmlForm+="<div><a href='javascript:TECHSOLVE.order.openBoxMenu("+ dataObj[i].idordem_servico +")' class='options "+ primColor + "'><div class='treeDot'>";
				htmlForm+="...";
				htmlForm+="</div></a>"
				+"<div class='div-options-floating "+primColor+"' id='div-options-floating-"+ dataObj[i].idordem_servico +"'>"
				+"<button class='btn opt-floating' onclick='TECHSOLVE.order.openView("+ dataObj[i].idordem_servico +")'>Visualizar</button>"
				+"</div>"
				+"</div>";
				
				htmlForm+="</div>";
		
			}
			
			htmlForm+="</div>";
			
			$("#section").append(htmlForm);
			
		},
		error: (info) => {
			TECHSOLVE.showPopUp("Mensagem", "Erro ao buscar ordens de serviço: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
		}
	});
	
}

TECHSOLVE.order.openBoxMenu = (idOS) => {
	$("#div-options-floating-"+idOS).show();
	$("body").attr("onclick", "TECHSOLVE.order.closeAllOptionsFloating()");
}

TECHSOLVE.order.closeAllOptionsFloating = () => {
	$(".div-options-floating").hide();
}

TECHSOLVE.order.openView = (idOS) => {
	
	$.ajax({
		type: "GET",
		url: TECHSOLVE.RESTPATH + "order/getbyid",
		data: "id="+idOS,
		success: async function(order){
			var orderObj = JSON.parse(order);
			var htmlFormated = "";
			
			htmlFormated += 
				"<label><p>ID da ordem de serviço:</p></label>"
				+"<input class='form-control' type='number' value='"+ orderObj.orderId +"' disabled>"
				+"<label><br><p>Status da ordem de serviço:</p></label>"
				+"<input class='form-control' type='text' value='"+ TECHSOLVE.order.getStatusAsText(orderObj.statusOS) +"' disabled>"
				+"<label><br><p>Data de criação da ordem de serviço:</p></label>"
				+"<input class='form-control' type='text' value='"+ formatDataPTBR(orderObj.creationDate) +"' disabled>"
				+"<label><br><p>Data de término da ordem de serviço:</p></label>"
				+"<input class='form-control' type='text' value='"+ formatDataPTBR(orderObj.finishDate) +"' disabled>"
				+"<label><br><p>Descrição do problema da ordem de serviço:</p></label>";
			
				if(orderObj.problemDesc == undefined){
						orderObj.problemDesc = "";
				}
			
			htmlFormated+=	
				"<textarea id='txtAreaProblemDesc' class='form-control' type='text' disabled>"+ orderObj.problemDesc +"</textarea>"
				+"<label><br><p>Cliente:</p></label>";
				
				var clientRespObj = await $.get(TECHSOLVE.RESTPATH + "client/getbyid", ("id="+orderObj.clientId));
				
			htmlFormated+=
				"<input class='form-control' type='text' value='"+ (clientRespObj.name) +"' disabled>"
				+"<label><br><p>Atendente responsável pela ordem de serviço:</p></label>";
				
				var atendente = await $.get(TECHSOLVE.RESTPATH + "user/getbyid", ("id="+orderObj.attendantRespId));
				
			htmlFormated+=
				"<input class='form-control' type='text' value='"+ atendente.name +"' disabled>"
				+"<label><br><p>Técnico responsável pela ordem de serviço:</p></label>";
				
				var tecnico = await $.get(TECHSOLVE.RESTPATH + "user/getbyid", ("id="+orderObj.technicianRespId));
				
				if(tecnico.name == undefined){
					tecnico.name = "";
				}
				
			htmlFormated+=
				"<input class='form-control' type='text' value='"+ tecnico.name +"' disabled>"
				+"<label><br><p>Orçamento da ordem de serviço:</p></label>";
				
				if(orderObj.budget==undefined){
					orderObj.budget = "";
				}
				
			htmlFormated+=
				"<textarea id='txtAreaBudget' class='form-control' type='text' disabled>"+ orderObj.budget +"</textarea>"
				+"<label><br><p>Valor total da ordem de serviço:</p></label>";
				
				var precoFormated = "";
				
				if(orderObj.totalPrice==0){
					precoFormated = "R$0";
				}else{
					precoFormated = "R$"+orderObj.totalPrice;
					if(precoFormated.includes(".")){
						precoFormated = precoFormated.replace(".", ",");
					}
				}
				
			htmlFormated+=
				"<input class='form-control' type='text' value='"+ precoFormated +"' disabled>"
				+"<label><br><p>IMEI do aparelho da ordem de serviço:</p></label>"
				+"<input class='form-control' type='text' value='"+ orderObj.imei +"' disabled>"
				+"<label><br><p>Modelo do aparelho da ordem de serviço:</p></label>"
				+"<input class='form-control' type='text' value='"+ orderObj.model +"' disabled>"
				+"<label><br><p>Cor do aparelho da ordem de serviço:</p></label>";
				
				if(orderObj.color==undefined){
					orderObj.color = "";
				}
				
			htmlFormated+=		
				"<input class='form-control' type='text' value='"+ orderObj.color +"' disabled>"
				+"<label><br><p>Existe um novo problema na manutenção do aparelho da ordem de serviço?</p></label>"
				
				var novoProblema="";
				if(orderObj.newProblem==0){
					novoProblema = "Não";
				}else{
					novoProblema = "Sim";
				}
				
			htmlFormated+=		
				"<input class='form-control' type='text' value='"+ novoProblema +"' disabled>"
				+"<label><br><p>Motivo do cancelamento da ordem de serviço:</p></label>"
				
				var motivoCancelamento="";
				if(orderObj.cancelReason!=undefined){
					motivoCancelamento = orderObj.cancelReason;
				}
				
			htmlFormated+=		
				"<textarea id='txtAreaMotivoCancelamento' class='form-control' type='text' disabled>"+ motivoCancelamento +"</textarea>"
				+"<label><br><p>Justificativa da transferência de responsabilidade da ordem de serviço:</p></label>"
				
				var justTransfResp = "";
				if(orderObj.justTransResp!=undefined){
					justTransfResp = orderObj.justTransResp;
				}
				
			htmlFormated+=
				"<textarea id='txtAreaJustTransResp' class='form-control' type='text' disabled>"+ justTransfResp +"</textarea>"
				+"<label><br><p>Informações do novo problema na ordem de serviço:</p></label>"
				
				var infoNewProblem="";
				if(orderObj.infoNewProblem!=undefined){
					infoNewProblem = orderObj.infoNewProblem;
				}
				
			htmlFormated+=
				"<textarea id='txtAreaInfoNewProblem' class='form-control' type='text' disabled>"+ infoNewProblem +"</textarea>"
				+"<br>";
				
				
			$("#modalWarning").html(htmlFormated);
			
			var modalAlter = {

				title: "Visualizar OS",
				height: 583,
				width: 835,
				modal: true,
				buttons: {
					"OK": function(){
						$(this).dialog("close");
					}
				},
				close: function(){
					
				}
			}	

			$("#modalWarning").dialog(modalAlter);
			
			var elementTxtArea = $("#txtAreaBudget")[0];
			elementTxtArea.style.height = ((elementTxtArea.scrollHeight)+2)+"px";
			
			var elementTxtAreaProblemDesc = $("#txtAreaProblemDesc")[0];
			elementTxtAreaProblemDesc.style.height = ((elementTxtAreaProblemDesc.scrollHeight)+2)+"px";

			var elementTxtAreaMotCancel = $("#txtAreaMotivoCancelamento")[0];
			elementTxtAreaMotCancel.style.height = ((elementTxtAreaMotCancel.scrollHeight)+2)+"px";

			var elementTxtAreaJustTransfResp = $("#txtAreaJustTransResp")[0];
			elementTxtAreaJustTransfResp.style.height = ((elementTxtAreaJustTransfResp.scrollHeight)+2)+"px";
			
			var elementTxtAreaInfoNewProblem = $("#txtAreaInfoNewProblem")[0];
			elementTxtAreaInfoNewProblem.style.height = ((elementTxtAreaInfoNewProblem.scrollHeight)+2)+"px";

		},
		error: function(info){
			TECHSOLVE.showPopUp("Mensagem", "Erro ao visualizar OS: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
		}
	});
}

TECHSOLVE.order.cancel = (idOS) => {
	
	var htmlFormated =
		"<label><p>Informe a justificativa para cancelar a O.S:</p></label>"
		+"<br><textarea name='txtAreaJustCancelar' id='txtAreaJustCancelar'></textarea>";
			
		$("#modalWarning").html("");	
		$("#modalWarning").html(htmlFormated);
		
		var modalAlter = {

			title: "Cancelar OS",
			height: 300,
			width: 600,
			modal: true,
			buttons: {
				"Confirmar": function(){
					var justCancelarOS = $("#txtAreaJustCancelar").val();
					
					if(justCancelarOS==null || justCancelarOS=="" || justCancelarOS==undefined){
						TECHSOLVE.showPopUp("Mensagem", "Informe uma justificativa válida para cancelar a O.S!", null);
						return;
					}else{
						var reqCancelar = $.get(TECHSOLVE.RESTPATH + "order/cancel", "idOS="+idOS+"&justCancelarOS="+justCancelarOS, (data)=>{
							TECHSOLVE.showPopUp("Mensagem", "Ordem de serviço cancelada com sucesso!", null);
							TECHSOLVE.order.getall();
						});
					}
					
					
					$(this).dialog("close");
				},
				"Cancelar": function(){
					$(this).dialog("close");
				}
			},
			close: function(){
				
			}
		}	

		$("#modalWarning").dialog(modalAlter);
	
}

TECHSOLVE.order.TrsfOS = async (idOS) => {
	
		var htmlFormated = "";
		
		htmlFormated += 
			"<label><p>Selecione para qual técnico a ordem de serviço será transferida:</p></label>"
			+"<select required id='tecnicianToTransf' name='tecnicianToTransf' class='form-control'>";
			
			var order = await $.get(TECHSOLVE.RESTPATH + "order/getbyid", ("id="+idOS));
			order = JSON.parse(order);
			
			var tecnicos = await $.get(TECHSOLVE.RESTPATH + "user/getbyidpermission", ("idPermission=3"));
			var selectedStr="";
			
			for(var i=0;i<tecnicos.length;i++){
				
				if(order.technicianRespId==tecnicos[i].idfuncionario){
					selectedStr = "selected";
				}else{
					selectedStr="";
				}
				
				htmlFormated += 
					"<option "+ selectedStr +" value='"+ tecnicos[i].idfuncionario +"'>"+tecnicos[i].nome+"</option>";

			}
			
		htmlFormated += 
			"</select>";
		htmlFormated += 
			"<br><label><br><p>Justificativa para transferência da O.S:</p></label><br>"
			+"<textarea id='txtAreaJustTransf' name='txtAreaJustTransf' required></textarea>";
			
		$("#modalWarning").html("");	
		$("#modalWarning").html(htmlFormated);
		
		var modalAlter = {

			title: "Transferir OS",
			height: 400,
			width: 600,
			modal: true,
			buttons: {
				"Confirmar": function(){
					var idTec = $("#tecnicianToTransf").val();
					var justTransfOS =$("#txtAreaJustTransf").val();
					
					if(idTec==null){
						TECHSOLVE.showPopUp("Mensagem", "Informe um técnico válido para transferir a O.S!", null);
						return;
					}else if(justTransfOS=="" || justTransfOS==" " || justTransfOS==undefined){
						TECHSOLVE.showPopUp("Mensagem", "Preencha o campo 'justificativa de transferência da O.S'!", null);
						return;
					}
					
					TECHSOLVE.order.transfTecResp(idOS, idTec, justTransfOS);
					$(this).dialog("close");
				},
				"Cancelar": function(){
					$(this).dialog("close");
				}
			},
			close: function(){
				
			}
		}	

		$("#modalWarning").dialog(modalAlter);
	
}

TECHSOLVE.order.transfTecResp = (idOS, idTec, justTransfOS) => {
	
	$.ajax({
		type: "PUT",
		url: TECHSOLVE.RESTPATH + "order/setTec",
		data: JSON.stringify({
			"idOS":parseInt(idOS),
			"idTec":parseInt(idTec),
			"justTransfOS":justTransfOS,
		}),
		success: async function(data){
			TECHSOLVE.showPopUp("Mensagem", "Técnico responsável pela ordem de serviço alterado com sucesso!", null);
			TECHSOLVE.order.getall();
		},
		error: (info) =>{
			TECHSOLVE.showPopUp("Mensagem", "Erro ao editar técnico responsável pela OS: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
		}	
	});
}
TECHSOLVE = new Object();
TECHSOLVE.permission = new Object();
TECHSOLVE.user = new Object();
TECHSOLVE.client = new Object();
TECHSOLVE.order = new Object();

var DINAMICCONTEXT ="";

TECHSOLVE.ROOT = "/TechSolve/";
TECHSOLVE.ACESSDIR = "/TechSolve/acess/";
TECHSOLVE.PATH = "/TechSolve/acess/pages/technician/";
TECHSOLVE.RESTPATH = "/TechSolve/acess/rest/";
TECHSOLVE.IMAGESPATH = "/TechSolve/acess/technician/assets/imgs/";

$(document).ready(function () {

    $("header").load(TECHSOLVE.PATH + "dinamic/header/");

});

function goPage(name, loadPlace){

	$("#"+loadPlace).html("");
	$("#"+loadPlace).load(TECHSOLVE.PATH + name);
	
	if(name=="orders"){
	
		DINAMICCONTEXT = name;
		document.title = "TechSolve | Ordens de Serviço";
		TECHSOLVE.order.getall();
		
	}else if(name=="myorders"){
	
		DINAMICCONTEXT = name;
		document.title = "TechSolve | Minhas O.S.";
		TECHSOLVE.order.getall();
		
	}
	
}



function verifyFormOk(){
	var verificationsNotOk = 0;
	for(var i=0;i<document.frmRegister.length;i++){
		if(document.frmRegister[i].type!='fieldset' 
		&& document.frmRegister[i].type!='button' 
		&& document.frmRegister[i].type!='reset' 
		&& document.frmRegister[i].type!='submit'
		&& document.frmRegister[i].id!="color"
		&& document.frmRegister[i].id!="filtroCliente"){
			
			if(
			(document.frmRegister[i].type=='date' 
			|| document.frmRegister[i].type=='number' 
			|| document.frmRegister[i].type=='text'
			|| document.frmRegister[i].type=='textarea' 
			|| document.frmRegister[i].type=='select-one'
			|| document.frmRegister[i].type=='password') 
			&& document.frmRegister[i].value==''){
			
				verificationsNotOk++;
				$("#submitRegister").attr("class", "btn btn-secondary mb-3 perm-btn");
				$("#submitRegister").attr("disabled", "");
				
			}else if(verificationsNotOk==0 && (document.frmRegister[i].type=='checkbox' || document.frmRegister[i].type=='radio') && document.frmRegister[i].checked==false){
				
				$("#submitRegister").attr("class", "btn btn-secondary mb-3 perm-btn");
			
			}else if(verificationsNotOk==0){
			
				$("#submitRegister").removeAttr("disabled");
				$("#submitRegister").attr("class", "btn btn-success mb-3 perm-btn");
			
			}
			
		}
	}

}

TECHSOLVE.showPopUp = function(title, warning, focusId){

		var modal = {

			title: title,
			height: 250,
			width: 400,
			modal: true,
			buttons: {
				"OK": function(){
					$(this).dialog("close");
					if(focusId!=null){
						document.getElementById(focusId).focus();
					}
				}
			}
		}

		$("#modalWarning").html(warning);
		$("#modalWarning").dialog(modal);

}

function convertPassword64(password, idField){
	
	var pass64 = btoa(password);
	$("#"+idField).val(pass64);
	
	return pass64;
}

function logout(){
	
	$.ajax({
		type: "GET",
		url: TECHSOLVE.ROOT + "logout",
		success: function(data){
			(data=='true') ? window.location.href = "/TechSolve": alert("Essa sessão já foi finalizada!");
		},
		error: function(info){
			TECHSOLVE.showPopUp("Mensagem", "Erro ao fazer logout: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
		}
	});
	
}

function formatDataPTBR(dataStr){
	
	var dataFormated = "";
	
	if(dataStr!=undefined){
		dataFormated += dataStr.slice(8, 10)+"/"+dataStr.slice(5, 7)+"/"+dataStr.slice(0, 4);
	}
	return dataFormated;
}
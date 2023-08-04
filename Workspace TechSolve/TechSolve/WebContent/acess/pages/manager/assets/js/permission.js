TECHSOLVE.permission.getall = function(){

	$.ajax({
		type: "GET",
		url: TECHSOLVE.RESTPATH + "permission/getall",
		success: function(data){
			permissoes = data;
			var tableHTML="";
			tableHTML = 
			"<table class='table table-bordered border-secondary' name='permissionTable' id='permissionTable'>"+
				"<thead>"+
					"<tr>"+
						"<th scope='col'><b>ID</b></th>"+
						"<th scope='col'>Nome</th>"+
						"<th scope='col'>Ações</th>"+
					"</tr>"+
				"</thead>"+
				"<tbody>";
				
					for(var i=0;i<data.length;i++){
						
						tableHTML +=
						"<tr>"+
							"<th scope='row'><b>"+data[i].id +"</b></th>"+
							"<td>"+data[i].descricao +"</td>"+
							"<td>"+
								"<a href='javascript:TECHSOLVE.permission.showedit("+ data[i].id +")'><img class='iconaction' src='" + TECHSOLVE.IMAGESPATH + "edit.png" + "' alt='Botão editar'></a>"+
								"&nbsp&nbsp&nbsp&nbsp&nbsp"+
								"<a href='javascript:TECHSOLVE.permission.delete("+ data[i].id +")'><img class='iconaction' src='" + TECHSOLVE.IMAGESPATH + "delete.png" + "' alt='Botão excluir'></a>"+
							"</td>"+
						"</tr>";
					
					}
					
					tableHTML +=	
				"</tbody>"+
			"</table>";
			
			if(document.getElementById("tablePermissionsdiv")==undefined){
				console.log(data)
			}else{
			document.getElementById("tablePermissionsdiv").innerHTML = tableHTML;
			}
			
		},
		error: function(info){
			TECHSOLVE.showPopUp("Mensagem", "Erro ao buscar as permissões: " + info.responseText + " - " + info.status + " - " + info.statusText, null);
		}
	});

}
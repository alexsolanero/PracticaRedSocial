$( document ).ready(function() {
	$(function() {

		var _emisor = sessionStorage.getItem("email");
		
		var _status = sessionStorage.getItem("status"); 
		
		if(_status != "ok"){
			
			alert("No ha iniciado sesion");
			location.href ="index.html";
		}
		
		document.getElementById("menuNombre").innerHTML = _emisor;
		document.getElementById("infoUser").innerHTML = "Bienvenido "+_emisor+"!!";
		
	});
	$('#logOut').click(function(){
		//borro la sesion
		sessionStorage.clear();
		//vuelvo al inicio
		location.href ="index.html";
		
	});
	
	
		
	$('#sendForm').on('click', function(e){
		$('#edit-form').submit();
	});
	
		
	cargarDatosInput();
	cargarMensajes();
});

function cargarDatosInput(){
	
	var emisor = sessionStorage.getItem("email");
	$('#emisor_hidden').val(emisor);
}


function cargarMensajes(){
	
	$.post( "mensajes.do", function( data ) {
		
		var json = JSON.parse(data);
		for(i=0;i<json.length;i++){
			var html = "<div class='col-md-4 col-md-offset-4'>";
			html += "<div class='col-md-4 col-md-offset-4'>";
			html += "<div class='panel panel-default panel-google-plus'>"; 
            html += "<div class='dropdown'>";
            html +=  "<span class='dropdown-toggle' data-toggle='dropdown'>";
            html += "<span class='glyphicon glyphicon-chevron-down'></span>";
            html += "</span>";
            html += "<ul class='dropdown-menu' role='menu'>";
            html += "<li role='presentation'><a href='#' onclick='eliminarMensaje("+i+");' ><span class='glyphicon glyphicon-trash'></span> Eliminar</a></li>";
            html += "</ul>";
            html += "</div>";
            html += "<div class='panel-heading'>";
            html += "<h5><span>enviado el </spa><span>"+json[i].fecha+"</span> </h5>";
            html += "<h5>"+json[i].emailemisor+"</h5>";
            html += "</div>";
            html += "<div class='panel-body'>";
            html += "<p id='mensaje-"+i+"'>"+json[i].mensaje+"</p>";
            html += "</div>";
            html += "<div class='panel-footer'>";
            html += "<input type='hidden' id='delete-"+i+"' value='"+json[i].idmensaje+"'>";
            html += "</div>";
            html += "</div>";
            html += "</div>";
			$('#div_mensajesprivados').append(html);	
		}
	});
}

function eliminarMensaje(id){
	
	var id = $('#delete-'+id).val();
	$.post( "deletemsg.do",{ id: id}, function( ) {
		location.href ="wallMsg.jsp";
	});
}

function showModal(id){
	
	var texto = $('#mensaje-'+id).text();
	var id = $('#delete-'+id).val();
	var destinatario =$('#emaildestinatario').text();
	
	$('#editModal').modal('show');
	//Inicializamos los valores a vacio.
	$('#edit-mensaje').text("");
	$('#edit-destinatario').text("");
	$('#edit-emisor').text("");
	$('#edit-id').text("");
	// Añadimos los nuevos valores.
	$('#edit-mensaje').text(texto);
	$('#edit-destinatario').text($('#emaildestinatario').text());
	$('#edit-emisor').text(_emisor);
	$('#edit-id').val(id);
	
}


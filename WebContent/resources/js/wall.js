$( document ).ready(function() {
	$(function() {

		var _name = sessionStorage.getItem("name");
		var _email = sessionStorage.getItem("email");
		var _status = sessionStorage.getItem("status"); 
		//alert("wall.js SESSION ln10--> name: "+_name+" email: "+_email+" status: "+_status);
		if(_status != "ok"){
			//ERROR DE ESTADO
			alert("No ha iniciado sesion");
			location.href ="index.html";
		}
		//$('#menuNombre').val();
		document.getElementById("menuNombre").innerHTML = _name;
		document.getElementById("infoUser").innerHTML = "Bienvenido "+_name+"!!";
		
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
	cargarPublicaciones();
});


function cargarDatosInput(){
	
	var nombre = sessionStorage.getItem("name");
	var email = sessionStorage.getItem("email");
	$('#nombre_hidden').val(nombre);
	$('#email_hidden').val(email);
}

function cargarPublicaciones(){
	
	$.post( "publicaciones.do", function( data ) {
		var json = JSON.parse(data);
		for(i=0;i<json.length;i++){
			var html = "<div class='col-md-4 col-md-offset-4'>";
			html += "<div class='panel panel-default panel-google-plus'>"; 
            html += "<div class='dropdown'>";
            html +=  "<span class='dropdown-toggle' data-toggle='dropdown'>";
            html += "<span class='glyphicon glyphicon-chevron-down'></span>";
            html += "</span>";
            html += "<ul class='dropdown-menu' role='menu'>";
            html += "<li role='presentation'><a href='#' onclick='showModal("+i+")'><span class='glyphicon glyphicon-edit'></span> Editar</a></li>";
            html += "<li role='presentation'><a href='#' onclick='eliminarPublicacion("+i+");' ><span class='glyphicon glyphicon-trash'></span> Eliminar</a></li>";
            html += "</ul>";
            html += "</div>";
            html += "<div class='panel-heading'>";
            html += "<h3>"+json[i].nombre+"</h3>";
            html += "<h5>"+json[i].email+"</h5>";
            html += "<h5><span>publicado el </spa><span>"+json[i].fecha+"</span> </h5>";
            html += "</div>";
            html += "<div class='panel-body'>";
            html += "<p id='mensaje-"+i+"'>"+json[i].mensaje+"</p>";
            html += "</div>";
            html += "<div class='panel-footer'>";
            html += "<button type='button' onclick='darLike("+i+");' class='btn btn-default '> <span class='glyphicon glyphicon-thumbs-up'></span></button>";
            html += "<input type='hidden' id='delete-"+i+"' value='"+json[i].idPublicacion+"'>";
            html += "</div>";
            html += "</div>";
            html += "</div>";
			$('#div_publicaciones').append(html);	
		}
	});
}

function eliminarPublicacion(id){
	
	var id = $('#delete-'+id).val();
	$.post( "deletepubli.do",{ id: id}, function( ) {
		location.href ="wall.jsp";
	});
}

function showModal(id){
	
	var texto = $('#mensaje-'+id).text();
	var id = $('#delete-'+id).val();
	var nombre = sessionStorage.getItem("name");
	var email = sessionStorage.getItem("email");
	$('#editModal').modal('show');
	//Inicializamos los valores a vacio.
	$('#edit-mensaje').text("");
	$('#edit-name').text("");
	$('#edit-email').text("");
	$('#edit-id').text("");
	// Añadimos los nuevos valores.
	$('#edit-mensaje').text(texto);
	$('#edit-name').val(nombre);
	$('#edit-email').val(email);
	$('#edit-id').val(id);
}

function darLike(id){
	
	var id = $('#delete-'+id).val();
	$.post( "deletepubli.do",{ id: id}, function( ) {
		location.href ="wall.jsp";
	});
}

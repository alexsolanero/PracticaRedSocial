<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Mens Ubiqua - Red Social</title>
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="resources/css/template.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="resources/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="resources/js/wallMsg.js"></script>
</head>
<body>
	<div class="container-fluid">
	<div class="row" style="background-color: lightblue">
		<div class="col-md-12" style="text-align:center">
			<img src="img/uclm.png" class="imgthumbnail">
		</div>
		<div class="col-md-12" style="text-align:center">
			<h4>Mens Ubiqua - Red Social</h4>
		</div>
	</div>
	<div class="row" style="background-color: lightgray">
		<div class="col-md-3 col-md-offset-9">

			<!--Menu del usuario  --------------------------------------------------------------------------------------->

			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav">
					<li><a href="#"><span class="glyphicon glyphiconuser"></span>
							Inicio</a></li>
					<li><a href="#"><span class="glyphicon glyphiconuser"></span>
							Mensajes</a></li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> Menú <b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="#" id=menuNombre>Usuario</a></li>
							<li class="divider"></li>
							<li><a href="#">Configuración</a></li>
							<li><a href="#">Ayuda</a></li>
							<li><a href="#">Acerca de...</a></li>
							<li class="divider"></li>
							<li><a href="#" id="logOut">Salir</a></li>
						</ul></li>

				</ul>
			</div>
		</div>
	</div>

	<div class="row" id="mesaggesPanel">
		<div class="col-md-3" style="background-color: lightblue" id="infoUser" style="text-align:center">información de usuario</div>
		<div class="col-md-6" style="text-align:center">mensajes de los contactos</div>
		<div class="col-md-3" style="background-color: lightgray;text-align:center">otras opciones</div>
	</div>
	
	<div class="row">
		<br>
		<form action="enviar.do" method="post">
			<div class="col-md-6 col-md-offset-4">
				
				<div class="form-group">
					<div>
					<label>Destinatario </label><textarea rows="1" cols="30" name="emaildestinatario"></textarea>
					</div>
					<input type="hidden" id="emisor_hidden" name="emailemisor" value="">
					<label>escribe tu mensaje</label>
					<div>
					<textarea rows="5" cols="70" name="mensaje"></textarea>
					</div>
				</div>
				<button type="submit" class="btn btn-primary btn-outline">Enviar</button>
			</div>
		</form>
	</div>
	<div class="row">
		<br>
	</div>
	<div class="row" id="div_mensajesprivados">
		
	
	</div>
	
</div>

<!-- Modal -->
<form id="edit-form" action="editarmsg.do" method="post">
<div id="editModal" class="modal fade" role="dialog">
  <div class="modal-dialog">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Editar mensaje</h4>
      </div>
      <div class="modal-body">
        	
        		<textarea id="edit-mensaje" rows="5" cols="70" name="mensaje"></textarea>
        		<input type="hidden" id="edit-destinatario" name="edit-destinatario" value="">
        		<input type="hidden" id="edit-emisor" name="edit-emisor" value="">
        		<input type="hidden" id="edit-id" name="edit-id" value="">
        	
      </div>
      <div class="modal-footer">
      	 <button type="button" id="sendForm" class="btn btn-primary" data-dismiss="modal">Guardar</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
      </div>
    </div>

  </div>
</div>
</form> 
</body>
</html>
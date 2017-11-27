package com.redsocial.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.redsocial.auxiliares.Utilidades;
import com.redsocial.modelo.Like;
import com.redsocial.modelo.Publicacion;
import com.redsocial.modelo.Respuesta;
import com.redsocial.modelo.Usuario;
import com.redsocial.persistencia.DAOLike;
import com.redsocial.persistencia.DAOPublicacion;
import com.redsocial.persistencia.DAORespuesta;
import com.redsocial.persistencia.DAOUsuario;

@Controller
public class PublicacionController {

	@RequestMapping(value = "publicar", method = RequestMethod.POST)
	public String home(HttpServletRequest request,Model model) {
		
		if (request.getSession().getAttribute("user")!=null) {
			String mensaje = request.getParameter("mensaje");
			Usuario user = (Usuario) request.getSession().getAttribute("user");
			
			 // Montamos la fecha actual para saber cuando se hizo la publicacion.
			String fechaPublicacion=Utilidades.obtenerFecha();
			
			
			Publicacion publicacion = new Publicacion();
			publicacion.setEmail(user.getemail());
			publicacion.setNombre(user.getNombre());
			publicacion.setMensaje(mensaje);
			publicacion.setFecha(fechaPublicacion);
			
			Publicacion newPublicacion = DAOPublicacion.insert(publicacion);
			
			return "redirect:wall";
		}else {
			return "home";
		}

	}
	
	@RequestMapping(value = "comentar", method = RequestMethod.POST)
	public String comentar(HttpServletRequest request,Model model) {
		
		if (request.getSession().getAttribute("user")!=null) {
			String mensaje = request.getParameter("respuesta");
			String idPublicacion = request.getParameter("respuesta-publicacion");
			Usuario user = (Usuario) request.getSession().getAttribute("user");
			
			String fechaPublicacion=Utilidades.obtenerFecha();
		     
		     Respuesta respuesta = new Respuesta(user.getemail(), fechaPublicacion, idPublicacion, mensaje, user.getNombre());
		     
		     ObjectId resultado = DAORespuesta.insert(respuesta);
			
			
			return "redirect:wall";
		}else {
			return "home";
		}

	}
	
	@RequestMapping(value = "borrarpublicacion", method = RequestMethod.GET)
	public String borrarpublicacion(HttpServletRequest request,Model model) throws Exception {
		
		if (request.getSession().getAttribute("user")!=null) {
			String idPublicacion = request.getParameter("id");
			ArrayList<Respuesta> respuestas = DAORespuesta.select(idPublicacion);
			for (int i=0;i<respuestas.size();i++) {
				DAORespuesta.delete(respuestas.get(i).getIdPublicacion());
			}
			Usuario user = DAOUsuario.select((Usuario) request.getSession().getAttribute("user"));
			Like onelike = DAOLike.checkLike(idPublicacion, user.getemail());
			if (onelike!=null) {
				DAOLike.delete(onelike);
			}
			DAOPublicacion.delete(idPublicacion);
			return "redirect:wall";
		}else {
			return "home";
		}

	}
	
	@RequestMapping(value = "editarpublicacion", method = RequestMethod.GET)
	public String editarpublicacion( @RequestParam String id,HttpServletRequest request,Model model) throws Exception {
		
		if (request.getSession().getAttribute("user")!=null) {
			Publicacion publicacion = DAOPublicacion.select(id);
			model.addAttribute("publi",publicacion);
			model.addAttribute("id",id);
			model.addAttribute("body","editarPublicacion");
			return "wall";
		}else {
			return "home";
		}

	}
	
	@RequestMapping(value = "updatepublicacion", method = RequestMethod.POST)
	public String updatePublicacion(HttpServletRequest request,Model model) throws Exception {
		
		if (request.getSession().getAttribute("user")!=null) {
			String idPublicacion = request.getParameter("idPublicacion");
			String nombre = request.getParameter("update-name");
			String email = request.getParameter("update-email");
			String fecha = request.getParameter("update-fecha");
			String mensaje = request.getParameter("update-mensaje");
			
			Publicacion publicacion = new Publicacion(idPublicacion, email, nombre, fecha, "", mensaje);
			DAOPublicacion.update(publicacion);
			
			return "redirect:wall";
		}else {
			return "home";
		}

	}
}

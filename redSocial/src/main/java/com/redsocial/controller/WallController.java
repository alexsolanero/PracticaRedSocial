package com.redsocial.controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.redsocial.modelo.Like;
import com.redsocial.modelo.Publicacion;
import com.redsocial.modelo.Respuesta;
import com.redsocial.modelo.Usuario;
import com.redsocial.persistencia.DAOLike;
import com.redsocial.persistencia.DAOPublicacion;
import com.redsocial.persistencia.DAORespuesta;
import com.redsocial.persistencia.DAOUsuario;

@Controller
public class WallController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws Exception 
	 */
	@RequestMapping(value="wall", method = RequestMethod.GET)
	public String wall(HttpServletRequest request, Model model) throws Exception {
		
		if (request.getSession().getAttribute("user")!=null) {
			ArrayList<Publicacion> publicaciones = DAOPublicacion.selectAll();
			Hashtable<String,Integer> likes = new Hashtable<String,Integer>();
			Hashtable<String,Integer> checklikes = new Hashtable<String,Integer>();
			Hashtable<String,ArrayList<Respuesta>> respuestas = new Hashtable<String,ArrayList<Respuesta>>();
			Usuario user = DAOUsuario.select((Usuario) request.getSession().getAttribute("user"));
			request.getSession().setAttribute("user", user);
			for (int i=0;i<publicaciones.size();i++) {
				int totalPublicaciones = 0;
				totalPublicaciones = DAOLike.select(publicaciones.get(i).getIdPublicacion()).size();
				likes.put(publicaciones.get(i).getIdPublicacion(), totalPublicaciones);
				Like onelike = DAOLike.checkLike(publicaciones.get(i).getIdPublicacion(), user.getemail());
				if (onelike!=null) {
					checklikes.put(publicaciones.get(i).getIdPublicacion(), 1);
				}else {
					checklikes.put(publicaciones.get(i).getIdPublicacion(), 0);
				}
				ArrayList<Respuesta> resultadoRespuesta = DAORespuesta.select(publicaciones.get(i).getIdPublicacion());
				respuestas.put(publicaciones.get(i).getIdPublicacion(), resultadoRespuesta);
				
			}
			model.addAttribute("publicaciones",publicaciones);
			model.addAttribute("respuestas",respuestas);
			model.addAttribute("likes",likes);
			model.addAttribute("user",user);
			model.addAttribute("checklikes",checklikes);
			model.addAttribute("body","publicaciones");
			
			return "wall";
		}else {
			return "home";
		}
		

	}
	
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,Model model) {
		
		if (request.getSession().getAttribute("user")!=null) {
			request.getSession().invalidate();
			return "home";
		}
		return "home";
	}
	
	@RequestMapping(value = "darlike", method = RequestMethod.POST)
	public String darlike(HttpServletRequest request,Model model) throws Exception {
		
		String idPublicacion = request.getParameter("like-publicacion");
		Usuario user = (Usuario) request.getSession().getAttribute("user");
		Like like = new Like(user.getemail(), idPublicacion);
		Like resultado = DAOLike.checkLike(idPublicacion, user.getemail());
		
		if (resultado!=null) {
			DAOLike.delete(like);
		}else {
			DAOLike.insert(like);
		}
		
		return "redirect:wall";
	}
	
}

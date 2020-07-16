package com.alexis.empleos.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alexis.empleos.model.Perfil;
import com.alexis.empleos.model.Solicitud;
import com.alexis.empleos.model.Usuario;
import com.alexis.empleos.service.ISolicitudesService;
import com.alexis.empleos.service.IUsuarioService;
import com.alexis.empleos.service.IVacanteService;
import com.alexis.empleos.util.Utileria;

@Controller
@RequestMapping(value = "/solicitudes")
public class SolicitudController {

	// Ruta empleada en el archivo properties
	@Value("${empleos.ruta.cv}")
	private String ruta;

	@Autowired
	private IVacanteService serviceVacante;

	@Autowired
	private IUsuarioService serviceUsuario;

	@Autowired
	private ISolicitudesService serviceSolicitud;

	@GetMapping("/create/{id}")
	public String mostrarSolicitud(Solicitud solicitud, @PathVariable("id") int idVacante, Model model,
			Authentication auth, HttpSession session) {
		Usuario usuario = serviceUsuario.buscarUsuarioPorNombre(auth.getName());
		model.addAttribute("vacante", serviceVacante.buscarVacantePorId(idVacante));
		if (session.getAttribute("user") == null) {
			usuario.setPassword(null);
			session.setAttribute("user", usuario);
		}
		if (buscarPerfil(usuario.getPerfiles())) {
			return "/solicitudes/formSolicitud";
		}
		return "redirect:/";
	}

	public boolean buscarPerfil(List<Perfil> perfiles) {
		boolean isUser = false;
		for (Perfil perfil : perfiles) {
			if (perfil.getPerfil().equals("USUARIO")) {
				isUser = true;
			}
		}
		return isUser;
	}

	@GetMapping("/index")
	public String listarSolicitudes(Model model) {
		model.addAttribute("solicitudes", serviceSolicitud.obtenerSolicitudes());
		return "solicitudes/listSolicitudes";
	}

	@PostMapping("/save")
	public String guardarSolicitud(Solicitud solicitud, BindingResult result, RedirectAttributes attributes,
			@RequestParam("archivoCV") MultipartFile multiPart, Authentication auth, Model model) {
		String userName = auth.getName();
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			return "/solicitudes/formSolicitud";
		}
		if (buscarSolicitud(userName, solicitud.getVacante().getId())) {
			attributes.addFlashAttribute("msg", "Ya aplicaste a esta vacante");
			return "redirect:/solicitudes/create/" + solicitud.getVacante().getId();
		}
		if (!multiPart.isEmpty()) {
			String nombreCV = Utileria.guardarArchivo(multiPart, ruta);
			if (nombreCV != null) {
				solicitud.setArchivo(nombreCV);
			}
		}
		solicitud.setUsuario(serviceUsuario.buscarUsuarioPorNombre(userName));
		solicitud.setFecha(new Date());
		serviceSolicitud.guardarSolicitud(solicitud);
		attributes.addFlashAttribute("msg", "Gracias por enviar tu CV!");
		return "redirect:/";
	}

	public boolean buscarSolicitud(String userName, int id) {
		boolean existe = false;
		for (Solicitud solicitud : serviceSolicitud.obtenerSolicitudes()) {
			if (solicitud.getUsuario().getUsername().equals(userName) && solicitud.getVacante().getId().equals(id)) {
				existe = true;
			}
		}
		return existe;
	}

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idSolicitud, RedirectAttributes attributes) {
		serviceSolicitud.eliminarSolicitud(idSolicitud);
		attributes.addFlashAttribute("msg", "La solicitud fue eliminada!.");
		return "redirect:/solicitudes/index";
	}

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}

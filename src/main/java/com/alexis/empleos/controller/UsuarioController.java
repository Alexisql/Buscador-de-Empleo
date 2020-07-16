package com.alexis.empleos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alexis.empleos.service.IUsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private IUsuarioService serviceUsuario;
	
	@GetMapping("/index")
	public String mostrarUsuarios(Model model) {
		model.addAttribute("usuarios", serviceUsuario.obtenerUsuarios());
		return "usuarios/listUsuarios";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminarUsuario(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
		serviceUsuario.eliminarUsuario(idUsuario);
		attributes.addFlashAttribute("msg", "Usuario Eliminado!");
		return "redirect:/usuarios/index";
	}

}

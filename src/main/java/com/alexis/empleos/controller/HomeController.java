package com.alexis.empleos.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alexis.empleos.model.Usuario;
import com.alexis.empleos.model.Vacante;
import com.alexis.empleos.service.ICategoriasService;
import com.alexis.empleos.service.IUsuarioService;
import com.alexis.empleos.service.IVacanteService;

@Controller
@Primary
public class HomeController {

	@Autowired
	private IVacanteService serviceVacante;

	@Autowired
	private ICategoriasService serviceCategoria;

	@Autowired
	private IUsuarioService serviceUsuario;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String mostrarHome(Model model) {
		model.addAttribute("vacantes", serviceVacante.buscarVacantesPorDestacadoAndEstatus(1, "Aprobada"));
		return "home";
	}

	@GetMapping("/index")
	public String index(Authentication auth, HttpSession session) {
		if (session.getAttribute("user") == null) {
			Usuario usuario = serviceUsuario.buscarUsuarioPorNombre(auth.getName());
			usuario.setPassword(null);
			System.out.println("USER: " + usuario.toString());
			session.setAttribute("user", usuario);
		}
		return "redirect:/";
	}

	@GetMapping("/usuarios/create")
	public String crearUsuario(Usuario usuario) {
		return "usuarios/fromRegistro";
	}

	@PostMapping("/usuarios/save")
	public String guardarUsuario(Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			return "usuarios/formRegistro";
		}
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		serviceUsuario.guardarUsuario(usuario);
		attributes.addFlashAttribute("msg", "Registro Exitoso!");
		return "redirect:/";
	}

	@GetMapping("/search")
	public String filtrarVacantes(@ModelAttribute("searchVacante") Vacante vacante, Model model) {
		System.out.println("Busacando... " + vacante);
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("descripcion",
				ExampleMatcher.GenericPropertyMatchers.contains()); // where descripcion LIKE %?%
		Example<Vacante> example = Example.of(vacante, matcher);
		model.addAttribute("vacantes", serviceVacante.buscarByExample(example));
		return "home";
	}

	@GetMapping("/login")
	public String mostrarLogin() {
		return "formLogin";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/index";
	}

	// Método utilizado para pasar una variable de tipo String vacia a null (Se
	// emplea en el filtro de búsqueda)
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	// Método empleado para utilizar modelos a nivel de clase (En todos sus métodos)
	@ModelAttribute
	public void modelGenericos(Model model) {
		model.addAttribute("categorias", serviceCategoria.obtenerCategorias());
		Vacante vacanteSearch = new Vacante();
		vacanteSearch.resetImagen();
		model.addAttribute("searchVacante", vacanteSearch);
	}

}

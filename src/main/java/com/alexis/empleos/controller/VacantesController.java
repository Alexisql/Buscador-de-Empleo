package com.alexis.empleos.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alexis.empleos.model.Vacante;
import com.alexis.empleos.service.ICategoriasService;
import com.alexis.empleos.service.IVacanteService;
import com.alexis.empleos.util.Utileria;

@Controller
@RequestMapping("/vacantes")
public class VacantesController {

	// Ruta empleada en el archivo properties
	@Value("${empleos.ruta.imagenes}")
	private String ruta;
	
	
	@Autowired
	private IVacanteService serviceVacante;

	@Autowired
	private ICategoriasService serviceCategoria;

	@GetMapping("/create")
	public String crearVacante(Vacante vacante, Model model) {
		return "vacantes/formVacante";
	}

	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		model.addAttribute("vacantes", serviceVacante.obtenerVacantes());
		return "vacantes/listVacantes";
	}
	
	@GetMapping(value = "/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
	Page<Vacante>lista = serviceVacante.buscarVacantesPaginacion(page);
	model.addAttribute("vacantes", lista);
	return "vacantes/listVacantes";
	}

	@PostMapping("/save")
	public String guardarVacante(Vacante vacante, BindingResult result, RedirectAttributes attributes,
			@RequestParam("archivoImagen") MultipartFile multiPart) {
		vacante.setCategoria(serviceCategoria.buscarCategoriaPorId(vacante.getCategoria().getId()));
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			return "vacantes/formVacante";
		}
		if (!multiPart.isEmpty()) {
			// String ruta = "/empleos/img-vacantes/"; // Linux/MAC
			//String ruta = "c:/empleos/img-vacantes/"; // Windows
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if (nombreImagen != null) { // La imagen si se subio
				// Procesamos la variable nombreImagen
				vacante.setImagen(nombreImagen);
			}
		}
		serviceVacante.guardarVacante(vacante);
		attributes.addFlashAttribute("msg", "Registro Exitoso!");
		return "redirect:/vacantes/index";
	}

	// Método utilizado para castear una variable de tipo Date a String, con su respectivo formato
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@GetMapping("/view/{id}")
	public String verDatalle(@PathVariable("id") int idVacante, Model model) {
		model.addAttribute("vacante", serviceVacante.buscarVacantePorId(idVacante));
		return "detalle";
	}

	@GetMapping("/delete/{id}")
	public String eliminarVacante(@PathVariable("id") int idVacante, RedirectAttributes attributes) {
		serviceVacante.eliminarVacantePorId(idVacante);
		attributes.addFlashAttribute("msg", "Vacante Eliminada!");
		return "redirect:/vacantes/index";
	}
	
	@GetMapping("/editar/{id}")
	public String editarVacante(@PathVariable("id") int idVacante, Model model) {
		model.addAttribute("vacante", serviceVacante.buscarVacantePorId(idVacante));
		return "vacantes/formVacante";
	}
	
	// Método empleado para utilizar modelos a nivel de clase (En todos sus métodos)
	@ModelAttribute
	private void obtenerModelGenericos(Model model) {
		model.addAttribute("categorias", serviceCategoria.obtenerCategorias());
	}

}

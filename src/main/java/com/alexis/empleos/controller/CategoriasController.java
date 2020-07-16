package com.alexis.empleos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alexis.empleos.model.Categoria;
import com.alexis.empleos.service.ICategoriasService;

@Controller
@RequestMapping(value = "/categorias")
public class CategoriasController {

	@Autowired
	private ICategoriasService serviceCategorias;

	// @GetMapping("/index")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String mostrarIndex(Model model) {
		model.addAttribute("categorias", serviceCategorias.obtenerCategorias());
		return "categorias/listCategorias";
	}
	
	@GetMapping(value = "/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
	Page<Categoria>lista = serviceCategorias.buscarCategoriasPaginacion(page);
	model.addAttribute("categorias", lista);
	return "categorias/listCategorias";
	}

	// @GetMapping("/create")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String crear(Categoria categoria) {
		return "categorias/formCategoria";
	}

	// @PostMapping("/save")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String guardar(Categoria categoria, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			return "categorias/formCategoria";
		}
		serviceCategorias.guardarCategoria(categoria);
		attributes.addFlashAttribute("msg", "Registro Exitoso!");
		return "redirect:/categorias/index";
	}
	
	@GetMapping("/editar/{id}")
	public String editarCategoria(@PathVariable("id") int idCategoria, Model model) {
		model.addAttribute("categoria", serviceCategorias.buscarCategoriaPorId(idCategoria));
		return "categorias/formCategoria";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminarCategoria(@PathVariable("id") int idCategoria, RedirectAttributes attributes) {
		serviceCategorias.eliminarCategoria(idCategoria);
		attributes.addFlashAttribute("msg", "Categoria Eliminada!");
		return "redirect:/categorias/index";
	}
}

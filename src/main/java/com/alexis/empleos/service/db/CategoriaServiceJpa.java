package com.alexis.empleos.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alexis.empleos.model.Categoria;
import com.alexis.empleos.repository.CategoriaRepository;
import com.alexis.empleos.service.ICategoriasService;

@Service
@Primary //Se usa cuando se tiene mas de dos implementaciones de una interface, la cual es un service (Debido a que los service manejan el patron singleton)
public class CategoriaServiceJpa implements ICategoriasService {

	@Autowired
	private CategoriaRepository categoriaRepo;
	
	@Override
	public void guardarCategoria(Categoria categoria) {
		categoriaRepo.save(categoria);
	}

	@Override
	public List<Categoria> obtenerCategorias() {
		return categoriaRepo.findAll();
	}

	@Override
	public Categoria buscarCategoriaPorId(Integer id) {
		Categoria categoria = null;
		Optional<Categoria> option = categoriaRepo.findById(id);
		if(option.isPresent()) {
			categoria = option.get();
		}
		return categoria;
	}

	@Override
	public void eliminarCategoria(Integer idCategoria) {
		categoriaRepo.deleteById(idCategoria);
	}

	@Override
	public Page<Categoria> buscarCategoriasPaginacion(Pageable page) {
		return categoriaRepo.findAll(page);
	}

}

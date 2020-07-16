package com.alexis.empleos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alexis.empleos.model.Categoria;

public interface ICategoriasService {
	public void guardarCategoria(Categoria categoria);
	public List<Categoria> obtenerCategorias();
	public Categoria buscarCategoriaPorId(Integer idCategoria);
	public void eliminarCategoria(Integer idCategoria);
	public Page<Categoria>buscarCategoriasPaginacion(Pageable page);
}
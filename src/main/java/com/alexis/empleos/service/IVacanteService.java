package com.alexis.empleos.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alexis.empleos.model.Vacante;

public interface IVacanteService {
	public List<Vacante> obtenerVacantes();
	public Vacante buscarVacantePorId(int idVacante);
	public void guardarVacante(Vacante vacante);
	public List<Vacante> buscarVacantesPorDestacadoAndEstatus(Integer destacado, String estatus);
	public void eliminarVacantePorId(Integer id);
	public List<Vacante> buscarByExample(Example<Vacante> vacante);
	public Page<Vacante>buscarVacantesPaginacion(Pageable page);
}

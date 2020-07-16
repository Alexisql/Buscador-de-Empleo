package com.alexis.empleos.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alexis.empleos.model.Vacante;
import com.alexis.empleos.repository.VacanteRepository;
import com.alexis.empleos.service.IVacanteService;

@Service
@Primary //Se usa cuando se tiene mas de dos implementaciones de una interface, la cual es un service (Debido a que los service manejan el patron singleton)
public class VacantesServiceJpa implements IVacanteService {

	@Autowired
	private VacanteRepository vacanteRepo;
	
	@Override
	public List<Vacante> obtenerVacantes() {
		return vacanteRepo.findAll();
	}

	@Override
	public Vacante buscarVacantePorId(int id) {
		Vacante vacante = null;
		Optional<Vacante> option = vacanteRepo.findById(id);
		if(option.isPresent()) {
			vacante = option.get();
		}
		return vacante;
	}

	@Override
	public void guardarVacante(Vacante vacante) {
		vacanteRepo.save(vacante);
	}

	@Override
	public List<Vacante> buscarVacantesPorDestacadoAndEstatus(Integer destacado, String estatus) {
		return vacanteRepo.findByDestacadoAndEstatusOrderByIdDesc(destacado, estatus);
	}

	@Override
	public void eliminarVacantePorId(Integer id) {
		vacanteRepo.deleteById(id);
	}

	@Override
	public List<Vacante> buscarByExample(Example<Vacante> vacante) {
		return vacanteRepo.findAll(vacante);
	}

	@Override
	public Page<Vacante> buscarVacantesPaginacion(Pageable page) {
		return vacanteRepo.findAll(page);
	}

	

	
}

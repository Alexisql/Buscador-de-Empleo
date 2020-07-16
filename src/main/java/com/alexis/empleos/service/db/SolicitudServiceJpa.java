package com.alexis.empleos.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexis.empleos.model.Solicitud;
import com.alexis.empleos.repository.SolicitudRepository;
import com.alexis.empleos.service.ISolicitudesService;

@Service
public class SolicitudServiceJpa implements ISolicitudesService {

	@Autowired
	private SolicitudRepository solicitudRepo;
	
	@Override
	public void guardarSolicitud(Solicitud solicitud) {
		solicitudRepo.save(solicitud);
	}

	@Override
	public void eliminarSolicitud(Integer idSolicitud) {
		solicitudRepo.deleteById(idSolicitud);
	}

	@Override
	public Solicitud buscarSolicitudPorId(Integer idSolicitud) {
		Solicitud solicitud = null;
		Optional<Solicitud> option = solicitudRepo.findById(idSolicitud);
		if(option.isPresent()) {
			solicitud = option.get();
		}
		return solicitud;
	}

	@Override
	public List<Solicitud> obtenerSolicitudes() {
		return solicitudRepo.findAll();
	}

}

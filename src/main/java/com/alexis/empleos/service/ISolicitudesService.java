package com.alexis.empleos.service;

import java.util.List;

import com.alexis.empleos.model.Solicitud;

public interface ISolicitudesService {

	public void guardarSolicitud(Solicitud solicitud);

	public void eliminarSolicitud(Integer idSolicitud);

	public List<Solicitud> obtenerSolicitudes();

	public Solicitud buscarSolicitudPorId(Integer idSolicitud);
}

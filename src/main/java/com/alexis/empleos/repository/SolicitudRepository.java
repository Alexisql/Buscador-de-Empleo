package com.alexis.empleos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexis.empleos.model.Solicitud;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

}

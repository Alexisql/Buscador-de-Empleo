package com.alexis.empleos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexis.empleos.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	public Usuario findByUsername(String userName);
}

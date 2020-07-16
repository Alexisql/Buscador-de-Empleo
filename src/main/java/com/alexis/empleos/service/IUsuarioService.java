package com.alexis.empleos.service;

import java.util.List;

import com.alexis.empleos.model.Usuario;

public interface IUsuarioService {
	
	public void guardarUsuario(Usuario usuario);
	public List<Usuario> obtenerUsuarios();
	public Usuario buscarUsuarioPorId(Integer idUsuario);
	public void eliminarUsuario(Integer idUsuario);
	public Usuario buscarUsuarioPorNombre(String name);

}

package com.alexis.empleos.service.db;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexis.empleos.model.Perfil;
import com.alexis.empleos.model.Usuario;
import com.alexis.empleos.repository.UsuarioRepository;
import com.alexis.empleos.service.IUsuarioService;

@Service
public class UsuariosServiceJpa implements IUsuarioService{

	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Override
	public void guardarUsuario(Usuario usuario) {
		usuario.setEstatus(1);
		Perfil perfil = new Perfil();
		perfil.setId(3);
		usuario.getPerfiles().add(perfil);
		usuario.setFechaRegistro(new Date());
		usuarioRepo.save(usuario);
	}

	@Override
	public List<Usuario> obtenerUsuarios() {
		return usuarioRepo.findAll();
	}

	@Override
	public Usuario buscarUsuarioPorId(Integer idUsuario) {
		Usuario usuario = null;
		Optional<Usuario> option = usuarioRepo.findById(idUsuario);
		if(option.isPresent()) {
			usuario = option.get();
		}
		return usuario;
	}

	@Override
	public void eliminarUsuario(Integer idUsuario) {
		usuarioRepo.deleteById(idUsuario);
	}

	@Override
	public Usuario buscarUsuarioPorNombre(String name) {
		return usuarioRepo.findByUsername(name);
	}

}

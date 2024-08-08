package com.daviof.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daviof.minhasfinancas.exception.ErroAutenticacao;
import com.daviof.minhasfinancas.exception.RegraNegocioException;
import com.daviof.minhasfinancas.model.entity.Usuario;
import com.daviof.minhasfinancas.model.repository.UsuarioRepository;
import com.daviof.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {

		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuario nao encontrado para o email informado");
		}
		
		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha invalida");
		}
		
		return usuario.get();
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		
		boolean existe = repository.existsByEmail(email);
		
		if(existe) {
			throw new RegraNegocioException("Já existe usuário cadastrtao com este email");
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return repository.findById(id);
	}

}

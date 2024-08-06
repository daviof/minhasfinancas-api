package com.daviof.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.daviof.minhasfinancas.exception.ErroAutenticacao;
import com.daviof.minhasfinancas.exception.RegraNegocioException;
import com.daviof.minhasfinancas.model.entity.Usuario;
import com.daviof.minhasfinancas.model.repository.UsuarioRepository;
import com.daviof.minhasfinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	@Before
	public void setUp() {
		
//		service = Mockito.spy(UsuarioServiceImpl.class);
//		
//		service = new UsuarioServiceImpl(repository);
		
	}
	
	@Test(expected = Test.None.class)
	public void deveSalvarUsuario() {

		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().email("email@email.com").senha("senha").nome("nome").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvao = service.salvarUsuario(new Usuario());
		
		Assertions.assertThat(usuarioSalvao).isNotNull();
		Assertions.assertThat(usuarioSalvao.getNome()).isEqualTo("nome");
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvaUsuarioComEmailjaCadastrado() {

		//cenario
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		
		//acao
		service.salvarUsuario(usuario);
		
		//verificacao
		Mockito.verify(repository, Mockito.never()).save(usuario);
		
	}
	
	@Test (expected = Test.None.class)
	public void deveAutencticaUmUsuarioComSucesso() {
		
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email("email").senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email, senha);
				
		//verificacao
		Assertions.assertThat(result).isNotNull();
		
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//acao
		Throwable exception =  Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha"));
		
		//verificacao
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuario nao encontrado para o email informado");
		
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		
		//cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
		Throwable exception =  Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha invalida");
		
	}
	
	@Test (expected = Test.None.class)
	public void deveValidarEmail() {
		
		UsuarioRepository usuarioRepositoryMock =  Mockito.mock(UsuarioRepository.class);
		
		//cenario
		repository.deleteAll();
		
		//acao
		service.validarEmail("email@email.com");
		
	}
	
	@Test (expected = RegraNegocioException.class)
	public void deveLancaErroAoValidarEmailQuandoExistirEmailCadastrado() {
		
		//cenario
		Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		repository.save(usuario);
		
		//acao
		service.validarEmail("email@email.com");
		
	}
}

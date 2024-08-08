package com.daviof.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daviof.minhasfinancas.exception.RegraNegocioException;
import com.daviof.minhasfinancas.model.entity.Lancamento;
import com.daviof.minhasfinancas.model.enums.StatusLancamento;
import com.daviof.minhasfinancas.model.enums.TipoLancamento;
import com.daviof.minhasfinancas.model.repository.LancamentoRepository;
import com.daviof.minhasfinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return repository.save(lancamento);
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
		
	}

	@Override
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		
		Example example = Example.of(lancamentoFiltro, ExampleMatcher.matching().withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		return null;
	}

	@Override
	public void atualizarStatus(Lancamento lancamentom, StatusLancamento status) {

		lancamentom.setStatus(status);
		atualizar(lancamentom);
		
	}

	@Override
	public void validar(Lancamento lancamento) {

		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			
			throw new RegraNegocioException("Informe descricao valida");
		}
		
		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um usuario");
		}
		
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		return repository.findById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoPorUsuario(Long id) {
		BigDecimal receita =  repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		BigDecimal despesa =  repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
		
		if (receita == null) {
			receita = BigDecimal.ZERO;
		}
		if (despesa == null) {
			despesa = BigDecimal.ZERO;
		}
		
		
		return receita.subtract(despesa);
	}
	

}

package com.daviof.minhasfinancas.service;

import java.util.List;

import com.daviof.minhasfinancas.model.entity.Lancamento;
import com.daviof.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamentom, StatusLancamento status);
	
	void validar(Lancamento lancamento);
}

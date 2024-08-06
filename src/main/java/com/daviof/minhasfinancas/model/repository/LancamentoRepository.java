package com.daviof.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daviof.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}

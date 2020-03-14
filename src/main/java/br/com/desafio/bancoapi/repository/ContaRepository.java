package br.com.desafio.bancoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.desafio.bancoapi.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {

}

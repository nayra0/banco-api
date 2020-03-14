package br.com.desafio.bancoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.desafio.bancoapi.model.Agencia;

public interface AgenciaRepository extends JpaRepository<Agencia, Long> {

}

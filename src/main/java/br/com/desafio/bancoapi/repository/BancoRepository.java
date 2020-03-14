package br.com.desafio.bancoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.desafio.bancoapi.model.Banco;

public interface BancoRepository extends JpaRepository<Banco, Long> {

}

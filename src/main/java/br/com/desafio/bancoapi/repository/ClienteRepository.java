package br.com.desafio.bancoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.desafio.bancoapi.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}

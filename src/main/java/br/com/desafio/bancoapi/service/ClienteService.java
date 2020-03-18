package br.com.desafio.bancoapi.service;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import br.com.desafio.bancoapi.model.Cliente;
import br.com.desafio.bancoapi.repository.ClienteRepository;

@Service
public class ClienteService {

  @Autowired
  ClienteRepository clienteRepository;

  public Cliente atualizar(Long id, br.com.desafio.bancoapi.model.Cliente Cliente) {
    Cliente ClienteSalvo = buscarPeloId(id);

    BeanUtils.copyProperties(Cliente, ClienteSalvo, "id");
    return clienteRepository.save(ClienteSalvo);
  }

  private Cliente buscarPeloId(Long id) {
    Cliente ClienteSalvo =
        clienteRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
    return ClienteSalvo;
  }


}

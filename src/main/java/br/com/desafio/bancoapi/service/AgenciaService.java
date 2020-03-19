package br.com.desafio.bancoapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import br.com.desafio.bancoapi.model.Agencia;
import br.com.desafio.bancoapi.repository.AgenciaRepository;

@Service
public class AgenciaService {

  @Autowired
  AgenciaRepository AgenciaRepository;

  public Agencia atualizar(Long id, Agencia Agencia) {
    Agencia agenciaSalva = buscarPeloId(id);

    BeanUtils.copyProperties(Agencia, agenciaSalva, "id");
    return AgenciaRepository.save(agenciaSalva);
  }

   Agencia buscarPeloId(Long id) {
    Agencia agenciaSalva =
        AgenciaRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException("Agência com id: " + id + " não encontrada", 1));
    return agenciaSalva;
  }


}

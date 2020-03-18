package br.com.desafio.bancoapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import br.com.desafio.bancoapi.model.Banco;
import br.com.desafio.bancoapi.repository.BancoRepository;

@Service
public class BancoService {

  @Autowired
  BancoRepository bancoRepository;

  public Banco atualizar(Long id, Banco banco) {
    Banco bancoSalvo = buscarPeloId(id);

    BeanUtils.copyProperties(banco, bancoSalvo, "id");
    return bancoRepository.save(bancoSalvo);
  }

  private Banco buscarPeloId(Long id) {
    Banco bancoSalvo =
        bancoRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
    return bancoSalvo;
  }


}

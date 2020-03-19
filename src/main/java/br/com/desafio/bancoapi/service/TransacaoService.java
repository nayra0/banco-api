package br.com.desafio.bancoapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import br.com.desafio.bancoapi.model.Transacao;
import br.com.desafio.bancoapi.repository.TransacaoRepository;

@Service
public class TransacaoService {

  @Autowired
  TransacaoRepository transacaoRepository;

  public Transacao atualizar(Long id, Transacao Transacao) {
    Transacao transacaoSalva = buscarPeloId(id);

    BeanUtils.copyProperties(Transacao, transacaoSalva, "id");
    return transacaoRepository.save(transacaoSalva);
  }

  public Transacao buscarPeloId(Long id) {
    Transacao transacaoSalva =
        transacaoRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException("Transação com id: " + id + " não encontrada", 1));
    return transacaoSalva;
  }


}

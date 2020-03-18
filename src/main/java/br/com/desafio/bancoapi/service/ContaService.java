package br.com.desafio.bancoapi.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import br.com.desafio.bancoapi.enums.TipoTransacao;
import br.com.desafio.bancoapi.model.Conta;
import br.com.desafio.bancoapi.model.Transacao;
import br.com.desafio.bancoapi.repository.ContaRepository;
import br.com.desafio.bancoapi.repository.TransacaoRepository;
import br.com.desafio.bancoapi.service.exception.SaldoInsuficienteException;

@Service
public class ContaService {

  @Autowired
  ContaRepository contaRepository;

  @Autowired
  TransacaoRepository transacaoRepository;

  public Conta atualizar(Long id, Conta conta) {
    Conta contaSalva = buscarPeloId(id);

    BeanUtils.copyProperties(conta, contaSalva, "id");
    return contaRepository.save(contaSalva);
  }

  private Conta buscarPeloId(Long id) {
    Conta contaSalva =
        contaRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
    return contaSalva;
  }

  public Transacao transferirDinheiro(Conta contaOrigem, Conta contaDestino, BigDecimal valor,
      LocalDateTime dataHora) {
    if (contaOrigem.getSaldo() == null) {
      contaOrigem.setSaldo(BigDecimal.ZERO);
    }

    if (contaDestino.getSaldo() == null) {
      contaDestino.setSaldo(BigDecimal.ZERO);
    }

    if (contaOrigem.getSaldo().compareTo(valor) >= 0) {
      Transacao transacaoTranferencia = new Transacao();
      transacaoTranferencia.setDataHora(dataHora);
      transacaoTranferencia.setTipoTransacao(TipoTransacao.TRANSFERENCIA);
      transacaoTranferencia.setValor(valor);
      transacaoTranferencia.setContaOrigem(contaOrigem);
      transacaoTranferencia.setContaDestino(contaDestino);

      transacaoRepository.save(transacaoTranferencia);

      if (contaOrigem.getTransacoes() == null) {
        contaOrigem.setTransacoes(new ArrayList<>());
      }
      contaOrigem.getTransacoes().add(transacaoTranferencia);
      contaRepository.save(contaOrigem);

      if (contaDestino.getTransacoes() == null) {
        contaDestino.setTransacoes(new ArrayList<>());
      }
      contaDestino.getTransacoes().add(transacaoTranferencia);
      contaRepository.save(contaDestino);

      return transacaoTranferencia;
    }

    StringBuilder mensagem = mensagemSaldoInsuficiente(contaOrigem);
    throw new SaldoInsuficienteException(mensagem.toString());

  }

  public Transacao sacarDinheiro(Conta conta, BigDecimal valor) {
    if (conta.getSaldo() == null) {
      conta.setSaldo(BigDecimal.ZERO);
    }

    if (conta.getSaldo().compareTo(valor) >= 0) {
      conta.setSaldo(conta.getSaldo().subtract(valor));

      Transacao transacaoSaque = new Transacao();
      transacaoSaque.setDataHora(LocalDateTime.now());
      transacaoSaque.setTipoTransacao(TipoTransacao.SAQUE);
      transacaoSaque.setValor(valor);
      transacaoRepository.save(transacaoSaque);

      if (conta.getTransacoes() == null) {
        conta.setTransacoes(new ArrayList<>());
      }

      conta.getTransacoes().add(transacaoSaque);
      contaRepository.save(conta);

      return transacaoSaque;
    }

    StringBuilder mensagem = mensagemSaldoInsuficiente(conta);
    throw new SaldoInsuficienteException(mensagem.toString());
  }


  public Transacao depositarDinheiro(Conta conta, BigDecimal valor, LocalDateTime dataHora) {
    if (conta.getSaldo() == null) {
      conta.setSaldo(BigDecimal.ZERO);
    }

    conta.setSaldo(conta.getSaldo().add(valor));

    Transacao transacaoDeposito = new Transacao();
    transacaoDeposito.setDataHora(dataHora);
    transacaoDeposito.setTipoTransacao(TipoTransacao.DEPOSITO);
    transacaoDeposito.setValor(valor);
    transacaoRepository.save(transacaoDeposito);

    if (conta.getTransacoes() == null) {
      conta.setTransacoes(new ArrayList<>());
    }

    conta.getTransacoes().add(transacaoDeposito);
    contaRepository.save(conta);

    return transacaoDeposito;
  }

  private StringBuilder mensagemSaldoInsuficiente(Conta conta) {
    StringBuilder mensagem = new StringBuilder();
    mensagem.append("Conta: ");
    mensagem.append(conta.getNumero());
    mensagem.append("/");
    mensagem.append(conta.getDigito());
    mensagem.append(" Agência: ");
    mensagem.append(conta.getAgencia().getCodigo());
    mensagem.append("/");
    mensagem.append(conta.getAgencia().getDigito());
    mensagem.append(" Banco: ");
    mensagem.append(conta.getAgencia().getBanco().getNome());
    mensagem.append(" Não possue saldo suficiente para realizar essa operação!");
    return mensagem;
  }

}

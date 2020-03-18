package br.com.desafio.bancoapi.service.exception;

public class SaldoInsuficienteException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public SaldoInsuficienteException(String mensagem) {
    super(mensagem);
  }

}

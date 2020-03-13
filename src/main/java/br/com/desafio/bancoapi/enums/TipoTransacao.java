package br.com.desafio.bancoapi.enums;

public enum TipoTransacao {

	SAQUE("Saque"), DEPOSITO("Depósito"), TRANSFERENCIA("Transferência");

	private String descricao;

	private TipoTransacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}

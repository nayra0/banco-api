package br.com.desafio.bancoapi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import br.com.desafio.bancoapi.enums.TipoTransacao;
import br.com.desafio.bancoapi.interfaces.Entidade;

@Entity
public class Transacao implements Entidade {

  @Id
  @SequenceGenerator(name = "transacao_seq", sequenceName = "transacao_seq", initialValue = 1,
      allocationSize = 1)
  @GeneratedValue(generator = "transacao_seq")
  private Long id;
  @Column(name = "data_hora", columnDefinition = "TIMESTAMP")
  @NotNull
  @JsonFormat(pattern =  "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime dataHora;
  @Digits(integer = 12, fraction = 6)
  @NotNull
  private BigDecimal valor;
  @Enumerated(EnumType.STRING)
  @NotNull
  private TipoTransacao tipoTransacao;
  @ManyToOne
  private Conta contaOrigem;
  @ManyToOne
  private Conta contaDestino;

  public Transacao() {}

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public void setDataHora(LocalDateTime dataHora) {
    this.dataHora = dataHora;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public TipoTransacao getTipoTransacao() {
    return tipoTransacao;
  }

  public void setTipoTransacao(TipoTransacao tipoTransacao) {
    this.tipoTransacao = tipoTransacao;
  }

  public Conta getContaOrigem() {
    return contaOrigem;
  }

  public void setContaOrigem(Conta contaOrigem) {
    this.contaOrigem = contaOrigem;
  }

  public Conta getContaDestino() {
    return contaDestino;
  }

  public void setContaDestino(Conta contaDestino) {
    this.contaDestino = contaDestino;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Transacao other = (Transacao) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}

package br.com.desafio.bancoapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import br.com.desafio.bancoapi.interfaces.Entidade;

@Entity
public class Agencia implements Entidade {

  @Id
  @SequenceGenerator(name = "agencia_seq", sequenceName = "agencia_seq", initialValue = 1,
      allocationSize = 1)
  @GeneratedValue(generator = "agencia_seq")
  private Long id;
  @NotEmpty
  private String codigo;
  private String digito;
  @NotNull
  @ManyToOne
  @JoinColumn(name = "banco_id")
  private Banco banco;

  public Agencia() {

  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getDigito() {
    return digito;
  }

  public void setDigito(String digito) {
    this.digito = digito;
  }

  public Banco getBanco() {
    return banco;
  }

  public void setBanco(Banco banco) {
    this.banco = banco;
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
    Agencia other = (Agencia) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}

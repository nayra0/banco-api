package br.com.desafio.bancoapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;
import br.com.desafio.bancoapi.interfaces.Entidade;

@Entity
public class Banco implements Entidade{

  @Id
  @SequenceGenerator(name = "banco_seq", sequenceName = "banco_seq", initialValue = 1,
      allocationSize = 1)
  @GeneratedValue(generator = "banco_seq")
  private Long id;
  @NotEmpty
  private String nome;
  @NotEmpty
  private String codigo;

  public Banco() {}

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
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
    Banco other = (Banco) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}

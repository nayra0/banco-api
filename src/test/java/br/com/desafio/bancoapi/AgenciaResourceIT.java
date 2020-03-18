package br.com.desafio.bancoapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import br.com.desafio.bancoapi.model.Agencia;
import br.com.desafio.bancoapi.model.Banco;
import br.com.desafio.bancoapi.repository.AgenciaRepository;
import br.com.desafio.bancoapi.repository.BancoRepository;
import io.restassured.http.ContentType;

public class AgenciaResourceIT extends AbstractResourceTest {

  @Autowired
  private AgenciaRepository agenciaRepository;

  @Autowired
  private BancoRepository bancoRepository;

  private Agencia agenciaExistente;

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @Override
  public String obterBaseUri() {
    return "/agencias";
  }

  @Override
  public String obterjsonCadastrados() {
    return "/json/agencias.json";
  }

  @Override
  public String obterJsonCorretoEntidade() {
    return "/json/agencia-correto.json";
  }


  @Test
  public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRegistroExistente() {
    given().pathParam("id", agenciaExistente.getId()).accept(ContentType.JSON).when().get("/{id}")
        .then().statusCode(HttpStatus.OK.value())
        .body("digito", equalTo(agenciaExistente.getDigito())).and()
        .body("codigo", equalTo(agenciaExistente.getCodigo()));
  }

  @Override
  public void prepararDados() {
    Banco banco = new Banco();
    banco.setCodigo("1");
    banco.setNome("BB");
    bancoRepository.save(banco);

    Agencia agencia = new Agencia();
    agencia.setCodigo("1");
    agencia.setDigito("x");
    agencia.setBanco(banco);
    agenciaRepository.save(agencia);

    Banco banco2 = new Banco();
    banco2.setCodigo("2");
    banco2.setNome("Caixa");
    bancoRepository.save(banco2);

    agenciaExistente = new Agencia();
    agenciaExistente.setCodigo("2");
    agenciaExistente.setDigito("2");
    agenciaExistente.setBanco(banco2);
    agenciaRepository.save(agenciaExistente);

    setQuantidadeCadastrados((int) agenciaRepository.count());

  }

}

package br.com.desafio.bancoapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import br.com.desafio.bancoapi.model.Banco;
import br.com.desafio.bancoapi.repository.BancoRepository;
import io.restassured.http.ContentType;

@ActiveProfiles("basic-security")
public class BancoResourceIT extends AbstractResourceTest {
  @Autowired
  private BancoRepository bancoRepository;

  private Banco bancoExistente;

  @Override
  public String obterBaseUri() {
    return "/bancos";
  }

  @Override
  public String obterjsonCadastrados() {
    return "/json/bancos.json";
  }

  @Override
  public String obterJsonCorretoEntidade() {
    return "/json/banco-correto.json";
  }

  @Test
  public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRegistroExistente() {
    given().header("Authorization", "Bearer " + getAccessToken())
        .pathParam("id", bancoExistente.getId()).accept(ContentType.JSON).when().get("/{id}").then()
        .statusCode(HttpStatus.OK.value()).body("nome", equalTo(bancoExistente.getNome())).and()
        .body("codigo", equalTo(bancoExistente.getCodigo()));
  }

  @Override
  public void prepararDados() {
    Banco banco = new Banco();
    banco.setCodigo("1");
    banco.setNome("Banco de Teste");
    bancoRepository.save(banco);

    bancoExistente = new Banco();
    bancoExistente.setCodigo("2");
    bancoExistente.setNome("Banco de Teste 2");
    bancoRepository.save(bancoExistente);

    setQuantidadeCadastrados((int) bancoRepository.count());

  }

}

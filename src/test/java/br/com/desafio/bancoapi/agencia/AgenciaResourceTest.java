package br.com.desafio.bancoapi.agencia;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import br.com.desafio.bancoapi.model.Banco;
import br.com.desafio.bancoapi.repository.BancoRepository;
import br.com.desafio.bancoapi.util.DatabaseUtils;
import br.com.desafio.bancoapi.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AgenciaResourceTest {

  private static final Long ID_INEXISTENTE = 100l;

  @LocalServerPort
  private int port;

  @Autowired
  private BancoRepository bancoRepository;

  @Autowired
  private DatabaseUtils databaseUtils;

  private Banco bancoExistente;
  private String jsonCorretoBanco;
  private String jsonBancos;
  private int quantidadeBancosCadastrados;

  @Before
  public void setUp() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.port = port;
    RestAssured.basePath = "/bancos";

    jsonCorretoBanco = ResourceUtils.getContentFromResource("/json/banco-correto.json");
    jsonBancos = ResourceUtils.getContentFromResource("/json/bancos.json");
    databaseUtils.apagarTabelas();
    prepararDados();
  }

  @Test
  public void deveRetornarStatus200_QuandoConsultarBancos() {
    given().accept(ContentType.JSON).when().get().then().statusCode(HttpStatus.OK.value());
  }

  @Test
  public void deveRetornarRespostaEStatus200_QuandoConsultarBancos() {
    Response response = given().get();
    response.then().statusCode(HttpStatus.OK.value());

    String bancos = response.body().asString();
    System.err.println(bancos);
    assertEquals(jsonBancos, bancos);
  }

  @Test
  public void deveRetornarQuantidadeCorretaDeBancos_QuandoConsultarBancos() {
    given().accept(ContentType.JSON).when().get().then().body("",
        hasSize(quantidadeBancosCadastrados));
  }

  @Test
  public void deveRetornarStatus201_QuandoCadastrarBanco() {
    given().body(jsonCorretoBanco).contentType(ContentType.JSON).accept(ContentType.JSON).when()
        .post().then().statusCode(HttpStatus.CREATED.value());
  }

  @Test
  public void deveRetornarRespostaEStatusCorretos_QuandoConsultarBancoExistente() {
    given().pathParam("id", bancoExistente.getId()).accept(ContentType.JSON).when().get("/{id}")
        .then().statusCode(HttpStatus.OK.value()).body("nome", equalTo(bancoExistente.getNome()))
        .and().body("codigo", equalTo(bancoExistente.getCodigo()));
  }

  @Test
  public void deveRetornarStatus404_QuandoConsultarBancoInexistente() {
    given().pathParam("id", ID_INEXISTENTE).accept(ContentType.JSON).when().get("/{id}").then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  private void prepararDados() {
    Banco banco = new Banco();
    banco.setCodigo("1");
    banco.setNome("Banco de Teste");
    bancoRepository.save(banco);

    bancoExistente = new Banco();
    bancoExistente.setCodigo("2");
    bancoExistente.setNome("Banco de Teste 2");
    bancoRepository.save(bancoExistente);

    quantidadeBancosCadastrados = (int) bancoRepository.count();
  }


}

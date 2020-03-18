package br.com.desafio.bancoapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import br.com.desafio.bancoapi.util.DatabaseUtils;
import br.com.desafio.bancoapi.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractResourceTest {

  public static final Long ID_INEXISTENTE = 100l;

  @LocalServerPort
  private int port;

  @Autowired
  private DatabaseUtils databaseUtils;

  private String jsonCorretoEntidade;
  private String jsonCadastrados;
  private int quantidadeCadastrados;

  public void setUp() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.port = port;
    RestAssured.basePath = obterBaseUri();

    this.jsonCadastrados = ResourceUtils.getContentFromResource(obterjsonCadastrados());
    this.jsonCorretoEntidade = ResourceUtils.getContentFromResource(obterJsonCorretoEntidade());

    databaseUtils.apagarTabelas();
    prepararDados();
  }

  public abstract String obterBaseUri();

  public abstract String obterjsonCadastrados();

  public abstract String obterJsonCorretoEntidade();

  public abstract void prepararDados();

  @Test
  public void deveRetornarStatus200_QuandoConsultarRegistros() {
    given().accept(ContentType.JSON).when().get().then().statusCode(HttpStatus.OK.value());
  }

  @Test
  public void deveRetornarRespostaEStatus200_QuandoConsultarRegistros() {
    Response response = given().get();
    response.then().statusCode(HttpStatus.OK.value());

    String registros = response.body().asString();
    System.err.println(registros);
    assertEquals(jsonCadastrados, registros);
  }

  @Test
  public void deveRetornarQuantidadeCorretaDeRegistros_QuandoConsultarRegistros() {
    given().accept(ContentType.JSON).when().get().then().body("", hasSize(quantidadeCadastrados));
  }

  @Test
  public void deveRetornarStatus201_QuandoCadastrarRegistro() {
    given().body(jsonCorretoEntidade).contentType(ContentType.JSON).accept(ContentType.JSON).when()
        .post().then().statusCode(HttpStatus.CREATED.value());
  }

  @Test
  public void deveRetornarStatus404_QuandoConsultarRegistroInexistente() {
    given().pathParam("id", ID_INEXISTENTE).accept(ContentType.JSON).when().get("/{id}").then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getJsonCorretoEntidade() {
    return jsonCorretoEntidade;
  }

  public void setJsonCorretoEntidade(String jsonCorretoEntidade) {
    this.jsonCorretoEntidade = jsonCorretoEntidade;
  }

  public String getJsonCadastrados() {
    return jsonCadastrados;
  }

  public void setJsonCadastrados(String jsonCadastrados) {
    this.jsonCadastrados = jsonCadastrados;
  }

  public int getQuantidadeCadastrados() {
    return quantidadeCadastrados;
  }

  public void setQuantidadeCadastrados(int quantidadeCadastrados) {
    this.quantidadeCadastrados = quantidadeCadastrados;
  }

}

package br.com.desafio.bancoapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
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
  public static final Long ID_EXISTENTE = 1l;

  @LocalServerPort
  private int port;

  @Autowired
  private DatabaseUtils databaseUtils;

  private String jsonCorretoEntidade;
  private String jsonCadastrados;
  private int quantidadeCadastrados;
  private String accessToken;

  @Before
  public void setUp() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.port = port;


    this.jsonCadastrados = ResourceUtils.getContentFromResource(obterjsonCadastrados());
    this.jsonCorretoEntidade = ResourceUtils.getContentFromResource(obterJsonCorretoEntidade());

    databaseUtils.apagarTabelas();
    prepararDados();


    Response response = given().auth().preemptive().basic("cliente", "cliente")
        .contentType("application/x-www-form-urlencoded").log().all().formParam("client", "cliente")
        .formParam("grant_type", "password").formParam("username", "admin")
        .formParam("password", "admin").when().post("/oauth/token");

    JSONObject jsonObject;

    try {
      jsonObject = new JSONObject(response.getBody().asString());

      accessToken = jsonObject.get("access_token").toString();
      // tokenType = jsonObject.get("token_type").toString();

    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    RestAssured.basePath = obterBaseUri();

  }

  @After
  public void setDown() {
    RestAssured.basePath = RestAssured.basePath.replace(obterBaseUri(), "");
  }

  public abstract String obterBaseUri();

  public abstract String obterjsonCadastrados();

  public abstract String obterJsonCorretoEntidade();

  public abstract void prepararDados();

  @Test
  public void deveRetornarStatus200_QuandoConsultarRegistros() {
    given().header("Authorization", "Bearer " + accessToken).when().get().then()
        .statusCode(HttpStatus.OK.value());
  }

  @Test
  public void deveRetornarRespostaEStatus200_QuandoConsultarRegistros() {
    Response response = given().header("Authorization", "Bearer " + accessToken).get();
    response.then().statusCode(HttpStatus.OK.value());

    String registros = response.body().asString();
    System.err.println(registros);
    assertEquals(jsonCadastrados, registros);
  }

  @Test
  public void deveRetornarQuantidadeCorretaDeRegistros_QuandoConsultarRegistros() {
    given().header("Authorization", "Bearer " + accessToken).when().get().then().body("",
        hasSize(quantidadeCadastrados));
  }

  @Test
  public void deveRetornarStatus201_QuandoCadastrarRegistro() {
    given().header("Authorization", "Bearer " + accessToken).body(jsonCorretoEntidade)
        .contentType(ContentType.JSON).accept(ContentType.JSON).when().post().then()
        .statusCode(HttpStatus.CREATED.value());
  }

  @Test
  public void deveRetornarStatus404_QuandoConsultarRegistroInexistente() {
    given().header("Authorization", "Bearer " + accessToken).pathParam("id", ID_INEXISTENTE)
        .accept(ContentType.JSON).when().get("/{id}").then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void deveRetornarStatus204_QuandoRemoverRegistro() {
    given().header("Authorization", "Bearer " + accessToken).pathParam("id", ID_EXISTENTE)
        .accept(ContentType.JSON).when().delete("/{id}").then()
        .statusCode(HttpStatus.NO_CONTENT.value());
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

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }



}

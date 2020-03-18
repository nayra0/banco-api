package br.com.desafio.bancoapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import br.com.desafio.bancoapi.model.Cliente;
import br.com.desafio.bancoapi.repository.ClienteRepository;
import io.restassured.http.ContentType;

public class ClienteResourceIT extends AbstractResourceTest {

  @Autowired
  private ClienteRepository clienteRepository;

  private Cliente clienteExistente;

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @Override
  public String obterBaseUri() {
    return "/clientes";
  }

  @Override
  public String obterjsonCadastrados() {
    return "/json/clientes.json";
  }

  @Override
  public String obterJsonCorretoEntidade() {
    return "/json/cliente-correto.json";
  }

  @Test
  public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRegistroExistente() {
    given().pathParam("id", clienteExistente.getId()).accept(ContentType.JSON).when().get("/{id}")
        .then().statusCode(HttpStatus.OK.value()).body("nome", equalTo(clienteExistente.getNome()))
        .and().body("cpf", equalTo(clienteExistente.getCpf()));
  }

  @Override
  public void prepararDados() {
    Cliente cliente = new Cliente();
    cliente.setNome("Cliente 1");
    cliente.setCpf("79074897096");
    clienteRepository.save(cliente);

    clienteExistente = new Cliente();
    clienteExistente.setNome("Cliente 2");
    clienteExistente.setCpf("50262103095");
    clienteRepository.save(clienteExistente);

    setQuantidadeCadastrados((int) clienteRepository.count());

  }

}

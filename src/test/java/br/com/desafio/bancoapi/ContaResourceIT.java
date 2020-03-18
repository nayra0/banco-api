package br.com.desafio.bancoapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import br.com.desafio.bancoapi.model.Agencia;
import br.com.desafio.bancoapi.model.Banco;
import br.com.desafio.bancoapi.model.Cliente;
import br.com.desafio.bancoapi.model.Conta;
import br.com.desafio.bancoapi.repository.AgenciaRepository;
import br.com.desafio.bancoapi.repository.BancoRepository;
import br.com.desafio.bancoapi.repository.ClienteRepository;
import br.com.desafio.bancoapi.repository.ContaRepository;
import io.restassured.http.ContentType;

public class ContaResourceIT extends AbstractResourceTest {

  @Autowired
  private ContaRepository contaRepository;

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private BancoRepository bancoRepository;

  @Autowired
  private AgenciaRepository agenciaRepository;

  private Conta contaExistente;

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @Override
  public String obterBaseUri() {
    return "/contas";
  }

  @Override
  public String obterjsonCadastrados() {
    return "/json/contas.json";
  }

  @Override
  public String obterJsonCorretoEntidade() {
    return "/json/conta-correto.json";
  }

  @Test
  public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRegistroExistente() {
    given().pathParam("id", contaExistente.getId()).accept(ContentType.JSON).when().get("/{id}")
        .then().statusCode(HttpStatus.OK.value())
        .body("numero", equalTo(contaExistente.getNumero())).and()
        .body("digito", equalTo(contaExistente.getDigito())).and()
        .body("saldo", equalTo(contaExistente.getSaldo().floatValue())).and()
        .body("ativa", equalTo(contaExistente.isAtiva())).and()
        .body("titular.nome", equalTo(contaExistente.getTitular().getNome()))
        .body("titular.cpf", equalTo(contaExistente.getTitular().getCpf()));
  }

  @Override
  public void prepararDados() {
    Cliente titular1 = new Cliente();
    titular1.setNome("Titular conta 1");
    titular1.setCpf("93398882005");
    clienteRepository.save(titular1);

    Banco banco = new Banco();
    banco.setCodigo("1");
    banco.setNome("BB");
    bancoRepository.save(banco);

    Agencia agencia = new Agencia();
    agencia.setCodigo("1");
    agencia.setDigito("x");
    agencia.setBanco(banco);
    agenciaRepository.save(agencia);

    Conta conta = new Conta();
    conta.setNumero("1");
    conta.setDigito("1");
    conta.setSaldo(new BigDecimal(100.0));
    conta.setAtiva(true);
    conta.setTitular(titular1);
    conta.setAgencia(agencia);
    contaRepository.save(conta);

    Cliente titular2 = new Cliente();
    titular2.setNome("Titular conta 1");
    titular2.setCpf("62143827083");
    clienteRepository.save(titular2);

    Banco banco2 = new Banco();
    banco2.setCodigo("2");
    banco2.setNome("Caixa");
    bancoRepository.save(banco2);

    Agencia agencia2 = new Agencia();
    agencia2.setCodigo("2");
    agencia2.setDigito("x");
    agencia2.setBanco(banco2);
    agenciaRepository.save(agencia2);

    contaExistente = new Conta();
    contaExistente.setNumero("2");
    contaExistente.setDigito("3");
    contaExistente.setSaldo(new BigDecimal(50.0));
    contaExistente.setAtiva(true);
    contaExistente.setTitular(titular2);
    contaExistente.setAgencia(agencia2);
    contaRepository.save(contaExistente);

    setQuantidadeCadastrados((int) contaRepository.count());

  }

}

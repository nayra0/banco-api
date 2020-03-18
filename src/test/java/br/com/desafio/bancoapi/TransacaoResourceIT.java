package br.com.desafio.bancoapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import br.com.desafio.bancoapi.model.Agencia;
import br.com.desafio.bancoapi.model.Banco;
import br.com.desafio.bancoapi.model.Cliente;
import br.com.desafio.bancoapi.model.Conta;
import br.com.desafio.bancoapi.model.Transacao;
import br.com.desafio.bancoapi.repository.AgenciaRepository;
import br.com.desafio.bancoapi.repository.BancoRepository;
import br.com.desafio.bancoapi.repository.ClienteRepository;
import br.com.desafio.bancoapi.repository.ContaRepository;
import br.com.desafio.bancoapi.service.ContaService;
import io.restassured.http.ContentType;

public class TransacaoResourceIT extends AbstractResourceTest {

  @Autowired
  private ContaRepository contaRepository;

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private BancoRepository bancoRepository;

  @Autowired
  private AgenciaRepository agenciaRepository;

  @Autowired
  private ContaService contaService;

  private Transacao transacaoExistente;

  @Override
  @Before
  public void setUp() {
    super.setUp();
  }

  @Override
  public String obterBaseUri() {
    return "/transacoes";
  }

  @Override
  public String obterjsonCadastrados() {
    return "/json/transacoes.json";
  }

  @Override
  public String obterJsonCorretoEntidade() {
    return "/json/transacao-correto.json";
  }

  @Test
  public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRegistroExistente() {
    given().pathParam("id", transacaoExistente.getId()).accept(ContentType.JSON).when().get("/{id}")
        .then().statusCode(HttpStatus.OK.value())
        .body("tipoTransacao", equalTo(transacaoExistente.getTipoTransacao().name())).and()
        .body("valor", equalTo(transacaoExistente.getValor().floatValue())).and()
        .body("contaOrigem.id", equalTo(transacaoExistente.getContaOrigem().getId().intValue())).and()
        .body("contaDestino.id", equalTo(transacaoExistente.getContaDestino().getId().intValue()));
  }

  @Override
  public void prepararDados() {
    String DataHorastr = "01/03/2010 12:30:00";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime dattaHora = LocalDateTime.parse(DataHorastr, formatter);
    
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
    conta.setAtiva(true);
    conta.setTitular(titular1);
    conta.setAgencia(agencia);
    contaRepository.save(conta);

    contaService.depositarDinheiro(conta, new BigDecimal(100.0), dattaHora);

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

    Conta conta2 = new Conta();
    conta2.setNumero("2");
    conta2.setDigito("3");
    conta2.setAtiva(true);
    conta2.setTitular(titular2);
    conta2.setAgencia(agencia2);
    contaRepository.save(conta2);
    
    transacaoExistente = contaService.transferirDinheiro(conta, conta2, new BigDecimal(50.0), dattaHora);

    setQuantidadeCadastrados((int) contaRepository.count());

  }

}

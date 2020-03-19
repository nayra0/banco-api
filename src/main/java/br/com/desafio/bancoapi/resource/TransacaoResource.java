package br.com.desafio.bancoapi.resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.com.desafio.bancoapi.event.RecursoCriadoEvent;
import br.com.desafio.bancoapi.exceptionhandler.Erro;
import br.com.desafio.bancoapi.model.Conta;
import br.com.desafio.bancoapi.model.Transacao;
import br.com.desafio.bancoapi.repository.TransacaoRepository;
import br.com.desafio.bancoapi.service.ContaService;
import br.com.desafio.bancoapi.service.TransacaoService;
import br.com.desafio.bancoapi.service.exception.SaldoInsuficienteException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transacoes")
@Api(value = "API REST Transações")
public class TransacaoResource {

  @Autowired
  private TransacaoRepository transacaoRepository;

  @Autowired
  private TransacaoService transacaoService;

  @Autowired
  private ContaService contaService;

  @Autowired
  private ApplicationEventPublisher publisher;

  @Autowired
  private MessageSource messageSource;

  @GetMapping
  @ApiOperation(value = "Retorna a lista de transações")
  public List<Transacao> listar() {
    return transacaoRepository.findAll();
  }

  @PostMapping
  @ApiOperation(value = "Salva uma transação")
  public ResponseEntity<Transacao> criar(@Valid @RequestBody Transacao transacao,
      HttpServletResponse response) {
    Transacao novaTransacao = transacaoRepository.save(transacao);

    publisher.publishEvent(new RecursoCriadoEvent(this, response, novaTransacao.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(novaTransacao);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Retorna uma transação")
  public ResponseEntity<Transacao> buscarPeloCodigo(@PathVariable Long id) {
    return transacaoRepository.findById(id).map(transacao -> ResponseEntity.ok(transacao))
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation(value = "Deleta uma transação")
  public void remover(@PathVariable Long id) {
    transacaoRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Atualiza uma transação")
  public ResponseEntity<Transacao> atualizar(@PathVariable Long id,
      @Valid @RequestBody Transacao transacao) {
    Transacao transacaoSalva = transacaoService.atualizar(id, transacao);
    return ResponseEntity.ok(transacaoSalva);
  }

  @ExceptionHandler({SaldoInsuficienteException.class})
  public ResponseEntity<Object> handleSaldoInsuficienteExceptionException(
      SaldoInsuficienteException ex) {
    String mensagemUsuario = messageSource.getMessage("transacao.saldo-insuficiente", null,
        LocaleContextHolder.getLocale());
    String mensagemDesenvolvedor = ex.toString();
    List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

    return ResponseEntity.badRequest().body(erros);
  }

  @PostMapping("/contas/{id}/sacar")
  @ApiOperation(value = "Sacar dinheiro da conta")
  public ResponseEntity<Transacao> sacar(@PathVariable Long id,
      @RequestBody Map<String, String> body) {

    BigDecimal valor = new BigDecimal(body.get("valor"));
    Conta conta = contaService.buscarPeloId(id);
    Transacao transacao = contaService.sacarDinheiro(conta, valor);
    return ResponseEntity.status(HttpStatus.CREATED).body(transacao);
  }

  @PostMapping("/contas/{id}/depositar")
  @ApiOperation(value = "Depositar dinheiro da conta")
  public ResponseEntity<Transacao> depositar(@PathVariable Long id,
      @RequestBody Map<String, String> body) {

    BigDecimal valor = new BigDecimal(body.get("valor"));
    Conta conta = contaService.buscarPeloId(id);
    Transacao transacao = contaService.depositarDinheiro(conta, valor, LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.CREATED).body(transacao);
  }

  @PostMapping("/contas/{idContaOrigem}/transferir/{idContaDestino}")
  @ApiOperation(value = "Tranferir dinheiro entre contas")
  public ResponseEntity<Transacao> transferir(@PathVariable Long idContaOrigem,
      @PathVariable Long idContaDestino, @RequestBody Map<String, String> body) {

    BigDecimal valor = new BigDecimal(body.get("valor"));
    Conta contaOrigem = contaService.buscarPeloId(idContaOrigem);
    Conta contaDestino = contaService.buscarPeloId(idContaDestino);
    Transacao transacao =
        contaService.transferirDinheiro(contaOrigem, contaDestino, valor, LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.CREATED).body(transacao);
  }


  @PostMapping("/contas/{idConta}")
  @ApiOperation(value = "Exibir transações da conta")
  public ResponseEntity<List<Transacao>> extrato(@PathVariable Long idConta) {
    Conta conta = contaService.buscarPeloId(idConta);
    return ResponseEntity.status(HttpStatus.CREATED).body(conta.getTransacoes());
  }

}

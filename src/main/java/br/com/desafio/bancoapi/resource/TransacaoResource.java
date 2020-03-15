package br.com.desafio.bancoapi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.bancoapi.event.RecursoCriadoEvent;
import br.com.desafio.bancoapi.model.Transacao;
import br.com.desafio.bancoapi.repository.TransacaoRepository;
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
  private ApplicationEventPublisher publisher;

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
    Transacao transacaoSalva = transacaoRepository.save(transacao);
    return ResponseEntity.ok(transacaoSalva);
  }
}

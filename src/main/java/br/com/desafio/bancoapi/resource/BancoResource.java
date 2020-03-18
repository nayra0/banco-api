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
import br.com.desafio.bancoapi.model.Banco;
import br.com.desafio.bancoapi.repository.BancoRepository;
import br.com.desafio.bancoapi.service.BancoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/bancos")
@Api(value = "API REST Bancos")
public class BancoResource {

  @Autowired
  private BancoRepository bancoRepository;

  @Autowired
  private BancoService bancoService;

  @Autowired
  private ApplicationEventPublisher publisher;

  @GetMapping
  @ApiOperation(value = "Retorna a lista de bancos")
  public List<Banco> listar() {
    return bancoRepository.findAll();
  }

  @PostMapping
  @ApiOperation(value = "Salva um banco")
  public ResponseEntity<Banco> criar(@Valid @RequestBody Banco banco,
      HttpServletResponse response) {
    Banco novoBanco = bancoRepository.save(banco);

    publisher.publishEvent(new RecursoCriadoEvent(this, response, novoBanco.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(novoBanco);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Retorna um banco")
  public ResponseEntity<Banco> buscarPeloId(@PathVariable Long id) {
    return bancoRepository.findById(id).map(banco -> ResponseEntity.ok(banco))
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation(value = "Deleta um banco")
  public void remover(@PathVariable Long id) {
    bancoRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Atualiza um banco")
  public ResponseEntity<Banco> atualizar(@PathVariable Long id, @Valid @RequestBody Banco banco) {
    Banco bancoSalvo = bancoService.atualizar(id, banco);
    return ResponseEntity.ok(bancoSalvo);
  }

}

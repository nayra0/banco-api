package br.com.desafio.bancoapi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import br.com.desafio.bancoapi.model.Agencia;
import br.com.desafio.bancoapi.repository.AgenciaRepository;

@RestController
@RequestMapping("/agencias")
public class AgenciaResource {

  @Autowired
  private AgenciaRepository agenciaRepository;

  @Autowired
  private ApplicationEventPublisher publisher;

  @GetMapping
  public List<Agencia> listar() {
    return agenciaRepository.findAll();
  }

  @PostMapping
  public ResponseEntity<Agencia> criar(@Valid @RequestBody Agencia agencia,
      HttpServletResponse response) {
    Agencia novaAgencia = agenciaRepository.save(agencia);

    publisher.publishEvent(new RecursoCriadoEvent(this, response, novaAgencia.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(novaAgencia);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Agencia> buscarPeloCodigo(@PathVariable Long id) {
    return agenciaRepository.findById(id).map(agencia -> ResponseEntity.ok(agencia))
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable Long id) {
    agenciaRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Agencia> atualizar(@PathVariable Long id,
      @Valid @RequestBody Agencia agencia) {
    Agencia agenciaSalva = agenciaRepository.save(agencia);
    return ResponseEntity.ok(agenciaSalva);
  }

}

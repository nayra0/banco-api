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
import br.com.desafio.bancoapi.model.Cliente;
import br.com.desafio.bancoapi.repository.ClienteRepository;
import br.com.desafio.bancoapi.service.ClienteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clientes")
@Api(value = "API REST Clientes")
public class ClienteResource {

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private ClienteService clienteService;

  @Autowired
  private ApplicationEventPublisher publisher;

  @GetMapping
  @ApiOperation(value = "Retorna a lista de clientes")
  private List<Cliente> listar() {
    return clienteRepository.findAll();
  }

  @PostMapping
  @ApiOperation(value = "Salva um cliente")
  public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente,
      HttpServletResponse response) {
    Cliente novoCliente = clienteRepository.save(cliente);

    publisher.publishEvent(new RecursoCriadoEvent(this, response, novoCliente.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Retorna um cliente")
  public ResponseEntity<Cliente> buscarPeloId(@PathVariable Long id) {
    return clienteRepository.findById(id).map(cliente -> ResponseEntity.ok(cliente))
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation(value = "Deleta um cliente")
  public void remover(@PathVariable Long id) {
    clienteRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Atualiza um cliente")
  public ResponseEntity<Cliente> atualizar(@PathVariable Long id,
      @Valid @RequestBody Cliente cliente) {
    Cliente clienteSalvo = clienteService.atualizar(id, cliente);
    return ResponseEntity.ok(clienteSalvo);
  }

}

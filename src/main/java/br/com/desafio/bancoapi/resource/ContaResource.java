package br.com.desafio.bancoapi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.bancoapi.event.RecursoCriadoEvent;
import br.com.desafio.bancoapi.model.Conta;
import br.com.desafio.bancoapi.repository.ContaRepository;

@RestController
@RequestMapping("/contas")
public class ContaResource {

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Conta> listar() {
		return contaRepository.findAll();
	}

	@PostMapping
	public ResponseEntity<Conta> criar(@Valid @RequestBody Conta conta, HttpServletResponse response) {
		Conta novaConta = contaRepository.save(conta);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, novaConta.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Conta> buscarPeloCodigo(@PathVariable Long id) {
		return contaRepository.findById(id).map(conta -> ResponseEntity.ok(conta))
				.orElse(ResponseEntity.notFound().build());
	}
}

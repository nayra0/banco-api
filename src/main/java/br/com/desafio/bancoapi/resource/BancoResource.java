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
import br.com.desafio.bancoapi.model.Banco;
import br.com.desafio.bancoapi.repository.BancoRepository;

@RestController
@RequestMapping("/bancos")
public class BancoResource {

	@Autowired
	private BancoRepository bancoRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Banco> listar() {
		return bancoRepository.findAll();
	}

	@PostMapping
	public ResponseEntity<Banco> criar(@Valid @RequestBody Banco banco, HttpServletResponse response) {
		Banco novoBanco = bancoRepository.save(banco);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, novoBanco.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(novoBanco);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Banco> buscarPeloCodigo(@PathVariable Long id) {
		return bancoRepository.findById(id).map(banco -> ResponseEntity.ok(banco))
				.orElse(ResponseEntity.notFound().build());
	}

}

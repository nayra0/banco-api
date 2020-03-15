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
import br.com.desafio.bancoapi.model.Transacao;
import br.com.desafio.bancoapi.repository.TransacaoRepository;

@RestController
@RequestMapping("/transacoes")
public class TransacaoResource {

	@Autowired
	private TransacaoRepository transacaoRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Transacao> listar() {
		return transacaoRepository.findAll();
	}

	@PostMapping
	public ResponseEntity<Transacao> criar(@Valid @RequestBody Transacao transacao, HttpServletResponse response) {
		Transacao novaTransacao = transacaoRepository.save(transacao);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, novaTransacao.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(novaTransacao);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Transacao> buscarPeloCodigo(@PathVariable Long id) {
		return transacaoRepository.findById(id).map(transacao -> ResponseEntity.ok(transacao))
				.orElse(ResponseEntity.notFound().build());
	}
}

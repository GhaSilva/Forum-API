package br.com.ghabriel.forum.controller;

import java.net.URI;
import java.util.Optional;
import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.ghabriel.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.ghabriel.forum.controller.dto.TopicoDto;
import br.com.ghabriel.forum.controller.form.AtualizacaoTopicoForm;
import br.com.ghabriel.forum.controller.form.TopicoForm;
import br.com.ghabriel.forum.modelo.Topico;
import br.com.ghabriel.forum.repository.CursoRepository;
import br.com.ghabriel.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
	@Cacheable(value = "listaDeTopicos")
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 10) Pageable paginacao) {

		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}

	}

	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTopicos")
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody TopicoForm form, UriComponentsBuilder uriBuilder)
			throws Exception {

		if (form.getTitulo() == null || form.getMensagem() == null || form.getNomeCurso() == null
				|| form.getTitulo().isEmpty() || form.getMensagem().isEmpty() || form.getNomeCurso().isEmpty()) {

			return ResponseEntity.notFound().build();

		} else {
			Topico topico = form.converter(cursoRepository);
			topicoRepository.save(topico);

			URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

			return ResponseEntity.created(uri).body(new TopicoDto(topico));
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}
		return ResponseEntity.notFound().build();

	}

	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos")
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody AtualizacaoTopicoForm form)
			throws Exception {
		if (form.getTitulo() == null || form.getMensagem() == null || form.getTitulo().isEmpty()
				|| form.getMensagem().isEmpty()) {

			throw new Exception("Campos inválidos");

		} else {

			Optional<Topico> optional = topicoRepository.findById(id);
			if (optional.isPresent()) {
				Topico topico = form.atualizar(id, topicoRepository);
				return ResponseEntity.ok(new TopicoDto(topico));
			}
			return ResponseEntity.notFound().build();

		}
	}

	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos")
	public ResponseEntity<?> remover(@PathVariable Long id) {

		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();

	}

}

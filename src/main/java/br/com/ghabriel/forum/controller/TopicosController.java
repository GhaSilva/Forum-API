package br.com.ghabriel.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public List<TopicoDto> lista(String nomeCurso) {
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
		} else {
			List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
			return TopicoDto.converter(topicos);
		}

	}

	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody TopicoForm form, UriComponentsBuilder uriBuilder)
			throws Exception {

		if (form.getTitulo() == null || form.getMensagem() == null || form.getNomeCurso() == null
				|| form.getTitulo().isEmpty() || form.getMensagem().isEmpty() || form.getNomeCurso().isEmpty()) {

			throw new Exception("Campos inválidos");

		} else {
			Topico topico = form.converter(cursoRepository);
			topicoRepository.save(topico);

			URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

			return ResponseEntity.created(uri).body(new TopicoDto(topico));
		}

	}

	@GetMapping("/{id}")
	public DetalhesDoTopicoDto detalhar(@PathVariable Long id) {
		Topico topico = topicoRepository.getOne(id);
		return new DetalhesDoTopicoDto(topico);

	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody AtualizacaoTopicoForm form)
			throws Exception {
		if (form.getTitulo() == null || form.getMensagem() == null || form.getTitulo().isEmpty()
				|| form.getMensagem().isEmpty()) {

			throw new Exception("Campos inválidos");

		} else {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));

		}
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id){
		topicoRepository.deleteById(id);
		return ResponseEntity.ok().build();

	}

}

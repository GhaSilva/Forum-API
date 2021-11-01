package br.com.ghabriel.forum.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.ghabriel.forum.controller.dto.TopicoDto;
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
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody  TopicoForm form, UriComponentsBuilder uriBuilder) throws Exception {

		if (form.getTitulo() == null || form.getMensagem() == null || form.getNomeCurso() == null
				|| form.getTitulo().isEmpty() || form.getMensagem().isEmpty() || form.getNomeCurso().isEmpty()) {
			
			throw new Exception("Campos inválidos");
			
		}else {
			Topico topico = form.converter(cursoRepository);
			topicoRepository.save(topico);

			URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

			return ResponseEntity.created(uri).body(new TopicoDto(topico));
		}



	}

}

package br.com.ghabriel.forum.controller.form;

import br.com.ghabriel.forum.modelo.Topico;
import br.com.ghabriel.forum.repository.TopicoRepository;

public class AtualizacaoTopicoForm {

	private String titulo;

	private String mensagem;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Topico atualizar(Long id, TopicoRepository topicoRepository) {
		Topico topico = topicoRepository.getOne(id);
		topico.setTitulo(this.titulo);
		topico.setMensagem(this.mensagem);
		return topico;
	}

}

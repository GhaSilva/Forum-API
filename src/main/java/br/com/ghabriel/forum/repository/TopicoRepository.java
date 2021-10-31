package br.com.ghabriel.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ghabriel.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{

}

package br.com.ghabriel.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ghabriel.forum.modelo.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{

	Curso findByNome(String nome);

	


}

package com.ProyectoTinder.demo.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ProyectoTinder.demo.entidades.Voto;
@Repository
public interface VotoRepositorio extends JpaRepository<Voto, String>{
	@Query("SELECT c FROM Voto c WHERE c.mascota1.id =:id ORDER BY c.fecha DESC") //buscar los votos realizados por mascota 1
public List<Voto> buscarVotosPropios(@Param("id")String id);
	@Query("SELECT c FROM Voto c WHERE c.mascota2.id =:id ORDER BY c.fecha DESC") //buscar los votos realizados por mascota 1
public List<Voto> buscarVotosRecibidos(@Param("id")String id);
}

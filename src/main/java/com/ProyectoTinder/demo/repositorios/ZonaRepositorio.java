package com.ProyectoTinder.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ProyectoTinder.demo.entidades.Zona;
@Repository
public interface ZonaRepositorio extends JpaRepository<Zona,String>{

}

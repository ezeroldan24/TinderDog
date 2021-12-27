package com.ProyectoTinder.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ProyectoTinder.demo.entidades.Foto;

public interface FotoRepositorio extends JpaRepository<Foto, String>{

}

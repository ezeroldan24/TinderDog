package com.ProyectoTinder.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ProyectoTinder.demo.entidades.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
@Query("Select c From Usuario c Where c.mail = :mail")
public Usuario buscarPorMail(@Param("mail")String mail);
}

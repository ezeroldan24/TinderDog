package com.ProyectoTinder.demo.servicio;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ProyectoTinder.demo.entidades.Foto;
import com.ProyectoTinder.demo.entidades.Mascota;
import com.ProyectoTinder.demo.entidades.Usuario;
import com.ProyectoTinder.demo.enumeracion.Sexo;
import com.ProyectoTinder.demo.enumeracion.Tipo;
import com.ProyectoTinder.demo.errores.ErrorServicio;
import com.ProyectoTinder.demo.repositorios.MascotaRepositorio;
import com.ProyectoTinder.demo.repositorios.UsuarioRepositorio;

@Service
public class MascotaServicio {
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private MascotaRepositorio mascotaRepositorio;
	@Autowired
	private FotoServicio fotoServicio;

	@Transactional
	public void agregarMascota(MultipartFile archivo, String idUsuario, String nombre, Sexo sexo,Tipo tipo) throws ErrorServicio {
		Usuario usuar = usuarioRepositorio.findById(idUsuario).get(); // trae el usuario q quiere ingresar la mascota
		validacion(nombre, sexo);
		Mascota masc = new Mascota();
		masc.setNombre(nombre);
		masc.setSexo(sexo);
		masc.setTipo(tipo);
		masc.setAlta(new Date());
		masc.setUsuario(usuar);
		Foto foto = fotoServicio.guardar(archivo);
		masc.setFoto(foto);
		mascotaRepositorio.save(masc);
	}

	@Transactional
	public void modificar(MultipartFile archivo, String idUsuario, String idMascota, String nombre, Sexo sexo, Tipo tipo)
			throws ErrorServicio {
		validacion(nombre, sexo);
		Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota); // busca el id de mascota
		if (respuesta.isPresent()) { // si existe
			Mascota masc = respuesta.get(); // se materializa
			if (masc.getUsuario().getId().equals(idUsuario)) { // se ve si el usuario de la mascota es el mismo que
																// realiza el cambio
				masc.setNombre(nombre);
				masc.setSexo(sexo);
				masc.setTipo(tipo);
				String idfoto = null;
				if (masc.getFoto() != null) {
					idfoto = masc.getFoto().getId();
					Foto foto = fotoServicio.actualizar(idfoto, archivo); // actualizamos la foto
					masc.setFoto(foto); // guardar la foto.
					mascotaRepositorio.save(masc);
				}
			} else {
				throw new ErrorServicio("No tiene permiso suficiente para realizar la operación");
			}
		} else {
			throw new ErrorServicio("No existe una mascosta con el identificador solicitado.");
		}
	}

	@Transactional
	public void eliminar(String idUsuario, String idMascota) throws ErrorServicio {
		Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota); // busca el id de mascota
		if (respuesta.isPresent()) { // si existe
			Mascota masc = respuesta.get(); // se materializa es decir se trae
			if (masc.getUsuario().getId().equals(idUsuario)) { // se ve si el usuario de la mascota es el mismo que
																// realiza el cambio
				masc.setBaja(new Date());
				mascotaRepositorio.save(masc);
			}
		} else {
			throw new ErrorServicio("No existe la mascota con el identificador solicitado");
		}
	}

	//OBTENER POR ID
	@Transactional
	public Mascota buscarPorId(String id) throws ErrorServicio{
		
		Optional<Mascota> result  = mascotaRepositorio.findById(id);
		
		if (result.isEmpty()) {
			throw new ErrorServicio("No se encontró el cliente");
		} else {
			return result.get();
		}
	}
	
	public List<Mascota> buscarMascotasPorUsuario(String id){
		return mascotaRepositorio.buscarMascotasPorUsuario(id);
	}

	public void validacion(String nombre, Sexo sexo) throws ErrorServicio {
		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServicio("El nombre no puede estar vacio");
		}
		if (sexo == null) {
			throw new ErrorServicio("El sexo no puede estar vacio");
		}
	}
}

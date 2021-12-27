package com.ProyectoTinder.demo.servicio;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ProyectoTinder.demo.entidades.Foto;
import com.ProyectoTinder.demo.errores.ErrorServicio;
import com.ProyectoTinder.demo.repositorios.FotoRepositorio;
@Service
public class FotoServicio {
	@Autowired
	 private FotoRepositorio fotoRepositorio;
	
	@Transactional
	public Foto guardar(MultipartFile archivo) throws ErrorServicio{
		
	if(archivo !=null && !archivo.isEmpty()) { //Si el archivo no es nulo
		Foto foto = new Foto();
		try {
		foto.setMime(archivo.getContentType()); //devuelve el tipo mime del archivo que viene adjunto
		foto.setNombre(archivo.getName()); //nombre del archivo
		foto.setContenido(archivo.getBytes());//contenido de la foto
		return fotoRepositorio.save(foto);
	}catch(Exception e){
		System.err.println(e.getMessage()); //si hay error se imprime en consola
	}
	
}
	return null;
}

	@Transactional //se guarda en la base de datos si no hay excepción
public Foto actualizar(String idFoto,MultipartFile archivo) throws ErrorServicio{
	if(archivo !=null && !archivo.isEmpty()) { //Si el archivo no es nulo
		Foto foto = new Foto();
		if(idFoto != null) { //Si el id de la foto es distinto de null (buscamos la foto para cambiarla)
			Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
			if(respuesta.isPresent()) {
				foto = respuesta.get(); //una vez que se busca la foto y la trae, la actualizo.
		}
		}
		try {
		foto.setMime(archivo.getContentType()); //devuelve el tipo mime del archivo que viene adjunto
		foto.setNombre(archivo.getName());
		foto.setContenido(archivo.getBytes());
		return fotoRepositorio.save(foto);
	}catch(Exception e){
		System.err.println(e.getMessage()); //si hay error se imprime en consola
	}
	
}
	return null;
}

}


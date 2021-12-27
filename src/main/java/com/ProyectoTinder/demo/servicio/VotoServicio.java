package com.ProyectoTinder.demo.servicio;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ProyectoTinder.demo.entidades.Mascota;
import com.ProyectoTinder.demo.entidades.Voto;
import com.ProyectoTinder.demo.errores.ErrorServicio;
import com.ProyectoTinder.demo.repositorios.MascotaRepositorio;
import com.ProyectoTinder.demo.repositorios.VotoRepositorio;

@Service
public class VotoServicio {
	@Autowired
	private MascotaRepositorio mascotaRepositorio;
	@Autowired
	private VotoRepositorio votoRepositorio;
	@Autowired
	private NotificacionServicio notificacionServicio;
	@Transactional
	public void votar(String idUsuario, String idMascota1, String idMascota2) throws ErrorServicio {
		if (idMascota1.equals(idMascota2)) {
			throw new ErrorServicio("No puede votarse a si mismo");
		}
		Voto voto = new Voto(); // crea el voto
		voto.setFecha(new Date()); // fecha y hora del voto
		Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota1); // se busca la mascota
		Mascota mascota1 = respuesta.get(); // se materializa
		if (mascota1.getUsuario().getId().equals(idUsuario)) { //se busca q la mascota este vinculada con el usuario
			voto.setMascota1(mascota1);
		} else {
			throw new ErrorServicio("No tiene permiso para realizar la operación solicitada.");
		}

		Optional<Mascota> respuesta2 = mascotaRepositorio.findById(idMascota2); // se busca si la segunda mascota existe
		if (respuesta2.isPresent()) {
			Mascota mascota2 = respuesta2.get();
			voto.setMascota2(mascota2);
			notificacionServicio.enviar("Tu mascota ha sido votada","Tinder de Mascota" , mascota2.getUsuario().getMail());//se le envia el mensaje al usuario que se le voto a su mascota
		} else {
			throw new ErrorServicio("No existe una mascota vinculada con el identificador");

		}
		votoRepositorio.save(voto);
		
	}
	
	@Transactional
	public void respondervoto(String idUsuario,String idVoto) throws ErrorServicio{
		Optional<Voto> respuesta = votoRepositorio.findById(idVoto);
		if(respuesta.isPresent()) {
			Voto voto = respuesta.get();
			voto.setRespuesta(new Date());
			if(voto.getMascota2().getUsuario().equals(idUsuario)) { //Si el usuario dueño de la mascota 2 es el mismo usuario que esta respondiendo
				notificacionServicio.enviar("Tu voto fue correspondido", "Tinder de mascota", voto.getMascota1().getUsuario().getMail()); //cuando se le devuelve el voto.
				votoRepositorio.save(voto);
		}else {
			throw new ErrorServicio("No tiene permiso para realizar la operación"); //si no es el usuario correcto
		}
		}else {
			throw new ErrorServicio("No existe el voto solicitado");
		}
	}
}



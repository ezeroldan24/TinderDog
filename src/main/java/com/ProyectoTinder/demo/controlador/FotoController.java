package com.ProyectoTinder.demo.controlador;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ProyectoTinder.demo.entidades.Mascota;
import com.ProyectoTinder.demo.entidades.Usuario;
import com.ProyectoTinder.demo.errores.ErrorServicio;
import com.ProyectoTinder.demo.servicio.MascotaServicio;
import com.ProyectoTinder.demo.servicio.UsuarioServicio;

@Controller
@RequestMapping("/foto")

public class FotoController {
	@Autowired
	private UsuarioServicio usuarioServicio;
	@Autowired
	private MascotaServicio mascotaServicio;

	// lo que hace esto es traer la foto a travez de una url
	@GetMapping("/usuario/{id}") // el path variable dice que el id lo va a sacar de lo que viene en {id}
	public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) { // ResponseEntity tiene la imagen y esto esta
																			// contenido en el arreglo de byte(esta persistido en el arreglo de byte)
		Usuario usuario;
		try {
			usuario = usuarioServicio.buscarPorId(id); // busca si el usuario existe
			if (usuario.getFoto() == null) { // verifica si la foto existe o no tiene foto
				throw new ErrorServicio("El usuario no tiene una foto asignada.");//si no tiene foto tira esta excepcion
			}
			byte[] foto = usuario.getFoto().getContenido(); // la variable foto tiene el contenido de la foto
			
			HttpHeaders headers = new HttpHeaders();// cabeceras: le dicen al navegador que estamos devolviendo una
													// imagen
			headers.setContentType(MediaType.IMAGE_JPEG);// la imagen es de tipo image_jpeg

			return new ResponseEntity<>(foto, headers, HttpStatus.OK);

		} catch (ErrorServicio e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // no se devuelve ni foto ni cabecera, sino que no funciona(no existe la imagen o error).
		}
		// http://localhost:2020/foto/usuario?id= (el id del usuario)
		//con esta url podemos ver la foto del usuario
	}
	
	@GetMapping("/mascota/{id}") // el path variable dice que el id lo va a sacar de lo que viene en {id}
	public ResponseEntity<byte[]> fotoMascota(@PathVariable String id) { // ResponseEntity tiene la imagen y esto esta
																			// contenido en el arreglo de byte
		Mascota mascota;
		try {
			mascota = mascotaServicio.buscarPorId(id); // busca si el usuario existe
			if (mascota.getFoto() == null) { // verifica si la foto existe o no tiene foto
				throw new ErrorServicio("El usuario no tiene una foto asignada.");//si no tiene foto tira esta excepcion
			}
			byte[] foto = mascota.getFoto().getContenido(); // la variable foto tiene el contenido de la foto
			HttpHeaders headers = new HttpHeaders();// cabeceras: le dicen al navegador que estamos devolviendo una
													// imagen
			headers.setContentType(MediaType.IMAGE_JPEG);// la imagen es de tipo image_jpeg

			return new ResponseEntity<>(foto, headers, HttpStatus.OK);

		} catch (ErrorServicio e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // no se devuelve ni foto ni cabecera, sino que no funciona(no existe la imagen o error).
		}
		// http://localhost:2020/foto/usuario?id= (el id del usuario)
		//con esta url podemos ver la foto del usuario
	}
}

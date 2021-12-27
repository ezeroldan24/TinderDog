package com.ProyectoTinder.demo.servicio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.ProyectoTinder.demo.entidades.Foto;
import com.ProyectoTinder.demo.entidades.Usuario;
import com.ProyectoTinder.demo.entidades.Zona;
import com.ProyectoTinder.demo.errores.ErrorServicio;
import com.ProyectoTinder.demo.repositorios.UsuarioRepositorio;
import com.ProyectoTinder.demo.repositorios.ZonaRepositorio;


@Service
public class UsuarioServicio implements UserDetailsService{
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private FotoServicio fotoservicio;
	@Autowired
	private NotificacionServicio notificacionServicio;
	@Autowired
	private ZonaRepositorio zonaRepositorio;
	@Transactional
public void registrar(MultipartFile archivo, String nombre,String apellido,String mail, String clave,String clave2,String idZona) throws ErrorServicio {
	validar(nombre,apellido,mail,clave,clave2,idZona);
	Zona zona = zonaRepositorio.getOne(idZona); //buscar la zona por el id que ingresamos
	Usuario usuar = new Usuario();
	usuar.setNombre(nombre);
	usuar.setApellido(apellido);
	usuar.setMail(mail);
	usuar.setZona(zona); //se le setea la zona si existe
	String encriptada = new BCryptPasswordEncoder().encode(clave);
	usuar.setClave(encriptada);
	usuar.setAlta(new Date());
	Foto foto = fotoservicio.guardar(archivo);
	usuar.setFoto(foto); //guardar la foto.
	
	usuarioRepositorio.save(usuar);
	
	//notificacionServicio.enviar("Bienvenido a Tinder de Mascota!", "Tinder de Mascota", usuar.getMail());
}
	//OBTENER POR ID
		@Transactional
		public Usuario buscarPorId(String id) throws ErrorServicio{
			
			Optional<Usuario> result  = usuarioRepositorio.findById(id);
			
			if (result.isEmpty()) {
				throw new ErrorServicio("No se encontró el cliente");
			} else {
				return result.get();
			}
		}

public void validar(String nombre,String apellido,String mail, String clave,String clave2,String zona) throws ErrorServicio {
	if(nombre == null || nombre.isEmpty()) {
		throw new ErrorServicio("El nombre del usuario no puede ser nulo");
	}
	if(apellido == null || apellido.isEmpty()) {
		throw new ErrorServicio("El apellido del usuario no puede ser nulo");
	}
	if(mail == null || mail.isEmpty()) {
		throw new ErrorServicio("El mail del usuario no puede ser nulo");
	}
	if(clave == null || clave.isEmpty() || clave.length()<6) {
		throw new ErrorServicio("La clave del usuario no puede ser nula y tiene q tener más de 6 digitos");
	}
	if(!clave.equals(clave2)) {
		throw new ErrorServicio("Las contraseñas deben ser iguales");
	}
	if(zona == null) {
		throw new ErrorServicio("No se encontro la zona solicitada");
	}
}
@Transactional
public void deshabilitar(String id) throws ErrorServicio {
	Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
	if(respuesta.isPresent()) {
		Usuario usuar = respuesta.get();
		usuar.setBaja(new Date());
		usuarioRepositorio.save(usuar);
	}else {
		throw new ErrorServicio("No se encontró el usuario solicitado");
	}
}
@Transactional
public void habilitar(String id) throws ErrorServicio {
	Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
	if(respuesta.isPresent()) {
		Usuario usuar = respuesta.get();
		usuar.setBaja(null);
		usuarioRepositorio.save(usuar);
	}else {
		throw new ErrorServicio("No se encontró el usuario solicitado");
	}
}
@Transactional
public void modificar(String id,MultipartFile archivo,String nombre,String apellido,String mail,String clave,String clave2, String idZona) throws ErrorServicio {
	validar(nombre,apellido,mail,clave,clave2,idZona);
	Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
	if(respuesta.isPresent()) {
	Usuario usua = usuarioRepositorio.findById(id).get();
	Zona zona = zonaRepositorio.getOne(idZona); 
	usua.setZona(zona);
	usua.setApellido(apellido);
	usua.setNombre(nombre);
	usua.setMail(mail);
	String encriptada = new BCryptPasswordEncoder().encode(clave);
	usua.setClave(encriptada);
	String idfoto = null;
	if(usua.getFoto() != null) { // si la foto es distinta de null
		idfoto = usua.getFoto().getId(); //se busca el id de la foto
	}
	Foto foto = fotoservicio.actualizar(idfoto,archivo); //actualizamos la foto
	usua.setFoto(foto); //guardar la foto.
	
	usuarioRepositorio.save(usua);
}else {
	throw new ErrorServicio("El usuario que quieres modificar no existe");
}
}

@Override
public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException { //IMPLEMENTAR UN METODO ABSTRACTO (RECIBE EL NOMBRE DE USUARIO Y BUSCAR EN BASE DE DATOS EL USUARIO)
		
		Usuario usuario = usuarioRepositorio.buscarPorMail(mail);
		if(usuario != null) {
		
		List<GrantedAuthority> permisos = new ArrayList<>();
		//PERMISOS
		
		GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
		permisos.add(p1);
		
		//Llamada para guardar en la sesion web
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		session.setAttribute("usuariosession", usuario); // usuariosession guarda el objeto usuario con todos sus datos logeado
		
		User user = new User(usuario.getMail(),usuario.getClave(),permisos);
		return  user;
		}else {
			return null;
	}
}
}





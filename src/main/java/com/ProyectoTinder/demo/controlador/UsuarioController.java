package com.ProyectoTinder.demo.controlador;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ProyectoTinder.demo.entidades.Usuario;
import com.ProyectoTinder.demo.entidades.Zona;
import com.ProyectoTinder.demo.errores.ErrorServicio;
import com.ProyectoTinder.demo.repositorios.ZonaRepositorio;
import com.ProyectoTinder.demo.servicio.UsuarioServicio;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
@Autowired
private UsuarioServicio usuarioServicio;
@Autowired
private ZonaRepositorio zonaRepositoro;


@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@GetMapping("/editar-perfil")
public String editarPerfil(HttpSession session,@RequestParam String id, ModelMap modelo) {
	List<Zona> zonas = zonaRepositoro.findAll(); //busca todas las zonas de la base de datos
	modelo.put("zonas", zonas); //estas zonas se guardan en el modelo y se usa en el html
	
	Usuario login = (Usuario) session.getAttribute("usuariosession");
	if(login == null || !login.getId().equals(id)) {
		return "redirect:/";
	}
	
	try {
		Usuario usuario = usuarioServicio.buscarPorId(id); //recibe el id del usuario logeado
		modelo.addAttribute("perfil",usuario); //y se envia al model con el nombre de variable perfil
	}catch(ErrorServicio e){
		modelo.addAttribute("error",e.getMessage());
		
	}
	return "perfil";
}

@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@PostMapping("/actualizar-perfil")
public String registrar(ModelMap modelo,MultipartFile archivo,HttpSession session,@RequestParam String id,@RequestParam String nombre,@RequestParam String apellido,@RequestParam String mail,@RequestParam String clave,String clave2, String idZona) {
	Usuario usuario =null;
	try {
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		if(login == null || !login.getId().equals(id)) {
			return "redirect:/";
		}
	usuario = usuarioServicio.buscarPorId(id); //se busca por id
	usuarioServicio.modificar(id, archivo, nombre, apellido, mail, clave, clave2, idZona); //se modifica
	
	session.setAttribute("usuariosession", usuario); // se setea el usuario con la modificación, se pisa con el usuario nuevo
	return "redirect:/";
	
}catch(ErrorServicio e) {
	List<Zona> zonas = zonaRepositoro.findAll(); //si no ocurre vuelve a cargar los datos y muestra el error
	modelo.put("zonas", zonas);
	modelo.put("error", e.getMessage());
	modelo.put("perfil", usuario);
}
	return "perfil";
}
}




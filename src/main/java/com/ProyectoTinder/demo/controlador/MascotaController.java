package com.ProyectoTinder.demo.controlador;



import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ProyectoTinder.demo.entidades.Mascota;
import com.ProyectoTinder.demo.entidades.Usuario;
import com.ProyectoTinder.demo.enumeracion.Sexo;
import com.ProyectoTinder.demo.enumeracion.Tipo;
import com.ProyectoTinder.demo.errores.ErrorServicio;

import com.ProyectoTinder.demo.servicio.MascotaServicio;


@Controller
@RequestMapping("/mascota")
public class MascotaController {

@Autowired
private MascotaServicio mascotaServicio;
@PostMapping("/eliminar-perfil")
public String eliminar (HttpSession session, @RequestParam String id) {
	
	try {
		Usuario login = (Usuario) session.getAttribute("usuariosession");
		mascotaServicio.eliminar(login.getId(), id);
	} catch (ErrorServicio e) {
		e.printStackTrace();
		
	}
	return "redirect:/mascota/mis-mascotas";
}

@GetMapping("/mis-mascotas")
public String misMascotas(HttpSession session, ModelMap modelo) {
	Usuario login = (Usuario) session.getAttribute("usuariosession");
	if(login==null) {
		return "redirect:/login";
	}
	List<Mascota> mascotas = mascotaServicio.buscarMascotasPorUsuario(login.getId());
	modelo.put("mascotas",mascotas);
	return "mascotas";
}


@GetMapping("/editar-perfil")
public String editarPerfil(HttpSession session,@RequestParam (required=false) String id,@RequestParam (required=false) String accion, ModelMap modelo) {
	if(accion==null) {
		accion="Crear";
	}
	Usuario login = (Usuario) session.getAttribute("usuariosession");
	if(login==null) {
		return "redirect:/";
	}
	Mascota mascota = new Mascota(); //se agrega una mascota
	if(id != null && !id.isEmpty()){
		try {
			mascota = mascotaServicio.buscarPorId(id);
		} catch (ErrorServicio e) {
			e.printStackTrace();
		}
	}
	modelo.put("perfil", mascota);//se le da el nombre perfil a mascota
	modelo.put("sexos", Sexo.values()); //la enumeración sexo devuelve values que es un array con todos los sexos
	modelo.put("accion",accion);
	modelo.put("tipos", Tipo.values());//la enumeración tipo devuelve values que es un array con todos los sexos
	return "mascota";
}

@PostMapping("/actualizar-perfil")
public String actualizar(ModelMap modelo,MultipartFile archivo, HttpSession session,@RequestParam String id,@RequestParam String nombre,@RequestParam Sexo sexo,@RequestParam Tipo tipo) {

	Usuario login=(Usuario) session.getAttribute("usuariosession"); //con esto sacamos el id del usuario
	if(login==null) {
		return "redirect:/";
	}
	try {
	if( id== null || id.isEmpty()) {
		mascotaServicio.agregarMascota(archivo, login.getId(), nombre, sexo, tipo);
	}else {
		mascotaServicio.modificar(archivo, login.getId(), id, nombre, sexo, tipo);
	}
	return "redirect:/";
	
}catch(ErrorServicio e) {
	Mascota mascota = new Mascota();
	mascota.setId(id);
	mascota.setNombre(nombre);
	mascota.setSexo(sexo);
	mascota.setTipo(tipo);
	modelo.put("sexos", Sexo.values());
	modelo.put("accion", "Actualizar");
	modelo.put("tipos", Tipo.values());
	modelo.put("error", e.getMessage());
	modelo.put("perfil", mascota);
	
}
	return "mascota";
}

}









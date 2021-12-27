package com.ProyectoTinder.demo.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ProyectoTinder.demo.entidades.Zona;
import com.ProyectoTinder.demo.errores.ErrorServicio;
import com.ProyectoTinder.demo.repositorios.ZonaRepositorio;
import com.ProyectoTinder.demo.servicio.UsuarioServicio;

@Controller
@RequestMapping("/")
public class MainController {
@Autowired
private UsuarioServicio usuarioServicio;
@Autowired
private ZonaRepositorio zonaRepositorio;

@GetMapping("/index")
public String index() {
	return "index";
}

@GetMapping("/registro")
public String registro(ModelMap modelo) {
	List<Zona> zonas = zonaRepositorio.findAll(); //lista todas las zonas
	modelo.put("zonas", zonas); //esto se manda a registro.html en el each
	return "registro";
}

@PostMapping("/registrar") //metodo post para el registro
//en el public se llama a los parametros de los name en input
public String registrar(ModelMap modelo,MultipartFile archivo,@RequestParam String nombre,@RequestParam String apellido,@RequestParam String mail,@RequestParam String clave1,@RequestParam String clave2,@RequestParam String idZona) {
	try {
		usuarioServicio.registrar(archivo, nombre, apellido, mail, clave1, clave2,idZona); //Se registra si todo sale bien	
	} catch (ErrorServicio e) {
		List<Zona> zonas = zonaRepositorio.findAll();
		modelo.put("zonas", zonas);
		modelo.put("error",e.getMessage()); // nombre de variable y la excepción
		//lo que se hace ahora es para que queden guardados los datos por mas que haya error y no tenga que repetirlo de nuevo
		modelo.put("nombre", nombre);
		modelo.put("apellido", apellido);
		modelo.put("mail", mail);
		modelo.put("clave1", clave1);
		modelo.put("clave2", clave2);
		// luego en la vista se puede ver esto en th:value
		return "registro.html"; //nos envia al registro nuevamente si hubo un error
	}
	return "exito"; //se va al index si se pudo registrar
}

@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@GetMapping("/exito")
public String exito(){
	return "exito";
}

@GetMapping("/nosotros")
public String nosotros() {
	return "nosotros";
}

}

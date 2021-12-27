package com.ProyectoTinder.demo.controlador;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorsController implements ErrorController {
@RequestMapping(value="/error", method = {RequestMethod.GET,RequestMethod.POST}) //EL REQUEST MAPPING ESTA ABAJO DEL PUBLIC PORQUE SE RECIBE METODOS POST O GET, CUALQUIER ERROR
public String mostrarPaginaDeError(Model modelo,HttpServletRequest HttpServletRequest) {
	String mensajeError ="";
	int codigoError= (int) HttpServletRequest.getAttribute("javax.servlet.error.status_code"); //numero del error, nos da el codigo de la respuesta del error en formato int
	switch(codigoError) {
	case 400:
		mensajeError="El recurso solicitado no existe";
		break;
	case 401:
		mensajeError="No se encuentra autorizado"; //necesitas iniciar sesión
		break;
	case 403:
		mensajeError="No tiene permisos para acceder al recurso"; //no tenes los roles
		break;
	case 404:
		mensajeError="El recurso solicitado no se ha encontrado";
		break;
	case 500:
		mensajeError="El servidor no pudo realizar la petición con éxito";
		break;
	default:
		
	}
	modelo.addAttribute("codigo",codigoError);
	modelo.addAttribute("mensaje",mensajeError);
	
	return "error";
}
}

package com.ProyectoTinder.demo.servicio;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



@Service
public class NotificacionServicio {

@Autowired
private JavaMailSender mailSender; //libreriria de mensajes

@Async //el hilo de ejecución no espera a que se termine de enviar el mail, lo ejecuta en un hilo paralelo(respuesta inmediata)

public void enviar(String cuerpo, String titulo, String mail )  {
	SimpleMailMessage mensaje = new SimpleMailMessage();
	mensaje.setTo(mail); // a quien va dirigido
	mensaje.setFrom("ezeroldan241@outlook.es");//desde quien sale el mensaje
	mensaje.setSubject(titulo); //el asunto
	mensaje.setText(cuerpo);//cuerpo del mensaje
	mailSender.send(mensaje);
}
}

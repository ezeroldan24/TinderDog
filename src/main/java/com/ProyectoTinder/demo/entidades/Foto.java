package com.ProyectoTinder.demo.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Foto {
@Id
@GeneratedValue(generator = "uuid")
@GenericGenerator(name = "uuid" , strategy = "uuid2")
private String id;
private String nombre;
private String mime;
@Lob @Basic(fetch = FetchType.LAZY) //Lob (tipo de dato grande). Basic(lazy se carga cuando se pida (query mas liviano)
private byte[] contenido; //contenido de la foto
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getMime() {
	return mime;
}
public void setMime(String mime) {
	this.mime = mime;
}
public byte[] getContenido() {
	return contenido;
}
public void setContenido(byte[] contenido) {
	this.contenido = contenido;
}


}

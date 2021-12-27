package com.ProyectoTinder.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ProyectoTinder.demo.servicio.UsuarioServicio;


@Configuration //CLASE DE CONFIGURACIÓN
@EnableWebSecurity // HABILITA LA SEGURIDAD WEB
@EnableGlobalMethodSecurity(prePostEnabled = true) // AGREGAR PROPIEDADES A LA APLICACIÓN, PARENTESIS PERMITE AUTORIZAR LAS URL
public class ConfiguracionSeguridad extends WebSecurityConfigurerAdapter{ //ESTA CLASE SE ENCARGA DE LA SEGURIDAD
// dentro de esto va a haber:
 //autenticación de los usuarios (que haya una sesión de usuario)y las autorizaciones(lo que tiene permitido cada usuario)
 //UserDatailService --> provee un metodo loadByUserName (metodo para que el usuario se logee)
// Metodo que configura la autenticación .
// Luego, la configuración de las peticiones http (accesos permitidos y no) con http
	@Autowired
	private UsuarioServicio usuarioServicio;
	
	//metodo para configurar autenticacion
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { //PERMITE CREAR AUTENTIFICACIONES (metodo de autentificación)
		auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//configuración de peticiones http
	//configuración de peticiones http
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/*","/img/*","/js/*").permitAll() //lo que esta permitido
		.and().formLogin()
		.loginPage("/login") //url de nuestro login
		.usernameParameter("mail") //esto va a estar en el login, cuando creemos el login name=username para que se entienda
		.passwordParameter("clave")
		.defaultSuccessUrl("/") //cuando el usuario se logee donde va a ir
		.loginProcessingUrl("/logincheck")//a que url tiene que ir para procesar el logeo
		.failureUrl("/login?error=error") //que sucede cuando se quiere autenticar y falla, manda un mensaje con un error
		.permitAll() //permitir a todo
		.and().logout()
		.logoutUrl("/logout") //la url de salir
		.logoutSuccessUrl("/login?logout")// cuando el logout es satisfactorio lo manda al login, va a decir login con el parametro logout
		.and().csrf().disable(); //proteccion en caso de que entre a otra pagina desde otro navegador, seguridad
	}
}
	

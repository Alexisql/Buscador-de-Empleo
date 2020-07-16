package com.alexis.empleos.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Esta clase es usada para obtener las imagenes que se encuentra en una ubicación externa al proyecto
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${empleos.ruta.imagenes}")
	private String ruta;
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// registry.addResourceHandler("/logos/**").addResourceLocations("file:/empleos/img-vacantes/");
		// // Linux
		registry.addResourceHandler("/logos/**").addResourceLocations("file:"+ruta); // Windows
	}
}

package unaj.iot.arduinowebapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *Descripción de la configuracion SSL, encriptación del archivo certificado, etc. 
 * */
@Configuration
@PropertySource(value = "classpath:application.properties")
public class ConfiguracionSSL {
	
	@Value("${clienteCert}")
	private String pathClienteCert;
	
	@Value("${servCert}")
	private String pathServCert;
	
	public String getPathClienteCert() {
		return pathClienteCert;
	}

	public String getPathServCert() {
		return pathServCert;
	}

	public ConfiguracionSSL() {
		System.out.println("constructor");
	}

	
}

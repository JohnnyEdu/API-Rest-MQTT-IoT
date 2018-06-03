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
	@Value("${server.ssl.key-store-type}")
	private String tipoEncrpKeyStore;
	@Value("${server.ssl.key-store}")
	private String keyStore;
	@Value("${server.ssl.key-store-password}")
	private String keyStorePass;
	@Value("${server.ssl.key-alias}")
	private String aliasKeyStore;
	@Value("${server.ssl.key-store-filename}")
	private String keyStoreFileName;
	
	public ConfiguracionSSL() {
		System.out.println("constructor");
	}

	public String getTipoEncrpKeyStore() {
		return tipoEncrpKeyStore;
	}

	public String getKeyStore() {
		return keyStore;
	}

	public String getKeyStorePass() {
		return keyStorePass;
	}

	public String getAliasKeyStore() {
		return aliasKeyStore;
	}

	public String getKeyStoreFileName() {
		return keyStoreFileName;
	}
	
	
	
}

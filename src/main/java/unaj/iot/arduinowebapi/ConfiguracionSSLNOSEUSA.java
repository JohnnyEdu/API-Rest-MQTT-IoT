package unaj.iot.arduinowebapi;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *Descripción de la configuracion SSL, encriptación del archivo certificado, etc. 
 * */
//@Component
//@ConfigurationProperties
public class ConfiguracionSSLNOSEUSA {
	private int puerto;
	private boolean requiereSeguridadSSL;
	private String tipoEncrpKeyStore;
	private String keyStore;
	private String keyStorePass;
	private String aliasKeyStore;
	
	public ConfiguracionSSLNOSEUSA() {
		System.err.println("SE INSTANCIO!!!!" + puerto + keyStore);
	}
	
	private Object llamarGetter(Object obj, String atributo) {
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(atributo, obj.getClass());
			return pd.getReadMethod().invoke(obj);
		} catch (IllegalAccessException |
				IllegalArgumentException | 
				InvocationTargetException | 
				IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String toString() {
		String camposConfig = "{";
		for(Field atributo: this.getClass().getDeclaredFields()) {
			camposConfig = new StringBuilder(
					atributo.getName())
					.append(":")
					.append(String.valueOf(llamarGetter(this, atributo.getName())))
					.append(";")
					.toString();
		}
		camposConfig += "}";
		return camposConfig;
	}
}

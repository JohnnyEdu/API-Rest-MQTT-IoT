package unaj.iot.arduinowebapi;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfTLSFactory {
	@Autowired
	ConfiguracionSSL configuracionTLS;
	
	public void configurarConexionTLS(MqttConnectOptions opciones) 
			throws KeyStoreException, 
			NoSuchAlgorithmException, 
			CertificateException, 
			FileNotFoundException, 
			IOException,
			UnrecoverableKeyException,
			KeyManagementException{
		char[] passwd = configuracionTLS.getKeyStorePass().toCharArray();
		
		ClassLoader classLoader = getClass().getClassLoader();
		URL urlCliente = classLoader.getResource(configuracionTLS.getPathClienteCert());
		URL urlServer = classLoader.getResource(configuracionTLS.getPathServCert());

		String pathClienteCert = urlCliente.getFile();
		String pathServCert = urlServer.getFile();
		
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		
		// client key and certificates are sent to server so it can authenticate us
		KeyStore ks = KeyStore.getInstance("PKCS12");
		
		InputStream fis = new FileInputStream(pathClienteCert);
		BufferedInputStream bis = new BufferedInputStream(fis);
		Certificate clientCert = cf.generateCertificate(bis);

		ks.load(null,null);
		ks.setCertificateEntry("ca-certificate", clientCert);
		
	    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
	    kmf.init(ks, passwd);
	    
		// CA certificate is used to authenticate server
		fis = new FileInputStream(pathServCert);
		bis = new BufferedInputStream(fis);
		Certificate cacert = cf.generateCertificate(bis);
		
		fis.close();
		bis.close();
		
		KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
		caKs.load(null, null);
		caKs.setCertificateEntry("ca-certificate", cacert);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(caKs);

	    
	    
	    SSLContext ctx = SSLContext.getInstance("TLSv1.2");
	    ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(),null);
	    opciones.setSocketFactory(ctx.getSocketFactory());
	}
	
}

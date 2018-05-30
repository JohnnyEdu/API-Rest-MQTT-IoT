package unaj.iot.arduinowebapi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class Main {
	public static void main(String[] args) {
		try {
		char[] passwd = "2018unaj".toCharArray();
		KeyStore ks = KeyStore.getInstance("PKCS12");
		String archivo = new File("C:\\Users\\jbaez\\Desktop\\APIInformacionIOT\\src\\main\\resources\\keystore_iot_unaj_pkcs12.p12").getPath(); 
	    ks.load(new FileInputStream(archivo), passwd);
		
	    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
	    kmf.init(ks, passwd);
	    
	    SSLContext ctx = SSLContext.getInstance("SSL");
	    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	    trustManagerFactory.init(ks);
	    ctx.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
	    
	    SSLSocket client = (SSLSocket) ctx.getSocketFactory().createSocket("broker.hivemq.com", 8000);
	    System.err.println(client.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

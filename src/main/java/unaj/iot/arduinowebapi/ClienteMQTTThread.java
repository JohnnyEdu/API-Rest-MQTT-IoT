package unaj.iot.arduinowebapi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
//TODO: buscar prototype
public class ClienteMQTTThread implements  Runnable,MqttCallback{
	private static MqttClient cliente;
	private final String BROKER_URL = "ssl://broker.hivemq.com";
	private final Integer BROKER_PUERTO_SSL = 8883;
	public static String SERVER_HOME;
	public static String ARCHIVO_HIST_TEMPERATURA;
	public static String ARCHIVO_HIST_HUMEDAD;
	public static final String TOPICO_BROKER = "meteorologiaUnajIoT";
	
	@Autowired
	ConfiguracionSSL configuracionSSL;
	
	
	
	 public ClienteMQTTThread() {
		 try {
			SERVER_HOME = new File(".").getCanonicalPath() + File.separator;
			ARCHIVO_HIST_TEMPERATURA = SERVER_HOME + "historicoTelemetriaTemperatura.txt";
			ARCHIVO_HIST_HUMEDAD = SERVER_HOME + "historicoTelemetriaHumedad.txt";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	 public void messageArrived(String topico, MqttMessage medida) throws Exception {
		 //TODO: revisar comportamiento para humedad
		 System.err.println(topico + ", msg = "+ medida);
		 BufferedWriter writter = new BufferedWriter(new FileWriter(ARCHIVO_HIST_TEMPERATURA,true));
		 writter.newLine();
		 writter.append(String.valueOf(medida));
		 writter.close();
	 }
	
	
	private void configurarConexionSSL(MqttConnectOptions opciones) 
			throws KeyStoreException, 
			NoSuchAlgorithmException, 
			CertificateException, 
			FileNotFoundException, 
			IOException,
			UnrecoverableKeyException,
			KeyManagementException{
		char[] passwd = configuracionSSL.getKeyStorePass().toCharArray();
		
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ClassLoader classLoader = getClass().getClassLoader();
		String archivo = new File(classLoader.getResource(configuracionSSL.getKeyStoreFileName()).getFile()).getPath(); 
	    ks.load(new FileInputStream(archivo), passwd);
		
	    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
	    kmf.init(ks, passwd);
	    
	    SSLContext ctx = SSLContext.getInstance("SSLv3");
	    ctx.init(kmf.getKeyManagers(), null, null);
	    opciones.setSocketFactory(ctx.getSocketFactory());
	}
	
	/**
	 * Consulta a Web Socket de HiveMQ: http://www.hivemq.com/demos/websocket-client/
	 * */
	@Override
	public void run() {
		try {
			if(cliente == null) {
				//se puede conectar con tcp://xxx:1883 ver en http://www.mqtt-dashboard.com/index.html
				cliente = new MqttClient(BROKER_URL + ":" + BROKER_PUERTO_SSL, MqttClient.generateClientId());
								
				MqttConnectOptions options = new MqttConnectOptions();
				Properties propiedades = new Properties();
				propiedades.setProperty("keyStore", configuracionSSL.getKeyStore());
				propiedades.setProperty("keyStorePassword", configuracionSSL.getKeyStorePass());
				//propiedades.setProperty("keyStoreType", configuracionSSL.getTipoEncrpKeyStore());
				
				try {
					configurarConexionSSL(options);
				} catch (UnrecoverableKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (KeyManagementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (KeyStoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CertificateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				options.setCleanSession(true);//no guarda estado de sesión
				
				//tiempo que tarda el cliente en enviar ping para mantener la conexión
				options.setKeepAliveInterval(200);
				options.setConnectionTimeout(60);
				//Topico | Mensaje | QoS | Retained?
				/*
				 * QoS: 3 niveles,  0 --> como mucho una vez
				 * 					1 --> al menos una vez
				 * 					2 --> exactamente 1 vez
				 * Se tetea por cada mensaje
				 * 
				 * https://www.eclipse.org/paho/files/mqttdoc/MQTTClient/html/qos.html
				 * */
				options.setWill(TOPICO_BROKER, "Se desconectó la API Web para Arduino UNO R3 UNAJ".getBytes(), 0, true);
				cliente.setCallback(this);
				cliente.connect(options);
				
				//para ver el test: http://www.hivemq.com/demos/websocket-client/
				cliente.publish( 
						TOPICO_BROKER, // topic 
					    "Se conectó la API Web para Arduino UNO R3 UNAJ".getBytes(), // payload 
					    2, // QoS 
					    true); // retained ? especifica si el broker guarda el mensaje para mostrarselo a cualquier suscriptor que se conecte 
				
				cliente.subscribe(TOPICO_BROKER);
				
					//TODO: dejar conexion activa?
				
				
//				SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
//				try {
//					socketFactory.createSocket("BROKER_URL", Integer.valueOf(BROKER_PUERTO_SSL));
//				} catch (NumberFormatException | IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			}
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}
}

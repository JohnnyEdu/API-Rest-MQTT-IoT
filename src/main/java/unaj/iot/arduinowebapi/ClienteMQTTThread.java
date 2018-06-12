package unaj.iot.arduinowebapi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;


@Component
@Scope("prototype")
//TODO: buscar prototype
public class ClienteMQTTThread implements  Runnable,MqttCallback{
	private static MqttClient cliente;
//	private final String BROKER_URL = "ssl://broker.hivemq.com";
	private final String BROKER_URL = "ssl://192.168.0.38";
	private final Integer BROKER_PUERTO_SSL = 8883;
	public static String SERVER_HOME;
	public static String ARCHIVO_HIST_TEMPERATURA;
	public static String ARCHIVO_HIST_HUMEDAD;
	public static final String TOPICO_BROKER = "meteorologiaUnajIoT";
	
	@Autowired
	ConfTLSFactory confTLSFactory;
	
	 public ClienteMQTTThread() {
		SERVER_HOME = System.getProperty("catalina.base") + File.separator + "archivosTelemetria" +File.separator;
		ARCHIVO_HIST_TEMPERATURA = SERVER_HOME + "historicoTelemetriaTemperatura.txt";
		ARCHIVO_HIST_HUMEDAD = SERVER_HOME + "historicoTelemetriaHumedad.txt";
	}
	 
	 public void publicar (String mensaje) throws MqttException{
		 	cliente.publish( 
					TOPICO_BROKER, // topic 
					mensaje.getBytes(), // payload 
				    2, // QoS 
				    false);
	 }
	 
	@Override
	 public void messageArrived(String topico, MqttMessage medida) throws Exception {
		 //TODO: revisar comportamiento para humedad
		 System.err.println(topico + ", msg = "+ medida);
		 BufferedWriter writter = new BufferedWriter(new FileWriter(ARCHIVO_HIST_TEMPERATURA,true));
		 writter.newLine();
		 MensajeTelemetria msj = new MensajeTelemetria(medida.toString(), "temperatura");
		 String json = new Gson().toJson(msj);
		 writter.append(json);
		 writter.close();
	 }
	
	
	/**
	 * Consulta a Web Socket de HiveMQ: http://www.hivemq.com/demos/websocket-client/
	 * */
	@Override
	public void run() {
		try {
			if(cliente == null) {
				//se puede conectar con tcp://xxx:1883 ver en http://www.mqtt-dashboard.com/index.html
				cliente = new MqttClient(BROKER_URL + ":" + BROKER_PUERTO_SSL, MqttClient.generateClientId()
						,new MemoryPersistence());
								
				MqttConnectOptions options = new MqttConnectOptions();
				
				try {
					confTLSFactory.configurarConexionTLS(options);
				} catch (Exception e) {
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
				cliente.subscribe(TOPICO_BROKER);
				
				
				//para ver el test: http://www.hivemq.com/demos/websocket-client/
				cliente.publish( 
						TOPICO_BROKER, // topic 
					    "Se conectó la API Web para Arduino UNO R3 UNAJ".getBytes(), // payload 
					    2, // QoS 
					    true); // retained ? especifica si el broker guarda el mensaje para mostrarselo a cualquier suscriptor que se conecte 
				
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

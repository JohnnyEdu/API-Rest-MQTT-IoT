package unaj.iot.arduinowebapi;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

/**
 *Clase controladora (router) de las request a la API 
 * */
@RestController
public class MensajeTelemetriaController {
	@Autowired
	private ThreadAsincronoService clienteMQTT;
	Calentador calentador;
	
	public MensajeTelemetriaController() {
		calentador = new Calentador();
	}
	 @RequestMapping("/publicarEnBroker")
	public void publicarEnBroker(@RequestParam("mensaje") String mensaje) {
		 try {
			clienteMQTT.cliente.publicar(mensaje);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	
    @RequestMapping("/ultimaMedicionTemp")
    public String ultimaMedicionTemperatura() {
    	String msjArduino = ArchivoTelemetriaService.getUltimaMedicionTemperatura();
    	String json = "{error: 'Sin resultados'}";
        return msjArduino!=null?msjArduino: json;
    }
    
    
    @RequestMapping("/encenderCalentador")
    public String encenderCalentador() {
    	Command calentadorCommand = new EncenderCalentadorCommand(calentador);
    	calentadorCommand.execute();
    	return "{estadoCalentador: "+calentador.isEncendido()+"}";
    }
    
    @RequestMapping("/apagarCalentador")
    public String apagarCalentador() {
    	Command calentadorCommand = new ApagarCalentadorCommand(calentador);
    	calentadorCommand.execute();
    	return "{estadoCalentador: "+calentador.isEncendido()+"}";
    }
}


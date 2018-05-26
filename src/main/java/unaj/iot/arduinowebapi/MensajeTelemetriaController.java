package unaj.iot.arduinowebapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
    @RequestMapping("/ultimaMedicionTemp")
    public String ultimaMedicionTemperatura() {
    	String msjArduino = ArchivoTelemetriaService.getUltimaMedicionTemperatura();
    	String json = "{error: 'Sin resultados'}";
    	if(msjArduino!=null) {
    		MensajeTelemetria medicion = new MensajeTelemetria(msjArduino, "temperatura");
    		Gson jsonParser = new Gson();
    		json = jsonParser.toJson(medicion);
    	}
        return json;
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


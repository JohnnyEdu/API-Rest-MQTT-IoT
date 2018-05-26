package unaj.iot.arduinowebapi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ArchivoTelemetriaService {
	
	public static String getUltimaMedicionTemperatura() {
		String lineaFinal = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(ClienteMQTTThread.ARCHIVO_HIST_TEMPERATURA));
            lineaFinal = reader.readLine();
            String lineaActual = reader.readLine();
            while(lineaActual != null) {
            	lineaFinal = lineaActual;
            	lineaActual = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException           e) {
        	e.printStackTrace();
        }
		
		//TODO: guardar la fecha y hora (timestamp)
		return lineaFinal;
	}
}

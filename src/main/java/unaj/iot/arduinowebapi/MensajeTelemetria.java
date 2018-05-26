package unaj.iot.arduinowebapi;

import java.util.Date;

public class MensajeTelemetria {

    //private long id;
    private long timestampMedida;
    private String magnitud;
    private String tipo;
    
    public MensajeTelemetria(String magnitud, String tipo) {
        this.magnitud = magnitud;
        this.timestampMedida = new Date().getTime();
        this.tipo = tipo;
    }

    public String getContent() {
        return magnitud;
    }

	public long getTimestampMedida() {
		return timestampMedida;
	}

	public String getMagnitud() {
		return magnitud;
	}
    
    
}

package unaj.iot.arduinowebapi;

public class Calentador {
	private static boolean encendido = false;
	
	public void encender() {
		encendido = true;
	}
	public void apagar() {
		encendido = false;
	}
	public boolean isEncendido() {
		return encendido;
	}
}

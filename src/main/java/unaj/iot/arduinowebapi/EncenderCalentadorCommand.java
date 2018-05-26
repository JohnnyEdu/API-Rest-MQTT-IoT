package unaj.iot.arduinowebapi;

public class EncenderCalentadorCommand implements Command {
	private Calentador calentador;
	
	EncenderCalentadorCommand(Calentador calentador){
		this.calentador = calentador;
	}
	
	@Override
	public void execute() {
		calentador.encender();
	}

}

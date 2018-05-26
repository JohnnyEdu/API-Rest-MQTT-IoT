package unaj.iot.arduinowebapi;

public class ApagarCalentadorCommand implements Command {
	private Calentador calentador;
	
	ApagarCalentadorCommand(Calentador calentador){
		this.calentador = calentador;
	}
	
	@Override
	public void execute() {
		calentador.apagar();
	}

}

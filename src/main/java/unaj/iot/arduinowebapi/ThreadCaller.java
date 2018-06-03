package unaj.iot.arduinowebapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ThreadCaller implements ApplicationListener<ApplicationReadyEvent>{
	@Autowired
	private ThreadAsincronoService clienteMQTT;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		clienteMQTT.executeAsynchronously();
	}
}

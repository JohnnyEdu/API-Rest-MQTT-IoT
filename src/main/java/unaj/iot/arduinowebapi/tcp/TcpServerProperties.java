package unaj.iot.arduinowebapi.tcp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConfigurationProperties(prefix = "properties")
@EnableConfigurationProperties
public class TcpServerProperties {
	
	@Value("${puetoTCPArduino}")
    private int port;

	@Value("${autostartPuertoTCP}")
    private boolean autoStart;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }
}

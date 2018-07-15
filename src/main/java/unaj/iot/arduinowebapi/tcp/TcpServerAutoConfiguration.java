package unaj.iot.arduinowebapi.tcp;

import unaj.iot.arduinowebapi.tcp.interfaces.Server;
import unaj.iot.arduinowebapi.tcp.TcpControllerBeanPostProcessor;
import unaj.iot.arduinowebapi.tcp.TcpServer;
import unaj.iot.arduinowebapi.tcp.TcpServerAutoStarterApplicationListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "properties", name = {"port", "autoStart"})
public class TcpServerAutoConfiguration {

    @Bean
    TcpServerAutoStarterApplicationListener tcpServerAutoStarterApplicationListener() {
        return new TcpServerAutoStarterApplicationListener();
    }

    @Bean
    TcpControllerBeanPostProcessor tcpControllerBeanPostProcessor() {
        return new TcpControllerBeanPostProcessor();
    }

    @Bean
    Server server(){
        return new TcpServer();
    }
}

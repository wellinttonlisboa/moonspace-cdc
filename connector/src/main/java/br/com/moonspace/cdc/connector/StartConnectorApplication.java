package br.com.moonspace.cdc.connector;

import br.com.moonspace.commons.configurations.KafkaConfig;
import br.com.moonspace.commons.configurations.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"br.com.moonspace.cdc.*"})
@Import({KafkaConfig.class, RedisConfig.class})
public class StartConnectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartConnectorApplication.class, args);
	}

}
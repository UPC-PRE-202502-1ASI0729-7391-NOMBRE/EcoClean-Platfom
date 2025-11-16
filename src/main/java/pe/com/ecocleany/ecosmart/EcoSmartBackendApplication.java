package pe.com.ecocleany.ecosmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcoSmartBackendApplication {
	public static void main(String[] args) {
        SpringApplication.run(EcoSmartBackendApplication.class, args);
	}
}

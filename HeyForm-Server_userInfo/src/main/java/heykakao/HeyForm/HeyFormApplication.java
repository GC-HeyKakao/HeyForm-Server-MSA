package heykakao.HeyForm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
public class HeyFormApplication {

	public static void main(String[] args) {

		SpringApplication.run(HeyFormApplication.class, args);
	}

}

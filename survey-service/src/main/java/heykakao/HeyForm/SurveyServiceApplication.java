package heykakao.HeyForm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableSwagger2
//@OpenAPIDefinition(info =
//	@Info(title = "survey API", version = "1.0", description = "Documentation Employee API v1.0")
//)
public class SurveyServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(SurveyServiceApplication.class, args);
	}

}

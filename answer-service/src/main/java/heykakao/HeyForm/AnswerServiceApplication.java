package heykakao.HeyForm;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@OpenAPIDefinition(info =
	@Info(title = "answer API", version = "1.0", description = "Documentation Employee API v1.0")
)
public class AnswerServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(AnswerServiceApplication.class, args);
	}

}

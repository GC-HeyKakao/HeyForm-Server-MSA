package heykakao.HeyForm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


@SpringBootApplication
//@EnableSwagger2
@OpenAPIDefinition(info =
	@Info(title = "User API", version = "1.0", description = "Documentation Employee API v1.0")
)
public class UserServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserServiceApplication.class, args);
	}

}

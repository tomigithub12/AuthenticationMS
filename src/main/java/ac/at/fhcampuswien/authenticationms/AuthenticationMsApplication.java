package ac.at.fhcampuswien.authenticationms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "AuthenticationMS API", version = "1.0"))
public class AuthenticationMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationMsApplication.class, args);
    }

}

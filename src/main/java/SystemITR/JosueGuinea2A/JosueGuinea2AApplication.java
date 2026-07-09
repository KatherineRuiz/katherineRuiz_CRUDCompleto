package SystemITR.JosueGuinea2A;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JosueGuinea2AApplication {

	public static void main(String[] args) {
		// Cargar variables del .env a System Properties
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);
		SpringApplication.run(JosueGuinea2AApplication.class, args);
	}
}

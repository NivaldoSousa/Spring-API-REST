package curso.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"curso.api.rest.model"}) //mapea o pacote de model e criar as tabelas automaticamente.
@ComponentScan(basePackages = {"curso.*"} ) // injeçao de dependencia, controla todos os objetos.
@EnableJpaRepositories(basePackages = {"curso.api.rest.repository"} ) //habilita toda as ferramentas de repository(interface).
@EnableTransactionManagement //habilita todas as facilidades de transaçao, requisiçoes com o banco de dados, salva,deleta e atualizar.
@EnableWebMvc //Model View Controller
@RestController // habilitar as ferramentas Rest
@EnableAutoConfiguration 
@EnableCaching
public class CursospringrestapiApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(CursospringrestapiApplication.class, args);

		/*Gera senha criptografada*/     
		//System.out.println(new BCryptPasswordEncoder().encode("123")); 
	}

	/*Mapeamento Global que refletem e todo o sistema*/
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/usuario/**") //libera parte do API
		.allowedMethods("*") //libera parte dos metodos do API
		.allowedOrigins("*"); // Identifica quais origens vao acessar as requisiçoes da API
      /* Libera apenas requisiçoes de sua escolha para qualquer URL, usuario e etc */
	}

}

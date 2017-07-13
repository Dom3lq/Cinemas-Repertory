package pl.cinemapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("POJOs")  
@SpringBootApplication
@EnableJpaRepositories("Repositories")
@EnableCaching
public class CoWKinieApplication {

	public static void main(String[] args) {
		try{
		SpringApplication.run(CoWKinieApplication.class, args);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

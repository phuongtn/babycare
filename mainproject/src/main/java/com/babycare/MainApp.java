package com.babycare;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/*@SpringBootApplication(exclude= {IGenericPagingRepo.class})*/
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
/*@ComponentScan(basePackages = "net.example.tool",
excludeFilters = {@ComponentScan.Filter(
  type = FilterType.ASSIGNABLE_TYPE,
  value = {IGenericPagingRepo.class})
})*/
public class MainApp {

	public static void main(String[] args) {
		SpringApplication.run(MainApp.class, args);
	}
}

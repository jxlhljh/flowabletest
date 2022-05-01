package cn.gzsendi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 */
@SpringBootApplication
public class FlowableApplicationStarter {

	public static void main(String[] args) {
		
		//SpringApplicationBuilder builder = new SpringApplicationBuilder(DemoApplicationStarter.class);
		//builder.headless(false).run(args);
		SpringApplication.run(FlowableApplicationStarter.class, args);
		
	}

}

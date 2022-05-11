package com.vizier;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vizier.agent.Runner;

/* the main class- entry point to the springboot application */
@SpringBootApplication
public class VizierAgent2Application implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(VizierAgent2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		new Runner().main(args);;
		
	}

}

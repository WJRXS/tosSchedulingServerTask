package com.citybridge.tos.schedulingServerTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

    /**
     * #TODO create mathc schedules in database that are checked in intervals.
     * @param args
     */
	public static void main(String[] args) {
			SpringApplication.run(Application.class, args);
	}

}

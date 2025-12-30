package io.example.dem.oom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.metrics.jfr.FlightRecorderApplicationStartup;

@SpringBootApplication
public class DemoOomApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DemoOomApplication.class);
        application.setApplicationStartup(new FlightRecorderApplicationStartup());
        application.run(args);
    }

}

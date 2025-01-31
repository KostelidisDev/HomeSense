package gr.ihu.robotics.homesense.api;

import gr.ihu.robotics.homesense.security.HomeSenseSecurityConfiguration;
import gr.ihu.robotics.homesense.service.HomeSenseServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        HomeSenseServiceConfiguration.class,
        HomeSenseSecurityConfiguration.class,
})
public class HomeSenseAPI {
    public static void main(String[] args) {
        SpringApplication.run(HomeSenseAPI.class, args);
    }
}

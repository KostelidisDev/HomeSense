package gr.ihu.robotics.homesense.domain;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EntityScan
@ComponentScan
@Configuration
@EnableAutoConfiguration
public class HomeSenseDomainConfiguration {
}

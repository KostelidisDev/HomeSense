package gr.ihu.robotics.homesense.service;

import gr.ihu.robotics.homesense.domain.HomeSenseDomainConfiguration;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configurable
@ComponentScan
@Import({
        HomeSenseDomainConfiguration.class,
})
@EnableScheduling
public class HomeSenseServiceConfiguration {
}

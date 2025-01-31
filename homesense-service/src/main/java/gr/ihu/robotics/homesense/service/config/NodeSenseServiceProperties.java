package gr.ihu.robotics.homesense.service.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "nodesense-service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NodeSenseServiceProperties {
    private String ipPrefix;
    private Integer timeout;
    private Integer scheduledTimeout;
    private Integer scheduledInterval;
    private Integer scheduledDelay;
}

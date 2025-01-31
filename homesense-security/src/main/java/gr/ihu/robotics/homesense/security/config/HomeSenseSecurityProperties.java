package gr.ihu.robotics.homesense.security.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "homesense-security")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeSenseSecurityProperties {
    private String[] allowedIps;
}

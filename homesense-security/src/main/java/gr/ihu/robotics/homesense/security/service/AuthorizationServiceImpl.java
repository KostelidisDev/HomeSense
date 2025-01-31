package gr.ihu.robotics.homesense.security.service;

import gr.ihu.robotics.homesense.security.config.HomeSenseSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final Log log = LogFactory.getLog(AuthorizationServiceImpl.class);
    private final HomeSenseSecurityProperties homeSenseSecurityProperties;

    @Override
    public Boolean isAllowed(final String clientIp) {
        if (clientIp == null || clientIp.isEmpty()) {
            log.warn("Client IP is null or empty");
            return false;
        }

        final String[] allowedIps = homeSenseSecurityProperties.getAllowedIps();

        if (allowedIps == null || allowedIps.length == 0) {
            log.warn("allowed ips is null or empty");
            return false;
        }

        final List<String> allowedIpsList = Arrays.stream(allowedIps).toList();
        boolean contains = allowedIpsList.contains(clientIp);
        if (!(contains)) {
            log.warn(
                    String.format(
                            "Client IP %s is not in allowed ips",
                            clientIp
                    )
            );
        }
        return contains;
    }
}

package gr.ihu.robotics.homesense.security.service;

public interface AuthorizationService {
    Boolean isAllowed(String clientIp);
}

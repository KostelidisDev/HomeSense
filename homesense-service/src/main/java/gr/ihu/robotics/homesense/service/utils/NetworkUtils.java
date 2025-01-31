package gr.ihu.robotics.homesense.service.utils;

import java.net.InetAddress;
import java.net.Socket;

public abstract class NetworkUtils {
    public static boolean isPortOpen(String ip, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(ip, port), timeout);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDeviceOnline(String ip, int timeout) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(timeout);
        } catch (Exception e) {
            return false;
        }
    }
}

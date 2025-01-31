package gr.ihu.robotics.homesense.service.rpc.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import gr.ihu.robotics.homesense.domain.model.NodeSenseData;
import gr.ihu.robotics.homesense.domain.model.NodeSenseStatus;
import gr.ihu.robotics.homesense.service.rpc.NodeSenseRpcService;
import gr.ihu.robotics.homesense.service.utils.NetworkUtils;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NodeSenseRpcServiceImpl implements NodeSenseRpcService {

    private static List<String> getAvailableIps(final String ipPrefix) {
        return io.vavr.collection.List.range(0, 255)
                .map(ipSuffix -> String.format("%s.%s", ipPrefix, ipSuffix));
    }

    @Override
    public Try<List<NodeSenseStatus>> discover(final String ipPrefix, final Integer timeout) {
        return Try.of(() -> {
            List<String> availableIps = getAvailableIps(ipPrefix);
            final Collection<NodeSenseStatus> devicesMutable = new ArrayList<>();
            final ExecutorService executorService = Executors.newFixedThreadPool(20);
            try {
                for (String ip : availableIps) {
                    executorService.submit(() -> {
                        if (!(NetworkUtils.isDeviceOnline(ip, timeout))) {
                            return;
                        }
                        if (!(NetworkUtils.isPortOpen(ip, 80, timeout))) {
                            return;
                        }
                        Try<NodeSenseStatus> status = getStatus(ip, timeout);
                        if (status.isFailure()) {
                            return;
                        }
                        NodeSenseStatus nodeSenseStatus = status.get();
                        if (nodeSenseStatus == null) {
                            return;
                        }
                        devicesMutable.add(nodeSenseStatus);
                    });
                }
            } finally {
                executorService.shutdown();
                //noinspection StatementWithEmptyBody
                while (!executorService.isTerminated()) {
                    // Wait...
                }
            }
            return List.ofAll(devicesMutable);
        });
    }

    @Override
    public Try<NodeSenseStatus> getStatus(String ip, Integer timeout) {
        return Try.of(() -> {
            try {
                // ToDo: Implement SSL-only

                @SuppressWarnings("HttpUrlsUsage")
                URL url = new URL("http://" + ip + "/status");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return getJsonFromConnection(connection, NodeSenseStatus.class);
                } else {
                    System.err.println("Failed to fetch status from " + ip + ", HTTP response code: " + responseCode);
                }
            } catch (JsonSyntaxException e) {
                System.err.println("Error parsing JSON response from " + ip + ": " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error connecting to " + ip + ": " + e.getMessage());
            }

            return null;
        });
    }

    @Override
    public Try<NodeSenseData> getData(String ip, Integer timeout) {
        return Try.of(() -> {
            try {
                // ToDo: Implement SSL-only
                @SuppressWarnings("HttpUrlsUsage")
                URL url = new URL("http://" + ip + "/data");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return getJsonFromConnection(connection, NodeSenseData.class);
                } else {
                    System.err.println("Failed to fetch status from " + ip + ", HTTP response code: " + responseCode);
                }
            } catch (JsonSyntaxException e) {
                System.err.println("Error parsing JSON response from " + ip + ": " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error connecting to " + ip + ": " + e.getMessage());
            }

            return new NodeSenseData(
                    "offline", 404, "0", null
            );
        });
    }

    private <T> T getJsonFromConnection(HttpURLConnection connection, Class<T> clazz) throws IOException {
        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        Gson gson = new Gson();
        return gson.fromJson(response.toString(), clazz);
    }


}

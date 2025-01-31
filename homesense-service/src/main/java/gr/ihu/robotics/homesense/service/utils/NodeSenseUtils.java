package gr.ihu.robotics.homesense.service.utils;

import gr.ihu.robotics.homesense.common.SensorType;

public abstract class NodeSenseUtils {
    public static SensorType sensorToSensorType(final String sensor) {
        if (sensor == null || sensor.isEmpty()) {
            return null;
        }
        return switch (sensor) {
            case "Light" -> SensorType.LIGHT;
            case "Gas" -> SensorType.GAS;
            case "Temperature" -> SensorType.TEMPERATURE;
            default -> null;
        };
    }
}

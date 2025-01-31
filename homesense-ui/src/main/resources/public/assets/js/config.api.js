function getApiConfig() {
    const protocol = "http";
    const hostname = "homesense.local";
    const port = 8080;
    return {
        "protocol": protocol,
        "hostname": hostname,
        "port": port,
        "baseUrl": `${protocol}://${hostname}:${port}`,
    }
}

const SensorType = Object.freeze({
    TEMP: "TEMPERATURE",
    GAS: "GAS",
    LIGHT: "LIGHT"
});

function sensorTypeRawToSensorType(raw) {
    if (SensorType.TEMP === raw) {
        return SensorType.TEMP;
    }
    if (SensorType.GAS === raw) {
        return SensorType.GAS;

    }
    if (SensorType.LIGHT === raw) {
        return SensorType.LIGHT;
    }
    return null;
}
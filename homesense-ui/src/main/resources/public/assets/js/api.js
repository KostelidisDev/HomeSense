class SensorRpcApi {
    constructor(configApi) {
        this.configApi = configApi;
    }

    discoverSensors() {
        const url = this.getUrl('api/rpc/sensors/discover');
        console.log('Discovering sensors using url ' + url);
        return fetch(url)
            .then(response => response.json());
    }

    discoverSensor(ip) {
        const url = this.getUrl(`api/rpc/sensors/discover/${ip}`);
        console.log('Discovering sensor using url ' + url);
        return fetch(url)
            .then(response => response.json());
    }

    getUrl(path) {
        return `${this.configApi['baseUrl']}/${path}`;
    }
}

class SensorCrudApi {
    constructor(configApi) {
        this.configApi = configApi;
    }

    createSensor(sensorObject) {
        const url = this.getUrl('api/crud/sensors');
        console.log("Creating sensor with sensor object", sensorObject, ' using url ' + url);
        return fetch(url, {
            body: JSON.stringify(sensorObject),
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
    }

    getSensors() {
        const url = this.getUrl('api/crud/sensors');
        console.log("Getting all sensors using url", url);
        return fetch(url)
            .then(response => response.json())
            .then(sensors => sensors.map(sensor => {
                sensor.createdAt = DateUtils.convertLocalDateTimeToISODate(sensor.createdAt);
                sensor.updatedAt = DateUtils.convertLocalDateTimeToISODate(sensor.updatedAt);
                return sensor;
            }))
    }

    deleteSensorById(sensorId) {
        const url = this.getUrl(`api/crud/sensors/${sensorId}`);
        console.log("Delete sensor with sensor id ", sensorId, ' using url ' + url);
        return fetch(url, {method: 'DELETE'})
    }

    getUrl(path) {
        return `${this.configApi['baseUrl']}/${path}`;
    }
}

class DateUtils {
    static convertLocalDateTimeToISODate(dateArray) {
        if (dateArray.length < 3) {
            throw new Error("Invalid date array format");
        }

        const [year, month, day, hour = 0, minute = 0, second = 0, nanoseconds = 0] = dateArray;

        const milliseconds = Math.floor(nanoseconds / 1_000_000);

        const date = new Date(year, month - 1, day, hour, minute, second, milliseconds);

        return date.toISOString();
    }
}

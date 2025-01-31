function appendSensor(sensorObject, onAdd, onEdit, onDelete, callbackOnAction, containerObject) {
    const {id, name, ip, type, createdAt} = sensorObject;

    const row = document.createElement('tr');

    row.innerHTML = `
    <td>${name}</td>
    <td>${ip}</td>
    <td>${type}</td>
    <td>${createdAt}</td>
    <td>
      <button class="btn btn-success btn-sm">Add</button>
      <button class="btn btn-primary btn-sm">Edit</button>
      <button class="btn btn-danger btn-sm">Delete</button>
    </td>
  `;

    const addButton = row.querySelector('.btn-success');
    const editButton = row.querySelector('.btn-primary');
    const deleteButton = row.querySelector('.btn-danger');

    if (onAdd == null) {
        addButton.classList.add('disabled');
    } else {
        addButton.addEventListener('click', () => {
            onAdd({
                name: name,
                ip: ip,
                type: type
            }).then(() => callbackOnAction());
        });
    }

    if (onEdit == null) {
        editButton.classList.add('disabled');
    } else {
        editButton.addEventListener('click', () => {
            onEdit(id)
        });
    }

    if (onDelete == null) {
        deleteButton.classList.add('disabled');
    } else {
        deleteButton.addEventListener('click', () => {
            onDelete(id).then(() => callbackOnAction());
        });
    }

    containerObject.append(row);
}

function appendGraph(sensorName,
                     sensorType,
                     apiConfig,
                     sensorId,
                     lastMinutes,
                     graphsObject,
                     getMinValueForType,
                     getMaxValueForType,
                     getValuesToShowForType,
                     getColorForType,
                     getYLabelForType,
) {
    const canvasObject = document.createElement('canvas')
    canvasObject.id = `graph-${sensorName}`

    const divObject = document.createElement('div')
    divObject.classList.add('col-md-6')
    divObject.appendChild(canvasObject)

    graphsObject.appendChild(divObject)

    const DATA_URL = `${apiConfig['baseUrl']}/api/rpc/sensor-samples/chartjs/${sensorId}/${lastMinutes}/true`

    // noinspection JSUnusedGlobalSymbols,JSUnresolvedReference
    const liveChart = new Chart(`graph-${sensorName}`, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: sensorName,
                data: [], // Add data dynamically later
                pointBackgroundColor: (context) => getColorForType(context, sensorType),
                pointBorderColor: (context) => getColorForType(context, sensorType),
                backgroundColor: 'rgba(255, 255, 255, 0.2)',
                borderColor: 'rgba(255, 255, 255, 1)',
            }]
        },
        options: {
            responsive: true,
            animation: {
                duration: 5,
                easing: 'easeInOutQuad',
            },
            transitions: {
                active: {
                    animation: {
                        duration: 5,
                        easing: 'easeOutCubic'
                    }
                }
            },
            elements: {
                point: {
                    borderWidth: 0,
                    radius: 1,
                }
            },
            scales: {
                x: {
                    type: 'time', // Time-series x-axis
                    time: {
                        tooltipFormat: 'HH:mm:ss',
                        displayFormats: {
                            second: 'HH:mm:ss',
                        }
                    },
                    title: {
                        display: true,
                        text: 'Time'
                    }
                },
                y: {
                    beginAtZero: true,
                    max: getMaxValueForType(sensorType),
                    min: getMinValueForType(sensorType),
                    title: {
                        display: true,
                        text: getYLabelForType(sensorType),
                    }
                }
            }
        }
    });

    async function fetchDataAndUpdateChart() {
        const response = await fetch(DATA_URL);
        if (!response.ok) throw new Error('Failed to fetch data');

        const data = await response.json();

        const timestamps = data.createdAt;
        // noinspection JSUnresolvedReference
        const values = getValuesToShowForType(data.raw, data.processed, sensorType)

        liveChart.data.labels = timestamps;
        liveChart.data.datasets[0].data = values;

        return liveChart.update();
    }

    // Fetch data and update the chart every 5 seconds
    setInterval(fetchDataAndUpdateChart, 5000);

    // Initial data fetch
    fetchDataAndUpdateChart()
        .catch(error => console.error('Error fetching live data:', error))
        .then(() => {
        })
}


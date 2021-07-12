$(document).ready(function () {
    // Leaflet map...
    let geoDataCall = getCountryGeoLocations();
    geoDataCall.then(data => {
        const myMap = L.map('shorten-links-by-country').setView([51.505, -0.09], 3);
        const myGeoData = rebuildGeoLocationData(urlGroupByCountry, data);
        L.tileLayer.provider('OpenStreetMap.Mapnik').addTo(myMap);
        L.geoJson(myGeoData, {style: style}).addTo(myMap);
    });

    // Charts with chart js
    const platformData = separateLabelAndData(urlGroupByPlatform);
    const platformElement = document.getElementById('clicks-by-platform');
    const platformChart = new Chart(platformElement, {
        type: 'pie',
        data: {
            labels: platformData.label,
            datasets: [{
                label: 'Clicks by platform',
                data: platformData.dataset,
                backgroundColor: arrayColors(urlGroupByPlatform.length)
            }],
            hoverOffset: 4
        }
    });

    const browserData = separateLabelAndData(urlGroupByBrowser);
    const browserElement = document.getElementById('clicks-by-browser').getContext("2d");
    browserElement.canvas.width = 300;
    browserElement.canvas.height = 300;
    const browserChart = new Chart(browserElement, {
        type: 'doughnut',
        data: {
            labels: browserData.label,
            datasets: [{
                label: 'Clicks by Browser',
                data: browserData.dataset,
                backgroundColor: arrayColors(urlGroupByBrowser.length)
            }],
            hoverOffset: 4
        }
    });

    const osData = separateLabelAndData(urlGroupByOS);
    const osElement = document.getElementById('clicks-by-os');
    const osChart = new Chart(osElement, {
        type: 'pie',
        data: {
            labels: osData.label,
            datasets: [{
                data: osData.dataset,
                backgroundColor: arrayColors(urlGroupByOS.length)
            }],
            hoverOffset: 4
        }
    });
});

function separateLabelAndData(data) {
    let myLabels = [];
    let myData = [];
    for (let i = 0; i < data.length; i++) {
        myLabels.push(data[i].first);
        myData.push(data[i].second);
    }
    return {
        label: myLabels,
        dataset: myData
    }
}

function randomColor() {
    let r = Math.floor(Math.random() * 255);
    let g = Math.floor(Math.random() * 255);
    let b = Math.floor(Math.random() * 255);
    return "rgba(" + r + "," + g + "," + b + ", 0.5)";
}

function arrayColors(n) {
    let colors = [];
    for (let i = 0; i < n; i++) {
        let color = randomColor();
        colors.push(color);
    }
    return colors;
}

function getCountryGeoLocations() {
    return axios.get('/json/geocountries.json')
        .then(res => {
            return res.data;
        })
        .catch(err => {
            alertify.notify('Error fetching map coordinates. Map will show, but with no data.', 'error', 5);
            return Promise.reject(err);
        });
}

function rebuildGeoLocationData(myData, geoCountryData) {
    let features = geoCountryData.features;
    let myFeatures = [];
    console.log(features);
    for (let i = 0; i < features.length; i++) {
        for (let j = 0; j < myData.length; j++) {
            if (features[i].properties.name === myData[j].first) {
                features[i].properties["density"] = myData[j].second;
                myFeatures.push(features[i]);
            }
        }
    }
    return {
        type: "FeatureCollection",
        features: myFeatures
    }
}

function getColor(d) {
    return d > 1000 ? '#800026' :
        d > 500 ? '#BD0026' :
            d > 200 ? '#E31A1C' :
                d > 100 ? '#FC4E2A' :
                    d > 50 ? '#FD8D3C' :
                        d > 20 ? '#FEB24C' :
                            d > 10 ? '#FED976' :
                                '#FFEDA0';
}

function style(feature) {
    return {
        fillColor: getColor(feature.properties.density),
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7
    };
}
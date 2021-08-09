/**
 * Until document is ready...
 */
document.addEventListener("DOMContentLoaded", function (event) {

  // Leaflet js map...
  let geoDataCall = fetchCountryData();
  geoDataCall.then(data => {
    const myMap = L.map('shorten-links-by-country').setView([51.505, -0.09], 3);
    const myGeoData = buildLocationData(groupedByCountry, data);
    L.tileLayer.provider('OpenStreetMap.Mapnik').addTo(myMap);
    L.geoJson(myGeoData, {style: styleMap}).addTo(myMap);
  });

  // Drawing charts...
  const osChartOptions = createPieChartOptions(groupedByOs, 'pie');
  const browserChartOptions = createPieChartOptions(groupedByBrowser, 'doughnut');
  const platformChartOptions = createPieChartOptions(groupedByPlatform, 'pie');

  const osChart = new Chart(document.querySelector('#clicks-by-os'), osChartOptions);
  const browserChart = new Chart(document.querySelector('#clicks-by-browser'), browserChartOptions);
  const platformChart = new Chart(document.querySelector('#clicks-by-platform'), platformChartOptions);
});

function createPieChartOptions(pieData, type) {
  return {
    type: type,
    data: {
      labels: pieData.labels,
      datasets: [{
        label: 'Clicks by platform',
        data: pieData.series,
        backgroundColor: getRandomColors(pieData.series.length)
      }],
      hoverOffset: 4
    }
  }
}

function fetchCountryData() {
  return axios.get('/public/data/countries-geojson.json')
    .then(res => {
      return res.data;
    })
    .catch(err => {
      alertify.notify('Error fetching map coordinates. Map will show, but with no data.', 'error', 5);
      return Promise.reject(err);
    });
}

function buildLocationData(myData, geoCountryData) {
  let features = geoCountryData.features;
  let myFeatures = [];
  let myDataSize = myData.series.length;
  for (let i = 0; i < features.length; i++) {
    for (let j = 0; j < myDataSize; j++) {
      if (features[i].properties.ISO_A2 === myData.labels[j]) {
        features[i].properties["density"] = myData.series[j];
        myFeatures.push(features[i]);
      }
    }
  }
  return {
    type: "FeatureCollection",
    features: myFeatures
  }
}
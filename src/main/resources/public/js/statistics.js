$(document).ready(function () {
    google.charts.load('current', {
        'packages': ['geochart'],
        'mapsApiKey': MapsApiKey
    });

    google.charts.setOnLoadCallback(drawURLGroupByCountryData);

    $(window).resize(function () {
        drawURLGroupByCountryData();
    });
});


function prepareURLGroupByCountryData() {
    let data = URLGroupByCountryData;
    let result = [];
    result.push(['Country', 'Clicks'])
    for (let i = 0; i < data.length; i++) {
        result.push([data[i].country, data[i].quantity])
    }
    return result;
}

function drawURLGroupByCountryData() {
    let mapChartData = prepareURLGroupByCountryData();
    const data = google.visualization.arrayToDataTable(mapChartData);
    const options = {}
    const chart = new google.visualization.GeoChart(document.getElementById('shorten-links-by-country'));
    chart.draw(data, options);
}
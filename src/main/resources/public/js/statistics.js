$(document).ready(function () {
    google.charts.load('current', {
        'packages': ['geochart', 'corechart'],
        'mapsApiKey': MapsApiKey
    });

    google.charts.setOnLoadCallback(drawURLGroupByCountryData);
    google.charts.setOnLoadCallback(drawURLGroupByPlatform);

    $(window).resize(function () {
        drawURLGroupByCountryData();
        drawURLGroupByPlatform();
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

function prepareURLGroupByPlatform() {
    let data = URLGroupByPlatform;
    let result = [];
    result.push(['Platform', 'Clicks'])
    for (let i = 0; i < data.length; i++) {
        result.push([data[i].platform, data[i].quantity])
    }
    return result;
}

function drawURLGroupByPlatform() {
    let pieChartData = prepareURLGroupByPlatform();
    const data = google.visualization.arrayToDataTable(pieChartData);
    const options = {}
    const chart = new google.visualization.PieChart(document.getElementById('clicks-by-platform'));
    chart.draw(data, options);
}
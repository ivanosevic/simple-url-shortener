$(document).ready(function () {
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
    for(let i = 0; i < data.length; i++) {
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
    for(let i = 0; i < n; i++) {
        let color = randomColor();
        colors.push(color);
    }
    return colors;
}
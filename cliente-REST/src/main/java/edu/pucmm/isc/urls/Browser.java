package edu.pucmm.isc.urls;

import java.util.ArrayList;

public class Browser {
    private ArrayList<String> series;
    private ArrayList<String> labels;

    public Browser(ArrayList<String> series, ArrayList<String> labels) {
        this.series = series;
        this.labels = labels;
    }

    public ArrayList<String> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<String> series) {
        this.series = series;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }
}

package edu.pucmm.eict.urlshortener.reports;

import java.util.List;


public class ChartData<S, L>{
    private List<S> series;
    private List<L> labels;

    public ChartData() {
    }

    public ChartData(List<S> series, List<L> labels) {
        this.series = series;
        this.labels = labels;
    }

    public List<S> getSeries() {
        return series;
    }

    public void setSeries(List<S> series) {
        this.series = series;
    }

    public List<L> getLabels() {
        return labels;
    }

    public void setLabels(List<L> labels) {
        this.labels = labels;
    }
}

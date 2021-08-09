package edu.pucmm.eict.urlshortener.reports;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;

public class DataConverter {
    public  <S, L> ChartData<S, L> toChartData(List<Tuple> tuple) {
        ArrayList<S> series = new ArrayList<>();
        ArrayList<L> labels = new ArrayList<>();
        for(var t : tuple) {
            L label = (L) t.get(0);
            S seriesValue = (S) t.get(1);
            series.add(seriesValue);
            labels.add(label);
        }
        return new ChartData<>(series, labels);
    }
}

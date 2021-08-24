package edu.pucmm.eict.reports;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;

public class DataConverter {

    @SuppressWarnings("unchecked")
    public  <S, L> ChartData<S, L> toChartData(List<Tuple> tuple) {
        List<S> series = new ArrayList<>();
        List<L> labels = new ArrayList<>();
        for(var t : tuple) {
            L label = (L) t.get(0);
            S seriesValue = (S) t.get(1);
            series.add(seriesValue);
            labels.add(label);
        }
        return new ChartData<>(series, labels);
    }
}

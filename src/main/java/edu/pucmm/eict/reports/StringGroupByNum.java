package edu.pucmm.eict.reports;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;

public class StringGroupByNum {
    private String first;
    private Long second;

    public StringGroupByNum() {
    }

    public StringGroupByNum(String first, Long second) {
        this.first = first;
        this.second = second;
    }

    public static List<StringGroupByNum> convertFromTuple(List<Tuple> result) {
        List<StringGroupByNum> data = new ArrayList<>();
        for(var t : result) {
            String first = (String) t.get(0);
            Long second = (Long) t.get(1);
            data.add(new StringGroupByNum(first, second));
        }
        return data;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public Long getSecond() {
        return second;
    }

    public void setSecond(Long second) {
        this.second = second;
    }
}
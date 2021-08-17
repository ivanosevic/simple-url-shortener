package edu.pucmm.eict.persistence;

import java.util.List;

public class Page<T> {
    private int totalPages;
    private int currentPage;
    private boolean isFirst;
    private boolean isLast;
    private boolean isEmpty;
    private List<T> results;

    public Page(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public Page(boolean isEmpty, List<T> results) {
        this.isEmpty = isEmpty;
        this.results = results;
    }

    public Page(int totalPages, int currentPage, boolean isFirst, boolean isLast, List<T> results) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.results = results;
        this.isEmpty = results.isEmpty();
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}


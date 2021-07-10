package edu.pucmm.eict.reports;

public class URLGroupByCountry {
    private String country;
    private Long quantity;

    public URLGroupByCountry() {
    }

    public URLGroupByCountry(String country, Long quantity) {
        this.country = country;
        this.quantity = quantity;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "URLGroupByCountry{" +
                "country='" + country + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
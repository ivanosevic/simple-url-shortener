package edu.pucmm.eict.reports;

public class URLGroupByPlataform {
    private String platform;
    private Long quantity;

    public URLGroupByPlataform(String platform, Long quantity) {
        this.platform = platform;
        this.quantity = quantity;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}

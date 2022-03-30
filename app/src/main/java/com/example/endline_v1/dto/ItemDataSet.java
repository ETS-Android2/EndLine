package com.example.endline_v1.dto;

public class ItemDataSet {

    private String name, category, buy_date, end_date, photo_url;

    public ItemDataSet(String name, String category, String buy_date, String end_date, String photo_url) {
        this.name = name;
        this.category = category;
        this.buy_date = buy_date;
        this.end_date = end_date;
        this.photo_url = photo_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBuy_date() {
        return buy_date;
    }

    public void setBuy_date(String buy_date) {
        this.buy_date = buy_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}

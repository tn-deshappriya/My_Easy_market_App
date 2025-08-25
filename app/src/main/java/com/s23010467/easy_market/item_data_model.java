package com.s23010467.easy_market;

public class item_data_model {
    String item_name,per_unit,discount,lowest_price,highest_price,wholesale_price,retail_price;

    item_data_model(){

    }
    public item_data_model(String item_name, String per_unit, String discount, String lowest_price, String highest_price, String wholesale_price, String retail_price) {
        this.item_name = item_name;
        this.per_unit = per_unit;
        this.discount = discount;
        this.lowest_price = lowest_price;
        this.highest_price = highest_price;
        this.wholesale_price = wholesale_price;
        this.retail_price = retail_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPer_unit() {
        return per_unit;
    }

    public void setPer_unit(String per_unit) {
        this.per_unit = per_unit;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getLowest_price() {
        return lowest_price;
    }

    public void setLowest_price(String lowest_price) {
        this.lowest_price = lowest_price;
    }

    public String getHighest_price() {
        return highest_price;
    }

    public void setHighest_price(String highest_price) {
        this.highest_price = highest_price;
    }

    public String getWholesale_price() {
        return wholesale_price;
    }

    public void setWholesale_price(String wholesale_price) {
        this.wholesale_price = wholesale_price;
    }

    public String getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(String retail_price) {
        this.retail_price = retail_price;
    }
}

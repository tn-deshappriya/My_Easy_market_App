package com.s23010467.easy_market;

public class Market {
    public String marketid;
    public String market_name;
    public String market_address;
    public String market_contact;
    public String market_category;
    public String ownerId;

    public Boolean isVerified;

    public Market(){

    }
    public Market(String marketid,String market_name,String market_address,String market_contact,String market_category,String ownerId,boolean isVerified){
        this.marketid= marketid;
        this.market_name= market_name;
        this.market_address = market_address;
        this.market_contact=market_contact;
        this.market_category = market_category;
        this.ownerId= ownerId;
        this.isVerified = isVerified;
    }
}

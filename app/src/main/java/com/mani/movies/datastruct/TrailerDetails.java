package com.mani.movies.datastruct;

public class TrailerDetails {


    public TrailerDetails(String name, String key, String site) {
        this.name = name;
        this.key = key;
        this.site = site;
    }
    private String name;
    private String key;
    private String site;

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }



}

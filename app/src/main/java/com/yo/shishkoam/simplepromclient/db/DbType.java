package com.yo.shishkoam.simplepromclient.db;

/**
 * Created by User on 26.02.2017
 */

public enum DbType {
    CACHE("cachetable"), FAVORITES("favoritetable");

    DbType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
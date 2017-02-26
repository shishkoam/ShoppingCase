package com.yo.shishkoam.simplepromclient.api;

/**
 * Created by User on 26.02.2017
 */

public class ApiRequest {

    private int limit;
    private int offset;
    private long category;
    private String sortType;

    public ApiRequest(int limit, int offset, long category, String sortType) {
        this.limit = limit;
        this.offset = offset;
        this.category = category;
        this.sortType = sortType;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}

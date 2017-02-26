
package com.yo.shishkoam.simplepromclient.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Catalog {

    @SerializedName("possible_sorts")
    @Expose
    private List<String> possibleSorts = null;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public List<String> getPossibleSorts() {
        return possibleSorts;
    }

    public void setPossibleSorts(List<String> possibleSorts) {
        this.possibleSorts = possibleSorts;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}

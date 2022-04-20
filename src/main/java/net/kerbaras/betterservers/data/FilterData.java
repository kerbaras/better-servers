package net.kerbaras.betterservers.data;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FilterData {
    private String query;
    private List<String> labels = new ArrayList<>();

    public FilterData (String query, @Nullable  List<String> labels){
        this.query = query;
        if (labels != null){
            this.labels = labels;
        }
    }

    public String getQuery() {
        return query;
    }

    public List<String> getLabels() {
        return labels;
    }
}

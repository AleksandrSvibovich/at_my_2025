package test.api.dto;

import test.api.dto.enums.STATUS;
import test.api.dto.enums.TYPES;

import java.util.ArrayList;
import java.util.List;

public class Lists {

    private int limit;
    private Filter filter;

    public Lists(int limit, String searchQuery, ArrayList<TYPES> type, ArrayList<STATUS> status) {
        this.limit = limit;
        this.filter = new Filter(searchQuery, type, status);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Filter getFilter() {
        return filter;
    }

}

class Filter {
    private String searchQuery;
    private ArrayList<TYPES> type;
    private ArrayList<STATUS> status;


    public Filter(String searchQuery, ArrayList<TYPES> type, ArrayList<STATUS> status) {
        this.searchQuery = searchQuery;
        this.type = type;
        this.status = status;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public ArrayList<TYPES> getTypes() {
        return type;
    }

    public ArrayList<STATUS> getStatus() {
        return status;
    }

    public void setStatus(ArrayList<STATUS> status) {
        this.status = status;
    }

    public void setTypes(ArrayList<TYPES> type) {
        this.type = type;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }


}
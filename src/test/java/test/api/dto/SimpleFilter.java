package test.api.dto;

public class SimpleFilter {
    public SimpleFilter(int limit, String searchString) {
        this.limit = limit;
        filterSearch = new FilterSearch(searchString);
    }

    public int getLimit() {
        return limit;
    }

    public int limit;
    public FilterSearch filterSearch;
}

class FilterSearch {
    public FilterSearch(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    String searchQuery;
}

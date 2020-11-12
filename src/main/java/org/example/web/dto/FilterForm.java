package org.example.web.dto;

import javax.validation.constraints.NotEmpty;

public class FilterForm {
    @NotEmpty
    private String typeFilter;
    @NotEmpty
    private String regex;

    public String getTypeFilter() { return this.typeFilter; }
    public String getRegex() { return this.regex; }
    public void setTypeFilter(String value) { this.typeFilter = value; }
    public void setRegex(String value) { this.regex = value; }
}

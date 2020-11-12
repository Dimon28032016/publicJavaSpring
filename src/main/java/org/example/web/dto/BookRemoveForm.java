package org.example.web.dto;

import javax.validation.constraints.NotEmpty;

public class BookRemoveForm {

    @NotEmpty
    private String type;

    @NotEmpty
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

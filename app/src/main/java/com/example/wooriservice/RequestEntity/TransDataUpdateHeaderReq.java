package com.example.wooriservice.RequestEntity;

import java.io.Serializable;

public class TransDataUpdateHeaderReq implements Serializable {
    private Integer id;
    private String category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) {
        this.category = category;
    }
}

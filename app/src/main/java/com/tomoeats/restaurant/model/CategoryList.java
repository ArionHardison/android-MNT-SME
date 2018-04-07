package com.tomoeats.restaurant.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tamil on 3/1/2018.
 */

public class CategoryList {

    String header;
    List<Product> productList = new ArrayList<>();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> availables) {
        this.productList = availables;
    }
}

package com.restaurantaround.app.model;

import com.restaurantaround.app.model.product.ProductResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tamil on 3/1/2018.
 */

public class ProductModel {

    String header;
    List<ProductResponse> productList = new ArrayList<>();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<ProductResponse> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductResponse> availables) {
        this.productList = availables;
    }
}

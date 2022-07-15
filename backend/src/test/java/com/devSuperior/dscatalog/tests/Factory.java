package com.devSuperior.dscatalog.tests;

import com.devSuperior.dscatalog.DTO.ProductDTO;
import com.devSuperior.dscatalog.entities.Category;
import com.devSuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){

        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "www.ghoogle.com", Instant.parse(("2020-10-20T03:00:00Z")));
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
    public static Category createCategory(){
        return new Category(2L, "Eletronics");
    }
}

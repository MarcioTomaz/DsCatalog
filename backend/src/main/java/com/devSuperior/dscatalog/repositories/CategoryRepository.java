package com.devSuperior.dscatalog.repositories;


import com.devSuperior.dscatalog.entities.Category;
import com.devSuperior.dscatalog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {


    @Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE "
            + "(COALESCE(:categories) IS NULL OR cats IN :categories) AND "
            + "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%'))) ")
    Page<Product> find(List<Category> categories, String name, Pageable pageable);

    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products")
    List<Product> findProductsWithCategories(List<Product> products);
    //Fetch ira buscar o produto junto com os obj das categorias e ele n funciona com page so funciona com list
}
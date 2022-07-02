package com.devSuperior.dscatalog.resources;

import com.devSuperior.dscatalog.Services.CategoryService;
import com.devSuperior.dscatalog.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {


    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>>findAll(){

        List<Category> list = categoryService.findALl();

        return ResponseEntity.ok().body(list);
    }
}

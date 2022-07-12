package com.devSuperior.dscatalog.Services;

import com.devSuperior.dscatalog.DTO.CategoryDTO;
import com.devSuperior.dscatalog.entities.Category;
import com.devSuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findALl(){

        List<Category> result = categoryRepository.findAll();

        return result.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
    };
}

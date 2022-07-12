package com.devSuperior.dscatalog.Services;

import com.devSuperior.dscatalog.DTO.CategoryDTO;
import com.devSuperior.dscatalog.Services.exceptions.ResourceNotFoundException;
import com.devSuperior.dscatalog.entities.Category;
import com.devSuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){

        Optional<Category> obj = categoryRepository.findById(id);
        Category entity = obj.orElseThrow( () -> new ResourceNotFoundException("Entity not found"));

        return new CategoryDTO(entity);
    };

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {

        Category entity = new Category();
        entity.setName(dto.getName());

        entity = categoryRepository.save(entity);

        return new CategoryDTO(entity);
    }
}

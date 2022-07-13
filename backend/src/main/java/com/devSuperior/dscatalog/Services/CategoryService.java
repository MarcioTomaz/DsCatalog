package com.devSuperior.dscatalog.Services;

import com.devSuperior.dscatalog.DTO.CategoryDTO;
import com.devSuperior.dscatalog.Services.exceptions.DatabaseException;
import com.devSuperior.dscatalog.Services.exceptions.ResourceNotFoundException;
import com.devSuperior.dscatalog.entities.Category;
import com.devSuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
=======
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
>>>>>>> 2ad20421da2332b8bc9053df0af8a57aee89b62c
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

<<<<<<< HEAD
    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {

        Category entity = new Category();
        entity.setName(dto.getName());

        entity = categoryRepository.save(entity);

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {

        try {

            Category entity = categoryRepository.getOne(id);
            entity.setName(dto.getName());

            entity = categoryRepository.save(entity);

            return new CategoryDTO(entity);

        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }

    }

    public void delete(Long id) {

        try {
            categoryRepository.deleteById(id);

        }catch (EmptyResultDataAccessException e){

            throw new ResourceNotFoundException("Id not found " + id);

        }catch ( DataIntegrityViolationException e){

            throw new DatabaseException("Integrity violetion");
        }
=======
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {

        Page<Category> result = categoryRepository.findAll(pageRequest);

        return result.map(x -> new CategoryDTO(x));
>>>>>>> 2ad20421da2332b8bc9053df0af8a57aee89b62c
    }
}

package com.devSuperior.dscatalog.Services;

import com.devSuperior.dscatalog.DTO.CategoryDTO;
import com.devSuperior.dscatalog.DTO.ProductDTO;
import com.devSuperior.dscatalog.Services.exceptions.DatabaseException;
import com.devSuperior.dscatalog.Services.exceptions.ResourceNotFoundException;
import com.devSuperior.dscatalog.entities.Category;
import com.devSuperior.dscatalog.entities.Product;
import com.devSuperior.dscatalog.repositories.CategoryRepository;
import com.devSuperior.dscatalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {

        Page<Product> result = productRepository.findAll(pageRequest);

        return result.map(x -> new ProductDTO(x));

    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findALl(){

        List<Product> result = productRepository.findAll();

        return result.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
    };

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow( () -> new ResourceNotFoundException("Entity not found") );

        return new ProductDTO(entity, entity.getCategories());
    }


    @Transactional
    public ProductDTO insert(ProductDTO dto) {

        Product entity = new Product();
        //entity.setName(dto.getName());

        entity = productRepository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {

        try {

            Product entity = productRepository.getOne(id);
           // entity.setName(dto.getName());

            entity = productRepository.save(entity);

            return new ProductDTO(entity);

        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }

    }

    public void delete(Long id) {

        try {
            productRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {

            throw new ResourceNotFoundException("Id not found " + id);

        } catch (DataIntegrityViolationException e) {

            throw new DatabaseException("Integrity violetion");
        }
    }

}

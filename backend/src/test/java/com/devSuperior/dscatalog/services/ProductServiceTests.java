package com.devSuperior.dscatalog.services;

import com.devSuperior.dscatalog.DTO.ProductDTO;
import com.devSuperior.dscatalog.Services.CategoryService;
import com.devSuperior.dscatalog.Services.ProductService;
import com.devSuperior.dscatalog.Services.exceptions.DatabaseException;
import com.devSuperior.dscatalog.Services.exceptions.ResourceNotFoundException;
import com.devSuperior.dscatalog.entities.Category;
import com.devSuperior.dscatalog.entities.Product;
import com.devSuperior.dscatalog.repositories.CategoryRepository;
import com.devSuperior.dscatalog.repositories.ProductRepository;
import com.devSuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class) // Não carrega o contexto, mas permite usar os recursos do spring JUnit ( teste de unidade: service/component )
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock //Usar quando a classe de teste não carrega o contexto da aplicação. É mais rápido e enxuto.     @ExtendWith
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setup() throws Exception{

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));
        category = Factory.createCategory();

        Mockito.when( repository.findAll((Pageable) any())).thenReturn(page);
        Mockito.when( repository.save(any())).thenReturn(product);

        Mockito.when( repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when( repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when( categoryRepository.find(any(), any(), any())).thenReturn(page);

        Mockito.when( repository.getOne(existingId)).thenReturn(product);
        Mockito.when( repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when( categoryRepository.getOne(existingId)).thenReturn(category);
        Mockito.when( categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.doNothing().when( repository ).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when( repository ).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when ( repository ).deleteById(dependentId);
    }

//    @Test
//    public void updateShouldReturnProductDTOWhenIdExists(){
//
//        ProductDTO productDTO1 = Factory.createProductDTO();
//
//        ProductDTO result = service.update(existingId, productDTO1);
//
//        Assertions.assertNotNull(result);
//    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists(){

        ProductDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows((ResourceNotFoundException.class),() -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPage(){

        Pageable pageable = PageRequest.of(0,10);

        Page<ProductDTO> result = service.findAllPaged(0L, "",pageable);

        Assertions.assertNotNull(result);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows((ResourceNotFoundException.class), () -> {
            service.delete(nonExistingId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){

        Assertions.assertThrows((DatabaseException.class), () -> {
            service.delete(dependentId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }

}

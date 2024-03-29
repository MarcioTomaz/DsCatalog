package com.devSuperior.dscatalog.resources;

import com.devSuperior.dscatalog.DTO.ProductDTO;
import com.devSuperior.dscatalog.Services.ProductService;
import com.devSuperior.dscatalog.Services.exceptions.DatabaseException;
import com.devSuperior.dscatalog.Services.exceptions.ResourceNotFoundException;
import com.devSuperior.dscatalog.tests.Factory;
import com.devSuperior.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean //Usar quando a classe de teste carrega o contexto da aplicação e precisa mockar algum bean do sistema. @WebMvcTest @SpringBootTest
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    private long existingId;
    private long nonExistingId;
    private long dependentId;

    @Autowired
    private TokenUtil tokenUtil;

    private String username;
    private String password;

    @BeforeEach
    void setUp() throws Exception{

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        username = "maria@gmail.com";
        password = "123456";

        productDTO = Factory.createProductDTO();

        page = new PageImpl<>(List.of(productDTO));

        Mockito.when(service.findAllPaged( any(), any(), any() )).thenReturn(page);

        Mockito.when(service.findById(existingId)).thenReturn(productDTO);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.update(eq(existingId), any())).thenReturn(productDTO);
        Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(service).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);

        Mockito.when(service.insert(any())).thenReturn(productDTO);
    }

    @Test
    public void insertShouldReturnCreatedAndProductDTO() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);


        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =  mockMvc.perform(post("/products")
                .content(jsonBody)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void deletedShouldReturnNoContentWhenIdDoesNotExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =  mockMvc.perform(delete("/products/{id}", nonExistingId)
                .content(jsonBody)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deletedShouldReturnNoContentWhenIdExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =  mockMvc.perform(delete("/products/{id}", existingId)
                .content(jsonBody)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);


        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =  mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnNotFoundExceptionWhenIdDoesNotExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);


        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result =  mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShoulReturnProductWhenIdExists() throws Exception {

        ResultActions result =  mockMvc.perform(get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShoulReturnNotFoundWhenIdDoesNotExists() throws Exception {

        ResultActions result =  mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {

        mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

    }

}

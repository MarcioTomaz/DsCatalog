package com.devSuperior.dscatalog.resources;

import com.devSuperior.dscatalog.DTO.ProductDTO;
import com.devSuperior.dscatalog.DTO.UserDTO;
import com.devSuperior.dscatalog.DTO.UserInsertDTO;
import com.devSuperior.dscatalog.DTO.UserUpdateDTO;
import com.devSuperior.dscatalog.Services.ProductService;
import com.devSuperior.dscatalog.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>>findAll(Pageable pageable){

        // Parametros : page, size, sort

        Page<UserDTO> list = userService.findAllPaged(pageable);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {

        UserDTO dto = userService.findById(id);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {

        UserDTO newDto = userService.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();

        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> update(@Valid @PathVariable Long id, @RequestBody @Valid UserUpdateDTO dto) {

        UserDTO newDto = userService.update(id, dto);

        return ResponseEntity.ok().body(newDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDTO> delete(@PathVariable Long id) {

        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

}

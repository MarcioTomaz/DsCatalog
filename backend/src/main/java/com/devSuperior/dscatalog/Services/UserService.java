package com.devSuperior.dscatalog.Services;

import com.devSuperior.dscatalog.DTO.RoleDTO;
import com.devSuperior.dscatalog.DTO.UserDTO;
import com.devSuperior.dscatalog.DTO.UserInsertDTO;
import com.devSuperior.dscatalog.DTO.UserUpdateDTO;
import com.devSuperior.dscatalog.Services.exceptions.DatabaseException;
import com.devSuperior.dscatalog.Services.exceptions.ResourceNotFoundException;
import com.devSuperior.dscatalog.entities.Role;
import com.devSuperior.dscatalog.entities.User;
import com.devSuperior.dscatalog.repositories.RoleRepository;
import com.devSuperior.dscatalog.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Page<UserDTO> findAllPaged(Pageable pageable) {

        Page<User> result = userRepository.findAll(pageable);

        return result.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findALl(){

        List<User> result = userRepository.findAll();

        return result.stream().map(UserDTO::new).collect(Collectors.toList());
    };

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {

        Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow( () -> new ResourceNotFoundException("Entity not found") );

        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {

        User entity = new User();
        copyDtoToEntity(dto, entity);

        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {

        try {

            User entity = userRepository.getOne(id);
            copyDtoToEntity(dto, entity);

            entity = userRepository.save(entity);

            return new UserDTO(entity);

        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {

        try {
            userRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {

            throw new ResourceNotFoundException("Id not found " + id);

        } catch (DataIntegrityViolationException e) {

            throw new DatabaseException("Integrity violetion");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();

        for (RoleDTO roleDto : dto.getRoles()){
            Role role = roleRepository.getOne(roleDto.getId());
            entity.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(userName);

        if( user == null){
            logger.error("user not found: " + userName);
            throw new UsernameNotFoundException("Email not found");
        }

        logger.info("User found: " + userName);
        return user;
    }
}

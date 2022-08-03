package com.devSuperior.dscatalog.repositories;

import com.devSuperior.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    List<User> findByFirstName(String firstName);
}

package com.devSuperior.dscatalog.repositories;

import com.devSuperior.dscatalog.entities.Role;
import com.devSuperior.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}

package com.tsp.dao;

import com.tsp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long>{
    Role findByName(String name);
}

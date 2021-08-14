package com.psk.becalm.model.repository;

import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(RoleUserEnum roleUserEnum);
}

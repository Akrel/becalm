package com.psk.becalm.model.repository;

import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleUserEnum> {
    Role findByRole(RoleUserEnum roleUserEnum);
}

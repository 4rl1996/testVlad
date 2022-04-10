package com.example.qaservice.repository;

import com.example.qaservice.data.entity.AdminRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRoleRepository extends CrudRepository<AdminRole, Long> {

    Optional<AdminRole> findAdminRoleByLoginAndPassword(String login, String password);

}

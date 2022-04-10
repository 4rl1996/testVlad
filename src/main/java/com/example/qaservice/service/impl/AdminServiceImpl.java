package com.example.qaservice.service.impl;

import com.example.qaservice.data.entity.AdminRole;
import com.example.qaservice.repository.AdminRoleRepository;
import com.example.qaservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRoleRepository repository;

    @Override
    public boolean isAdmin(AdminRole role) {
        return repository.findAdminRoleByLoginAndPassword(role.getLogin(), role.getPassword()).isPresent();
    }
}

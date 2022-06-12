package com.example.qaservice.service.impl;

import com.example.qaservice.data.entity.AdminRole;
import com.example.qaservice.repository.AdminRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class AdminServiceImplTest {

    @Mock
    private AdminRoleRepository repository;

    @InjectMocks
    private AdminServiceImpl testedService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isAdminTrueTest() {

        AdminRole adminRole = adminRole();

        when(repository.findAdminRoleByLoginAndPassword("roma", "2")).thenReturn(Optional.of(adminRole));

        boolean actual = testedService.isAdmin(adminRole);

        assertThat(actual).isTrue();
    }

    @Test
    void isAdminFalseTest() {

        AdminRole adminRole = adminRole();

        when(repository.findAdminRoleByLoginAndPassword("roma", "2")).thenReturn(Optional.empty());

        boolean actual = testedService.isAdmin(adminRole);

        assertThat(actual).isFalse();
    }

    private AdminRole adminRole() {
        return AdminRole.builder()
            .id(1L)
            .login("roma")
            .password("2")
            .build();
    }
}
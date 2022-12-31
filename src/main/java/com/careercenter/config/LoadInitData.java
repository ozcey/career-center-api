package com.careercenter.config;

import com.careercenter.entities.Role;
import com.careercenter.model.RoleName;
import com.careercenter.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadInitData {

    private final RoleRepository roleRepository;

    @EventListener
    public void loadData(ApplicationReadyEvent readyEvent) {
        Boolean adminRoleExists = roleRepository.existsByName(RoleName.ROLE_ADMIN);
        Boolean userRoleExists = roleRepository.existsByName(RoleName.ROLE_USER);
        if (!adminRoleExists && !userRoleExists) {
            roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build());
            roleRepository.save(Role.builder().name(RoleName.ROLE_USER).build());
        }
    }
}

package com.careercenter.config;

import com.careercenter.entities.Role;
import com.careercenter.model.RoleName;
import com.careercenter.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
@Profile("prod")
@RequiredArgsConstructor
public class LoadRoleData {

    private final RoleRepository roleRepository;

    @EventListener
    public void loadData(ApplicationReadyEvent readyEvent) {
        long count = roleRepository.count();
        if(count == 0){
            List<RoleName> roleNames = Arrays.asList(RoleName.ROLE_ADMIN, RoleName.ROLE_USER);
            for(RoleName roleName: roleNames){
                roleRepository.save(Role.builder().name(roleName).build());
            }
        }

    }
}

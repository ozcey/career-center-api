package com.careercenter;

import com.careercenter.entities.Role;
import com.careercenter.model.RoleName;
import com.careercenter.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CareerCenterApplication {

	public static void main(String[] args) {

		SpringApplication.run(CareerCenterApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner saveRoles(RoleRepository roleRepository) {
//		return args -> {
//			roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build());
//			roleRepository.save(Role.builder().name(RoleName.ROLE_USER).build());
//
//		};
//	}

}

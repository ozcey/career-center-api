package com.careercenter.integration;

import com.careercenter.entities.Address;
import com.careercenter.entities.Applicant;
import com.careercenter.entities.Role;
import com.careercenter.entities.User;
import com.careercenter.exception.NotFoundException;
import com.careercenter.model.RoleName;
import com.careercenter.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class IntegrationTestData {

    public static List<Applicant> setApplicantList() {
        Applicant applicant = Applicant.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .phone("1234567890")
                .age(30)
                .category(new String[]{"IT", "Education"})
                .degree("B.S")
                .gender("Male")
                .languages(new String[]{"English"})
                .address(Address.builder()
                        .street("123 Main St")
                        .city("Atlanta")
                        .state("GA")
                        .zipcode("12345")
                        .build())
                .build();
        Applicant applicant2 = Applicant.builder()
                .firstName("Mark")
                .lastName("Johnson")
                .email("mark@gmail.com")
                .phone("1234560000")
                .age(35)
                .category(new String[]{"IT", "Education"})
                .degree("B.S")
                .gender("Male")
                .languages(new String[]{"English"})
                .address(Address.builder()
                        .street("111 Elm Dr")
                        .city("Atlanta")
                        .state("GA")
                        .zipcode("11456")
                        .build())
                .build();
        return Arrays.asList(applicant, applicant2);
    }

    public static List<User> setUserList(RoleRepository roleRepository){
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(NotFoundException::new);
        HashSet<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        User adminUser = User.builder()
                .name("Admin User")
                .username("admin")
                .email("admin@gmail.com")
                .password("password")
                .roles(adminRoles)
                .build();
        Role userRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(NotFoundException::new);
        HashSet<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        User user = User.builder()
                .name("Seed User")
                .username("seed")
                .email("seed@gmail.com")
                .password("password")
                .roles(userRoles)
                .build();
        return Arrays.asList(adminUser, user);
    }
}

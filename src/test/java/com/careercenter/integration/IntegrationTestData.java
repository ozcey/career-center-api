package com.careercenter.integration;

import com.careercenter.entities.Address;
import com.careercenter.entities.Applicant;
import com.careercenter.entities.Role;
import com.careercenter.entities.User;
import com.careercenter.exception.NotFoundException;
import com.careercenter.model.AddressRequest;
import com.careercenter.model.ApplicantRequest;
import com.careercenter.model.RoleName;
import com.careercenter.repositories.RoleRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static ApplicantRequest getApplicantRequest(){
        return ApplicantRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .phone("1234567890")
                .age(30)
                .category(new String[]{"IT", "Education"})
                .degree("B.S")
                .gender("Male")
                .languages(new String[]{"English"})
                .address(AddressRequest.builder()
                        .street("123 Main St")
                        .city("Atlanta")
                        .state("GA")
                        .zipcode("12345")
                        .build())
                .build();
    }

    public static List<User> setUserList(RoleRepository roleRepository){
        List<User> userList = setUsersWithoutRoles();
//        set admin role to user at index 0
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(NotFoundException::new);
        HashSet<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        userList.get(0).setRoles(adminRoles);
//        set user role to user at index 1
        Role userRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(NotFoundException::new);
        HashSet<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        userList.get(1).setRoles(userRoles);
        return userList;
    }

    public static List<User> setUsersWithoutRoles(){
        User adminUser = User.builder()
                .name("Admin User")
                .username("admin")
                .email("admin@gmail.com")
                .password("password")
                .build();
        User user = User.builder()
                .name("Seed User")
                .username("seed")
                .email("seed@gmail.com")
                .password("password")
                .build();
        return Arrays.asList(adminUser, user);
    }

    public static void setUserRoles(User user, String role){
        Set<Role> roles = new HashSet<>();
        Role userRole;
        if(role.equals("ROLE_ADMIN")){
           userRole = Role.builder().name(RoleName.ROLE_ADMIN).build();
        } else if(role.equals("ROLE_USER")){
            userRole = Role.builder().name(RoleName.ROLE_USER).build();
        } else {
            throw new NotFoundException("user role");
        }
        roles.add(userRole);
        user.setRoles(roles);
    }
}

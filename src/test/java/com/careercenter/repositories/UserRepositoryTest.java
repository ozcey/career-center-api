package com.careercenter.repositories;

import com.careercenter.entities.Role;
import com.careercenter.entities.User;
import com.careercenter.integration.IntegrationTestData;
import com.careercenter.model.RoleName;
import com.careercenter.repositories.RoleRepository;
import com.careercenter.repositories.UserRepository;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build());
        roleRepository.save(Role.builder().name(RoleName.ROLE_USER).build());
        userList = IntegrationTestData.setUserList(roleRepository);
    }

    @Test
    void findAll() {
        userRepository.saveAll(userList);

        List<User> users = userRepository.findAll();

        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(userList.size());
    }

    @Test
    void findUserByEmail() {
        User user = userList.get(0);
        userRepository.save(user);

        User entity = userRepository.findUserByEmail(user.getEmail()).get();

        assertThat(entity).isNotNull();
        assertThat(entity.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findUserByUsername() {
        User user = userList.get(0);
        userRepository.save(user);

        User entity = userRepository.findUserByUsername(user.getUsername()).get();

        assertThat(entity).isNotNull();
        assertThat(entity.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void findUserById() {
        User user = userList.get(0);
        User savedUser = userRepository.save(user);

        User entity = userRepository.findUserById(savedUser.getId()).get();

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void existsByUsername() {
        User user = userList.get(0);
        userRepository.save(user);

        Boolean usernameExists = userRepository.existsByUsername(user.getUsername());

        assertThat(usernameExists).isTrue();
    }

    @Test
    void existsByEmail() {
        User user = userList.get(0);
        userRepository.save(user);

        Boolean emailExists = userRepository.existsByEmail(user.getEmail());

        assertThat(emailExists).isTrue();
    }

    @Test
    void save(){
        User user = userList.get(0);

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
    }

    @Test
    void update(){
        User user = userList.get(0);
        User savedUser = userRepository.save(user);
        savedUser.setName("Mike Scott");
        savedUser.setPassword("1234567890");

        User entity = userRepository.save(savedUser);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(savedUser.getId());
        assertThat(entity.getName()).isEqualTo(savedUser.getName());
        assertThat(entity.getPassword()).isEqualTo(savedUser.getPassword());
    }

    @Test
    void delete(){
        User user = userList.get(0);
        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        assertThrows(NoSuchElementException.class, () -> {
           User entity = userRepository.findUserById(savedUser.getId()).orElseThrow(NoSuchElementException::new);
        });
    }
}
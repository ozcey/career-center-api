package com.careercenter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.careercenter.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select u from User u where u.email = :email")
	Optional<User> findUserByEmail(@Param("email") String email);

	@Query("select u from User u where u.username = :username")
	Optional<User> findUserByUsername(String username);

	@Query("select u from User u where id = :id")
	Optional<User> findUserById(@Param("id") Long id);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

}

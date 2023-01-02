package com.careercenter.entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 2, max = 100, message = "Name must be at least 2 characters.")
	@NotNull(message = "Name cannot be null")
	@Column(name = "name")
	private String name;

	@NaturalId
	@Size(min = 3, max = 50, message = "Username must be at least 2 characters.")
	@NotNull(message = "Username cannot be null")
	@Column(name = "username", unique = true)
	private String username;

	@NaturalId
	@Email(message = "Please enter a valid email.")
	@NotNull(message = "Email cannot be null")
	@Column(name = "email", unique = true)
	private String email;

	@Size(min = 8, message = "Password must be at least 8 characters.")
	@NotNull(message = "Password cannot be null")
	@Column(name = "password")
	private String password;

}

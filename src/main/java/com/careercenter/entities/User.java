package com.careercenter.entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.careercenter.entities.Role;
import org.hibernate.annotations.NaturalId;

import javax.persistence.JoinColumn;

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
@SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1, initialValue = 10001)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
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

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<Role>();

	public User(Long id, String name, String username, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
	}

}

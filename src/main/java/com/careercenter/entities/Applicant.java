package com.careercenter.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.annotations.NaturalId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "applicants")
public class Applicant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 2, max = 50, message = "First Name must be at least 2 characters.")
	@NotNull(message = "First Name cannot be null.")
	@Column(name = "first_name")
	private String firstName;

	@Size(min = 2, max = 50, message = "Last Name must be at least 2 characters.")
	@NotNull(message = "Last Name cannot be null.")
	@Column(name = "last_name")
	private String lastName;

	@NaturalId
	@Email(message = "Invalid email address.")
	@Column(name = "email")
	private String email;

	@Pattern(regexp = "^\\d{10}$")
	@NotNull(message = "Phone cannot be null.")
	@Column(name = "phone")
	private String phone;

	@Column(name = "categories", columnDefinition = "text[]")
	@Type(type = "com.careercenter.utils.SqlStringArrayType")
	private String[] category;
	
	@NotNull(message = "Age cannot be null.")
	@Column(name = "age")
	private int age;
	
	@Pattern(regexp = "Female|Male")
	@NotNull(message = "Gender cannot be null.")
	@Column(name = "gender")
	private String gender;

	@NotNull(message = "Degree cannot be null.")
	@Column(name = "degree")
	private String degree;
	
	@Column(name = "languages", columnDefinition = "text[]")
	@Type(type = "com.careercenter.utils.SqlStringArrayType")
	private String[] languages;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

}

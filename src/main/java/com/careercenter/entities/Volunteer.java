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
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "volunteers")
public class Volunteer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 2, max = 50, message = "Name must be at least 2 characters.")
	@NotNull(message = "Name cannot be null.")
	@Column(name = "name")
	private String name;

	@NaturalId
	@Email(message = "Invalid email address.")
	@Column(name = "email")
	private String email;

	@Pattern(regexp = "^\\d{10}$")
	@NotNull(message = "Phone cannot be null.")
	@Column(name = "phone")
	private String phone;

	@NotNull(message = "Job Title cannot be null.")
	@Column(name = "job_title")
	private String jobTitle;

	@NotNull(message = "Industry cannot be null.")
	@Column(name = "industry")
	private String industry;
	
	@NotNull(message = "Area of Interest cannot be null.")
	@Column(name = "area_of_interest")
	private String areaOfInterest;


}

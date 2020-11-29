package com.careercenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "companies")
@SequenceGenerator(name = "company_id_seq", sequenceName = "company_id_seq", allocationSize = 1, initialValue = 1001)
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_id_seq")
	private Long id;

	@Size(min = 2, max = 50, message = "Name must be at least 2 characters.")
	@NotNull(message = "Name cannot be null.")
	@Column(name = "name")
	private String name;

	@Size(max = 50)
	@NotNull(message = "City cannot be null")
	@Column(name = "city")
	private String city;

	@Pattern(regexp = "([A-Z]{2})")
	@NotNull(message = "State cannot be null")
	@Column(name = "state")
	private String state;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "volunteer_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Volunteer volunteer;

}

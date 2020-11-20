package com.careercenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address")
@SequenceGenerator(name = "address_id_seq", sequenceName = "address_id_seq", allocationSize = 1, initialValue = 5001)
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_id_seq")
	private Long address_id;

	@Size(max = 50)
	@NotNull(message = "Street cannot be null")
	@Column(name = "street")
	private String street;

	@Size(max = 50)
	@NotNull(message = "City cannot be null")
	@Column(name = "city")
	private String city;

	@Pattern(regexp = "([A-Z]{2})")
	@NotNull(message = "State cannot be null")
	@Column(name = "state")
	private String state;

	@Pattern(regexp = "^\\d{5}(?:[-\\s]\\d{4})?$")
	@NotNull(message = "Zip code cannot be null")
	@Column(name = "zipcode")
	private String zipcode;

}

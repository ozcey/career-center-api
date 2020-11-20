package com.careercenter.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_id_seq")
	private Long id;

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

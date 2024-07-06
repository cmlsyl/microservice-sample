package com.cs.productservice.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductCreateDTO(
	@NotBlank
	String name,
	@Min(0)
	double price,
	@Min(1)
	long stockCount
) {}
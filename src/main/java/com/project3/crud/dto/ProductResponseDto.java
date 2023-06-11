package com.project3.crud.dto;

import com.project3.crud.models.Customer;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public record ProductResponseDto(
        Integer id,
        String description,
        String productname,
        BigDecimal price,
        CustomerResponseDtoForProduct customerResponseDtoForProduct
) {
}

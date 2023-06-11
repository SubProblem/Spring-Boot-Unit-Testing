package com.project3.crud.dto;

import com.project3.crud.models.Product;
import jakarta.annotation.Nullable;

import java.util.List;

public record CustomerResponseDto(
        Integer id,
        String firstname,
        String lastname,
        String email,
        List<Product> products
) {
}

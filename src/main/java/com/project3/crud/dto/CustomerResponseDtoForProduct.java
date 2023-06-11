package com.project3.crud.dto;

public record CustomerResponseDtoForProduct(
        Integer id,
        String firstname,
        String lastname,
        String email
) {
}

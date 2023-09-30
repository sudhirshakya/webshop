package com.example.webshop.products;

import jakarta.validation.constraints.NotBlank;

public class CategoryDto {

    public String code;

    @NotBlank
    public String name;

    public String parent;

    public boolean deleted;

    public long updatedAt;
}

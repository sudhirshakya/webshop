package com.example.webshop.products;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        var dto = new CategoryDto();
        dto.code = category.code;
        dto.name = category.name;
        dto.parent = category.parent == null ? "" : category.parent.code;
        dto.deleted = category.deleted;
        dto.updatedAt = category.updatedAt;
        return dto;
    }

    public Category fromDto(CategoryDto dto) {
        var category = new Category();
        category.code = dto.code;
        category.name = dto.name;
        return category;
    }
}

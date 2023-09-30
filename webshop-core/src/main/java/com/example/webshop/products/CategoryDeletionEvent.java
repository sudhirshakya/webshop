package com.example.webshop.products;

public class CategoryDeletionEvent {

    public Category category;

    public static CategoryDeletionEvent of(Category category) {
        CategoryDeletionEvent event = new CategoryDeletionEvent();
        event.category = category;
        return event;
    }
}

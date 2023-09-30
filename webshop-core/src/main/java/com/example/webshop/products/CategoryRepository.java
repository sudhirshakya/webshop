package com.example.webshop.products;

import java.util.Optional;
import java.util.stream.Stream;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class CategoryRepository {

    @Inject
    EntityManager em;

    public void persist(Category category) {
        em.merge(category);
    }

    public Optional<Category> findByCode(String code) {
        var categories = em.createNamedQuery(Category.CODE, Category.class)
            .setParameter("code", code)
            .getResultList();
        if (categories.isEmpty())
            return Optional.empty();
        return Optional.of(categories.getFirst());
    }

    public Stream<Category> listUpdatedSince(long since) {
        return em.createNamedQuery(Category.UPDATED, Category.class)
            .setParameter("updatedAt", since)
            .getResultStream();
    }

    public Stream<Category> listActive() {
        return em.createNamedQuery(Category.ACTIVE, Category.class)
            .getResultStream();
    }

    public Stream<Category> listChildren(Category category) {
        return em.createNamedQuery(Category.CHILDREN, Category.class)
            .setParameter("parent", category)
            .getResultStream();
    }
}

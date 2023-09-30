package com.example.webshop.products;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(indexes = {
        @Index(columnList = "code", unique = true),
        @Index(columnList = "parent_id"),
        @Index(columnList = "deleted"),
        @Index(columnList = "updatedAt")
})
@NamedQueries({ @NamedQuery(name = Category.CODE, query = "select c from Category c where c.code = :code"),
        @NamedQuery(name = Category.ACTIVE, query = "select c from Category c where c.deleted = false"),
        @NamedQuery(name = Category.UPDATED, query = "select c from Category c where c.updatedAt > :updatedAt"),
        @NamedQuery(name = Category.CHILDREN, query = "select c from Category c where parent = :parent and deleted = false") })
public class Category extends AuditedEntity {

    private static final String NAMESPACE = "Category.";
    protected static final String CODE = NAMESPACE + "code";
    protected static final String ACTIVE = NAMESPACE + "active";
    protected static final String UPDATED = NAMESPACE + "updated";
    protected static final String CHILDREN = NAMESPACE + "children";

    public String code;

    public String name;

    @ManyToOne
    public Category parent;

    public boolean deleted;
}

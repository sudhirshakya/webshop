package com.example.webshop.products;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Inject
    public CategoryMapper mapper;

    @Inject
    CategoryRepository repository;

    @Inject
    Event<CategoryDeletionEvent> deletionEvent;

    public List<CategoryDto> fetch(long since) {
        logger.info("Fetching list of all categories updated after {}", since);
        Stream<Category> categories = since == 0 ? repository.listActive() : repository.listUpdatedSince(since);
        return categories.map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto add(CategoryDto dto) {
        logger.info("Adding new category {}.", dto);
        var category = mapper.fromDto(dto);

        logger.debug("Computing the category code and validating for duplication.");
        String code = isEmpty(dto.parent) ? dto.code.toUpperCase()
                : String.format("%s.%s", dto.parent, dto.code.toUpperCase());
        if (repository.findByCode(code)
            .isPresent())
            throw new BadRequestException(String.format("Category with code(%s) already exists.", code));
        category.code = code;
        
        logger.debug("Validating the parent category({}) and linking it.", dto.parent);
        if (!isEmpty(dto.parent)) {
            category.parent = repository.findByCode(dto.parent)
                .orElseThrow(() -> new BadRequestException(
                        String.format("Category with code(%s) does not exist.", dto.parent)));
        }
        
        logger.debug("Persisting the category({}) to database.", category);
        repository.persist(category);

        return mapper.toDto(category);
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim()
            .equals("");
    }

    @Transactional
    public CategoryDto update(String code, CategoryDto dto) {
        logger.info("Updating category({}) to {}.", code, dto);
        var category = findByCode(code);
        category.name = dto.name;
        repository.persist(category);
        return mapper.toDto(category);
    }

    @Transactional
    public CategoryDto delete(String code) {
        logger.info("Deleting category({}), child categories and related products.", code);
        var category = findByCode(code);
        markDeleted(category);
        return mapper.toDto(category);
    }

    private void markDeleted(Category category) {
        category.deleted = true;
        repository.persist(category);
        deletionEvent.fire(CategoryDeletionEvent.of(category));
    }

    public void deleteChildCategories(@Observes CategoryDeletionEvent event) {
        repository.listChildren(event.category)
            .forEach(this::markDeleted);
    }

    private Category findByCode(String code) {
        return repository.findByCode(code)
            .orElseThrow(() -> new NotFoundException(String.format("Category with code(%s) was not found.", code)));
    }
}

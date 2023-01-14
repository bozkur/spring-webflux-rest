package com.springframeworkguru.springwebfluxrest.controller;

import com.springframeworkguru.springwebfluxrest.domain.Category;
import com.springframeworkguru.springwebfluxrest.repository.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class CategoryController {

    private CategoryRepository repository;


    public CategoryController(CategoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/v1/categories")
    public Flux<Category> listCategories() {
        return repository.findAll();
    }

    @GetMapping("/api/v1/categories/{id}")
    public Mono<Category> listCategoryById(@PathVariable String id) {
        return repository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/categories")
    public Mono<Void> createCategory(@RequestBody Publisher<Category> category) {
        return repository.saveAll(category).then();
    }

    @PutMapping("/api/v1/categories/{id}")
    public Mono<Category> updateCategory(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return repository.save(category);
    }

    @PatchMapping("/api/v1/categories/{id}")
    public Mono<Category> patchCategory(@PathVariable String id, @RequestBody Category category) {
        Mono<Category> foundCategoryMono = repository.findById(id);
        Category foundCategory = foundCategoryMono.block();

        if (!foundCategory.getName().equals(category.getName())) {
            foundCategory.setName(category.getName());
            return repository.save(foundCategory);
        }

        return Mono.just(foundCategory);
    }
}

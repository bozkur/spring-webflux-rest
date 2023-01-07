package com.springframeworkguru.springwebfluxrest.repository;

import com.springframeworkguru.springwebfluxrest.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}

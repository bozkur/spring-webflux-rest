package com.springframeworkguru.springwebfluxrest.controller;

import com.springframeworkguru.springwebfluxrest.domain.Category;
import com.springframeworkguru.springwebfluxrest.repository.CategoryRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    private CategoryRepository repository;
    private WebTestClient client;
    private CategoryController controller;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(CategoryRepository.class);
        controller = new CategoryController(repository);
        client = WebTestClient.bindToController(controller).build();
    }

    @Test
    void shouldListCategories() {
        when(repository.findAll()).thenReturn(Flux.just(Category.builder().name("Name1").build(),
                Category.builder().name("Name2").build()));

        client.get()
                .uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void shouldListCategoryById() {
        String id = "myid";
        given(repository.findById(id)).willReturn(Mono.just(Category.builder().name("Cat").build()));

        client.get()
                .uri("/api/v1/categories/"+id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Category.class);
    }

    @Test
    void shouldCreateACategory() {
        given(repository.saveAll(any(Publisher.class))).willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().name("Kategori").build());

        client.post()
                .uri("/api/v1/categories")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void shouldUpdateACategory() {
        given(repository.save(any())).willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().name("Kategori").build());

        client.put()
                .uri("/api/v1/categories/idddd")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void shouldPatchACategory() {
        String id = "someid";
        Mono<Category> catNameMono = Mono.just(Category.builder().name("cat name").build());
        given(repository.findById(id)).willReturn(catNameMono);
        Category patch = Category.builder().name("Updated").build();

        client.patch()
                .uri("/api/v1/categories/"+id)
                .body(Mono.just(patch), Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getName(), Matchers.equalTo(patch.getName()));
    }

    @Test
    void shouldPatchNoChangeWhenNameDoesNotChange() {
        String id = "someid";
        Category category = Category.builder().name("cat name").build();
        Mono<Category> catNameMono = Mono.just(category);
        given(repository.findById(id)).willReturn(catNameMono);

        client.patch()
                .uri("/api/v1/categories/"+id)
                .body(Mono.just(category), Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(repository, never()).save(any());
    }


}
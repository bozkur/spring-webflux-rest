package com.springframeworkguru.springwebfluxrest.controller;

import com.springframeworkguru.springwebfluxrest.domain.Vendor;
import com.springframeworkguru.springwebfluxrest.repository.VendorRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class VendorControllerTest {

    private WebTestClient testClient;
    private VendorRepository repository;
    private VendorController controller;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(VendorRepository.class);
        controller = new VendorController(repository);
        testClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void shouldListVendors() {
        given(repository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("barni").lastName("moloztaş").build(),
                        Vendor.builder().firstName("fred").lastName("çakmaktaş").build()));

        testClient.get()
                .uri("/api/v1/vendors/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void shouldGetVendorById() {
        String vendorId = "vendorId";
        given(repository.findById(vendorId))
                .willReturn(Mono.just(Vendor.builder().firstName("barni").lastName("moloztaş").id(vendorId).build()));

        testClient.get()
                .uri("/api/v1/vendors/"+vendorId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class);
    }

    @Test
    void shouldCreateVendor() {
        given(repository.saveAll(ArgumentMatchers.any(Publisher.class))).willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> toBeSaved = Mono.just(Vendor.builder().firstName("Fred").lastName("Çakmaktaş").build());
        testClient.post()
                .uri("/api/v1/vendors/")
                .body(toBeSaved, Vendor.class)
                .exchange()
                .expectStatus().isCreated();

    }

    @Test
    void shouldUpdateVendor() {
        given(repository.save(ArgumentMatchers.any()))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> toBeUpdated = Mono.just(Vendor.builder().firstName("Fred").lastName("Çakmaktaş").build());

        testClient.put()
                .uri("/api/v1/vendors/someId")
                .body(toBeUpdated, Vendor.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldPatchVendorWhenFirstNameChanges() {
        String id = "someid";
        Vendor vendor = Vendor.builder().firstName("Fred").lastName("ÇakmakTaş").build();
        given(repository.findById(id)).willReturn(Mono.just(vendor));
        Vendor patch = Vendor.builder().firstName("Barney").build();

        requestPatch(id, patch);

        ArgumentCaptor<Vendor> vendorCaptor = ArgumentCaptor.forClass(Vendor.class);
        verify(repository).save(vendorCaptor.capture());
        assertThat(vendorCaptor.getValue().getFirstName(), Matchers.equalTo(patch.getFirstName()));
    }

    @Test
    void shouldPatchVendorWhenLastNameChanges() {
        String id = "someid";
        Vendor vendor = Vendor.builder().firstName("Fred").lastName("ÇakmakTaş").build();
        given(repository.findById(id)).willReturn(Mono.just(vendor));
        Vendor patch = Vendor.builder().lastName("Moloztaş").build();

        requestPatch(id, patch);

        ArgumentCaptor<Vendor> vendorCaptor = ArgumentCaptor.forClass(Vendor.class);
        verify(repository).save(vendorCaptor.capture());
        assertThat(vendorCaptor.getValue().getLastName(), Matchers.equalTo(patch.getLastName()));
    }

    @Test
    void shouldPatchNoChangesWhenValuesInPatchAreSameAsOriginal() {
        String id = "someid";
        Vendor vendor = Vendor.builder().firstName("Fred").lastName("ÇakmakTaş").build();
        given(repository.findById(id)).willReturn(Mono.just(vendor));
        Vendor patch = Vendor.builder().firstName("Fred").lastName("ÇakmakTaş").build();

        requestPatch(id, patch);

        verify(repository, never()).save(any());
    }

    private void requestPatch(String id, Vendor patch) {
        testClient.patch()
                .uri("/api/v1/vendors/" + id)
                .body(Mono.just(patch), Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }


}
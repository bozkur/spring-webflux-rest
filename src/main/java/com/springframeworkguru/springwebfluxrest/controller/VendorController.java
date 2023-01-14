package com.springframeworkguru.springwebfluxrest.controller;

import com.springframeworkguru.springwebfluxrest.domain.Vendor;
import com.springframeworkguru.springwebfluxrest.repository.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class VendorController {
    private final VendorRepository repository;

    public VendorController(VendorRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/v1/vendors")
    public Flux<Vendor> listVendors() {
        return repository.findAll();
    }

    @GetMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> getVendor(@PathVariable String id) {
        return repository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/vendors")
    public Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendor) {
        return repository.saveAll(vendor).then();
    }

    @PutMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return repository.save(vendor);
    }
    
    @PatchMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor patch) {
        Vendor vendor = repository.findById(id).block();
        String firstName = patch.getFirstName();
        boolean changed = false;
        if (firstName != null && !firstName.equals(vendor.getFirstName())) {
            vendor.setFirstName(firstName);
            changed = true;
        }
        String lastName = patch.getLastName();
        if (lastName != null && !lastName.equals(vendor.getLastName())) {
            vendor.setLastName(lastName);
            changed = true;
        }
        if (changed) {
            return repository.save(vendor);
        }
        return Mono.just(vendor);
    }

    
}

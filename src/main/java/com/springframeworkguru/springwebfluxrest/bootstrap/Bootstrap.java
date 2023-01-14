package com.springframeworkguru.springwebfluxrest.bootstrap;

import com.springframeworkguru.springwebfluxrest.domain.Category;
import com.springframeworkguru.springwebfluxrest.domain.Vendor;
import com.springframeworkguru.springwebfluxrest.repository.CategoryRepository;
import com.springframeworkguru.springwebfluxrest.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Slf4j
@Configuration
public class Bootstrap implements CommandLineRunner {

    private final VendorRepository vendorRepository;

    private final CategoryRepository categoryRepository;

    public Bootstrap(VendorRepository vendorRepository, CategoryRepository categoryRepository) {
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Mono<Long> count = vendorRepository.count();
        Long sizeOfVendors = count.block();
        if (sizeOfVendors == 0) {
            log.info("Creating vendors...");
            Vendor vendor1 = createVendor("Fred", "Çakmaktaş");
            Vendor vendor2 = createVendor("Barni", "Moloztaş");
            vendorRepository.save(vendor1).block();
            vendorRepository.save(vendor2).block();
        }

        Long sizeOfCategories = categoryRepository.count().block();
        if (sizeOfCategories == 0) {
            log.info("Creating categories...");
            categoryRepository.save(createCategory("Fruits")).block();
            categoryRepository.save(createCategory("Vegetables")).block();
        }

    }

    private Vendor createVendor(String firstName, String lastName) {
        return Vendor.builder().firstName(firstName)
                .lastName(lastName)
                .id(UUID.randomUUID().toString())
                .build();
    }

    private Category createCategory(String name) {
        return Category.builder().id(UUID.randomUUID().toString())
                .name(name)
                .build();
    }
}

package com.springframeworkguru.springwebfluxrest.bootstrap;

import com.springframeworkguru.springwebfluxrest.domain.Vendor;
import com.springframeworkguru.springwebfluxrest.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Slf4j
@Configuration
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public void run(String... args) throws Exception {
        Mono<Long> count = vendorRepository.count();
        Long sizeOfVendors = count.block();
        if (sizeOfVendors == 0) {
            log.info("Creating vendors...");
            Vendor vendor1 = createVendor("Vendor 1");
            Vendor vendor2 = createVendor("Vendor 2");
            vendorRepository.save(vendor1).block();
            vendorRepository.save(vendor2).block();
        }

    }

    private Vendor createVendor(String name) {
        Vendor vendor1 = new Vendor();
        vendor1.setId(UUID.randomUUID().toString());
        vendor1.setName(name);
        return vendor1;
    }
}

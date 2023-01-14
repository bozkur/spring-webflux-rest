package com.springframeworkguru.springwebfluxrest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DataMongoTest
class SpringWebfluxRestApplicationTests {

    @Test
    void contextLoads() {
    }

}

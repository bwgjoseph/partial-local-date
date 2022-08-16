package com.bwgjoseph.partiallocaldate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

@Import(value = MongoConfig.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class EmploymentCrudRepositoryTests {
    @Autowired
    private EmploymentRepository repository;
    private String createdId;

    @Test
    @Order(1)
    void create() {
        EmploymentDO employmentDO = EmploymentDO.builder()
                                        .companyName("SG")
                                        .localDate(LocalDate.now())
                                        .localDateTime(LocalDateTime.now())
                                        .offsetDateTime(OffsetDateTime.now())
                                        .partialLocalDate(PartialLocalDate.now())
                                        .build();

        EmploymentDO created = this.repository.save(employmentDO);
        this.createdId = created.getId();
        System.out.println("createdId: " + this.createdId);

        Assertions.assertThat(created.getId()).isNotBlank().isNotNull();
    }

    @Test
    @Order(2)
    void find() {
        System.out.println("createdId: " + this.createdId);
        EmploymentDO find = this.repository.findById(this.createdId).get();

        Assertions.assertThat(find.getId()).isNotBlank().isNotNull();
        Assertions.assertThat(find).hasFieldOrProperty("partialLocalDate");
        Assertions.assertThat(find.getPartialLocalDate().isValidLocalDate()).isTrue();
    }

    @Test
    @Order(3)
    void update() {
        EmploymentDO find = this.repository.findById(this.createdId).get();
        EmploymentDO update = find.toBuilder()
            .companyName("SGD")
            .partialLocalDate(new PartialLocalDate("2022-08-00"))
            .build();

        EmploymentDO updated = this.repository.save(update);

        Assertions.assertThat(updated.getPartialLocalDate().isValidLocalDate()).isFalse();
    }

    @Test
    @Order(4)
    void delete() {
        this.repository.deleteById(this.createdId);

        Optional<EmploymentDO> notExist = this.repository.findById(this.createdId);

        Assertions.assertThat(notExist).isNotPresent();
    }

}

package com.bwgjoseph.partiallocaldate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
public class PartialLocalDateSerializerJsonTests {
    @Autowired
    private JacksonTester<EmploymentDO> json;

    @Test
    void testSerialize() throws IOException {
        EmploymentDO employmentDO = EmploymentDO.builder()
                                        .id("12345")
                                        .companyName("SG")
                                        .localDate(LocalDate.now())
                                        .localDateTime(LocalDateTime.now())
                                        .offsetDateTime(OffsetDateTime.now())
                                        .partialLocalDate(PartialLocalDate.of(2022, 8, 10))
                                        .build();

        JsonContent<EmploymentDO> result = this.json.write(employmentDO);

        Assertions.assertThat(result)
            .extractingJsonPathStringValue("$.partialLocalDate")
            .isEqualTo("2022-08-10");
    }
}

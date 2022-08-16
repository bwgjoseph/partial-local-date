package com.bwgjoseph.partiallocaldate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmploymentDTO {
    private String companyName;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private OffsetDateTime offsetDateTime;
    private PartialLocalDate partialLocalDate;
}

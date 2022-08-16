package com.bwgjoseph.partiallocaldate;

import org.springframework.core.convert.converter.Converter;

public class EmploymentDoToDToConverter implements Converter<EmploymentDO, EmploymentDTO> {

    @Override
    public EmploymentDTO convert(EmploymentDO source) {

        return EmploymentDTO.builder()
                .companyName(source.getCompanyName())
                .localDate(source.getLocalDate())
                .localDateTime(source.getLocalDateTime())
                .offsetDateTime(source.getOffsetDateTime())
                .partialLocalDate(source.getPartialLocalDate())
                .build();
    }

}

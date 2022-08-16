package com.bwgjoseph.partiallocaldate;

import org.springframework.core.convert.converter.Converter;

public class EmploymentDtoToDoConverter implements Converter<EmploymentDTO, EmploymentDO> {

    @Override
    public EmploymentDO convert(EmploymentDTO source) {

        return EmploymentDO.builder()
                .companyName(source.getCompanyName())
                .localDate(source.getLocalDate())
                .localDateTime(source.getLocalDateTime())
                .offsetDateTime(source.getOffsetDateTime())
                .partialLocalDate(source.getPartialLocalDate())
                .build();
    }

}

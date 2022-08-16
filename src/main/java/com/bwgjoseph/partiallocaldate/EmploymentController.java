package com.bwgjoseph.partiallocaldate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/employment")
public class EmploymentController {
    private final EmploymentRepository repository;

    @PostMapping
    public EmploymentDO createEmployment(@RequestBody EmploymentDTO employmentDTO) {
        log.info("request {}", employmentDTO);

        EmploymentDO saved = this.repository.save(new EmploymentDtoToDoConverter().convert(employmentDTO));

        log.info("saved {}", saved);

        EmploymentDO find = this.repository.findById(saved.getId()).get();

        log.info("find {}", find);

        return find;
    }

}

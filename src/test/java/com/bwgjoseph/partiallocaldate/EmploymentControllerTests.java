package com.bwgjoseph.partiallocaldate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@TestInstance(Lifecycle.PER_CLASS)
// @WebMvcTest will include @JsonComponent
@WebMvcTest(controllers = EmploymentController.class)
public class EmploymentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmploymentRepository repository;

    private String id = "62fbaf978e282930c7869eb1";

    @BeforeEach
    void beforeEach() {
        EmploymentDO employmentDO = EmploymentDO.builder()
                                        .id(id)
                                        .companyName("SG")
                                        .localDate(LocalDate.now())
                                        .localDateTime(LocalDateTime.now())
                                        .offsetDateTime(OffsetDateTime.now())
                                        .partialLocalDate(PartialLocalDate.of(2022, 8, 20))
                                        .build();

        when(this.repository.save(any(EmploymentDO.class)))
            .thenReturn(employmentDO);

        when(this.repository.findById(id)).thenReturn(Optional.of(employmentDO));
    }

    @Test
    void createEmployment() throws Exception {
        this.mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/employment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "companyName": "SG",
                            "localDate": "2022-08-16",
                            "localDateTime": "2022-08-16T16:28:24.441",
                            "offsetDateTime": "2022-08-16T16:28:24.441+07:00",
                            "partialLocalDate": "2022-08-20"
                        }
                    """))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.partialLocalDate").value("2022-08-20"));
    }
}

package com.dev.studylog.enumtest;

import com.dev.studylog.validation.Person;
import com.dev.studylog.validation.ValidController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ValidController.class)
@ExtendWith(SpringExtension.class)
@Slf4j
public class ValidationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("validator 테스트")
    public void vadlidatorTest() {
        Person person = Person.builder()
                .name(null)
                .age(-1L)
                .build();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        for (ConstraintViolation<Person> constraintViolation : constraintViolations) {
            log.info("{}", constraintViolation.getPropertyPath());
            log.info("{}", constraintViolation.getRootBeanClass());
            log.info("{}", constraintViolation.getInvalidValue());
            log.info("{}", constraintViolation.getMessage());
        }
    }

    @Test
    @DisplayName("valid 어노테이션 검증")
    public void validTest() throws Exception {
        Person person = Person.builder()
                .name(null)
                .age(-1L)
                .build();

        mockMvc.perform(get("/valid-annotation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(person)))
                .andDo(print());
    }
}

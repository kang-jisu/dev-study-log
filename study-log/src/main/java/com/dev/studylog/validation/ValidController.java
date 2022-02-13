package com.dev.studylog.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class ValidController {

    @GetMapping("/valid-annotation")
    public void valid(@Valid @RequestBody Person person) {
        log.info("{} {}", person.getName(), person.getAge());
    }
}

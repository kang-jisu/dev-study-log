package com.dev.studylog.httpRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
public class RequestBodyController {

// Setter 없이 parameter binding 시키는 방법
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        binder.initDirectFieldAccess();
//    }

    @PostMapping("/request-body")
    public ResponseEntity<String> requestBody(@RequestBody String name, @RequestBody Long id, HttpServletRequest httpRequest) {
        log.info(name);
        return ResponseEntity.ok(name);
    }

    @PostMapping("/servlet-request")
    public void servletRequest(HttpServletRequest httpRequest) {
        log.info(String.valueOf(httpRequest.getParameter("aa")));
        log.info(String.valueOf(httpRequest.getParameter("bb")));
        log.info(String.valueOf(httpRequest.getAttribute("name")));
        log.info(String.valueOf(httpRequest.getAttribute("id")));
    }

    @GetMapping("/request-param")
    public void requestParam(@RequestParam String name, @RequestParam Long id, @RequestParam(required = false, defaultValue = "default") String requireValue
            , @RequestParam Map<String, Object> map) {
        log.info(name);
        log.info(String.valueOf(id));
        log.info(requireValue);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            log.info("{}:{}", entry.getKey(), String.valueOf(entry.getValue()));
        }
    }

    @GetMapping("/path-variable/{id}/{name}")
    public void pathVariable(@PathVariable Long id, @PathVariable(name = "name") String username) {
        log.info(String.valueOf(id));
        log.info(username);
    }

    // TODO ENUM CHECK
    @GetMapping("/enum/{color1}")
    public void queryEnum(@PathVariable Color color1, @RequestParam Color color2) {
        log.info(String.valueOf(color1));
        log.info(String.valueOf(color2));
        log.info(String.valueOf(color1.equals(Color.RED)));
    }

    @GetMapping("/model-attribute")
    public ResponseEntity<ModelAttributeDto> modelAttribute(@ModelAttribute ModelAttributeDto requestDto) {
        return ResponseEntity.ok(requestDto);
    }

    @GetMapping("/model-attribute/binding")
    public ResponseEntity<String> modelAttributeBindingResult(@ModelAttribute ModelAttributeDto requestDto, BindingResult bindingResult) {
        log.info("request : {},{}", requestDto.getId(), requestDto.getName());
        bindingResult.getFieldErrors().stream()
                .forEach(error -> log.info("{} ,{}", error.getField(), String.valueOf(error.getRejectedValue())));
        return null;
    }

    @GetMapping("/model-attribute/valid")
    public ResponseEntity<String> modelAttributeValid(@Valid @ModelAttribute ModelAttributeDto requestDto, BindingResult bindingResult) {
        log.info("request : {},{}", requestDto.getId(), requestDto.getName());

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().stream()
                    .forEach(error -> log.info("{} ,{}, {}", error.getField(), String.valueOf(error.getRejectedValue()), error.getDefaultMessage()));
        }
        return null;
    }
}

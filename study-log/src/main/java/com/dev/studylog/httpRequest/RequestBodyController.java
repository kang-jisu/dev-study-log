package com.dev.studylog.httpRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@Slf4j
public class RequestBodyController {

// Setter 없이 parameter binding 시키는 방법
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        binder.initDirectFieldAccess();
//    }


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
    @GetMapping("/enum/{myColor1}")
    public void queryEnum(@PathVariable MyColor myColor1, @RequestParam MyColor myColor2) {
        log.info(String.valueOf(myColor1));
        log.info(String.valueOf(myColor2));
        log.info(String.valueOf(myColor1.equals(MyColor.RED)));
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

    @PostMapping("/request-body")
    public ResponseEntity<RequestBodyDto> requestBody(@RequestBody RequestBodyDto requestBodyDto) {
        return ResponseEntity.ok(requestBodyDto);
    }

    @PostMapping("/request-body/valid")
    public ResponseEntity<RequestBodyDto> requestBodyValid(@Valid @RequestBody RequestBodyDto requestBodyDto ){

        return ResponseEntity.ok(requestBodyDto);
    }

    @GetMapping("/param/list")
    public ResponseEntity<String> requestParamList(
            @RequestParam(required = false) List< String> names,
            @RequestParam(required = false) List< MyColor> colors,
            @RequestParam(required = false)  List< Long> nums
    ) {
        log.info("names: {}", names);
        log.info("colors: {}", colors);
        log.info("nums: {}", nums);
        return ResponseEntity.ok("ok");
    }
}

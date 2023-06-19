package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestCController {

    private final TestCService testCService;
    @GetMapping("/testC")
    public void testC() throws Exception {
        testCService.experimentTest();
    }
}

package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModuleAController {

    @GetMapping("/testA")
    public String testA() {
        ModuleCApplication moduleCApplication = new ModuleCApplication();
        return "hello";
    }
}

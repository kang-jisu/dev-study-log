package com.group.libraryapp.controller.example

import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.group.libraryapp.service.example.RequestDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class TestController {
    @PostMapping("/test")
    fun test(
            @RequestBody req: RequestDTO
    ): String {
        println(req)
        return "success"
    }
}

data class RequestDTO(
        @field:JsonUnwrapped
        private val id: TestId,
        private val name: String,
)

data class TestId(
        private val value: String = "",
        private val version: Long = 0L,
) {
    constructor() : this("", 0L)
}
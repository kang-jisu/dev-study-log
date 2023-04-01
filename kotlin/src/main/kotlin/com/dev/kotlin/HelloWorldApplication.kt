package com.dev.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HelloWorldApplication {
}

fun main(args: Array<String>) {
    println("hello world")
    runApplication<HelloWorldApplication>(*args)
}
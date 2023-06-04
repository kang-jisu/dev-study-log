package com.group.libraryapp

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class JunitTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            println("모든 테스트 시작 전")
        }
        @AfterAll
        @JvmStatic
        fun afterAll() {
            println("모든 테스트 종료 후")
        }
    }

    @Test
    fun test1() {
        println("테스트 1")
    }

    @Test
    fun test2() {
        println("테스트 2")
    }
}
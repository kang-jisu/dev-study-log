package com.group.libraryapp.service.example

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*

class AServiceTests : BehaviorSpec({
    val dBService: DBService = mockk()

    val aService = AService(
            dBService
    )

    Given("id가 주어지고") {
        val id = "1"
        When("data가 존재하면") {
            every { dBService.get(id)} returns Data("1", "content")
            aService.request(RequestDto("1", "new content"))

            Then("modify가 호출된다.") {
                verify(exactly = 1) { dBService.modify(any()) }
                verify(exactly = 0) { dBService.save(any()) }
            }
        }
        When("data가 존재하지 않으면") {
            every { dBService.get(id)} returns null
            aService.request(RequestDto("1", "content"))

            Then("save가 호출된다.") {
                verify(exactly = 0) { dBService.modify(any()) }
                verify(exactly = 1) { dBService.save(any()) }
            }
        }
    }

    beforeTest{
        justRun { dBService.modify(any())}
        justRun { dBService.save(any()) }
    }

    afterTest {
        clearMocks(dBService)
    }
})
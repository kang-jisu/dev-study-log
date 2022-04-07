package com.dev.studylog.httpRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestBodyController.class)
@DisplayName("List로 받아오는 RequestParam테스트")
public class ListTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @DisplayName("param을 여러개 보내면 list로 받아와진다.")
    void paramListTest() throws Exception {
        mockMvc.perform(get("/param/list")
                .param("nums","")
                .param("nums","")
        )
//                .param("nums",",,,")
//                .param("colors",",,"))
                .andDo(print())
                .andExpect(status().isOk());

    }
}
package com.dev.studylog.httpRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RequestBodyController.class)
@ExtendWith(SpringExtension.class)
public class RequestBodyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void ResponseBodyNoArgsAndGetSetter() throws Exception{
        String json = "{\"name\":\"jisu\", \"id\":1}";
        mockMvc.perform(post("/request-body")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andDo(print())
                .andReturn();
        // body null
    }

    @Test
    public void HttpservletRequest() throws Exception{
        String json = "{\"name\":\"jisu\", \"id\":1}";
        mockMvc.perform(post("/servlet-request?bb=bb")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("aa","aa")
                .content(json))
                .andDo(print())
                .andReturn();
        //body null
        //param ok
    }

    @Test
    public void requestParam() throws Exception{
        mockMvc.perform(get("/request-param")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("id","1")
                .param("name","jisu"))
                .andDo(print())
                .andReturn();
    }


    @Test
    public void pathVariable() throws  Exception {
        mockMvc.perform(get("/path-variable/1/hello")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void queryEnum() throws  Exception {
        mockMvc.perform(get("/enum/RED")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("color2", "GREN"))
                .andDo(print())
                .andReturn();
        //2022-02-09 17:53:47.906  WARN 18686 --- [    Test worker] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.method.annotation.MethodArgumentTypeMismatchException: Failed to convert value of type 'java.lang.String' to required type 'com.dev.studylog.httpRequest.Color'; nested exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.String] to type [@org.springframework.web.bind.annotation.RequestParam com.dev.studylog.httpRequest.Color] for value 'GREN'; nested exception is java.lang.IllegalArgumentException: No enum constant com.dev.studylog.httpRequest.Color.GREN]
        //String->enum 처리 테스트해보기
    }

    @Test
    public void modelAttribute() throws Exception{
        mockMvc.perform(get("/model-attribute")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("id","1")
                .param("name","jisu"))
                .andDo(print())
                .andExpect(jsonPath("name").value("jisu"))
                .andExpect(jsonPath("id").value("1"))
                .andReturn();
    }

    @Test
    @DisplayName("Long에 String을 넣었을 때 BindingResult 객체에 저장되는지 테스트 ")
    public void modelAttributeBindingResult() throws Exception{
        mockMvc.perform(get("/model-attribute/binding")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("id","ㅁㄴ")
                .param("name","jisu"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("Long에 20 이하의 값을 넣었을 떄BindingResult 객체에 저장되는지 테스트 ")
    public void modelAttributeBindingValid() throws Exception{
        mockMvc.perform(get("/model-attribute/valid")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("id","10")
                .param("name","jisu"))
                .andDo(print())
                .andReturn();
    }
}

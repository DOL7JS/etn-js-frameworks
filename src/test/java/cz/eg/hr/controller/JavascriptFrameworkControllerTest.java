package cz.eg.hr.controller;

import cz.eg.hr.repository.JavascriptFrameworkRepository;
import cz.eg.hr.repository.VersionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
class JavascriptFrameworkControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JavascriptFrameworkRepository javascriptFrameworkRepository;
    @Autowired
    private VersionRepository versionRepository;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    @AfterEach
    void clearData() {
        versionRepository.deleteAll();
        javascriptFrameworkRepository.deleteAll();
    }

//    int exampleId = 1;

    // Every endpoint in controller is calling method in service, therefore I just prepared incomplete tests for controllers.
    // Tests would be very similar as tests for services, here could be tested http response status, etc.
    @Test
    void getAllFrameworkTest() {
//        MockHttpServletResponse response = mockMvc.perform(get("/framework").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getOneFrameworkTest() throws Exception {
//        MockHttpServletResponse response = mockMvc.perform(get("/framework/" + exampleId).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void addFrameworkTest() throws Exception {
//        ObjectMapper ow = new ObjectMapper();
//        JavaScriptFrameworkInputDto javaScriptFrameworkInputDto = new JavaScriptFrameworkInputDto("React");
//        MockHttpServletResponse response = mockMvc.perform(post("/framework").contentType(MediaType.APPLICATION_JSON)
//            .content(ow.writeValueAsString(javaScriptFrameworkInputDto))).andReturn().getResponse();
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void updateFrameworkTest() throws Exception {
//        ObjectMapper ow = new ObjectMapper();
//        JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto = new JavascriptFrameworkUpdateDto("Angular");
//
//        MockHttpServletResponse response = mockMvc.perform(put("/framework/" + exampleId).contentType(MediaType.APPLICATION_JSON)
//            .content(ow.writeValueAsString(javascriptFrameworkUpdateDto))).andReturn().getResponse();
//
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void addVersionToFrameworkTest() throws Exception {
//        ObjectMapper ow = new ObjectMapper();
//        VersionInDto versionInDto = new VersionInDto("1.1", LocalDate.of(2020, 1, 1), 1);
//
//        MockHttpServletResponse response = mockMvc.perform(post("/framework/" + exampleId).contentType(MediaType.APPLICATION_JSON)
//            .content(ow.writeValueAsString(versionInDto))).andReturn().getResponse();
//
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void deleteFrameworkTest() throws Exception {
//        MockHttpServletResponse response = mockMvc.perform(delete("/framework/" + exampleId).contentType(MediaType.APPLICATION_JSON))
//            .andReturn().getResponse();
//
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void fulltextSearchFrameworkTest() throws Exception {
//        String text = "React";
//
//        MockHttpServletResponse response = mockMvc.perform(get("/framework/fulltextSearch?text=" + text).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
//
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}

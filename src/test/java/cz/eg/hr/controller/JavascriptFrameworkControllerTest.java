package cz.eg.hr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.repository.JavascriptFrameworkRepository;
import cz.eg.hr.services.JavascriptFrameworkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
class JavascriptFrameworkControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JavascriptFrameworkRepository javascriptFrameworkRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void getZeroFrameworkTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/framework").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals("[]", response.getContentAsString());
    }

    @Test
    void getOneFrameworkTest() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        JavascriptFramework save = javascriptFrameworkRepository.save(javascriptFramework);
        MockHttpServletResponse response = mockMvc.perform(get("/framework").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        ObjectMapper mapper = new ObjectMapper();
        String s2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(response.getContentAsString()));
        String s = ow.writeValueAsString(List.of(save));
        String s3 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(s));

        assertEquals(s, s2);
    }
}

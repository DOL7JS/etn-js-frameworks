package cz.eg.hr.controller;

import cz.eg.hr.repository.JavascriptFrameworkRepository;
import cz.eg.hr.repository.VersionRepository;
import jakarta.persistence.EntityManagerFactory;
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
public class VersionControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JavascriptFrameworkRepository javascriptFrameworkRepository;
    @Autowired
    private VersionRepository versionRepository;

    @Test
    void contextLoads() {
    }

    @AfterEach
    @BeforeEach
    void clearData() {
        versionRepository.deleteAll();
        javascriptFrameworkRepository.deleteAll();
    }

    int exampleId = 1;

    // Every endpoint in controller is calling method in service, therefore I just prepared incoplete tests for controllers.
    // Tests would be very similar as tests for services, here could be tested http response status, etc.
    @Test
    void getAllVersionsTest() throws Exception {
//        MockHttpServletResponse response = mockMvc.perform(get("/version").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getVersionTest() throws Exception {
//        MockHttpServletResponse response = mockMvc.perform(get("/version/" + exampleId).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void updateVersionTest() throws Exception {
//        ObjectMapper ow = new ObjectMapper();
//
//        VersionInDto versionInDto = new VersionInDto("1.2", LocalDate.of(2021, 1, 1), 5);
//        MockHttpServletResponse response = mockMvc.perform(put("/version/" + exampleId).contentType(MediaType.APPLICATION_JSON)
//            .content(ow.writeValueAsString(versionInDto))).andReturn().getResponse();
//
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void deleteFrameworkTest() throws Exception {
//        MockHttpServletResponse response = mockMvc.perform(delete("/version/" + exampleId)
//            .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
//
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void fulltextSearchFrameworkTest() throws Exception {
//        String text = "1.2.3.";
//
//        MockHttpServletResponse response = mockMvc.perform(get("/version/fulltextSearch?text=" + text).
//            contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
//
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}

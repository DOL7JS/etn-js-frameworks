package cz.eg.hr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.JavaScriptFrameworkInputDto;
import cz.eg.hr.dtos.JavascriptFrameworkUpdateDto;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.services.JavascriptFrameworkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = "test")
@WebMvcTest(JavascriptFrameworkController.class)
class JavascriptFrameworkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavascriptFrameworkService javascriptFrameworkService;

    @Test
    void whenGETFramework_thenReturnJsonOfTwoJavascriptFrameworks_test() throws Exception {
        JavascriptFramework javascriptFramework1 = new JavascriptFramework("React");
        JavascriptFramework javascriptFramework2 = new JavascriptFramework("Angular");

        when(javascriptFrameworkService.getAllJavascriptFrameworks()).thenReturn(List.of(javascriptFramework1, javascriptFramework2));

        mockMvc.perform(get("/framework"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value(javascriptFramework1.getName()))
            .andExpect(jsonPath("$[1].name").value(javascriptFramework2.getName()));
    }

    @Test
    void givenJavascriptFrameworkID_whenGETFrameworkID_thenReturnJsonJavascriptFrameworkById_test() throws Exception {
        JavascriptFramework javascriptFramework1 = new JavascriptFramework("React");

        when(javascriptFrameworkService.getJavascriptFramework(1L)).thenReturn(javascriptFramework1);

        mockMvc.perform(get("/framework/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(javascriptFramework1.getName()));
    }

    @Test
    void givenJavaScriptFrameworkInputDtoWithName_whenPOSTFramework_thenReturnJsonOfNewJavascriptFramework_test() throws Exception {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        ObjectMapper ow = new ObjectMapper();
        JavaScriptFrameworkInputDto javaScriptFrameworkInputDto = new JavaScriptFrameworkInputDto(javascriptFramework.getName());

        when(javascriptFrameworkService.addJavascriptFramework(any(JavaScriptFrameworkInputDto.class))).thenReturn(javascriptFramework);

        mockMvc.perform(post("/framework")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(javaScriptFrameworkInputDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(javascriptFramework.getName()));
    }

    @Test
    void givenJavaScriptFrameworkInputDtoWithInvalidName_whenPOSTFramework_thenReturnBadRequest_test() throws Exception {
        ObjectMapper ow = new ObjectMapper();
        JavaScriptFrameworkInputDto javaScriptFrameworkInputDto = new JavaScriptFrameworkInputDto();

        mockMvc.perform(post("/framework")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(javaScriptFrameworkInputDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenJavaScriptFrameworkInputDtoWithInvalidStars_whenPOSTFramework_thenReturnBadRequest_test() throws Exception {
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());
        JavaScriptFrameworkInputDto javaScriptFrameworkInputDto = new JavaScriptFrameworkInputDto("React", "1.1", LocalDate.of(2020, 1, 1), 10);

        mockMvc.perform(post("/framework")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(javaScriptFrameworkInputDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenUpdateJavascriptFrameworkUpdateDto_whenPATCHFrameworkID_thenReturnUpdatedJsonJavascriptFramework_test() throws Exception {
        ObjectMapper ow = new ObjectMapper();
        JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto = new JavascriptFrameworkUpdateDto("Angular");
        JavascriptFramework javascriptFramework = new JavascriptFramework(javascriptFrameworkUpdateDto.getName());

        when(javascriptFrameworkService.updateJavascriptFramework(anyLong(), any(JavascriptFrameworkUpdateDto.class))).thenReturn(javascriptFramework);

        mockMvc.perform(patch("/framework/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(javascriptFrameworkUpdateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(javascriptFrameworkUpdateDto.getName()));
    }

    @Test
    void givenUpdateJavascriptFrameworkUpdateDtoWithInvalidName_whenPATCHFrameworkID_thenReturnBadRequest_test() throws Exception {
        ObjectMapper ow = new ObjectMapper();
        JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto = new JavascriptFrameworkUpdateDto("");

        mockMvc.perform(patch("/framework/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(javascriptFrameworkUpdateDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenVersionInDto_whenPOSTFrameworkID_thenReturnJsonJavascriptFrameworkWithAddedVersion_test() throws Exception {
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());

        VersionInDto versionInDto = new VersionInDto("1.1", LocalDate.of(2020, 1, 1), 1);
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        javascriptFramework.setVersions(Set.of(new Version(versionInDto.getVersionNumber(), versionInDto.getEndOfSupport(), versionInDto.getStars())));

        when(javascriptFrameworkService.addVersionToJavascriptFramework(anyLong(), any(VersionInDto.class))).thenReturn(javascriptFramework);

        mockMvc.perform(post("/framework/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(versionInDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(javascriptFramework.getName()))
            .andExpect(jsonPath("$.versions[0].versionNumber").value(versionInDto.getVersionNumber()));
    }

    @Test
    void givenVersionInDtoWithInvalidVersionNumber_whenPOSTFrameworkID_thenReturnBadRequest_test() throws Exception {
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());

        VersionInDto versionInDto = new VersionInDto("1.1", LocalDate.of(2020, 1, 1), 1);
        versionInDto.setVersionNumber(null);

        mockMvc.perform(post("/framework/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(versionInDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenVersionInDtoWithInvalidStars_whenPOSTFrameworkID_thenReturnBadRequest_test() throws Exception {
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());

        VersionInDto versionInDto = new VersionInDto("1.1", LocalDate.of(2020, 1, 1), 1);
        versionInDto.setStars(null);

        mockMvc.perform(post("/framework/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(versionInDto)))
            .andExpect(status().isBadRequest());

        versionInDto.setStars(10);
        mockMvc.perform(post("/framework/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(versionInDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenJavascriptID_whenDELELTEFrameworkID_thenVerifyExecutionOfDeleteFramework_test() throws Exception {
        mockMvc.perform(delete("/framework/1")).andExpect(status().isOk());
        verify(javascriptFrameworkService, times(1)).deleteFramework(anyLong());
    }

    @Test
    void givenSearchText_whenFulltextSearch_thenReturnJsonListOfFoundJavascriptFramework_test() throws Exception {
        String text = "React";
        JavascriptFramework javascriptFramework = new JavascriptFramework(text);

        when(javascriptFrameworkService.fulltextSearch(text)).thenReturn(List.of(javascriptFramework));

        mockMvc.perform(get("/framework/fulltextSearch?text=" + text))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value(text));

    }

}

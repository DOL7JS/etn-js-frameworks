package cz.eg.hr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.dtos.VersionOutDto;
import cz.eg.hr.services.VersionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class VersionControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VersionService versionService;


    @Test
    void whenGETVersion_thenReturnJsonOfAllVersions_test() throws Exception {
        VersionOutDto v1 = new VersionOutDto("1.1", LocalDate.of(2020, 1, 1), 1);
        VersionOutDto v2 = new VersionOutDto("1.1", LocalDate.of(2021, 1, 1), 2);

        when(versionService.getAllVersions()).thenReturn(List.of(v1, v2));

        mockMvc.perform(get("/version"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].versionNumber").value(v1.getVersionNumber()))
            .andExpect(jsonPath("$[1].versionNumber").value(v2.getVersionNumber()))
            .andExpect(jsonPath("$[0].stars").value(v1.getStars()))
            .andExpect(jsonPath("$[1].stars").value(v2.getStars()));
    }

    @Test
    void givenVersionID_whenGETVersionID_thenReturnJsonVersionByID_test() throws Exception {
        VersionOutDto v1 = new VersionOutDto("1.1", LocalDate.of(2020, 1, 1), 1);

        when(versionService.getVersion(anyLong())).thenReturn(v1);

        mockMvc.perform(get("/version/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.versionNumber").value(v1.getVersionNumber()))
            .andExpect(jsonPath("$.endOfSupport").value(v1.getEndOfSupport().toString()))
            .andExpect(jsonPath("$.stars").value(v1.getStars()));
    }

    @Test
    void givenVersionInDto_whenPUTVersionID_thenReturnJsonUpdatedJavascriptVersion_Test() throws Exception {
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());

        VersionInDto versionInDto = new VersionInDto("1.2", LocalDate.of(2021, 1, 1), 5);
        Version version = new Version(versionInDto.getVersionNumber(), versionInDto.getEndOfSupport(), versionInDto.getStars());

        when(versionService.updateJavascriptFrameworkVersion(anyLong(), any(VersionInDto.class))).thenReturn(version);

        mockMvc.perform(put("/version/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(versionInDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.versionNumber").value(version.getVersionNumber()))
            .andExpect(jsonPath("$.endOfSupport").value(version.getEndOfSupport().toString()))
            .andExpect(jsonPath("$.stars").value(version.getStars()));
    }

    @Test
    void givenInvalidVersionNumber_whenPUTVersionID_thenReturnBadRequest_test() throws Exception {
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());

        VersionInDto versionInDto = new VersionInDto("", LocalDate.of(2021, 1, 1), 5);

        mockMvc.perform(put("/version/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(versionInDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidStars_whenPUTVersionID_thenReturnBadRequest_test() throws Exception {
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());

        VersionInDto versionInDto = new VersionInDto("React", LocalDate.of(2021, 1, 1), 10);

        mockMvc.perform(put("/version/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(versionInDto)))
            .andExpect(status().isBadRequest());

        versionInDto.setStars(null);
        mockMvc.perform(put("/version/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(versionInDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenVersionID_whenDELETEVersionID_thenVerifyExecutionDeleteVersion_test() throws Exception {
        mockMvc.perform(delete("/version/1")).andExpect(status().isOk());

        verify(versionService, times(1)).deleteVersion(anyLong());
    }

    @Test
    void givenSearchText_whenFulltextSearch_thenReturnJsonListOfFoundVersion_test() throws Exception {
        String text = "1.2.3.";
        Version version = new Version(text, LocalDate.of(2020, 1, 1), 5);

        when(versionService.fulltextSearch(text)).thenReturn(List.of(version));

        mockMvc.perform(get("/version/fulltextSearch?text=" + text))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].versionNumber").value(text));

    }


}

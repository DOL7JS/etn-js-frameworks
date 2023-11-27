package cz.eg.hr.services;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.JavaScriptFrameworkInputDto;
import cz.eg.hr.dtos.JavascriptFrameworkDto;
import cz.eg.hr.dtos.JavascriptFrameworkUpdateDto;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.repository.JavascriptFrameworkRepository;
import cz.eg.hr.repository.VersionRepository;
import cz.eg.hr.rest.exceptions.EntityAlreadyExistsException;
import cz.eg.hr.rest.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles(profiles = "test")
public class JavascriptFrameworkServiceTest {

    @InjectMocks
    private JavascriptFrameworkService javascriptFrameworkService;
    @Mock
    private VersionRepository versionRepository;
    @Mock
    private JavascriptFrameworkRepository javascriptFrameworkRepository;
    @Mock
    private FulltextSearchService<JavascriptFrameworkDto> fulltextSearchService;
    @Spy
    private ModelMapper modelMapper;

    @Test
    public void whenGetAllJavascriptFrameworks_thenReturnListOfTwoJavascriptFrameworks_test() {
        JavascriptFramework javascriptFramework1 = new JavascriptFramework("React");
        JavascriptFramework javascriptFramework2 = new JavascriptFramework("Angular");

        when(javascriptFrameworkRepository.findAll()).thenReturn(List.of(javascriptFramework1, javascriptFramework2));

        Iterable<JavascriptFrameworkDto> allFrameworks = javascriptFrameworkService.getAllJavascriptFrameworks();

        assertEquals(2, ((Collection<JavascriptFrameworkDto>) allFrameworks).size());
        assertTrue(((Collection<JavascriptFrameworkDto>) allFrameworks).stream().anyMatch(js -> js.getName().equals(javascriptFramework1.getName())));
        assertTrue(((Collection<JavascriptFrameworkDto>) allFrameworks).stream().anyMatch(js -> js.getName().equals(javascriptFramework2.getName())));
    }

    @Test
    public void givenJavascriptFrameworkID_whenGetJavascriptFramework_thenReturnJavascriptFrameworkById_test() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        javascriptFramework.setId(1L);
        when(javascriptFrameworkRepository.findById(javascriptFramework.getId())).thenReturn(Optional.of(javascriptFramework));
        JavascriptFrameworkDto javascriptFrameworkById = javascriptFrameworkService.getJavascriptFramework(javascriptFramework.getId());

        assertEquals(javascriptFramework.getId(), javascriptFrameworkById.getId());
        assertEquals(javascriptFramework.getName(), javascriptFrameworkById.getName());
    }

    @Test
    public void givenWrongJavascriptFrameworkID_whenGetJavascriptFramework_thenReturnEntityNotFoundException_test() {
        assertThrows(EntityNotFoundException.class, () -> {
            javascriptFrameworkService.getJavascriptFramework(1L);
        });
    }

    @Test
    public void givenJavaScriptFrameworkInputDtoWithVersion_whenAddJavascriptFramework_thenReturnNewJavascriptFramework_test() {
        JavaScriptFrameworkInputDto javaScriptFrameworkInputDto = new JavaScriptFrameworkInputDto("React", "1.1", LocalDate.of(2020, 1, 1), 1);
        JavascriptFramework javascriptFramework = new JavascriptFramework(javaScriptFrameworkInputDto.getName());
        Version version = new Version(javaScriptFrameworkInputDto.getVersionNumber(), javaScriptFrameworkInputDto.getEndOfSupport(), javaScriptFrameworkInputDto.getStars());
        version.setJavascriptFramework(javascriptFramework);

        when(javascriptFrameworkRepository.save(any(JavascriptFramework.class))).thenReturn(javascriptFramework);
        when(javascriptFrameworkRepository.existsByName(anyString())).thenReturn(false);
        when(versionRepository.save(any(Version.class))).thenReturn(version);

        JavascriptFrameworkDto javascriptFrameworkReturned = javascriptFrameworkService.addJavascriptFramework(javaScriptFrameworkInputDto);

        assertEquals(javaScriptFrameworkInputDto.getName(), javascriptFrameworkReturned.getName());
        assertEquals(1, javascriptFrameworkReturned.getVersions().size());
        assertTrue(javascriptFrameworkReturned.getVersions().stream().anyMatch(v -> v.getVersionNumber().equals(javaScriptFrameworkInputDto.getVersionNumber())));
        assertTrue(javascriptFrameworkReturned.getVersions().stream().anyMatch(v -> v.getStars() == javaScriptFrameworkInputDto.getStars()));
        assertTrue(javascriptFrameworkReturned.getVersions().stream().anyMatch(v -> v.getEndOfSupport().equals(javaScriptFrameworkInputDto.getEndOfSupport())));
    }

    @Test
    public void givenJavaScriptFrameworkInputDtoWithName_whenAddJavascriptFramework_thenReturnNewJavascriptFramework_test() {
        JavaScriptFrameworkInputDto javaScriptFrameworkInputDto = new JavaScriptFrameworkInputDto("React");
        when(javascriptFrameworkRepository.save(any(JavascriptFramework.class))).thenReturn(new JavascriptFramework(javaScriptFrameworkInputDto.getName()));
        when(javascriptFrameworkRepository.existsByName(anyString())).thenReturn(false);

        JavascriptFrameworkDto javascriptFramework = javascriptFrameworkService.addJavascriptFramework(javaScriptFrameworkInputDto);

        assertEquals(javaScriptFrameworkInputDto.getName(), javascriptFramework.getName());
        assertNull(javascriptFramework.getVersions());
    }

    @Test
    public void givenJavaScriptFrameworkInputDtoWithName_whenAddJavascriptFramework_thenReturnEntityAlreadyExistsException_test() {
        JavaScriptFrameworkInputDto javaScriptFrameworkInputDto = new JavaScriptFrameworkInputDto("React");

        when(javascriptFrameworkRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            javascriptFrameworkService.addJavascriptFramework(javaScriptFrameworkInputDto);
        });
    }

    @Test
    public void givenNullJavaScriptFrameworkInputDto_whenAddJavascriptFramework_thenReturnNullPointerException_test() {
        assertThrows(NullPointerException.class, () -> {
            javascriptFrameworkService.addJavascriptFramework(null);
        });
    }

    @Test
    public void givenJavaScriptFrameworkInputDtoWithNullName_whenAddJavascriptFramework_thenReturnNullPointerException_test() {
        JavaScriptFrameworkInputDto javaScriptFrameworkInputDto = new JavaScriptFrameworkInputDto(null);

        assertThrows(NullPointerException.class, () -> {
            javascriptFrameworkService.addJavascriptFramework(javaScriptFrameworkInputDto);
        });
    }

    @Test
    public void givenVersionInDto_whenAddVersionToJavascriptFramework_thenReturnJavascriptFrameworkWithAddedVersion_test() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        javascriptFramework.setId(1L);
        VersionInDto versionInDto = new VersionInDto("1.1", LocalDate.of(2020, 1, 1), 4);
        Version version = new Version("1.1", LocalDate.of(2020, 1, 1), 4);
        javascriptFramework.setVersions(new HashSet<>());
        when(javascriptFrameworkRepository.findById(1L)).thenReturn(Optional.of(javascriptFramework));
        when(versionRepository.existsByVersionNumberAndJavascriptFramework(anyString(), any(JavascriptFramework.class))).thenReturn(false);
        when(versionRepository.save(any(Version.class))).thenReturn(version);

        JavascriptFrameworkDto javascriptFrameworkWithVersion = javascriptFrameworkService.addVersionToJavascriptFramework(javascriptFramework.getId(), versionInDto);

        assertNotNull(javascriptFrameworkWithVersion.getVersions());
        assertEquals(1, javascriptFrameworkWithVersion.getVersions().size());
        assertTrue(javascriptFrameworkWithVersion.getVersions().stream().anyMatch(v -> v.getVersionNumber().equals(versionInDto.getVersionNumber())));
        assertTrue(javascriptFrameworkWithVersion.getVersions().stream().anyMatch(v -> v.getStars() == versionInDto.getStars()));
        assertTrue(javascriptFrameworkWithVersion.getVersions().stream().anyMatch(v -> v.getEndOfSupport().equals(versionInDto.getEndOfSupport())));
    }

    @Test
    public void givenVersionInDto_whenAddVersionToJavascriptFramework_thenReturnEntityNotFoundException_test() {
        VersionInDto versionInDto = new VersionInDto("1.1", LocalDate.of(2020, 1, 1), 4);
        assertThrows(EntityNotFoundException.class, () -> {
            javascriptFrameworkService.addVersionToJavascriptFramework(1L, versionInDto);
        });
    }

    @Test
    public void givenNullVersionInDto_whenAddVersionToJavascriptFramework_thenReturnNullPointerException_test() {
        assertThrows(NullPointerException.class, () -> {
            javascriptFrameworkService.addVersionToJavascriptFramework(1L, null);
        });
    }

    @Test
    public void givenVersionInDtoWithNullVersionNumber_whenAddVersionToJavascriptFramework_thenReturnNullPointerException_test() {
        VersionInDto versionInDto = new VersionInDto(null, LocalDate.of(2020, 1, 1), 4);
        assertThrows(NullPointerException.class, () -> {
            javascriptFrameworkService.addVersionToJavascriptFramework(1L, versionInDto);
        });
    }

    @Test
    public void givenVersionInDto_whenAddVersionToJavascriptFramework_thenReturnEntityAlreadyExistsException_test() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        VersionInDto versionInDto = new VersionInDto("1.1", LocalDate.of(2020, 1, 1), 4);

        when(javascriptFrameworkRepository.findById(1L)).thenReturn(Optional.of(javascriptFramework));
        when(versionRepository.existsByVersionNumberAndJavascriptFramework(anyString(), any(JavascriptFramework.class))).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            javascriptFrameworkService.addVersionToJavascriptFramework(1L, versionInDto);
        });
    }

    @Test
    public void givenUpdateJavascriptFrameworkUpdateDto_whenUpdateJavascriptFramework_thenReturnUpdatedJavascriptFramework_test() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        javascriptFramework.setId(1L);
        JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto = new JavascriptFrameworkUpdateDto("Angular");
        JavascriptFramework javascriptFrameworkUpdated = new JavascriptFramework(javascriptFrameworkUpdateDto.getName());
        javascriptFrameworkUpdated.setId(javascriptFramework.getId());

        when(javascriptFrameworkRepository.existsByName(anyString())).thenReturn(false);
        when(javascriptFrameworkRepository.findById(javascriptFramework.getId())).thenReturn(Optional.of(javascriptFramework));
        when(javascriptFrameworkRepository.save(any(JavascriptFramework.class))).thenReturn(javascriptFrameworkUpdated);

        JavascriptFrameworkDto javascriptFrameworkById = javascriptFrameworkService.updateJavascriptFramework(javascriptFramework.getId(), javascriptFrameworkUpdateDto);

        assertEquals(javascriptFramework.getId(), javascriptFrameworkById.getId());
        assertEquals(javascriptFrameworkUpdateDto.getName(), javascriptFrameworkById.getName());
    }

    @Test
    public void givenNullJavascriptFrameworkUpdateDto_whenUpdateJavascriptFramework_thenReturnNullPointerException_test() {
        assertThrows(NullPointerException.class, () -> {
            javascriptFrameworkService.updateJavascriptFramework(1L, null);
        });
    }

    @Test
    public void givenJavascriptFrameworkUpdateDtoWithNameNull_whenUpdateJavascriptFramework_thenReturnNullPointerException_test() {
        JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto = new JavascriptFrameworkUpdateDto(null);
        assertThrows(NullPointerException.class, () -> {
            javascriptFrameworkService.updateJavascriptFramework(1L, javascriptFrameworkUpdateDto);
        });
    }

    @Test
    public void givenJavascriptFrameworkUpdateDto_whenUpdateJavascriptFramework_thenReturnEntityAlreadyExistsException_test() {
        JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto = new JavascriptFrameworkUpdateDto("React");

        when(javascriptFrameworkRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            javascriptFrameworkService.updateJavascriptFramework(1L, javascriptFrameworkUpdateDto);
        });
    }

    @Test
    public void givenJavascriptFrameworkUpdateDto_whenUpdateJavascriptFramework_thenReturnEntityNotFoundException_test() {
        JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto = new JavascriptFrameworkUpdateDto("React");

        when(javascriptFrameworkRepository.existsByName(anyString())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            javascriptFrameworkService.updateJavascriptFramework(1L, javascriptFrameworkUpdateDto);
        });
    }

    @Test
    public void givenJavascriptID_whenDeleteJavascriptFramework_thenVerifyExecutionOfDeleteByID_test() {
        when(javascriptFrameworkRepository.existsById(anyLong())).thenReturn(true);

        javascriptFrameworkService.deleteFramework(1L);

        verify(javascriptFrameworkRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void givenJavascriptID_whenDeleteJavascriptFramework_thenVerifyNotExecutionOfDeleteByID_test() {
        when(javascriptFrameworkRepository.existsById(anyLong())).thenReturn(false);

        javascriptFrameworkService.deleteFramework(1L);

        verify(javascriptFrameworkRepository, times(0)).deleteById(anyLong());

    }

    @Test
    public void givenSearchText_whenFulltextSearch_thenReturnListOfFoundJavascriptFramework_test() {
        JavascriptFrameworkDto javascriptFramework1 = new JavascriptFrameworkDto();
        javascriptFramework1.setName("React");

        when(fulltextSearchService.fulltextSearch(new String[]{"name"}, javascriptFramework1.getName(), JavascriptFrameworkDto.class)).thenReturn(List.of(javascriptFramework1));

        List<JavascriptFrameworkDto> fulltextSearch = javascriptFrameworkService.fulltextSearch(javascriptFramework1.getName());
        assertEquals(1, fulltextSearch.size());
    }
}

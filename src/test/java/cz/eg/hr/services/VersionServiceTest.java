package cz.eg.hr.services;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.dtos.VersionOutDto;
import cz.eg.hr.repository.VersionRepository;
import cz.eg.hr.rest.exceptions.EntityAlreadyExistsException;
import cz.eg.hr.rest.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@ActiveProfiles(profiles = "test")
public class VersionServiceTest {

    @InjectMocks
    private VersionService versionService;
    @Mock
    private VersionRepository versionRepository;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private FulltextSearchService<VersionOutDto> fulltextSearchService;

    @Test
    public void whenGetAllVersions_thenReturnListOfAllVersions_test() {
        Version v1 = new Version("1.1", LocalDate.of(2020, 1, 1), 4);
        Version v2 = new Version("1.2", LocalDate.of(2021, 1, 1), 5);

        when(versionRepository.findAll()).thenReturn(List.of(v1, v2));

        Iterable<VersionOutDto> allVersions = versionService.getAllVersions();

        assertEquals(2, ((Collection<?>) allVersions).size());
        assertTrue(((Collection<VersionOutDto>) allVersions).stream().anyMatch(v -> v.getVersionNumber().equals(v1.getVersionNumber())));
        assertTrue(((Collection<VersionOutDto>) allVersions).stream().anyMatch(v -> v.getVersionNumber().equals(v2.getVersionNumber())));
    }

    @Test
    public void givenVersionID_whenGetVersion_thenReturnVersionByID_test() {
        Version v1 = new Version("1.1", LocalDate.of(2020, 1, 1), 4);
        v1.setId(1L);

        when(versionRepository.findById(v1.getId())).thenReturn(Optional.of(v1));
        VersionOutDto version = versionService.getVersion(v1.getId());

        assertEquals(v1.getId(), version.getId());
        assertEquals(v1.getVersionNumber(), version.getVersionNumber());
        assertEquals(v1.getStars(), version.getStars());
        assertEquals(v1.getEndOfSupport(), version.getEndOfSupport());
    }

    @Test
    public void givenWrongVersionID_whenGetVersion_thenReturnExceptionEntityNotFoundException_Test() {

        assertThrows(EntityNotFoundException.class, () -> {
            versionService.getVersion(1L);
        });
    }

    @Test
    public void givenVersionInDto_whenUpdateJavascriptVersion_thenReturnUpdatedJavascriptVersion_Test() {
        VersionInDto versionInDto = new VersionInDto("2.2", LocalDate.of(2021, 1, 1), 5);
        Version v1 = new Version("1.1", LocalDate.of(2020, 1, 1), 1);
        v1.setId(1L);
        Version versionUpdated = new Version(versionInDto.getVersionNumber(), versionInDto.getEndOfSupport(), versionInDto.getStars());
        versionUpdated.setId(v1.getId());

        when(versionRepository.findById(v1.getId())).thenReturn(Optional.of(v1));
        when(versionRepository.existsByVersionNumberAndJavascriptFrameworkAndIdIsNot(anyString(), any(JavascriptFramework.class), anyLong())).thenReturn(false);
        when(versionRepository.save(any(Version.class))).thenReturn(versionUpdated);

        VersionOutDto version = versionService.updateJavascriptFrameworkVersion(v1.getId(), versionInDto);

        assertEquals(versionUpdated.getId(), version.getId());
        assertEquals(versionUpdated.getVersionNumber(), version.getVersionNumber());
        assertEquals(versionUpdated.getStars(), version.getStars());
        assertEquals(versionUpdated.getEndOfSupport(), version.getEndOfSupport());
    }

    @Test
    public void givenWrongVersionID_whenUpdateJavascriptVersion_thenReturnEntityNotFoundException_test() {
        VersionInDto versionInDto = new VersionInDto("2.2", LocalDate.of(2021, 1, 1), 5);

        assertThrows(EntityNotFoundException.class, () -> {
            versionService.updateJavascriptFrameworkVersion(1L, versionInDto);
        });
    }

    @Test
    public void givenNullVersionInDto_whenUpdateJavascriptVersion_thenReturnNullPointerException_test() {
        assertThrows(NullPointerException.class, () -> {
            versionService.updateJavascriptFrameworkVersion(1L, null);
        });
    }

    @Test
    public void givenNullNameInVersionInDto_whenUpdateJavascriptVersion_thenReturnNullPointerException_test() {
        VersionInDto versionInDto = new VersionInDto(null, LocalDate.of(2021, 1, 1), 5);
        assertThrows(NullPointerException.class, () -> {
            versionService.updateJavascriptFrameworkVersion(1L, versionInDto);
        });
    }

    @Test
    public void givenVersionThatAlreadyExistsInJavascript_whenUpdateJavascriptVersion_thenReturnEntityAlreadyExistsException_test() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        VersionInDto versionInDto = new VersionInDto("2.2", LocalDate.of(2021, 1, 1), 5);
        Version v1 = new Version("1.1", LocalDate.of(2020, 1, 1), 1);
        v1.setId(1L);
        v1.setJavascriptFramework(javascriptFramework);

        when(versionRepository.findById(v1.getId())).thenReturn(Optional.of(v1));
        when(versionRepository.existsByVersionNumberAndJavascriptFrameworkAndIdIsNot(anyString(), any(JavascriptFramework.class), anyLong())).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            versionService.updateJavascriptFrameworkVersion(v1.getId(), versionInDto);
        });

    }

    @Test
    public void givenVersionID_whenDeleteVersion_thenVerifyExecutionOfDeleteByID_test() {
        when(versionRepository.existsById(anyLong())).thenReturn(true);

        versionService.deleteVersion(1L);

        verify(versionRepository, times(1)).deleteById(1L);
        verify(versionRepository, times(0)).deleteById(2L);
    }

    @Test
    public void givenVersionID_whenDeleteVersion_thenVerifyNotExecutionOfDeleteByID_test() {
        when(versionRepository.existsById(anyLong())).thenReturn(false);

        versionService.deleteVersion(1L);

        verify(versionRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void givenSearchText_whenFulltextSearch_thenReturnListOfFoundVersion_test() {
        VersionOutDto v1 = new VersionOutDto("1.2.3.4", LocalDate.of(2020, 1, 1), 4);

        when(fulltextSearchService.fulltextSearch(new String[]{"stars", "endOfSupport", "versionNumber"}, v1.getVersionNumber(), VersionOutDto.class)).thenReturn(List.of(v1));

        List<VersionOutDto> list = versionService.fulltextSearch(v1.getVersionNumber());

        assertEquals(1, list.size());
    }
}

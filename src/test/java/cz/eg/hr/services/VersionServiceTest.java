package cz.eg.hr.services;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.dtos.VersionOutDto;
import cz.eg.hr.repository.JavascriptFrameworkRepository;
import cz.eg.hr.repository.VersionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles(profiles = "test")
public class VersionServiceTest {

    @Autowired
    private VersionService versionService;
    @Autowired
    private VersionRepository versionRepository;
    @Autowired
    private JavascriptFrameworkRepository javascriptFrameworkRepository;

    @BeforeEach
    @AfterEach
    void clearData() {
        versionRepository.deleteAll();
        javascriptFrameworkRepository.deleteAll();

    }

    @Test
    public void getAllVersionsTest() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        Version v1 = new Version("1.1", LocalDate.of(2020, 1, 1), 4);
        Version v2 = new Version("1.2", LocalDate.of(2021, 1, 1), 5);
        javascriptFramework.setVersions(Set.of(v1, v2));
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(javascriptFramework);
        v1.setJavascriptFramework(javascriptFrameworkSaved);
        v2.setJavascriptFramework(javascriptFrameworkSaved);
        versionRepository.save(v1);
        versionRepository.save(v2);

        Iterable<VersionOutDto> allVersions = versionService.getAllVersions();

        assertEquals(2, ((Collection<?>) allVersions).size());
        assertTrue(((Collection<VersionOutDto>) allVersions).stream().anyMatch(v -> v.getVersionNumber().equals(v1.getVersionNumber())));
        assertTrue(((Collection<VersionOutDto>) allVersions).stream().anyMatch(v -> v.getVersionNumber().equals(v2.getVersionNumber())));
    }

    @Test
    public void getVersionByIdTest() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        Version v1 = new Version("1.1", LocalDate.of(2020, 1, 1), 4);
        Version v2 = new Version("1.2", LocalDate.of(2021, 1, 1), 5);
        javascriptFramework.setVersions(Set.of(v1, v2));
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(javascriptFramework);
        v1.setJavascriptFramework(javascriptFrameworkSaved);
        v2.setJavascriptFramework(javascriptFrameworkSaved);
        Version versionSaved = versionRepository.save(v1);
        versionRepository.save(v2);

        VersionOutDto version = versionService.getVersion(versionSaved.getId());

        assertEquals(versionSaved.getId(), version.getId());
        assertEquals(versionSaved.getVersionNumber(), version.getVersionNumber());
        assertEquals(versionSaved.getStars(), version.getStars());
        assertEquals(versionSaved.getEndOfSupport(), version.getEndOfSupport());
        assertEquals(versionSaved.getJavascriptFramework().getName(), version.getJavascriptFramework());
    }

    @Test
    public void getVersionByIdNotFoundTest() {

        assertThrows(NoSuchElementException.class, () -> {
            versionService.getVersion(1L);
        });
    }

    @Test
    public void updateVersionTest() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        Version v1 = new Version("1.1", LocalDate.of(2020, 1, 1), 1);
        javascriptFramework.setVersions(Set.of(v1));
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(javascriptFramework);
        v1.setJavascriptFramework(javascriptFrameworkSaved);
        Version versionSaved = versionRepository.save(v1);
        VersionInDto versionInDto = new VersionInDto("2.2", LocalDate.of(2021, 1, 1), 5);

        Version version = versionService.updateJavascriptFrameworkVersion(versionSaved.getId(), versionInDto);

        assertEquals(versionSaved.getId(), version.getId());
        assertEquals(versionInDto.getVersionNumber(), version.getVersionNumber());
        assertEquals(versionInDto.getStars(), version.getStars());
        assertEquals(versionInDto.getEndOfSupport(), version.getEndOfSupport());
    }

    @Test
    public void updateVersionNotFoundTest() {
        VersionInDto versionInDto = new VersionInDto("2.2", LocalDate.of(2021, 1, 1), 5);

        assertThrows(NoSuchElementException.class, () -> {
            versionService.updateJavascriptFrameworkVersion(1L, versionInDto);
        });
    }

    @Test
    public void updateVersionAlreadyExistsTest() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        Version v1 = new Version("1.1", LocalDate.of(2020, 1, 1), 1);
        Version v2 = new Version("1.2", LocalDate.of(2020, 1, 1), 1);
        javascriptFramework.setVersions(Set.of(v1, v2));
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(javascriptFramework);
        v1.setJavascriptFramework(javascriptFrameworkSaved);
        v2.setJavascriptFramework(javascriptFrameworkSaved);
        Version versionSaved = versionRepository.save(v1);
        versionRepository.save(v2);
        VersionInDto versionInDto = new VersionInDto("1.2", LocalDate.of(2021, 1, 1), 5);

        assertThrows(IllegalArgumentException.class, () -> {
            versionService.updateJavascriptFrameworkVersion(versionSaved.getId(), versionInDto);
        });

    }

    @Test
    public void deleteVersionTest() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        Version v1 = new Version("1.1", LocalDate.of(2020, 1, 1), 1);
        javascriptFramework.setVersions(Set.of(v1));
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(javascriptFramework);
        v1.setJavascriptFramework(javascriptFrameworkSaved);
        Version versionSaved = versionRepository.save(v1);
        assertEquals(1, ((Collection<?>) versionRepository.findAll()).size());

        versionService.deleteVersion(versionSaved.getId());

        assertEquals(0, ((Collection<?>) versionRepository.findAll()).size());
    }

    @Test
    public void fulltextSearchVersionTest() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        Version v1 = new Version("1.2.3.4", LocalDate.of(2020, 1, 1), 4);
        Version v2 = new Version("1.1.1.2", LocalDate.of(2021, 1, 1), 5);
        Version v3 = new Version("1.1.1.2", LocalDate.of(2021, 1, 1), 5);
        javascriptFramework.setVersions(Set.of(v1, v2, v3));
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(javascriptFramework);
        v1.setJavascriptFramework(javascriptFrameworkSaved);
        v2.setJavascriptFramework(javascriptFrameworkSaved);
        v3.setJavascriptFramework(javascriptFrameworkSaved);
        versionRepository.save(v1);
        versionRepository.save(v2);
        versionRepository.save(v3);

        List list1 = versionService.fulltextSearch("1.1.1.");
        List list2 = versionService.fulltextSearch("1.2.3.");

        assertEquals(2, list1.size());
        assertEquals(1, list2.size());
    }
}

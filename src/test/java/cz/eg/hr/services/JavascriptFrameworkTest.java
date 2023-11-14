package cz.eg.hr.services;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.dtos.JavaScriptFrameworkInputDto;
import cz.eg.hr.dtos.JavascriptFrameworkUpdateDto;
import cz.eg.hr.dtos.VersionInDto;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(profiles = "test")
public class JavascriptFrameworkTest {

    @Autowired
    private JavascriptFrameworkService javascriptFrameworkService;
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
    public void getAllJavascriptFrameworksTest() {
        JavascriptFramework javascriptFramework1 = new JavascriptFramework("React");
        JavascriptFramework javascriptFramework2 = new JavascriptFramework("Angular");
        JavascriptFramework javascriptFrameworkSaved1 = javascriptFrameworkRepository.save(javascriptFramework1);
        JavascriptFramework javascriptFrameworkSaved2 = javascriptFrameworkRepository.save(javascriptFramework2);

        Iterable<JavascriptFramework> allFrameworks = javascriptFrameworkService.getAllJavascriptFrameworks();

        assertEquals(2, ((Collection<?>) allFrameworks).size());
        assertTrue(((Collection<JavascriptFramework>) allFrameworks).stream().anyMatch(js -> js.getId().equals(javascriptFrameworkSaved1.getId())));
        assertTrue(((Collection<JavascriptFramework>) allFrameworks).stream().anyMatch(js -> js.getId().equals(javascriptFrameworkSaved2.getId())));
        assertTrue(((Collection<JavascriptFramework>) allFrameworks).stream().anyMatch(js -> js.getName().equals(javascriptFrameworkSaved1.getName())));
        assertTrue(((Collection<JavascriptFramework>) allFrameworks).stream().anyMatch(js -> js.getName().equals(javascriptFrameworkSaved2.getName())));
    }

    @Test
    public void getJavascriptFrameworkByIdTest() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(javascriptFramework);

        JavascriptFramework javascriptFrameworkById = javascriptFrameworkService.getJavascriptFramework(javascriptFrameworkSaved.getId());

        assertEquals(javascriptFrameworkSaved.getId(), javascriptFrameworkById.getId());
        assertEquals(javascriptFrameworkSaved.getName(), javascriptFrameworkById.getName());
    }

    @Test
    public void getJavascriptFrameworkByIdNotFoundTest() {
        assertThrows(NoSuchElementException.class, () -> {
            javascriptFrameworkService.getJavascriptFramework(1L);
        });
    }

    @Test
    public void addJavascriptFrameworkWithVersionTest() {
        JavaScriptFrameworkInputDto javaScriptFrameworkInputDto = new JavaScriptFrameworkInputDto("React", "1.1", LocalDate.of(2020, 1, 1), 1);

        JavascriptFramework javascriptFramework = javascriptFrameworkService.addJavascriptFramework(javaScriptFrameworkInputDto);

        assertEquals(javaScriptFrameworkInputDto.getName(), javascriptFramework.getName());
        assertEquals(1, javascriptFramework.getVersions().size());
        assertTrue(javascriptFramework.getVersions().stream().anyMatch(v -> v.getVersionNumber().equals(javaScriptFrameworkInputDto.getVersionNumber())));
        assertTrue(javascriptFramework.getVersions().stream().anyMatch(v -> v.getStars() == javaScriptFrameworkInputDto.getStars()));
        assertTrue(javascriptFramework.getVersions().stream().anyMatch(v -> v.getEndOfSupport().equals(javaScriptFrameworkInputDto.getEndOfSupport())));
    }

    @Test
    public void addJavascriptFrameworkSoloTest() {
        JavaScriptFrameworkInputDto javaScriptFrameworkInputDto = new JavaScriptFrameworkInputDto("React");

        JavascriptFramework javascriptFramework = javascriptFrameworkService.addJavascriptFramework(javaScriptFrameworkInputDto);

        assertEquals(javaScriptFrameworkInputDto.getName(), javascriptFramework.getName());
        assertNull(javascriptFramework.getVersions());
    }

    @Test
    public void addJavascriptFrameworkAlreadyExistsTest() {
        JavaScriptFrameworkInputDto javaScriptFrameworkInputDto = new JavaScriptFrameworkInputDto("React");
        javascriptFrameworkService.addJavascriptFramework(javaScriptFrameworkInputDto);
        assertThrows(IllegalArgumentException.class, () -> {
            javascriptFrameworkService.addJavascriptFramework(javaScriptFrameworkInputDto);
        });
    }

    @Test
    public void addVersionToJavascriptFrameworkTest() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(javascriptFramework);
        VersionInDto versionInDto = new VersionInDto("1.1", LocalDate.of(2020, 1, 1), 4);

        JavascriptFramework javascriptFrameworkWithVersion = javascriptFrameworkService.addVersionToJavascriptFramework(javascriptFrameworkSaved.getId(), versionInDto);

        assertNotNull(javascriptFrameworkWithVersion.getVersions());
        assertEquals(1, javascriptFrameworkWithVersion.getVersions().size());
        assertTrue(javascriptFrameworkWithVersion.getVersions().stream().anyMatch(v -> v.getVersionNumber().equals(versionInDto.getVersionNumber())));
        assertTrue(javascriptFrameworkWithVersion.getVersions().stream().anyMatch(v -> v.getStars() == versionInDto.getStars()));
        assertTrue(javascriptFrameworkWithVersion.getVersions().stream().anyMatch(v -> v.getEndOfSupport().equals(versionInDto.getEndOfSupport())));
    }

    @Test
    public void addVersionToJavascriptFrameworkNotFoundTest() {
        VersionInDto versionInDto = new VersionInDto("1.1", LocalDate.of(2020, 1, 1), 4);
        assertThrows(NoSuchElementException.class, () -> {
            javascriptFrameworkService.addVersionToJavascriptFramework(1L, versionInDto);
        });
    }

    @Test
    public void addVersionToJavascriptFrameworkVersionAlreadyExistsTest() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(javascriptFramework);
        VersionInDto versionInDto = new VersionInDto("1.1", LocalDate.of(2020, 1, 1), 4);
        javascriptFrameworkService.addVersionToJavascriptFramework(javascriptFrameworkSaved.getId(), versionInDto);

        assertThrows(IllegalArgumentException.class, () -> {
            javascriptFrameworkService.addVersionToJavascriptFramework(javascriptFrameworkSaved.getId(), versionInDto);
        });
    }

    @Test
    public void updateJavascriptFrameworkTest() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(javascriptFramework);
        JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto = new JavascriptFrameworkUpdateDto("Angular");

        JavascriptFramework javascriptFrameworkById = javascriptFrameworkService.updateJavascriptFramework(javascriptFrameworkSaved.getId(), javascriptFrameworkUpdateDto);

        assertEquals(javascriptFrameworkSaved.getId(), javascriptFrameworkById.getId());
        assertEquals(javascriptFrameworkUpdateDto.getName(), javascriptFrameworkById.getName());
    }

    @Test
    public void updateJavascriptFrameworkByIdInputNullTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            javascriptFrameworkService.updateJavascriptFramework(1L, null);
        });
    }

    @Test
    public void updateJavascriptFrameworkByIdNameExistsTest() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        javascriptFrameworkRepository.save(javascriptFramework);
        JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto = new JavascriptFrameworkUpdateDto("React");

        assertThrows(IllegalArgumentException.class, () -> {
            javascriptFrameworkService.updateJavascriptFramework(1L, javascriptFrameworkUpdateDto);
        });
    }

    @Test
    public void updateJavascriptFrameworkByIdNotFoundTest() {
        JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto = new JavascriptFrameworkUpdateDto("React");

        assertThrows(NoSuchElementException.class, () -> {
            javascriptFrameworkService.updateJavascriptFramework(1L, javascriptFrameworkUpdateDto);
        });
    }

    @Test
    public void deleteJavascriptFrameworkTest() {
        JavascriptFramework javascriptFramework1 = new JavascriptFramework("React");
        JavascriptFramework javascriptFramework2 = new JavascriptFramework("Angular");
        JavascriptFramework javascriptFrameworkSaved1 = javascriptFrameworkRepository.save(javascriptFramework1);
        JavascriptFramework javascriptFrameworkSaved2 = javascriptFrameworkRepository.save(javascriptFramework2);
        assertEquals(2, ((Collection<JavascriptFramework>) javascriptFrameworkRepository.findAll()).size());

        javascriptFrameworkService.deleteFramework(javascriptFrameworkSaved1.getId());
        assertEquals(1, ((Collection<JavascriptFramework>) javascriptFrameworkRepository.findAll()).size());
        assertEquals(javascriptFramework2.getId(), javascriptFrameworkRepository.findById(javascriptFramework2.getId()).get().getId());
        assertEquals(javascriptFramework2.getName(), javascriptFrameworkRepository.findById(javascriptFramework2.getId()).get().getName());

        javascriptFrameworkService.deleteFramework(javascriptFrameworkSaved2.getId());
        assertEquals(0, ((Collection<JavascriptFramework>) javascriptFrameworkRepository.findAll()).size());
    }

    @Test
    public void fulltextSearchJavascriptFrameworkTest() {
        JavascriptFramework javascriptFramework1 = new JavascriptFramework("React");
        JavascriptFramework javascriptFramework2 = new JavascriptFramework("Angular");
        javascriptFrameworkRepository.save(javascriptFramework1);
        javascriptFrameworkRepository.save(javascriptFramework2);

        List fulltextSearch1 = javascriptFrameworkService.fulltextSearch("Reac");
        assertEquals(1, fulltextSearch1.size());

        List fulltextSearch2 = javascriptFrameworkService.fulltextSearch("Angula");
        assertEquals(1, fulltextSearch2.size());

        List fulltextSearch3 = javascriptFrameworkService.fulltextSearch("Vue");
        assertEquals(0, fulltextSearch3.size());
    }
}

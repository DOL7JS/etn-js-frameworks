package cz.eg.hr.services;


import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.data.Version;
import cz.eg.hr.repository.JavascriptFrameworkRepository;
import cz.eg.hr.repository.VersionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles(profiles = "test")
public class FulltextSearchServiceTest {
    @Autowired
    private FulltextSearchService<JavascriptFramework> fulltextSearchServiceJavascriptFramework;
    @Autowired
    private FulltextSearchService<Version> fulltextSearchServiceVersion;
    @Autowired
    private JavascriptFrameworkRepository javascriptFrameworkRepository;
    @Autowired
    private VersionRepository versionRepository;


    @BeforeEach
    @AfterEach
    void deleteRepository() {
        javascriptFrameworkRepository.deleteAll();
        versionRepository.deleteAll();
    }

    @Test
    public void givenSearchText_whenFulltextSearch_thenReturnListOfFoundJavascriptFramework_test() {
        JavascriptFramework javascriptFramework1 = new JavascriptFramework("React");
        javascriptFrameworkRepository.save(javascriptFramework1);

        List<JavascriptFramework> fulltextSearch = fulltextSearchServiceJavascriptFramework.fulltextSearch(new String[]{"name"}, javascriptFramework1.getName(), JavascriptFramework.class);
        assertEquals(1, fulltextSearch.size());
    }

    @Test
    public void givenSearchText_whenFulltextSearch_thenReturnListOfFoundVersions_test() {
        JavascriptFramework javascriptFramework1 = new JavascriptFramework("React");
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(javascriptFramework1);

        Version v1 = new Version("1.1", LocalDate.of(2020, 1, 1), 1);
        v1.setJavascriptFramework(javascriptFrameworkSaved);
        Version v2 = new Version("1.2", LocalDate.of(2020, 1, 1), 1);
        v2.setJavascriptFramework(javascriptFrameworkSaved);
        versionRepository.save(v1);

        List<Version> fulltextSearch = fulltextSearchServiceVersion.fulltextSearch(new String[]{"versionNumber", "endOfSupport", "stars"}, v1.getVersionNumber(), Version.class);
        assertEquals(1, fulltextSearch.size());

        versionRepository.save(v2);
        fulltextSearch = fulltextSearchServiceVersion.fulltextSearch(new String[]{"versionNumber", "endOfSupport", "stars"}, v1.getVersionNumber(), Version.class);
        assertEquals(2, fulltextSearch.size());


    }

}

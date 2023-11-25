package cz.eg.hr.repositories;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.data.Version;
import cz.eg.hr.repository.VersionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class VersionRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    VersionRepository versionRepository;

    @Test
    public void givenVersionAndJavascriptFramework_whenExistsByVersionNumberAndJavascriptFrameworkAndIdIsNot_thenReturnFalseWhenInJavascriptExistsVersionWithDifferentID() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        javascriptFramework = entityManager.persistAndFlush(javascriptFramework);
        Version version = new Version("1.1", LocalDate.of(2020, 1, 1), 1);
        version.setJavascriptFramework(javascriptFramework);
        version = entityManager.persistAndFlush(version);

        assertThat(versionRepository.existsByVersionNumberAndJavascriptFrameworkAndIdIsNot(version.getVersionNumber(), javascriptFramework, version.getId())).isFalse();
        assertThat(versionRepository.existsByVersionNumberAndJavascriptFrameworkAndIdIsNot(version.getVersionNumber(), javascriptFramework, 2L)).isTrue();
    }

    @Test
    public void givenVersionAndJavascriptFramework_whenExistsByVersionNumberAndJavascriptFramework_thenReturnTrueWhenInJavascriptExistsVersion() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");
        javascriptFramework = entityManager.persistAndFlush(javascriptFramework);
        Version version = new Version("1.1", LocalDate.of(2020, 1, 1), 1);
        version.setJavascriptFramework(javascriptFramework);
        version = entityManager.persistAndFlush(version);

        assertThat(versionRepository.existsByVersionNumberAndJavascriptFramework(version.getVersionNumber(), javascriptFramework)).isTrue();
        assertThat(versionRepository.existsByVersionNumberAndJavascriptFramework("1.2", javascriptFramework)).isFalse();
    }
}

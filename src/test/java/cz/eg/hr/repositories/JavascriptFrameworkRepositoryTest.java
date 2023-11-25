package cz.eg.hr.repositories;


import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.repository.JavascriptFrameworkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JavascriptFrameworkRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JavascriptFrameworkRepository javascriptFrameworkRepository;

    @Test
    public void givenJavascriptFramework_whenExistsByName_thenReturnTrueIfJavascriptExistsByName() {
        JavascriptFramework javascriptFramework = new JavascriptFramework("React");

        javascriptFramework = entityManager.persistAndFlush(javascriptFramework);

        assertThat(javascriptFrameworkRepository.existsByName(javascriptFramework.getName())).isTrue();
        assertThat(javascriptFrameworkRepository.existsByName("Angular")).isFalse();
    }

}

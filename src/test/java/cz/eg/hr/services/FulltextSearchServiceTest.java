package cz.eg.hr.services;


import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.repository.VersionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.lucene.util.fst.Builder;
import org.aspectj.lang.annotation.Before;
import org.hibernate.search.engine.search.query.dsl.SearchQuerySelectStep;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
public class FulltextSearchServiceTest {
//    @InjectMocks
//    private FulltextSearchService<JavascriptFramework> fulltextSearchService;
//    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
//    private SearchSession searchSession;
//    @Spy
//    private EntityManager entityManager;
//
//    @Test
//    public void givenSearchText_whenFulltextSearch_thenReturnListOfFoundJavascriptFramework_test() {
//
//        JavascriptFramework javascriptFramework1 = new JavascriptFramework("React");
//        List<JavascriptFramework> list = new ArrayList<>();
//        list.add(javascriptFramework1);
//        when(searchSession.search(JavascriptFramework.class)
//            .where(f -> f
//                .simpleQueryString()
//                .field("name")
//                .matching("React"))
//            .fetchAllHits())
//            .thenReturn(list);
//
//
//        List<JavascriptFramework> fulltextSearch = fulltextSearchService.fulltextSearch(new String[]{"name"}, javascriptFramework1.getName(), JavascriptFramework.class);
//        assertEquals(1, fulltextSearch.size());
//    }

}

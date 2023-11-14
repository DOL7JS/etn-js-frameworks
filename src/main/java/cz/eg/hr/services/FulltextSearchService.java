package cz.eg.hr.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FulltextSearchService {
    private final EntityManagerFactory entityManagerFactory;

    public FulltextSearchService(EntityManagerFactory entityManagerFactory) throws InterruptedException {
        this.entityManagerFactory = entityManagerFactory;
        EntityManager em = this.entityManagerFactory.createEntityManager();
        SearchSession searchSession = Search.session(em);
        MassIndexer massIndexer = searchSession.massIndexer();
        massIndexer.startAndWait();
    }

    public List fulltextSearch(String[] fields, String text, Class<?>[] types) {
        EntityManager em = entityManagerFactory.createEntityManager();
        SearchSession searchSession = Search.session(em);
        return searchSession.search(Arrays.asList(types))
            .where(f -> f.simpleQueryString().
                fields(fields).matching(text + "~1")).fetchAllHits();
    }
}

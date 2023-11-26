package cz.eg.hr.services;

import jakarta.persistence.EntityManager;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FulltextSearchService<T> implements IFulltextSearchService<T> {
    private final EntityManager entityManager;

    public FulltextSearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<T> fulltextSearch(String[] fields, String text, Class<T> type) {
        SearchSession searchSession = Search.session(entityManager);
        return searchSession
            .search(type)
            .where(f -> f
                .simpleQueryString()
                .fields(fields)
                .matching(text))
            .fetchAllHits();
    }
}

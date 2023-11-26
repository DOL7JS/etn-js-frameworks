package cz.eg.hr.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FulltextSearchService<T> implements IFulltextSearchService<T> {
    private final SearchSession searchSession;

    public FulltextSearchService(EntityManager entityManager) {
        this.searchSession = Search.session(entityManager);
    }

    @Transactional
    public List<T> fulltextSearch(String[] fields, String text, Class<T> type) {
        return searchSession
            .search(type)
            .where(f -> f
                .simpleQueryString()
                .fields(fields)
                .matching(text + "~1"))
            .fetchAllHits();
    }
}

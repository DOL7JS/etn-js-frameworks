package cz.eg.hr.repository;

import cz.eg.hr.data.JavascriptFramework;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JavascriptFrameworkRepository extends CrudRepository<JavascriptFramework, Long> {
    boolean existsByName(String name);
}

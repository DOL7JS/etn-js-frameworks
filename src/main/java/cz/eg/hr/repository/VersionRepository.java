package cz.eg.hr.repository;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.data.Version;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface VersionRepository extends CrudRepository<Version, Long> {

    @Query("select (count(v) > 0) from Version v where v.versionNumber = ?1 and v.javascriptFramework = ?2")
    boolean existsByVersionNumberAndJavascriptFramework(String versionNumber, JavascriptFramework javascriptFramework);

    @Query("""
        select (count(v) > 0) from Version v
        where v.versionNumber = ?1 and v.javascriptFramework = ?2 and v.id <> ?3""")
    boolean existsByVersionNumberAndJavascriptFrameworkAndIdIsNot(String versionNumber, JavascriptFramework javascriptFramework, Long id);
}

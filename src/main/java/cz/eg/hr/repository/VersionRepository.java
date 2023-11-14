package cz.eg.hr.repository;

import cz.eg.hr.data.Version;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface VersionRepository extends CrudRepository<Version, Long> {
}

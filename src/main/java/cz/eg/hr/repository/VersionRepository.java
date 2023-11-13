package cz.eg.hr.repository;

import cz.eg.hr.data.Version;
import org.springframework.data.repository.CrudRepository;

public interface VersionRepository extends CrudRepository<Version, Long> {
}

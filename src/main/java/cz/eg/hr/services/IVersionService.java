package cz.eg.hr.services;

import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.dtos.VersionOutDto;
import cz.eg.hr.rest.exceptions.EntityAlreadyExistsException;
import cz.eg.hr.rest.exceptions.EntityNotFoundException;

import java.util.List;

public interface IVersionService {
    /**
     * Method to get one Version by id
     *
     * @param id ID of Version to be returned.
     * @return Return {@link Version} mapped to VersionOutDto if is found by id
     * @throws EntityNotFoundException If entity Version is not found
     */
    VersionOutDto getVersion(Long id);

    /**
     * Method to get all Versions in database
     *
     * @return All Versions in database mapped to VersionOutDto
     */
    Iterable<VersionOutDto> getAllVersions();

    /**
     * Method that updates Version in database. Version will be updated if exists and if in corresponding Javascript framework
     * is not version with same versionNumber.
     *
     * @param id           ID of Version to be updated.
     * @param versionInDto DTO with values that will update Version
     * @return Updated Version mapped to VersionOutDto
     * @throws EntityNotFoundException      If Version is not found
     * @throws EntityAlreadyExistsException If Version exists in corresponding Javascript framework with same versionNumber
     */
    VersionOutDto updateJavascriptFrameworkVersion(Long id, VersionInDto versionInDto);

    /**
     * Delete Version by id if exists
     *
     * @param id ID of Version to be deleted.
     */
    void deleteVersion(Long id);

    /**
     * Fulltext search in table Version
     *
     * @param text Text to be searched in table
     * @return List of found Versions mapped to VersionOutDto with corresponding values
     */
    List<VersionOutDto> fulltextSearch(String text);
}

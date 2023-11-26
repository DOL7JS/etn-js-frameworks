package cz.eg.hr.services;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.dtos.JavaScriptFrameworkInputDto;
import cz.eg.hr.dtos.JavascriptFrameworkDto;
import cz.eg.hr.dtos.JavascriptFrameworkUpdateDto;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.rest.exceptions.EntityAlreadyExistsException;
import cz.eg.hr.rest.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IJavascriptFrameworkService {
    /**
     * Method to get all JavascriptFrameworks in database
     *
     * @return All JavascriptFrameworks in database mapped to JavascriptFrameworkDto
     */
    Iterable<JavascriptFrameworkDto> getAllJavascriptFrameworks();

    /**
     * Method to get one JavascriptFrameworks by id
     *
     * @param id ID of JavascriptFramework to be returned
     * @return One {@link JavascriptFramework} mapped to JavascriptFrameworkDto if is found by id
     * @throws EntityNotFoundException If entity JavascriptFramework is not found
     */
    JavascriptFrameworkDto getJavascriptFramework(Long id);

    /**
     * Method to add JavascriptFramework to database if JavascriptFramework with same name
     * does not exist already
     *
     * @param javaScriptFrameworkInputDto DTO with values that will be added to JavascriptFramework table
     * @return Created JavascriptFramework in database mapped to JavascriptFrameworkDto
     * @throws EntityAlreadyExistsException If name of JavascriptFramework already exists
     */
    JavascriptFrameworkDto addJavascriptFramework(JavaScriptFrameworkInputDto javaScriptFrameworkInputDto);

    /**
     * Method to add Version to JavascriptFramework if Version does not exist in current JavascriptFramework
     *
     * @param javascriptFrameworkId ID of JavascriptFramework to which will be Version added
     * @param versionInDto          Version to be added to JavascriptFramework
     * @return JavascriptFramework mapped to JavascriptFrameworkDto to which was added new Version
     * @throws EntityAlreadyExistsException If Version exists in current Javascript framework with same versionNumber
     */
    JavascriptFrameworkDto addVersionToJavascriptFramework(Long javascriptFrameworkId, @NotNull VersionInDto versionInDto);

    /**
     * Method to update name of JavascriptFramework if is found
     *
     * @param id                           ID of JavascriptFramework to be updated
     * @param javascriptFrameworkUpdateDto DTO with values that will update JavascriptFramework
     * @return Updated JavascriptFramework mapped to JavascriptFrameworkDto
     * @throws EntityAlreadyExistsException If name of JavascriptFramework already exists
     */
    JavascriptFrameworkDto updateJavascriptFramework(Long id, @NotNull JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto);

    /**
     * Delete JavascriptFramework by id if exists
     *
     * @param id ID of JavascriptFramework to be deleted.
     */
    void deleteFramework(Long id);

    /**
     * Fulltext search in table JavascriptFramework
     *
     * @param text Text to be searched in table
     * @return List of found JavascriptFramework mapped to JavascriptFrameworkDto with corresponding values
     */
    List<JavascriptFrameworkDto> fulltextSearch(String text);
}

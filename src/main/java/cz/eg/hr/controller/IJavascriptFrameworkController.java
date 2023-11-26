package cz.eg.hr.controller;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.dtos.JavaScriptFrameworkInputDto;
import cz.eg.hr.dtos.JavascriptFrameworkDto;
import cz.eg.hr.dtos.JavascriptFrameworkUpdateDto;
import cz.eg.hr.dtos.VersionInDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Interface for handling HTTP request for JavascriptFrameworks
 *
 * @author Petr Dolejs
 */
public interface IJavascriptFrameworkController {

    /**
     * Retrieves all JavascriptFrameworks using a GET request if the operation is successful.
     * Returns with status codes:
     * 200: successfully retrieve JavascriptFrameworks
     *
     * @return ResponseEntity that contains all JavascriptFrameworks if the operation is successful
     */
    ResponseEntity<Iterable<JavascriptFrameworkDto>> getAllFrameworks();

    /**
     * Retrieves one JavascriptFramework by ID using a GET request if the operation is successful.
     * Returns with status codes:
     * 200: successfully retrieve JavascriptFramework
     * 404: JavascriptFramework is not found
     *
     * @param id ID of JavascriptFramework to be found
     * @return ResponseEntity that contains one JavascriptFramework found by ID if the operation is successful
     */
    ResponseEntity<JavascriptFrameworkDto> getFramework(@PathVariable Long id);

    /**
     * Add JavascriptFramework using a POST request if the operation is successful.
     * It will add JavascriptFramework if JavaScriptFrameworkInputDto contains only name,
     * Returns with status codes:
     * 200: successfully added JavascriptFramework
     * 400: JavascriptFramework name of JavascriptFramework already exists
     *
     * @param javaScriptFrameworkInputDto DTO which contains values to be added to database
     * @return ResponseEntity that contains added JavascriptFramework if the operation is successful
     */
    ResponseEntity<JavascriptFrameworkDto> addFramework(@RequestBody @Valid JavaScriptFrameworkInputDto javaScriptFrameworkInputDto);

    /**
     * Add Version to JavascriptFramework using a POST request if the operation is successful.
     * Returns with status codes:
     * 200: successfully added Version to JavascriptFramework
     * 400: Version with same versionNumber already exists in JavascriptFramework
     *
     * @param id           ID of JavascriptFramework to which will be Version added
     * @param versionInDto DTO if Version to be added to JavascriptFramework
     * @return ResponseEntity that contains JavascriptFramework if the operation is successful
     */
    ResponseEntity<JavascriptFrameworkDto> addVersionToFramework(@PathVariable Long id, @RequestBody @Valid VersionInDto versionInDto);

    /**
     * Update JavascriptFramework by ID using a PATCH request if the operation is successful.
     * Returns with status codes:
     * 200: successfully updated JavascriptFramework
     * 400: JavascriptFramework with new name already exist
     * 404: JavascriptFramework is not found
     *
     * @param id                           ID of JavascriptFramework to be updated
     * @param javascriptFrameworkUpdateDto DTO containing new values
     * @return ResponseEntity that contains updated JavascriptFramework if the operation is successful
     */
    ResponseEntity<JavascriptFrameworkDto> updateFramework(@PathVariable Long id, @RequestBody @Valid JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto);

    /**
     * Delete JavascriptFramework by ID using a DELETE request if the operation is successful.
     *
     * @param id ID of JavascriptFramework to be deleted
     */
    void deleteFramework(@PathVariable Long id);

    /**
     * Fulltext Search in JavascriptFramework using a GET request if the operation is successful.
     * Returns with status codes:
     * 200: successfully retrieve JavascriptFramework
     *
     * @param text Text to be searched in table
     * @return ResponseEntity that contains found Versions if the operation is successful
     */
    ResponseEntity<List<JavascriptFrameworkDto>> fulltextSearch(@RequestParam String text);
}

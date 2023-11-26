package cz.eg.hr.services;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.dtos.JavaScriptFrameworkInputDto;
import cz.eg.hr.dtos.JavascriptFrameworkDto;
import cz.eg.hr.dtos.JavascriptFrameworkUpdateDto;
import cz.eg.hr.dtos.VersionInDto;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IJavascriptFrameworkService {

    Iterable<JavascriptFrameworkDto> getAllJavascriptFrameworks();

    JavascriptFrameworkDto getJavascriptFramework(Long id);

    JavascriptFrameworkDto addJavascriptFramework(JavaScriptFrameworkInputDto javaScriptFrameworkInputDto);

    JavascriptFrameworkDto addVersionToJavascriptFramework(Long javascriptFrameworkId, @NotNull VersionInDto versionInDto);

    JavascriptFrameworkDto updateJavascriptFramework(Long id, @NotNull JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto);

    void deleteFramework(Long id);

    List<JavascriptFrameworkDto> fulltextSearch(String text);
}

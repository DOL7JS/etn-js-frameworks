package cz.eg.hr.services;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.dtos.JavaScriptFrameworkInputDto;
import cz.eg.hr.dtos.JavascriptFrameworkUpdateDto;
import cz.eg.hr.dtos.VersionInDto;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IJavascriptFrameworkService {

    public Iterable<?> getAllJavascriptFrameworks();

    public JavascriptFramework getJavascriptFramework(Long id);

    public JavascriptFramework addJavascriptFramework(JavaScriptFrameworkInputDto javaScriptFrameworkInputDto);

    public JavascriptFramework addVersionToJavascriptFramework(Long javascriptFrameworkId, @NotNull VersionInDto versionInDto);

    public JavascriptFramework updateJavascriptFramework(Long id, @NotNull JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto);

    public void deleteFramework(Long id);

    public List<?> fulltextSearch(String text);
}

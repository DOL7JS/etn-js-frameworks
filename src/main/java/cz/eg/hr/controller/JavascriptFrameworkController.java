package cz.eg.hr.controller;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.dtos.JavaScriptFrameworkInputDto;
import cz.eg.hr.dtos.JavascriptFrameworkDto;
import cz.eg.hr.dtos.JavascriptFrameworkUpdateDto;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.services.JavascriptFrameworkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("framework")
@RestController
public class JavascriptFrameworkController implements IJavascriptFrameworkController {

    private final JavascriptFrameworkService javascriptFrameworkService;

    @Autowired
    public JavascriptFrameworkController(JavascriptFrameworkService javascriptFrameworkService) {
        this.javascriptFrameworkService = javascriptFrameworkService;
    }


    @GetMapping("")
    public ResponseEntity<Iterable<JavascriptFrameworkDto>> getAllFrameworks() {
        return ResponseEntity.ok(javascriptFrameworkService.getAllJavascriptFrameworks());
    }


    @GetMapping("{id}")
    public ResponseEntity<JavascriptFrameworkDto> getFramework(@PathVariable Long id) {
        return ResponseEntity.ok(javascriptFrameworkService.getJavascriptFramework(id));
    }


    @PostMapping("")
    public ResponseEntity<JavascriptFrameworkDto> addFramework(@RequestBody @Valid JavaScriptFrameworkInputDto javaScriptFrameworkInputDto) {
        return ResponseEntity.ok(javascriptFrameworkService.addJavascriptFramework(javaScriptFrameworkInputDto));
    }


    @PostMapping("{id}")
    public ResponseEntity<JavascriptFrameworkDto> addVersionToFramework(@PathVariable Long id, @RequestBody @Valid VersionInDto versionInDto) {
        return ResponseEntity.ok(javascriptFrameworkService.addVersionToJavascriptFramework(id, versionInDto));
    }


    @PatchMapping("{id}")
    public ResponseEntity<JavascriptFrameworkDto> updateFramework(@PathVariable Long id, @RequestBody @Valid JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto) {
        return ResponseEntity.ok(javascriptFrameworkService.updateJavascriptFramework(id, javascriptFrameworkUpdateDto));
    }


    @DeleteMapping("{id}")
    public void deleteFramework(@PathVariable Long id) {
        javascriptFrameworkService.deleteFramework(id);
    }


    @GetMapping("fulltextSearch")
    public ResponseEntity<List<JavascriptFrameworkDto>> fulltextSearch(@RequestParam String text) {
        return ResponseEntity.ok(javascriptFrameworkService.fulltextSearch(text));
    }

}

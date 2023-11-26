package cz.eg.hr.controller;


import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.dtos.VersionOutDto;
import cz.eg.hr.services.VersionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("version")
public class VersionController implements IVersionController {

    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }


    @GetMapping("")
    public ResponseEntity<Iterable<VersionOutDto>> getAllVersion() {
        return ResponseEntity.ok(versionService.getAllVersions());
    }


    @GetMapping("{id}")
    public ResponseEntity<VersionOutDto> getVersion(@PathVariable Long id) {
        return ResponseEntity.ok(versionService.getVersion(id));
    }


    @PutMapping("{id}")
    public ResponseEntity<VersionOutDto> updateVersion(@PathVariable Long id, @RequestBody @Valid VersionInDto versionInDto) {
        return ResponseEntity.ok(versionService.updateJavascriptFrameworkVersion(id, versionInDto));
    }


    @DeleteMapping("{id}")
    public void deleteVersion(@PathVariable Long id) {
        versionService.deleteVersion(id);
    }


    @GetMapping("fulltextSearch")
    public ResponseEntity<List<VersionOutDto>> fulltextSearch(@RequestParam String text) {
        return ResponseEntity.ok(versionService.fulltextSearch(text));
    }
}

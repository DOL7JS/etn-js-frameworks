package cz.eg.hr.services;


import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.JavaScriptFrameworkInputDto;
import cz.eg.hr.dtos.JavascriptFrameworkUpdateDto;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.repository.JavascriptFrameworkRepository;
import cz.eg.hr.repository.VersionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class JavascriptFrameworkService {

    private final JavascriptFrameworkRepository javascriptFrameworkRepository;
    private final VersionRepository versionRepository;

    public JavascriptFrameworkService(JavascriptFrameworkRepository javascriptFrameworkRepository, VersionRepository versionRepository) {
        this.javascriptFrameworkRepository = javascriptFrameworkRepository;
        this.versionRepository = versionRepository;
    }

    public Iterable<JavascriptFramework> getAllJavascriptFrameworks() {
        return javascriptFrameworkRepository.findAll();
    }

    public JavascriptFramework getJavascriptFramework(Long id) {
        return javascriptFrameworkRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Framework with id " + id + " not found."));
    }

    public JavascriptFramework addJavascriptFramework(JavaScriptFrameworkInputDto javaScriptFrameworkInputDto) {
        if (javascriptFrameworkRepository.existsByName(javaScriptFrameworkInputDto.getName())) {
            throw new IllegalArgumentException("Framework " + javaScriptFrameworkInputDto.getName() + " already exists.");
        }
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(new JavascriptFramework(javaScriptFrameworkInputDto.getName()));
        if (javaScriptFrameworkInputDto.getVersionNumber() != null) {
            Version version = new Version(javaScriptFrameworkInputDto.getVersionNumber(), javaScriptFrameworkInputDto.getEndOfSupport(), javaScriptFrameworkInputDto.getStars());
            version.setJavascriptFramework(javascriptFrameworkSaved);
            Version versionSaved = versionRepository.save(version);
            javascriptFrameworkSaved.setVersions(Set.of(versionSaved));
        }
        return javascriptFrameworkSaved;
    }

    public JavascriptFramework addVersionToJavascriptFramework(Long javascriptFrameworkId, VersionInDto versionInDto) {
        JavascriptFramework javascriptFramework = javascriptFrameworkRepository.findById(javascriptFrameworkId).orElseThrow(() -> new NoSuchElementException("Framework with id " + javascriptFrameworkId + " not found."));
        boolean versionAlreadyExists = javascriptFramework.getVersions().stream().anyMatch(version -> version.getVersionNumber().equals(versionInDto.getVersionNumber()));
        if (versionAlreadyExists) {
            throw new IllegalArgumentException("Version " + versionInDto.getVersionNumber() + " already exists in js framework " + javascriptFramework.getName());
        }
        Version version = new Version(versionInDto.getVersionNumber(), versionInDto.getEndOfSupport(), versionInDto.getStars());
        version.setJavascriptFramework(javascriptFramework);
        Version versionSaved = versionRepository.save(version);
        javascriptFramework.getVersions().add(versionSaved);
        return javascriptFramework;
    }

    public JavascriptFramework updateJavascriptFramework(Long id, JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto) {
        if (javascriptFrameworkRepository.existsByName(javascriptFrameworkUpdateDto.getName())) {
            throw new IllegalArgumentException("Framework " + javascriptFrameworkUpdateDto.getName() + "already exists.");
        }
        JavascriptFramework javascriptFramework = javascriptFrameworkRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Framework with id " + id + " not found."));
        javascriptFramework.setName(javascriptFrameworkUpdateDto.getName());
        return javascriptFrameworkRepository.save(javascriptFramework);
    }

    public void deleteFramework(Long id) {
        if (javascriptFrameworkRepository.existsById(id)) {
            javascriptFrameworkRepository.deleteById(id);
        }
    }
}


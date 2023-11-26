package cz.eg.hr.services;


import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.JavaScriptFrameworkInputDto;
import cz.eg.hr.dtos.JavascriptFrameworkDto;
import cz.eg.hr.dtos.JavascriptFrameworkUpdateDto;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.repository.JavascriptFrameworkRepository;
import cz.eg.hr.repository.VersionRepository;
import cz.eg.hr.rest.exceptions.EntityAlreadyExistsException;
import cz.eg.hr.rest.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * Service class for CRUD operations on JavascriptFramework entity and add Version to JavascriptFramework.
 * This class also can do fulltext search in JavascriptFramework table in database.
 *
 * @author Petr Dolejs
 */
@Service
public class JavascriptFrameworkService implements IJavascriptFrameworkService {

    private final JavascriptFrameworkRepository javascriptFrameworkRepository;
    private final VersionRepository versionRepository;
    private final FulltextSearchService<JavascriptFrameworkDto> fulltextSearchService;
    private final ModelMapper modelMapper;

    public JavascriptFrameworkService(JavascriptFrameworkRepository javascriptFrameworkRepository, VersionRepository versionRepository, FulltextSearchService<JavascriptFrameworkDto> fulltextSearchService, ModelMapper modelMapper) {
        this.javascriptFrameworkRepository = javascriptFrameworkRepository;
        this.versionRepository = versionRepository;
        this.fulltextSearchService = fulltextSearchService;
        this.modelMapper = modelMapper;
    }


    public Iterable<JavascriptFrameworkDto> getAllJavascriptFrameworks() {
        return StreamSupport.stream(javascriptFrameworkRepository.findAll().spliterator(), false).map(entity -> modelMapper.map(entity, JavascriptFrameworkDto.class)).toList();
    }


    public JavascriptFrameworkDto getJavascriptFramework(Long id) {
        return modelMapper.map(javascriptFrameworkRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Framework with id " + id + " not found.");
        }), JavascriptFrameworkDto.class);
    }

    public JavascriptFrameworkDto addJavascriptFramework(@NotNull JavaScriptFrameworkInputDto javaScriptFrameworkInputDto) {
        if (javascriptFrameworkRepository.existsByName(javaScriptFrameworkInputDto.getName())) {
            throw new EntityAlreadyExistsException("Framework " + javaScriptFrameworkInputDto.getName() + " already exists.");
        }
        JavascriptFramework javascriptFrameworkSaved = javascriptFrameworkRepository.save(new JavascriptFramework(javaScriptFrameworkInputDto.getName()));
        if (javaScriptFrameworkInputDto.getVersionNumber() != null) {
            Version version = new Version(javaScriptFrameworkInputDto.getVersionNumber(), javaScriptFrameworkInputDto.getEndOfSupport(), javaScriptFrameworkInputDto.getStars());
            version.setJavascriptFramework(javascriptFrameworkSaved);
            Version versionSaved = versionRepository.save(version);
            javascriptFrameworkSaved.setVersions(Set.of(versionSaved));
        }
        return modelMapper.map(javascriptFrameworkSaved, JavascriptFrameworkDto.class);
    }


    public JavascriptFrameworkDto addVersionToJavascriptFramework(Long javascriptFrameworkId, @NotNull VersionInDto versionInDto) {
        JavascriptFramework javascriptFramework = javascriptFrameworkRepository.findById(javascriptFrameworkId).orElseThrow(() -> new EntityNotFoundException("Framework with id " + javascriptFrameworkId + " not found."));
        boolean versionAlreadyExists = versionRepository.existsByVersionNumberAndJavascriptFramework(versionInDto.getVersionNumber(), javascriptFramework);
        if (versionAlreadyExists) {
            throw new EntityAlreadyExistsException("Version " + versionInDto.getVersionNumber() + " already exists in js framework " + javascriptFramework.getName());
        }
        Version version = new Version(versionInDto.getVersionNumber(), versionInDto.getEndOfSupport(), versionInDto.getStars());
        version.setJavascriptFramework(javascriptFramework);
        Version versionSaved = versionRepository.save(version);
        javascriptFramework.getVersions().add(versionSaved);
        return modelMapper.map(javascriptFramework, JavascriptFrameworkDto.class);
    }


    public JavascriptFrameworkDto updateJavascriptFramework(Long id, @NotNull JavascriptFrameworkUpdateDto javascriptFrameworkUpdateDto) {
        Objects.requireNonNull(javascriptFrameworkUpdateDto);

        if (javascriptFrameworkRepository.existsByName(javascriptFrameworkUpdateDto.getName())) {
            throw new EntityAlreadyExistsException("Framework " + javascriptFrameworkUpdateDto.getName() + " already exists.");
        }
        JavascriptFramework javascriptFramework = javascriptFrameworkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Framework with id " + id + " not found."));
        javascriptFramework.setName(javascriptFrameworkUpdateDto.getName());
        return modelMapper.map(javascriptFrameworkRepository.save(javascriptFramework), JavascriptFrameworkDto.class);
    }


    public void deleteFramework(Long id) {
        if (javascriptFrameworkRepository.existsById(id)) {
            javascriptFrameworkRepository.deleteById(id);
        }
    }

    public List<JavascriptFrameworkDto> fulltextSearch(String text) {
        return fulltextSearchService.fulltextSearch(new String[]{"name"}, text, JavascriptFrameworkDto.class);
    }
}


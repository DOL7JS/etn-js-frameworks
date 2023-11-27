package cz.eg.hr.services;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.dtos.VersionOutDto;
import cz.eg.hr.repository.VersionRepository;
import cz.eg.hr.rest.exceptions.EntityAlreadyExistsException;
import cz.eg.hr.rest.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service class for get, update and delete operations on Version entity and fulltext search in Version table in database
 *
 * @author Petr Dolejs
 */
@Service
public class VersionService implements IVersionService {

    private final VersionRepository versionRepository;
    private final ModelMapper modelMapper;
    private final FulltextSearchService<VersionOutDto> fulltextSearchService;

    public VersionService(VersionRepository versionRepository, ModelMapper modelMapperIn, FulltextSearchService<VersionOutDto> fulltextSearchService) {
        this.versionRepository = versionRepository;
        this.modelMapper = modelMapperIn;
        this.fulltextSearchService = fulltextSearchService;
        TypeMap<Version, VersionOutDto> propertyMapper = modelMapper.createTypeMap(Version.class, VersionOutDto.class);
        propertyMapper.addMappings(modelMapper -> modelMapper.map(source -> source.getJavascriptFramework().getName(), VersionOutDto::setJavascriptFramework));

    }

    public VersionOutDto getVersion(Long id) {
        Version version = versionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Version with id " + id + " not found."));
        return modelMapper.map(version, VersionOutDto.class);
    }

    public Iterable<VersionOutDto> getAllVersions() {
        return StreamSupport.stream(versionRepository.findAll().spliterator(), false).map(item -> modelMapper.map(item, VersionOutDto.class)).collect(Collectors.toList());
    }

    public VersionOutDto updateJavascriptFrameworkVersion(Long id, @NotNull VersionInDto versionInDto) {
        Objects.requireNonNull(versionInDto);
        Objects.requireNonNull(versionInDto.getVersionNumber());

        Version version = versionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Version with id " + id + " not found."));
        JavascriptFramework javascriptFramework = version.getJavascriptFramework();
        boolean versionAlreadyExists = versionRepository.existsByVersionNumberAndJavascriptFrameworkAndIdIsNot(versionInDto.getVersionNumber(), javascriptFramework, id);
        if (versionAlreadyExists) {
            throw new EntityAlreadyExistsException("Version " + versionInDto.getVersionNumber() + " already exists in js framework " + javascriptFramework.getName());
        }
        Version versionUpdated = modelMapper.map(versionInDto, Version.class);
        versionUpdated.setId(version.getId());
        versionUpdated.setJavascriptFramework(javascriptFramework);
        return modelMapper.map(versionRepository.save(versionUpdated), VersionOutDto.class);
    }

    public void deleteVersion(Long id) {
        if (versionRepository.existsById(id)) {
            versionRepository.deleteById(id);
        }
    }

    public List<VersionOutDto> fulltextSearch(String text) {
        return fulltextSearchService.fulltextSearch(new String[]{"stars", "endOfSupport", "versionNumber"}, text, VersionOutDto.class);
    }
}

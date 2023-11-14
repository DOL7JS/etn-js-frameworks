package cz.eg.hr.services;

import cz.eg.hr.data.JavascriptFramework;
import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.dtos.VersionOutDto;
import cz.eg.hr.repository.VersionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.StreamSupport;

@Service
public class VersionService {

    private final VersionRepository versionRepository;
    private final ModelMapper modelMapper;
    private final TypeMap<Version, VersionOutDto> propertyMapper;
    private final FulltextSearchService fulltextSearchService;

    public VersionService(VersionRepository versionRepository, ModelMapper modelMapperIn, FulltextSearchService fulltextSearchService) {
        this.versionRepository = versionRepository;
        this.modelMapper = modelMapperIn;
        this.fulltextSearchService = fulltextSearchService;
        propertyMapper = modelMapper.createTypeMap(Version.class, VersionOutDto.class);
        propertyMapper.addMappings(modelMapper -> modelMapper.map(source -> source.getJavascriptFramework().getName(), VersionOutDto::setJavascriptFramework));
    }


    public VersionOutDto getVersion(Long id) {
        Version version = versionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Version with id " + id + " not found."));
        return modelMapper.map(version, VersionOutDto.class);
    }

    public Iterable<VersionOutDto> getAllVersions() {
        return StreamSupport.stream(versionRepository.findAll().spliterator(), false).map(item -> modelMapper.map(item, VersionOutDto.class)).toList();
    }

    public Version updateJavascriptFrameworkVersion(Long id, VersionInDto versionInDto) {
        Version version = versionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Version with id " + id + " not found."));
        JavascriptFramework javascriptFramework = version.getJavascriptFramework();
        Set<Version> versions = javascriptFramework.getVersions();
        boolean versionAlreadyExists = versions.stream().anyMatch(v -> v.getVersionNumber().equals(versionInDto.getVersionNumber()) && !v.getId().equals(id));
        if (versionAlreadyExists) {
            throw new IllegalArgumentException("Version " + version.getVersionNumber() + " already exists in js framework " + javascriptFramework.getName());
        }
        Version versionUpdated = modelMapper.map(versionInDto, Version.class);
        versionUpdated.setId(version.getId());
        versionUpdated.setJavascriptFramework(javascriptFramework);
        return versionRepository.save(versionUpdated);
    }

    public void deleteVersion(Long id) {
        if (versionRepository.existsById(id)) {
            versionRepository.deleteById(id);
        }
    }

    public List fulltextSearch(String text) {
        return fulltextSearchService.fulltextSearch(new String[]{"stars", "endOfSupport", "versionNumber"}, text, new Class[]{Version.class});
    }
}

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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class VersionService {

    private final VersionRepository versionRepository;
    private final ModelMapper modelMapper;
    TypeMap<Version, VersionOutDto> propertyMapper;

    public VersionService(VersionRepository versionRepository, ModelMapper modelMapperIn) {
        this.versionRepository = versionRepository;
        this.modelMapper = modelMapperIn;
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
        boolean versionAlreadyExists = javascriptFramework.getVersions().stream().anyMatch(v -> v.getVersionNumber().equals(versionInDto.getVersionNumber()) && !v.getId().equals(id));
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
}

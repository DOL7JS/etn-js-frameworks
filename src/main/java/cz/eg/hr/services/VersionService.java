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

    /**
     * Method to get one Version by id
     *
     * @param id ID of Version to be returned.
     * @return Return {@link VersionOutDto} if Version is found by id
     * @throws EntityNotFoundException If entity Version is not found
     */

    public VersionOutDto getVersion(Long id) {
        Version version = versionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Version with id " + id + " not found."));
        return modelMapper.map(version, VersionOutDto.class);
    }

    /**
     * Method to get all Versions in database
     *
     * @return All Versions in database
     */
    public Iterable<VersionOutDto> getAllVersions() {
        return StreamSupport.stream(versionRepository.findAll().spliterator(), false).map(item -> modelMapper.map(item, VersionOutDto.class)).collect(Collectors.toList());
    }

    /**
     * Method that updates Version in database. Version will be updated if exists and in  Javascript framework
     * is not version with same versionNumber.
     *
     * @param id           ID of Version to be updated.
     * @param versionInDto DTO with values that will update Version
     * @return Updated Version
     * @throws EntityNotFoundException      If Version is not found
     * @throws EntityAlreadyExistsException If Version exists in current Javascript framework with same versionNumber
     */
    public VersionOutDto updateJavascriptFrameworkVersion(Long id, @NotNull VersionInDto versionInDto) {
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

    /**
     * Delete Version by id if exists
     *
     * @param id ID of Version to be deleted.
     */
    public void deleteVersion(Long id) {
        if (versionRepository.existsById(id)) {
            versionRepository.deleteById(id);
        }
    }

    /**
     * Fulltext search in table Version
     *
     * @param text Text to be searched in table
     * @return List of found Versions with corresponding values
     */
    public List<VersionOutDto> fulltextSearch(String text) {
        return fulltextSearchService.fulltextSearch(new String[]{"stars", "endOfSupport", "versionNumber"}, text, VersionOutDto.class);
    }
}

package cz.eg.hr.services;

import cz.eg.hr.data.Version;
import cz.eg.hr.dtos.VersionInDto;
import cz.eg.hr.dtos.VersionOutDto;

import java.util.List;

public interface IVersionService {
    VersionOutDto getVersion(Long id);

    Iterable<VersionOutDto> getAllVersions();

    VersionOutDto updateJavascriptFrameworkVersion(Long id, VersionInDto versionInDto);

    void deleteVersion(Long id);

    List<VersionOutDto> fulltextSearch(String text);
}

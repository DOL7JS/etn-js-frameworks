package cz.eg.hr.dtos;

import cz.eg.hr.data.Version;

import java.util.Set;

public class JavascriptFrameworkDto {
    private Long id;

    private String name;

    private Set<Version> versions;

    public JavascriptFrameworkDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Version> getVersions() {
        return versions;
    }

    public void setVersions(Set<Version> versions) {
        this.versions = versions;
    }
}

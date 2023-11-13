package cz.eg.hr.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

public class VersionOutDto {

    @NotBlank(message = "Id is mandatory")
    private Long id;
    @NotBlank(message = "Version number is mandatory")
    private String versionNumber;
    private LocalDate endOfSupport;
    @Range(min = 0, max = 5, message = "Stars have to be between 0-5")
    @NotNull
    private Integer stars;
    @NotBlank(message = "Javascript framework name is mandatory.")
    private String javascriptFramework;

    public VersionOutDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public LocalDate getEndOfSupport() {
        return endOfSupport;
    }

    public void setEndOfSupport(LocalDate endOfSupport) {
        this.endOfSupport = endOfSupport;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String getJavascriptFramework() {
        return javascriptFramework;
    }

    public void setJavascriptFramework(String javascriptFramework) {
        this.javascriptFramework = javascriptFramework;
    }
}

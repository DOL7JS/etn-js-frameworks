package cz.eg.hr.dtos;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

public class VersionInDto {

    @NotBlank(message = "Version number is mandatory")
    private String versionNumber;
    private LocalDate endOfSupport;
    @Range(min = 0, max = 5, message = "Stars have to be between 0-5")
    @NotNull
    private Integer stars;

    public VersionInDto() {
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
}

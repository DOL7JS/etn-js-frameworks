package cz.eg.hr.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

public class JavaScriptFrameworkInputDto {

    @NotBlank(message = "Name is mandatory")
    @Length(max = 30)
    private String name;
    private String versionNumber;
    private LocalDate endOfSupport;
    @Range(min = 0, max = 5, message = "Stars have to be between 0-5")
    private int stars;

    public JavaScriptFrameworkInputDto() {
    }

    public JavaScriptFrameworkInputDto(String name, String versionNumber, LocalDate endOfSupport, int stars) {
        this.name = name;
        this.versionNumber = versionNumber;
        this.endOfSupport = endOfSupport;
        this.stars = stars;
    }

    public JavaScriptFrameworkInputDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}

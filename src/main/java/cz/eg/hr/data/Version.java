package cz.eg.hr.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Version {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String versionNumber;
    private LocalDate endOfSupport;
    private int stars;

    @ManyToOne()
    @JoinColumn(nullable = false)
    @JsonIgnore
    private JavascriptFramework javascriptFramework;

    public Version() {
    }

    public Version(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Version(String versionNumber, LocalDate endOfSupport, int stars) {
        this.versionNumber = versionNumber;
        this.endOfSupport = endOfSupport;
        this.stars = stars;
    }

    public JavascriptFramework getJavascriptFramework() {
        return javascriptFramework;
    }

    public void setJavascriptFramework(JavascriptFramework javascriptFramework) {
        this.javascriptFramework = javascriptFramework;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

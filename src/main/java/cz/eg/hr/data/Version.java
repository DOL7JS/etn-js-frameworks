package cz.eg.hr.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.eg.hr.bridges.IntegerValueBridge;
import cz.eg.hr.bridges.LocalDateBridge;
import jakarta.persistence.*;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.time.LocalDate;

@Entity
@Indexed(index = "idx_version")
public class Version {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @FullTextField
    private String versionNumber;
    @GenericField(valueBridge = @ValueBridgeRef(type = LocalDateBridge.class))
    private LocalDate endOfSupport;
    @GenericField(valueBridge = @ValueBridgeRef(type = IntegerValueBridge.class))
    private Integer stars;

    @ManyToOne()
    @JoinColumn(nullable = false)
    @JsonIgnore
    private JavascriptFramework javascriptFramework;

    public Version() {
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

    public void setStars(Integer stars) {
        this.stars = stars;
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

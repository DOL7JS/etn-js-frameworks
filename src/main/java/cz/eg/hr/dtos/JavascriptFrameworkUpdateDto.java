package cz.eg.hr.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;


public class JavascriptFrameworkUpdateDto {

    @NotBlank(message = "Name is mandatory")
    @Length(max = 30)
    private String name;

    public JavascriptFrameworkUpdateDto() {
    }

    public JavascriptFrameworkUpdateDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

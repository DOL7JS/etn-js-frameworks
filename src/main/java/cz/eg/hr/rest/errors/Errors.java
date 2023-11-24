package cz.eg.hr.rest.errors;

import java.util.List;

public record Errors(List<ValidationError> errors) {
}

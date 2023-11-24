package cz.eg.hr.rest.errors;

public record ValidationError(String field, String message) {
}

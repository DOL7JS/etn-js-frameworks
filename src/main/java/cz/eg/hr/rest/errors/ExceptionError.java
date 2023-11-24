package cz.eg.hr.rest.errors;

import java.sql.Timestamp;

public record ExceptionError(String message, int statusCode, Timestamp timestamp,String location) {
}

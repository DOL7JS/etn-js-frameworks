package cz.eg.hr.bridges;

import org.hibernate.search.engine.cfg.spi.ParseUtils;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateBridge implements ValueBridge<LocalDate, String> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public String toIndexedValue(LocalDate value, ValueBridgeToIndexedValueContext context) {
        return value == null ? null : FORMATTER.format(value);
    }

    @Override
    public LocalDate fromIndexedValue(String value, ValueBridgeFromIndexedValueContext context) {
        return value == null ? null : ParseUtils.parseLocalDate(value);

    }
}

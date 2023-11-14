package cz.eg.hr.bridges;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class IntegerValueBridge implements ValueBridge<Integer, String> {

    @Override
    public String toIndexedValue(Integer value, ValueBridgeToIndexedValueContext context) {
        return value == null ? null : String.valueOf(value);

    }

    @Override
    public Integer fromIndexedValue(String value, ValueBridgeFromIndexedValueContext context) {
        return value == null ? null : Integer.parseInt(value);
    }


}

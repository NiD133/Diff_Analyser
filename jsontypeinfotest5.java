package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonTypeInfoTestTest5 {

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, visible = true, defaultImpl = JsonTypeInfo.class, requireTypeIdForSubtypes = OptBoolean.TRUE)
    private final static class Anno1 {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY, property = "ext", defaultImpl = Void.class, requireTypeIdForSubtypes = OptBoolean.FALSE)
    private final static class Anno2 {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY, property = "ext", defaultImpl = Void.class)
    private final static class Anno3 {
    }

    @Test
    public void testDefaultValueForRequireTypeIdForSubtypes() {
        // default value
        JsonTypeInfo.Value v3 = JsonTypeInfo.Value.from(Anno3.class.getAnnotation(JsonTypeInfo.class));
        assertNull(v3.getRequireTypeIdForSubtypes());
        // toString()
        assertEquals("JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext," + "defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=null)", v3.toString());
    }
}

package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonTypeInfoTestTest2 {

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
    public void testFromAnnotation() throws Exception {
        JsonTypeInfo.Value v1 = JsonTypeInfo.Value.from(Anno1.class.getAnnotation(JsonTypeInfo.class));
        assertEquals(JsonTypeInfo.Id.CLASS, v1.getIdType());
        // default from annotation definition
        assertEquals(JsonTypeInfo.As.PROPERTY, v1.getInclusionType());
        // default from annotation definition
        assertEquals("@class", v1.getPropertyName());
        assertTrue(v1.getIdVisible());
        assertNull(v1.getDefaultImpl());
        assertTrue(v1.getRequireTypeIdForSubtypes());
        JsonTypeInfo.Value v2 = JsonTypeInfo.Value.from(Anno2.class.getAnnotation(JsonTypeInfo.class));
        assertEquals(JsonTypeInfo.Id.NAME, v2.getIdType());
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, v2.getInclusionType());
        assertEquals("ext", v2.getPropertyName());
        assertFalse(v2.getIdVisible());
        assertEquals(Void.class, v2.getDefaultImpl());
        assertFalse(v2.getRequireTypeIdForSubtypes());
        assertTrue(v1.equals(v1));
        assertTrue(v2.equals(v2));
        assertFalse(v1.equals(v2));
        assertFalse(v2.equals(v1));
        assertEquals("JsonTypeInfo.Value(idType=CLASS,includeAs=PROPERTY,propertyName=@class,defaultImpl=NULL,idVisible=true,requireTypeIdForSubtypes=true)", v1.toString());
        assertEquals("JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext,defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=false)", v2.toString());
    }
}

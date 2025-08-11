package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonTypeInfoTest {

    // Test classes with different JsonTypeInfo configurations
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, visible = true,
            defaultImpl = JsonTypeInfo.class, requireTypeIdForSubtypes = OptBoolean.TRUE)
    private static final class ClassTypeInfo { }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY,
            property = "ext", defaultImpl = Void.class, requireTypeIdForSubtypes = OptBoolean.FALSE)
    private static final class NameTypeInfo { }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY,
            property = "ext", defaultImpl = Void.class)
    private static final class DefaultNameTypeInfo { }

    @Test
    public void testNullJsonTypeInfoValue() {
        // Ensure that a null input returns a null JsonTypeInfo.Value
        assertNull(JsonTypeInfo.Value.from(null));
    }

    @Test
    public void testJsonTypeInfoFromAnnotations() throws Exception {
        // Test JsonTypeInfo.Value creation from ClassTypeInfo annotation
        JsonTypeInfo.Value classTypeInfoValue = JsonTypeInfo.Value.from(ClassTypeInfo.class.getAnnotation(JsonTypeInfo.class));
        assertClassTypeInfo(classTypeInfoValue);

        // Test JsonTypeInfo.Value creation from NameTypeInfo annotation
        JsonTypeInfo.Value nameTypeInfoValue = JsonTypeInfo.Value.from(NameTypeInfo.class.getAnnotation(JsonTypeInfo.class));
        assertNameTypeInfo(nameTypeInfoValue);

        // Validate equality and toString representations
        assertNotEquals(classTypeInfoValue, nameTypeInfoValue);
        assertEquals("JsonTypeInfo.Value(idType=CLASS,includeAs=PROPERTY,propertyName=@class,defaultImpl=NULL,idVisible=true,requireTypeIdForSubtypes=true)", classTypeInfoValue.toString());
        assertEquals("JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext,defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=false)", nameTypeInfoValue.toString());
    }

    @Test
    public void testJsonTypeInfoMutators() throws Exception {
        JsonTypeInfo.Value value = JsonTypeInfo.Value.from(ClassTypeInfo.class.getAnnotation(JsonTypeInfo.class));

        // Test mutator methods for JsonTypeInfo.Value
        assertSame(value, value.withIdType(JsonTypeInfo.Id.CLASS));
        assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, value.withIdType(JsonTypeInfo.Id.MINIMAL_CLASS).getIdType());
        assertEquals(JsonTypeInfo.Id.SIMPLE_NAME, value.withIdType(JsonTypeInfo.Id.SIMPLE_NAME).getIdType());

        assertSame(value, value.withInclusionType(JsonTypeInfo.As.PROPERTY));
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, value.withInclusionType(JsonTypeInfo.As.EXTERNAL_PROPERTY).getInclusionType());

        assertSame(value, value.withDefaultImpl(null));
        assertEquals(String.class, value.withDefaultImpl(String.class).getDefaultImpl());

        assertSame(value, value.withIdVisible(true));
        assertFalse(value.withIdVisible(false).getIdVisible());

        assertEquals("foobar", value.withPropertyName("foobar").getPropertyName());
    }

    @Test
    public void testRequireTypeIdForSubtypes() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        assertNull(emptyValue.getRequireTypeIdForSubtypes());

        JsonTypeInfo.Value requireTypeIdTrue = emptyValue.withRequireTypeIdForSubtypes(Boolean.TRUE);
        assertEquals(Boolean.TRUE, requireTypeIdTrue.getRequireTypeIdForSubtypes());

        JsonTypeInfo.Value requireTypeIdFalse = emptyValue.withRequireTypeIdForSubtypes(Boolean.FALSE);
        assertEquals(Boolean.FALSE, requireTypeIdFalse.getRequireTypeIdForSubtypes());

        JsonTypeInfo.Value requireTypeIdDefault = emptyValue.withRequireTypeIdForSubtypes(null);
        assertNull(requireTypeIdDefault.getRequireTypeIdForSubtypes());
    }

    @Test
    public void testDefaultValueForRequireTypeIdForSubtypes() {
        JsonTypeInfo.Value defaultNameTypeInfoValue = JsonTypeInfo.Value.from(DefaultNameTypeInfo.class.getAnnotation(JsonTypeInfo.class));
        assertNull(defaultNameTypeInfoValue.getRequireTypeIdForSubtypes());
        
        assertEquals("JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext," 
                + "defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=null)", defaultNameTypeInfoValue.toString());
    }

    private void assertClassTypeInfo(JsonTypeInfo.Value value) {
        assertEquals(JsonTypeInfo.Id.CLASS, value.getIdType());
        assertEquals(JsonTypeInfo.As.PROPERTY, value.getInclusionType());
        assertEquals("@class", value.getPropertyName());
        assertTrue(value.getIdVisible());
        assertNull(value.getDefaultImpl());
        assertTrue(value.getRequireTypeIdForSubtypes());
    }

    private void assertNameTypeInfo(JsonTypeInfo.Value value) {
        assertEquals(JsonTypeInfo.Id.NAME, value.getIdType());
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, value.getInclusionType());
        assertEquals("ext", value.getPropertyName());
        assertFalse(value.getIdVisible());
        assertEquals(Void.class, value.getDefaultImpl());
        assertFalse(value.getRequireTypeIdForSubtypes());
    }
}
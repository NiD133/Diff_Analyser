package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for JsonTypeInfo.Value class functionality including:
 * - Creating Value instances from annotations
 * - Mutating Value instances 
 * - Handling requireTypeIdForSubtypes property
 */
public class JsonTypeInfoTest {
    
    // Test annotation with CLASS id type and explicit settings
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS, 
        visible = true,
        defaultImpl = JsonTypeInfo.class, 
        requireTypeIdForSubtypes = OptBoolean.TRUE
    )
    private static final class ClassTypeInfoAnnotation { }

    // Test annotation with NAME id type and EXTERNAL_PROPERTY inclusion
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, 
        include = As.EXTERNAL_PROPERTY,
        property = "ext",
        defaultImpl = Void.class, 
        requireTypeIdForSubtypes = OptBoolean.FALSE
    )
    private static final class ExternalPropertyAnnotation { }

    // Test annotation with default requireTypeIdForSubtypes (not explicitly set)
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, 
        include = As.EXTERNAL_PROPERTY,
        property = "ext",
        defaultImpl = Void.class
    )
    private static final class DefaultRequireTypeIdAnnotation { }

    @Test
    public void testCreateValueFromNullAnnotation() {
        // When creating Value from null annotation, should return null (not empty value)
        assertNull(JsonTypeInfo.Value.from(null));
    }

    @Test
    public void testCreateValueFromClassTypeAnnotation() throws Exception {
        JsonTypeInfo annotation = ClassTypeInfoAnnotation.class.getAnnotation(JsonTypeInfo.class);
        JsonTypeInfo.Value value = JsonTypeInfo.Value.from(annotation);
        
        // Verify all properties are correctly extracted from annotation
        assertEquals(JsonTypeInfo.Id.CLASS, value.getIdType());
        assertEquals(JsonTypeInfo.As.PROPERTY, value.getInclusionType()); // default value
        assertEquals("@class", value.getPropertyName()); // default for CLASS type
        assertTrue(value.getIdVisible());
        assertNull(value.getDefaultImpl()); // JsonTypeInfo.class means "no default"
        assertTrue(value.getRequireTypeIdForSubtypes());
        
        // Verify string representation
        String expectedToString = "JsonTypeInfo.Value(idType=CLASS,includeAs=PROPERTY,propertyName=@class," +
                                "defaultImpl=NULL,idVisible=true,requireTypeIdForSubtypes=true)";
        assertEquals(expectedToString, value.toString());
    }

    @Test
    public void testCreateValueFromExternalPropertyAnnotation() throws Exception {
        JsonTypeInfo annotation = ExternalPropertyAnnotation.class.getAnnotation(JsonTypeInfo.class);
        JsonTypeInfo.Value value = JsonTypeInfo.Value.from(annotation);
        
        // Verify all properties are correctly extracted from annotation
        assertEquals(JsonTypeInfo.Id.NAME, value.getIdType());
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, value.getInclusionType());
        assertEquals("ext", value.getPropertyName());
        assertFalse(value.getIdVisible()); // default value
        assertEquals(Void.class, value.getDefaultImpl());
        assertFalse(value.getRequireTypeIdForSubtypes());
        
        // Verify string representation
        String expectedToString = "JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext," +
                                "defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=false)";
        assertEquals(expectedToString, value.toString());
    }

    @Test
    public void testValueEquality() throws Exception {
        JsonTypeInfo.Value classValue = JsonTypeInfo.Value.from(
            ClassTypeInfoAnnotation.class.getAnnotation(JsonTypeInfo.class));
        JsonTypeInfo.Value externalValue = JsonTypeInfo.Value.from(
            ExternalPropertyAnnotation.class.getAnnotation(JsonTypeInfo.class));

        // Test reflexive equality
        assertTrue(classValue.equals(classValue));
        assertTrue(externalValue.equals(externalValue));

        // Test inequality between different configurations
        assertFalse(classValue.equals(externalValue));
        assertFalse(externalValue.equals(classValue));
    }

    @Test
    public void testMutateIdType() throws Exception {
        JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.from(
            ClassTypeInfoAnnotation.class.getAnnotation(JsonTypeInfo.class));
        
        // Mutating to same value should return same instance
        assertSame(originalValue, originalValue.withIdType(JsonTypeInfo.Id.CLASS));
        
        // Mutating to different values should create new instances
        JsonTypeInfo.Value minimalClassValue = originalValue.withIdType(JsonTypeInfo.Id.MINIMAL_CLASS);
        assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, minimalClassValue.getIdType());
        
        JsonTypeInfo.Value simpleNameValue = originalValue.withIdType(JsonTypeInfo.Id.SIMPLE_NAME);
        assertEquals(JsonTypeInfo.Id.SIMPLE_NAME, simpleNameValue.getIdType());
    }

    @Test
    public void testMutateInclusionType() throws Exception {
        JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.from(
            ClassTypeInfoAnnotation.class.getAnnotation(JsonTypeInfo.class));
        
        // Mutating to same value should return same instance
        assertSame(originalValue, originalValue.withInclusionType(JsonTypeInfo.As.PROPERTY));
        
        // Mutating to different value should create new instance
        JsonTypeInfo.Value externalPropertyValue = originalValue.withInclusionType(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, externalPropertyValue.getInclusionType());
    }

    @Test
    public void testMutateDefaultImplementation() throws Exception {
        JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.from(
            ClassTypeInfoAnnotation.class.getAnnotation(JsonTypeInfo.class));
        
        // Mutating to same value (null) should return same instance
        assertSame(originalValue, originalValue.withDefaultImpl(null));
        
        // Mutating to different value should create new instance
        JsonTypeInfo.Value stringDefaultValue = originalValue.withDefaultImpl(String.class);
        assertEquals(String.class, stringDefaultValue.getDefaultImpl());
    }

    @Test
    public void testMutateIdVisibility() throws Exception {
        JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.from(
            ClassTypeInfoAnnotation.class.getAnnotation(JsonTypeInfo.class));
        
        // Mutating to same value should return same instance
        assertSame(originalValue, originalValue.withIdVisible(true));
        
        // Mutating to different value should create new instance
        JsonTypeInfo.Value invisibleValue = originalValue.withIdVisible(false);
        assertFalse(invisibleValue.getIdVisible());
    }

    @Test
    public void testMutatePropertyName() throws Exception {
        JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.from(
            ClassTypeInfoAnnotation.class.getAnnotation(JsonTypeInfo.class));
        
        JsonTypeInfo.Value customPropertyValue = originalValue.withPropertyName("foobar");
        assertEquals("foobar", customPropertyValue.getPropertyName());
    }

    @Test
    public void testMutateRequireTypeIdForSubtypes() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        assertNull(emptyValue.getRequireTypeIdForSubtypes());

        // Test setting to true
        JsonTypeInfo.Value requireTypeIdTrue = emptyValue.withRequireTypeIdForSubtypes(Boolean.TRUE);
        assertEquals(Boolean.TRUE, requireTypeIdTrue.getRequireTypeIdForSubtypes());

        // Test setting to false
        JsonTypeInfo.Value requireTypeIdFalse = emptyValue.withRequireTypeIdForSubtypes(Boolean.FALSE);
        assertEquals(Boolean.FALSE, requireTypeIdFalse.getRequireTypeIdForSubtypes());

        // Test setting to null (default)
        JsonTypeInfo.Value requireTypeIdDefault = emptyValue.withRequireTypeIdForSubtypes(null);
        assertNull(requireTypeIdDefault.getRequireTypeIdForSubtypes());
    }

    @Test
    public void testDefaultRequireTypeIdForSubtypes() {
        // When requireTypeIdForSubtypes is not explicitly set, should default to null
        JsonTypeInfo.Value value = JsonTypeInfo.Value.from(
            DefaultRequireTypeIdAnnotation.class.getAnnotation(JsonTypeInfo.class));
        assertNull(value.getRequireTypeIdForSubtypes());
        
        // Verify string representation includes null value
        String expectedToString = "JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext," + 
                                "defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=null)";
        assertEquals(expectedToString, value.toString());
    }
}
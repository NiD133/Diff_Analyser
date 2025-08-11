package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for JsonTypeInfo.Value class which handles JSON type information configuration.
 * This class is used to configure how type metadata is included in JSON serialization/deserialization.
 */
public class JsonTypeInfoValueTest {

    // Test data constants
    private static final String SAMPLE_PROPERTY_NAME = "customTypeProperty";
    private static final String COMPLEX_PROPERTY_NAME = "@2LLQRbW9{J2*\"1GY";

    // ========== Id.getDefaultPropertyName() Tests ==========

    @Test
    public void customIdType_shouldReturnNullDefaultPropertyName() {
        JsonTypeInfo.Id customId = JsonTypeInfo.Id.CUSTOM;
        
        String defaultPropertyName = customId.getDefaultPropertyName();
        
        assertNull("CUSTOM id type should not have a default property name", defaultPropertyName);
    }

    // ========== Value.withPropertyName() Tests ==========

    @Test
    public void withPropertyName_whenSettingToNull_shouldReturnSameInstance() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        JsonTypeInfo.Value result = emptyValue.withPropertyName(null);
        
        assertSame("Setting property name to null should return same instance", emptyValue, result);
    }

    @Test
    public void withPropertyName_whenSettingToNewValue_shouldCreateNewInstance() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        JsonTypeInfo.Value result = emptyValue.withPropertyName("EXTERNAL_PROPERTY");
        
        assertNotEquals("Setting new property name should create different instance", emptyValue, result);
        assertFalse("New instance should have idVisible=false", result.getIdVisible());
    }

    // ========== Value.withRequireTypeIdForSubtypes() Tests ==========

    @Test
    public void withRequireTypeIdForSubtypes_whenChangingValue_shouldCreateNewInstance() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        Boolean requireTypeId = Boolean.valueOf("EXTERNAL_PROPERTY"); // false
        
        JsonTypeInfo.Value result = emptyValue.withRequireTypeIdForSubtypes(requireTypeId);
        
        assertNotEquals("Changing requireTypeIdForSubtypes should create new instance", emptyValue, result);
        assertFalse("Result should have idVisible=false", result.getIdVisible());
    }

    @Test
    public void withRequireTypeIdForSubtypes_whenSettingToNull_shouldReturnSameInstance() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        JsonTypeInfo.Value result = emptyValue.withRequireTypeIdForSubtypes(null);
        
        assertSame("Setting requireTypeIdForSubtypes to null should return same instance", emptyValue, result);
    }

    @Test
    public void withRequireTypeIdForSubtypes_whenSettingToSameValue_shouldReturnSameInstance() {
        // Create a value with specific configuration
        JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.MINIMAL_CLASS, 
            JsonTypeInfo.As.WRAPPER_OBJECT, 
            COMPLEX_PROPERTY_NAME, 
            Object.class, 
            false, 
            Boolean.FALSE
        );
        
        JsonTypeInfo.Value result = originalValue.withRequireTypeIdForSubtypes(Boolean.FALSE);
        
        assertEquals("Setting same requireTypeIdForSubtypes value should be equal", originalValue, result);
        assertEquals(COMPLEX_PROPERTY_NAME, result.getPropertyName());
        assertFalse(result.getIdVisible());
    }

    // ========== Value.withIdVisible() Tests ==========

    @Test
    public void withIdVisible_whenChangingValue_shouldCreateNewInstance() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        JsonTypeInfo.Value result = emptyValue.withIdVisible(true);
        
        assertNotEquals("Changing idVisible should create new instance", emptyValue, result);
    }

    @Test
    public void withIdVisible_whenSettingToSameValue_shouldReturnSameInstance() {
        JsonTypeInfo.Value configuredValue = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.MINIMAL_CLASS, 
            JsonTypeInfo.As.WRAPPER_OBJECT, 
            COMPLEX_PROPERTY_NAME, 
            Object.class, 
            false, 
            Boolean.FALSE
        );
        
        JsonTypeInfo.Value result = configuredValue.withIdVisible(false);
        
        assertSame("Setting same idVisible value should return same instance", configuredValue, result);
        assertEquals(COMPLEX_PROPERTY_NAME, result.getPropertyName());
    }

    // ========== Value.withInclusionType() Tests ==========

    @Test
    public void withInclusionType_whenSettingToSameValue_shouldReturnSameInstance() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        JsonTypeInfo.Value result = emptyValue.withInclusionType(JsonTypeInfo.As.PROPERTY);
        
        assertSame("Setting same inclusion type should return same instance", emptyValue, result);
    }

    @Test
    public void withInclusionType_whenChangingValue_shouldCreateNewInstance() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        JsonTypeInfo.Value result = emptyValue.withInclusionType(JsonTypeInfo.As.WRAPPER_OBJECT);
        
        assertNotEquals("Changing inclusion type should create new instance", emptyValue, result);
        assertFalse("New instance should have idVisible=false", result.getIdVisible());
    }

    // ========== Value.withIdType() Tests ==========

    @Test
    public void withIdType_whenSettingToSameValue_shouldReturnSameInstance() {
        JsonTypeInfo.Value configuredValue = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.MINIMAL_CLASS, 
            JsonTypeInfo.As.WRAPPER_OBJECT, 
            COMPLEX_PROPERTY_NAME, 
            Object.class, 
            false, 
            Boolean.FALSE
        );
        
        JsonTypeInfo.Value result = configuredValue.withIdType(JsonTypeInfo.Id.MINIMAL_CLASS);
        
        assertSame("Setting same ID type should return same instance", configuredValue, result);
        assertEquals(COMPLEX_PROPERTY_NAME, result.getPropertyName());
        assertFalse(result.getIdVisible());
    }

    @Test
    public void withIdType_whenChangingValue_shouldCreateNewInstance() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        JsonTypeInfo.Value result = emptyValue.withIdType(JsonTypeInfo.Id.CLASS);
        
        assertEquals("ID type should be changed", JsonTypeInfo.Id.CLASS, result.getIdType());
        assertFalse("New instance should have idVisible=false", result.getIdVisible());
    }

    // ========== Value.withDefaultImpl() Tests ==========

    @Test
    public void withDefaultImpl_whenChangingValue_shouldCreateNewInstance() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        JsonTypeInfo.Value result = emptyValue.withDefaultImpl(Object.class);
        
        assertNotEquals("Changing default implementation should create new instance", emptyValue, result);
        assertFalse("New instance should have idVisible=false", result.getIdVisible());
    }

    @Test
    public void withDefaultImpl_whenSettingToSameValue_shouldReturnSameInstance() {
        JsonTypeInfo.Value configuredValue = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.MINIMAL_CLASS, 
            JsonTypeInfo.As.WRAPPER_OBJECT, 
            COMPLEX_PROPERTY_NAME, 
            Object.class, 
            false, 
            Boolean.FALSE
        );
        
        JsonTypeInfo.Value result = configuredValue.withDefaultImpl(Object.class);
        
        assertSame("Setting same default implementation should return same instance", configuredValue, result);
        assertEquals(COMPLEX_PROPERTY_NAME, result.getPropertyName());
        assertFalse(result.getIdVisible());
    }

    // ========== Value.equals() Tests ==========

    @Test
    public void equals_whenComparingWithDifferentTypes_shouldReturnFalse() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        boolean result = emptyValue.equals("EXTERNAL_PROPERTY");
        
        assertFalse("Value should not equal string", result);
    }

    @Test
    public void equals_whenComparingWithNull_shouldReturnFalse() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        boolean result = emptyValue.equals(null);
        
        assertFalse("Value should not equal null", result);
    }

    @Test
    public void equals_whenComparingSameInstance_shouldReturnTrue() {
        JsonTypeInfo.Value configuredValue = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.MINIMAL_CLASS, 
            JsonTypeInfo.As.WRAPPER_OBJECT, 
            COMPLEX_PROPERTY_NAME, 
            Object.class, 
            false, 
            Boolean.FALSE
        );
        
        boolean result = configuredValue.equals(configuredValue);
        
        assertTrue("Value should equal itself", result);
        assertEquals(COMPLEX_PROPERTY_NAME, configuredValue.getPropertyName());
        assertFalse(configuredValue.getIdVisible());
    }

    @Test
    public void equals_whenComparingDifferentConfigurations_shouldReturnFalse() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value configuredValue = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.DEDUCTION, 
            JsonTypeInfo.As.WRAPPER_OBJECT, 
            "", 
            Object.class, 
            false, 
            Boolean.FALSE
        );
        
        boolean result = configuredValue.equals(emptyValue);
        
        assertFalse("Different configurations should not be equal", result);
        assertFalse(configuredValue.getIdVisible());
    }

    // ========== Value.toString() Tests ==========

    @Test
    public void toString_forEmptyValue_shouldShowCorrectFormat() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        String result = emptyValue.toString();
        
        assertEquals("Empty value toString should match expected format",
            "JsonTypeInfo.Value(idType=NONE,includeAs=PROPERTY,propertyName=null,defaultImpl=NULL,idVisible=false,requireTypeIdForSubtypes=null)", 
            result);
    }

    @Test
    public void toString_forConfiguredValue_shouldShowAllProperties() {
        JsonTypeInfo.Value configuredValue = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.MINIMAL_CLASS, 
            JsonTypeInfo.As.WRAPPER_OBJECT, 
            COMPLEX_PROPERTY_NAME, 
            Object.class, 
            false, 
            Boolean.FALSE
        );
        
        String result = configuredValue.toString();
        
        assertEquals("Configured value toString should show all properties",
            "JsonTypeInfo.Value(idType=MINIMAL_CLASS,includeAs=WRAPPER_OBJECT,propertyName=" + COMPLEX_PROPERTY_NAME + ",defaultImpl=java.lang.Object,idVisible=false,requireTypeIdForSubtypes=false)", 
            result);
    }

    // ========== Value.isEnabled() Tests ==========

    @Test
    public void isEnabled_forEmptyValue_shouldReturnFalse() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        boolean result = JsonTypeInfo.Value.isEnabled(emptyValue);
        
        assertFalse("Empty value should not be enabled", result);
    }

    @Test
    public void isEnabled_forConfiguredValue_shouldReturnTrue() {
        JsonTypeInfo.Value configuredValue = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.MINIMAL_CLASS, 
            JsonTypeInfo.As.WRAPPER_OBJECT, 
            COMPLEX_PROPERTY_NAME, 
            Object.class, 
            false, 
            Boolean.FALSE
        );
        
        boolean result = JsonTypeInfo.Value.isEnabled(configuredValue);
        
        assertTrue("Configured value should be enabled", result);
        assertFalse(configuredValue.getIdVisible());
        assertEquals(COMPLEX_PROPERTY_NAME, configuredValue.getPropertyName());
    }

    @Test
    public void isEnabled_forNullValue_shouldReturnFalse() {
        boolean result = JsonTypeInfo.Value.isEnabled(null);
        
        assertFalse("Null value should not be enabled", result);
    }

    // ========== Value.from() Tests ==========

    @Test
    public void from_withNullAnnotation_shouldReturnNull() {
        JsonTypeInfo.Value result = JsonTypeInfo.Value.from(null);
        
        assertNull("from(null) should return null", result);
    }

    @Test
    public void from_withMockedAnnotationReturningNulls_shouldThrowNullPointerException() {
        JsonTypeInfo mockAnnotation = mock(JsonTypeInfo.class);
        when(mockAnnotation.defaultImpl()).thenReturn(null);
        when(mockAnnotation.include()).thenReturn(null);
        when(mockAnnotation.property()).thenReturn(null);
        when(mockAnnotation.requireTypeIdForSubtypes()).thenReturn(null);
        when(mockAnnotation.use()).thenReturn(null);
        when(mockAnnotation.visible()).thenReturn(false);
        
        try {
            JsonTypeInfo.Value.from(mockAnnotation);
            fail("Expected NullPointerException when annotation returns null values");
        } catch (NullPointerException e) {
            // Expected - the method should handle null annotation properties gracefully
        }
    }

    // ========== Value.construct() Tests ==========

    @Test
    public void construct_withComplexConfiguration_shouldCreateCorrectValue() {
        JsonTypeInfo.Value result = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.CLASS, 
            JsonTypeInfo.As.WRAPPER_OBJECT, 
            "customProperty", 
            JsonTypeInfo.class, 
            true, 
            Boolean.FALSE
        );
        
        assertTrue("Should have idVisible=true", result.getIdVisible());
        assertEquals("Should have correct property name", "customProperty", result.getPropertyName());
    }

    @Test
    public void construct_withNameIdAndNullProperty_shouldUseDefaultPropertyName() {
        JsonTypeInfo.Value result = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.NAME, 
            JsonTypeInfo.As.PROPERTY, 
            null, 
            Integer.class, 
            false, 
            Boolean.FALSE
        );
        
        assertEquals("Should use default property name for NAME id", "@type", result.getPropertyName());
        assertFalse("Should have idVisible=false", result.getIdVisible());
    }

    // ========== Getter Tests ==========

    @Test
    public void getters_shouldReturnCorrectValues() {
        JsonTypeInfo.Value configuredValue = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.MINIMAL_CLASS, 
            JsonTypeInfo.As.WRAPPER_OBJECT, 
            "testProperty", 
            Integer.class, 
            false, 
            Boolean.TRUE
        );
        
        // Test all getters
        assertNotNull("getDefaultImpl should not be null", configuredValue.getDefaultImpl());
        assertEquals("getIdType should match", JsonTypeInfo.Id.MINIMAL_CLASS, configuredValue.getIdType());
        assertEquals("getInclusionType should match", JsonTypeInfo.As.WRAPPER_OBJECT, configuredValue.getInclusionType());
        assertEquals("getPropertyName should match", "testProperty", configuredValue.getPropertyName());
        assertFalse("getIdVisible should be false", configuredValue.getIdVisible());
        assertEquals("getRequireTypeIdForSubtypes should match", Boolean.TRUE, configuredValue.getRequireTypeIdForSubtypes());
    }

    @Test
    public void getRequireTypeIdForSubtypes_forEmptyValue_shouldReturnNull() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        Boolean result = emptyValue.getRequireTypeIdForSubtypes();
        
        assertNull("Empty value should have null requireTypeIdForSubtypes", result);
    }

    @Test
    public void getIdVisible_forEmptyValue_shouldReturnFalse() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        boolean result = emptyValue.getIdVisible();
        
        assertFalse("Empty value should have idVisible=false", result);
    }

    @Test
    public void valueFor_shouldReturnJsonTypeInfoClass() {
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        
        Class<JsonTypeInfo> result = emptyValue.valueFor();
        
        assertEquals("valueFor should return JsonTypeInfo class", JsonTypeInfo.class, result);
    }

    // ========== Constructor Tests ==========

    @Test
    public void constructor_withVisibleTrue_shouldCreateCorrectValue() {
        JsonTypeInfo.Value result = new JsonTypeInfo.Value(
            JsonTypeInfo.Id.DEDUCTION, 
            JsonTypeInfo.As.WRAPPER_OBJECT, 
            "testProperty", 
            Integer.class, 
            true, 
            Boolean.TRUE
        );
        
        assertTrue("Should have idVisible=true", result.getIdVisible());
        assertNotNull("Should have property name", result.getPropertyName());
    }

    @Test
    public void construct_withExistingPropertyInclusion_shouldCreateCorrectValue() {
        JsonTypeInfo.Value result = JsonTypeInfo.Value.construct(
            JsonTypeInfo.Id.CLASS, 
            JsonTypeInfo.As.EXISTING_PROPERTY, 
            "existingProp", 
            Object.class, 
            true, 
            Boolean.FALSE
        );
        
        assertTrue("Should have idVisible=true", result.getIdVisible());
        assertEquals("Should have correct property name", "existingProp", result.getPropertyName());
        assertEquals("Should have correct inclusion type", JsonTypeInfo.As.EXISTING_PROPERTY, result.getInclusionType());
    }
}
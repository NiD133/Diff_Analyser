package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for JacksonInject annotation and its Value helper class.
 * Tests cover empty values, annotation parsing, standard object methods, and factory methods.
 */
public class JacksonInjectTest {
    
    /**
     * Test class with various JacksonInject annotation configurations
     * used to verify annotation parsing functionality.
     */
    private static final class TestClassWithInjectAnnotations {
        @JacksonInject(value = "inject", useInput = OptBoolean.FALSE, optional = OptBoolean.FALSE)
        public int fieldWithAllPropertiesSet;

        @JacksonInject
        public int fieldWithDefaultValues;

        @JacksonInject(optional = OptBoolean.TRUE)
        public int optionalField;
    }

    private static final JacksonInject.Value EMPTY_VALUE = JacksonInject.Value.empty();

    @Test
    public void testEmptyValueBehavior() {
        // Verify empty value properties
        assertNull(EMPTY_VALUE.getId(), "Empty value should have null ID");
        assertNull(EMPTY_VALUE.getUseInput(), "Empty value should have null useInput");
        
        // Test willUseInput behavior with different defaults
        assertTrue(EMPTY_VALUE.willUseInput(true), 
            "Empty value should use default when willUseInput called with true");
        assertFalse(EMPTY_VALUE.willUseInput(false), 
            "Empty value should use default when willUseInput called with false");

        // Test that construct method returns same empty instance for null/empty values
        assertSame(EMPTY_VALUE, JacksonInject.Value.construct(null, null, null),
            "Constructing with all nulls should return same empty instance");
        assertSame(EMPTY_VALUE, JacksonInject.Value.construct("", null, null),
            "Empty string should be coerced to null and return same empty instance");
    }

    @Test
    public void testCreatingValueFromAnnotation() throws Exception {
        // Test handling of null annotation
        assertSame(EMPTY_VALUE, JacksonInject.Value.from(null), 
            "Creating value from null annotation should return empty value");

        // Test annotation with all properties explicitly set
        JacksonInject annotationWithAllProperties = getAnnotationFromField("fieldWithAllPropertiesSet");
        JacksonInject.Value valueFromFullAnnotation = JacksonInject.Value.from(annotationWithAllProperties);
        
        assertEquals("inject", valueFromFullAnnotation.getId(), 
            "Value should have correct ID from annotation");
        assertEquals(Boolean.FALSE, valueFromFullAnnotation.getUseInput(), 
            "Value should have correct useInput from annotation");
        assertEquals("JacksonInject.Value(id=inject,useInput=false,optional=false)", 
            valueFromFullAnnotation.toString(),
            "String representation should match expected format");
        
        // Verify inequality with empty value
        assertNotEquals(valueFromFullAnnotation, EMPTY_VALUE, 
            "Value with properties should not equal empty value");
        assertNotEquals(EMPTY_VALUE, valueFromFullAnnotation, 
            "Empty value should not equal value with properties");

        // Test annotation with default values
        JacksonInject annotationWithDefaults = getAnnotationFromField("fieldWithDefaultValues");
        JacksonInject.Value valueFromDefaultAnnotation = JacksonInject.Value.from(annotationWithDefaults);
        
        assertEquals(JacksonInject.Value.construct(null, null, null), valueFromDefaultAnnotation,
            "Annotation with defaults should create value equivalent to all nulls");

        // Test annotation with optional field set
        JacksonInject optionalAnnotation = getAnnotationFromField("optionalField");
        JacksonInject.Value valueFromOptionalAnnotation = JacksonInject.Value.from(optionalAnnotation);
        
        assertEquals(JacksonInject.Value.construct(null, null, true), valueFromOptionalAnnotation,
            "Optional annotation should create value with optional=true");
    }

    @Test
    public void testStandardObjectMethods() {
        testToStringMethod();
        testHashCodeMethod();
        testEqualsMethod();
    }

    private void testToStringMethod() {
        assertEquals("JacksonInject.Value(id=null,useInput=null,optional=null)",
            EMPTY_VALUE.toString(),
            "Empty value toString should show all null properties");
    }

    private void testHashCodeMethod() {
        int emptyValueHashCode = EMPTY_VALUE.hashCode();
        assertNotEquals(0, emptyValueHashCode, 
            "Hash code should not be zero");

        JacksonInject.Value valueWithProperties = JacksonInject.Value.construct("test", true, false);
        int valueWithPropertiesHashCode = valueWithProperties.hashCode();
        assertNotEquals(0, valueWithPropertiesHashCode, 
            "Hash code for value with properties should not be zero");
    }

    @SuppressWarnings("unlikely-arg-type")
    private void testEqualsMethod() {
        // Test reflexivity
        assertEquals(EMPTY_VALUE, EMPTY_VALUE, "Value should equal itself");
        
        // Test null and different type handling
        assertNotEquals(EMPTY_VALUE, null, "Value should not equal null");
        assertNotEquals(EMPTY_VALUE, "string", "Value should not equal different type");

        // Test equality with same values
        JacksonInject.Value value1 = JacksonInject.Value.construct("test", true, true);
        JacksonInject.Value value2 = JacksonInject.Value.construct("test", true, true);
        assertEquals(value1, value2, "Values with same properties should be equal");

        // Test inequality with different properties
        testInequalityForDifferentProperties(value1);
    }

    private void testInequalityForDifferentProperties(JacksonInject.Value baseValue) {
        JacksonInject.Value valueWithDifferentId = JacksonInject.Value.construct("different", true, true);
        JacksonInject.Value valueWithDifferentUseInput = JacksonInject.Value.construct("test", false, true);
        JacksonInject.Value valueWithDifferentOptional = JacksonInject.Value.construct("test", true, false);
        JacksonInject.Value valueWithNullId = JacksonInject.Value.construct(null, true, true);
        JacksonInject.Value valueWithNullUseInput = JacksonInject.Value.construct("test", null, true);
        JacksonInject.Value valueWithNullOptional = JacksonInject.Value.construct("test", true, null);

        assertNotEquals(baseValue, valueWithDifferentId, "Values with different IDs should not be equal");
        assertNotEquals(baseValue, valueWithDifferentUseInput, "Values with different useInput should not be equal");
        assertNotEquals(baseValue, valueWithDifferentOptional, "Values with different optional should not be equal");
        assertNotEquals(baseValue, valueWithNullId, "Values with different ID nullability should not be equal");
        assertNotEquals(baseValue, valueWithNullUseInput, "Values with different useInput nullability should not be equal");
        assertNotEquals(baseValue, valueWithNullOptional, "Values with different optional nullability should not be equal");
    }

    @Test
    public void testFactoryAndMutatorMethods() {
        testWithIdMethod();
        testWithUseInputMethod();
        testWithOptionalMethod();
    }

    private void testWithIdMethod() {
        JacksonInject.Value valueWithId = EMPTY_VALUE.withId("testId");
        
        assertNotSame(EMPTY_VALUE, valueWithId, "withId should create new instance");
        assertEquals("testId", valueWithId.getId(), "New value should have correct ID");
        
        // Test that setting same ID returns same instance
        assertSame(valueWithId, valueWithId.withId("testId"), 
            "Setting same ID should return same instance");
    }

    private void testWithUseInputMethod() {
        JacksonInject.Value baseValue = EMPTY_VALUE.withId("test");
        JacksonInject.Value valueWithUseInput = baseValue.withUseInput(Boolean.TRUE);
        
        assertNotSame(baseValue, valueWithUseInput, "withUseInput should create new instance");
        assertNotEquals(baseValue, valueWithUseInput, "Values should not be equal after useInput change");
        assertNotEquals(valueWithUseInput, baseValue, "Equality should be symmetric");
        
        // Test that setting same useInput returns same instance
        assertSame(valueWithUseInput, valueWithUseInput.withUseInput(Boolean.TRUE),
            "Setting same useInput should return same instance");
    }

    private void testWithOptionalMethod() {
        JacksonInject.Value baseValue = EMPTY_VALUE.withId("test");
        JacksonInject.Value valueWithOptional = baseValue.withOptional(Boolean.TRUE);
        
        assertNotSame(baseValue, valueWithOptional, "withOptional should create new instance");
        assertNotEquals(baseValue, valueWithOptional, "Values should not be equal after optional change");
        assertNotEquals(valueWithOptional, baseValue, "Equality should be symmetric");
        assertTrue(valueWithOptional.getOptional(), "Optional property should be set correctly");
        
        // Test that setting same optional returns same instance
        assertSame(valueWithOptional, valueWithOptional.withOptional(Boolean.TRUE),
            "Setting same optional should return same instance");
    }

    /**
     * Helper method to get JacksonInject annotation from a field in the test class.
     */
    private JacksonInject getAnnotationFromField(String fieldName) throws NoSuchFieldException {
        return TestClassWithInjectAnnotations.class
            .getField(fieldName)
            .getAnnotation(JacksonInject.class);
    }
}
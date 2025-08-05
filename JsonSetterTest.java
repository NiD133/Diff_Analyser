package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for JsonSetter.Value class functionality.
 * Tests the configuration value object that encapsulates JsonSetter annotation settings
 * for null handling during JSON deserialization.
 */
public class JsonSetterTest {
    
    /**
     * Test helper class with JsonSetter annotation configured for:
     * - nulls=FAIL: Fail when encountering null values
     * - contentNulls=SKIP: Skip null values in collections/arrays
     */
    private final static class TestClassWithJsonSetterAnnotation {
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
        public int annotatedField;
    }

    private final JsonSetter.Value emptyValue = JsonSetter.Value.empty();

    @Test
    public void testEmptyValueHasDefaultSettings() {
        // Empty value should have default null handling for both value and content nulls
        assertEquals(Nulls.DEFAULT, emptyValue.getValueNulls());
        assertEquals(Nulls.DEFAULT, emptyValue.getContentNulls());

        // Should be associated with JsonSetter annotation
        assertEquals(JsonSetter.class, emptyValue.valueFor());

        // Non-default accessors should return null when values are default
        assertNull(emptyValue.nonDefaultValueNulls());
        assertNull(emptyValue.nonDefaultContentNulls());
    }

    @Test
    public void testStandardObjectMethods() {
        // Test toString() produces expected format
        assertEquals("JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)",
                emptyValue.toString());
        
        // Test hashCode() returns non-zero value
        int hashCode = emptyValue.hashCode();
        if (hashCode == 0) {
            fail("HashCode should not be zero");
        }
        
        // Test equals() method behavior
        assertEquals(emptyValue, emptyValue); // reflexive
        assertFalse(emptyValue.equals(null)); // null comparison
        assertFalse(emptyValue.equals("xyz")); // different type comparison
    }

    @Test
    public void testCreatingValueFromAnnotation() throws Exception {
        // Creating from null annotation should return empty value
        assertSame(emptyValue, JsonSetter.Value.from(null));

        // Extract annotation from test class and create Value from it
        JsonSetter annotation = TestClassWithJsonSetterAnnotation.class
                .getField("annotatedField")
                .getAnnotation(JsonSetter.class);
        
        JsonSetter.Value valueFromAnnotation = JsonSetter.Value.from(annotation);
        
        // Should match the annotation's configured values
        assertEquals(Nulls.FAIL, valueFromAnnotation.getValueNulls());
        assertEquals(Nulls.SKIP, valueFromAnnotation.getContentNulls());
    }

    @Test
    public void testConstructorWithNullParameters() throws Exception {
        // Constructing with null parameters should return empty value
        JsonSetter.Value constructedValue = JsonSetter.Value.construct(null, null);
        assertSame(emptyValue, constructedValue);
    }

    @Test
    public void testFactoryMethodsForSpecificNullHandling() throws Exception {
        // Test factory method for content nulls only
        JsonSetter.Value contentNullsValue = JsonSetter.Value.forContentNulls(Nulls.SET);
        assertEquals(Nulls.DEFAULT, contentNullsValue.getValueNulls());
        assertEquals(Nulls.SET, contentNullsValue.getContentNulls());
        assertEquals(Nulls.SET, contentNullsValue.nonDefaultContentNulls());

        // Test factory method for value nulls only
        JsonSetter.Value valueNullsValue = JsonSetter.Value.forValueNulls(Nulls.SKIP);
        assertEquals(Nulls.SKIP, valueNullsValue.getValueNulls());
        assertEquals(Nulls.DEFAULT, valueNullsValue.getContentNulls());
        assertEquals(Nulls.SKIP, valueNullsValue.nonDefaultValueNulls());
    }

    @Test
    public void testBuildingValueWithChainedMethods() {
        // Start with empty value and chain modifications
        JsonSetter.Value modifiedValue = emptyValue.withContentNulls(Nulls.SKIP);
        assertEquals(Nulls.SKIP, modifiedValue.getContentNulls());
        
        // Chain another modification
        modifiedValue = modifiedValue.withValueNulls(Nulls.FAIL);
        assertEquals(Nulls.FAIL, modifiedValue.getValueNulls());
    }

    @Test
    public void testWithMethodsBehaviorAndImmutability() {
        // Setting null should return same instance (no change)
        JsonSetter.Value unchangedValue = emptyValue.withContentNulls(null);
        assertSame(emptyValue, unchangedValue);
        
        // Setting actual value should create new instance
        JsonSetter.Value modifiedValue = unchangedValue.withContentNulls(Nulls.FAIL);
        assertEquals(Nulls.FAIL, modifiedValue.getContentNulls());
        
        // Setting same value again should return same instance (optimization)
        assertSame(modifiedValue, modifiedValue.withContentNulls(Nulls.FAIL));

        // Test value nulls modification
        JsonSetter.Value valueNullsModified = modifiedValue.withValueNulls(Nulls.SKIP);
        assertEquals(Nulls.SKIP, valueNullsModified.getValueNulls());
        
        // Original and modified should not be equal
        assertFalse(modifiedValue.equals(valueNullsModified));
        assertFalse(valueNullsModified.equals(modifiedValue));

        // Test resetting both values to default with null parameters
        JsonSetter.Value resetToDefault = valueNullsModified.withValueNulls(null, null);
        assertEquals(Nulls.DEFAULT, resetToDefault.getContentNulls());
        assertEquals(Nulls.DEFAULT, resetToDefault.getValueNulls());
        
        // Setting same values again should return same instance
        assertSame(resetToDefault, resetToDefault.withValueNulls(null, null));

        // Test merging with overrides
        JsonSetter.Value mergedValue = resetToDefault.withOverrides(valueNullsModified);
        assertNotSame(valueNullsModified, mergedValue);
        assertEquals(mergedValue, valueNullsModified);
        assertEquals(valueNullsModified, mergedValue);
    }
}
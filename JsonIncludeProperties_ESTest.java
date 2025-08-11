package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import java.util.LinkedHashSet;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for JsonIncludeProperties.Value class functionality.
 * Tests cover creation, equality, overrides, and basic operations.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class JsonIncludePropertiesValueTest extends JsonIncludeProperties_ESTest_scaffolding {

    // Constants for better readability
    private static final String SAMPLE_PROPERTY_1 = "LI }f.Ax<09fr";
    private static final String SAMPLE_PROPERTY_2 = "K|D{$!:2zda";
    private static final int TEST_TIMEOUT = 4000;

    // ========== Basic Property Access Tests ==========
    
    @Test(timeout = TEST_TIMEOUT)
    public void testGetIncluded_AllValue_ReturnsNull() throws Throwable {
        // Given: The predefined ALL value
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        
        // When: Getting included properties
        Set<String> includedProperties = allValue.getIncluded();
        
        // Then: Should return null (meaning all properties are included)
        assertNull("ALL value should return null for included properties", includedProperties);
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testValueFor_ReturnsCorrectAnnotationClass() throws Throwable {
        // Given: Any JsonIncludeProperties.Value instance
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.ALL;
        
        // When: Getting the annotation class
        Class<JsonIncludeProperties> annotationClass = value.valueFor();
        
        // Then: Should return JsonIncludeProperties class
        assertEquals("Should return JsonIncludeProperties interface", 
                    "interface com.fasterxml.jackson.annotation.JsonIncludeProperties", 
                    annotationClass.toString());
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testToString_AllValue_ShowsNullIncluded() throws Throwable {
        // Given: The ALL value
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        
        // When: Converting to string
        String stringRepresentation = allValue.toString();
        
        // Then: Should show null included properties
        assertEquals("JsonIncludeProperties.Value(included=null)", stringRepresentation);
    }

    // ========== Factory Method Tests ==========

    @Test(timeout = TEST_TIMEOUT)
    public void testFrom_WithStringArray_CreatesValueSuccessfully() throws Throwable {
        // Given: A mock annotation with string array
        String[] properties = new String[5];
        JsonIncludeProperties mockAnnotation = createMockAnnotationWithProperties(properties);
        
        // When: Creating Value from annotation
        JsonIncludeProperties.Value result = JsonIncludeProperties.Value.from(mockAnnotation);
        
        // Then: Should create a valid Value instance
        assertNotNull("Should create a non-null Value instance", result);
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testFrom_WithNullAnnotation_ReturnsValidValue() throws Throwable {
        // When: Creating Value from null annotation
        JsonIncludeProperties.Value result = JsonIncludeProperties.Value.from(null);
        
        // Then: Should return a valid Value instance
        assertNotNull("Should handle null annotation gracefully", result);
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testFrom_WithEmptyStringArray_CreatesValueSuccessfully() throws Throwable {
        // Given: A mock annotation with empty string array
        String[] emptyProperties = new String[0];
        JsonIncludeProperties mockAnnotation = createMockAnnotationWithProperties(emptyProperties);
        
        // When: Creating Value from annotation
        JsonIncludeProperties.Value result = JsonIncludeProperties.Value.from(mockAnnotation);
        
        // Then: Should create a valid Value instance
        assertNotNull("Should create Value from empty array", result);
    }

    // ========== Equality Tests ==========

    @Test(timeout = TEST_TIMEOUT)
    public void testEquals_SameInstance_ReturnsTrue() throws Throwable {
        // Given: A Value instance
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.all();
        
        // When & Then: Comparing with itself should return true
        assertTrue("Same instance should be equal to itself", value.equals(value));
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testEquals_WithNull_ReturnsFalse() throws Throwable {
        // Given: A Value instance
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.all();
        
        // When & Then: Comparing with null should return false
        assertFalse("Value should not equal null", value.equals(null));
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testEquals_WithDifferentType_ReturnsFalse() throws Throwable {
        // Given: A Value instance and a different type object
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.all();
        Object differentTypeObject = new Object();
        
        // When & Then: Comparing with different type should return false
        assertFalse("Value should not equal different type object", 
                   value.equals(differentTypeObject));
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testEquals_AllValueWithNullSetValue_ReturnsTrue() throws Throwable {
        // Given: ALL value and a Value with null set
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        JsonIncludeProperties.Value nullSetValue = new JsonIncludeProperties.Value(null);
        
        // When & Then: They should be equal (both represent "all properties")
        assertTrue("ALL value should equal Value with null set", 
                  nullSetValue.equals(allValue));
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testEquals_AllValueWithAnnotationFromNull_ReturnsFalse() throws Throwable {
        // Given: ALL value and a Value created from null annotation
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        JsonIncludeProperties mockAnnotation = createMockAnnotationWithProperties(null);
        JsonIncludeProperties.Value fromNullAnnotation = JsonIncludeProperties.Value.from(mockAnnotation);
        
        // When: Comparing them
        boolean areEqual = allValue.equals(fromNullAnnotation);
        
        // Then: They should not be equal
        assertFalse("ALL value should not equal value from null annotation", areEqual);
        assertFalse("Equality should be symmetric", 
                   fromNullAnnotation.equals(allValue));
    }

    // ========== Override Tests ==========

    @Test(timeout = TEST_TIMEOUT)
    public void testWithOverrides_WithNull_ReturnsSameInstance() throws Throwable {
        // Given: A Value instance
        JsonIncludeProperties.Value originalValue = JsonIncludeProperties.Value.all();
        
        // When: Overriding with null
        JsonIncludeProperties.Value result = originalValue.withOverrides(null);
        
        // Then: Should return the same instance
        assertSame("Overriding with null should return same instance", result, originalValue);
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testWithOverrides_SameValueWithProperties_CreatesNewEqualInstance() throws Throwable {
        // Given: A Value with specific properties
        Set<String> properties = createSetWithProperty(SAMPLE_PROPERTY_1);
        JsonIncludeProperties.Value originalValue = new JsonIncludeProperties.Value(properties);
        
        // When: Overriding with itself
        JsonIncludeProperties.Value result = originalValue.withOverrides(originalValue);
        
        // Then: Should create new instance that is equal but not same
        assertTrue("Result should be equal to original", result.equals(originalValue));
        assertNotSame("Should create new instance", result, originalValue);
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testWithOverrides_EmptySetWithNonEmptySet_ReturnsOriginal() throws Throwable {
        // Given: Empty set Value and non-empty set Value
        JsonIncludeProperties.Value emptySetValue = createValueFromEmptyArray();
        Set<String> nonEmptySet = createSetWithProperty(SAMPLE_PROPERTY_2);
        JsonIncludeProperties.Value nonEmptyValue = new JsonIncludeProperties.Value(nonEmptySet);
        
        // When: Overriding empty with non-empty
        JsonIncludeProperties.Value result = emptySetValue.withOverrides(nonEmptyValue);
        
        // Then: Should return equivalent to original (empty wins in intersection)
        assertNotSame("Should create new instance", result, emptySetValue);
        assertTrue("Result should be equal to original empty set", 
                  result.equals(emptySetValue));
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testWithOverrides_AllValueWithSpecificValue_ReturnsSpecificValue() throws Throwable {
        // Given: ALL value and a specific Value from null annotation
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        JsonIncludeProperties mockAnnotation = createMockAnnotationWithProperties(null);
        JsonIncludeProperties.Value specificValue = JsonIncludeProperties.Value.from(mockAnnotation);
        
        // When: Overriding ALL with specific
        JsonIncludeProperties.Value result = allValue.withOverrides(specificValue);
        
        // Then: Should return the specific value
        assertSame("Should return the override value", result, specificValue);
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testWithOverrides_SpecificValueWithAllValue_ReturnsSpecificValue() throws Throwable {
        // Given: ALL value and a specific Value from null annotation  
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        JsonIncludeProperties mockAnnotation = createMockAnnotationWithProperties(null);
        JsonIncludeProperties.Value specificValue = JsonIncludeProperties.Value.from(mockAnnotation);
        
        // When: Overriding specific with ALL
        JsonIncludeProperties.Value result = specificValue.withOverrides(allValue);
        
        // Then: Should return the specific value (it doesn't change)
        assertSame("Should return the original specific value", result, specificValue);
        assertFalse("Result should not equal ALL value", result.equals(allValue));
    }

    // ========== Collection Interaction Tests ==========

    @Test(timeout = TEST_TIMEOUT)
    public void testLinkedHashSetRemove_WithAllValue_ReturnsFalse() throws Throwable {
        // Given: A LinkedHashSet and ALL value
        LinkedHashSet<String> set = new LinkedHashSet<>();
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        
        // When: Trying to remove ALL value from set
        boolean wasRemoved = set.remove(allValue);
        
        // Then: Should return false (value not in set)
        assertFalse("Should not remove non-existent value", wasRemoved);
    }

    @Test(timeout = TEST_TIMEOUT)
    public void testLinkedHashSetRemove_WithEmptyArrayValue_ReturnsFalse() throws Throwable {
        // Given: A LinkedHashSet and Value from empty array
        LinkedHashSet<String> set = new LinkedHashSet<>();
        JsonIncludeProperties.Value emptyValue = createValueFromEmptyArray();
        
        // When: Trying to remove empty value from set
        boolean wasRemoved = set.remove(emptyValue);
        
        // Then: Should return false (value not in set)
        assertFalse("Should not remove non-existent value", wasRemoved);
    }

    // ========== Helper Methods ==========

    private JsonIncludeProperties createMockAnnotationWithProperties(String[] properties) {
        JsonIncludeProperties mockAnnotation = mock(JsonIncludeProperties.class, CALLS_REAL_METHODS);
        doReturn(properties).when(mockAnnotation).value();
        return mockAnnotation;
    }

    private Set<String> createSetWithProperty(String property) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add(property);
        return set;
    }

    private JsonIncludeProperties.Value createValueFromEmptyArray() {
        String[] emptyArray = new String[0];
        JsonIncludeProperties mockAnnotation = createMockAnnotationWithProperties(emptyArray);
        return JsonIncludeProperties.Value.from(mockAnnotation);
    }
}
package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link Separators}.
 * 
 * Tests the immutable behavior of Separators class, ensuring that:
 * - Setting the same separator value returns the same instance (optimization)
 * - Setting a different separator value creates a new instance
 * - All separator values are correctly maintained
 */
class SeparatorsTest {

    // Test constants for better readability
    private static final char INITIAL_SEPARATOR = '5';
    private static final char SAME_SEPARATOR = '5';
    private static final char DIFFERENT_SEPARATOR = '6';
    private static final char EXCLAMATION_SEPARATOR = '!';

    @Test
    void withArrayValueSeparator_ShouldReturnSameInstanceWhenValueUnchanged() {
        // Given: A Separators instance with all separators set to '5'
        Separators originalSeparators = createSeparatorsWithAllSameValues(INITIAL_SEPARATOR);
        
        // When: Setting array value separator to the same value
        Separators resultWithSameValue = originalSeparators.withArrayValueSeparator(SAME_SEPARATOR);
        
        // Then: Should return the same instance (optimization for unchanged values)
        assertSame(originalSeparators, resultWithSameValue, 
            "Should return same instance when array value separator is unchanged");
        assertAllSeparatorsEqual(resultWithSameValue, INITIAL_SEPARATOR, INITIAL_SEPARATOR, INITIAL_SEPARATOR);
    }

    @Test
    void withArrayValueSeparator_ShouldReturnNewInstanceWhenValueChanged() {
        // Given: A Separators instance with all separators set to '5'
        Separators originalSeparators = createSeparatorsWithAllSameValues(INITIAL_SEPARATOR);
        
        // When: Setting array value separator to a different value
        Separators resultWithDifferentValue = originalSeparators.withArrayValueSeparator(DIFFERENT_SEPARATOR);
        
        // Then: Should return a new instance with only array separator changed
        assertNotSame(originalSeparators, resultWithDifferentValue, 
            "Should return new instance when array value separator is changed");
        assertAllSeparatorsEqual(resultWithDifferentValue, INITIAL_SEPARATOR, INITIAL_SEPARATOR, DIFFERENT_SEPARATOR);
    }

    @Test
    void withObjectEntrySeparator_ShouldMaintainImmutableBehavior() {
        // Given: A Separators instance with all separators set to '5'
        Separators originalSeparators = createSeparatorsWithAllSameValues(INITIAL_SEPARATOR);
        
        // When: Changing object entry separator to '!'
        Separators firstChange = originalSeparators.withObjectEntrySeparator(EXCLAMATION_SEPARATOR);
        
        // Then: Should create new instance with correct values
        assertAllSeparatorsEqual(firstChange, EXCLAMATION_SEPARATOR, INITIAL_SEPARATOR, INITIAL_SEPARATOR);
        
        // When: Setting object entry separator to the same value again
        Separators secondChange = firstChange.withObjectEntrySeparator(EXCLAMATION_SEPARATOR);
        
        // Then: Should return the same instance (optimization)
        assertSame(firstChange, secondChange, 
            "Should return same instance when object entry separator is unchanged");
        assertAllSeparatorsEqual(secondChange, EXCLAMATION_SEPARATOR, INITIAL_SEPARATOR, INITIAL_SEPARATOR);
        
        // And: Original instance should remain unchanged
        assertAllSeparatorsEqual(originalSeparators, INITIAL_SEPARATOR, INITIAL_SEPARATOR, INITIAL_SEPARATOR);
    }

    @Test
    void withObjectFieldValueSeparator_ShouldReturnSameInstanceWhenValueUnchanged() {
        // Given: A Separators instance with all separators set to '5'
        Separators originalSeparators = createSeparatorsWithAllSameValues(INITIAL_SEPARATOR);
        
        // When: Setting object field value separator to the same value
        Separators resultWithSameValue = originalSeparators.withObjectFieldValueSeparator(SAME_SEPARATOR);
        
        // Then: Should return the same instance (optimization)
        assertSame(originalSeparators, resultWithSameValue, 
            "Should return same instance when object field value separator is unchanged");
        assertAllSeparatorsEqual(resultWithSameValue, INITIAL_SEPARATOR, INITIAL_SEPARATOR, INITIAL_SEPARATOR);
    }

    @Test
    void withObjectFieldValueSeparator_ShouldReturnNewInstanceWhenValueChanged() {
        // Given: A Separators instance with all separators set to '5'
        Separators originalSeparators = createSeparatorsWithAllSameValues(INITIAL_SEPARATOR);
        
        // When: Setting object field value separator to a different value
        Separators resultWithDifferentValue = originalSeparators.withObjectFieldValueSeparator(DIFFERENT_SEPARATOR);
        
        // Then: Should return a new instance with only object field value separator changed
        assertNotSame(originalSeparators, resultWithDifferentValue, 
            "Should return new instance when object field value separator is changed");
        assertAllSeparatorsEqual(resultWithDifferentValue, INITIAL_SEPARATOR, DIFFERENT_SEPARATOR, INITIAL_SEPARATOR);
    }

    // Helper methods for better test readability

    /**
     * Creates a Separators instance with all separator values set to the same character.
     */
    private Separators createSeparatorsWithAllSameValues(char separatorValue) {
        return new Separators(separatorValue, separatorValue, separatorValue);
    }

    /**
     * Asserts that all separator values match the expected values.
     * 
     * @param separators The Separators instance to check
     * @param expectedObjectEntry Expected object entry separator
     * @param expectedObjectFieldValue Expected object field value separator  
     * @param expectedArrayValue Expected array value separator
     */
    private void assertAllSeparatorsEqual(Separators separators, 
                                        char expectedObjectEntry, 
                                        char expectedObjectFieldValue, 
                                        char expectedArrayValue) {
        assertEquals(expectedObjectEntry, separators.getObjectEntrySeparator(), 
            "Object entry separator should match expected value");
        assertEquals(expectedObjectFieldValue, separators.getObjectFieldValueSeparator(), 
            "Object field value separator should match expected value");
        assertEquals(expectedArrayValue, separators.getArrayValueSeparator(), 
            "Array value separator should match expected value");
    }
}
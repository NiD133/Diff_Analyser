package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeparatorsTest {

    // Tests for withArrayValueSeparator()
    
    @Test
    void withArrayValueSeparator_whenSameValue_returnsSameInstance() {
        Separators original = new Separators('5', '5', '5');
        Separators updated = original.withArrayValueSeparator('5');

        assertSame(original, updated, "Should return same instance when value unchanged");
    }

    @Test
    void withArrayValueSeparator_whenNewValue_returnsNewInstanceWithUpdatedSeparator() {
        Separators original = new Separators('5', '5', '5');
        Separators updated = original.withArrayValueSeparator('6');

        assertNotSame(original, updated, "Should return new instance when value changes");
        assertEquals('6', updated.getArrayValueSeparator(), "Array separator should be updated");
        assertEquals('5', updated.getObjectEntrySeparator(), "Object entry separator should remain unchanged");
        assertEquals('5', updated.getObjectFieldValueSeparator(), "Object field separator should remain unchanged");
    }

    // Tests for withObjectEntrySeparator()
    
    @Test
    void withObjectEntrySeparator_whenSameValue_returnsSameInstance() {
        Separators original = new Separators('5', '5', '5');
        Separators updated = original.withObjectEntrySeparator('5');
        
        assertSame(original, updated, "Should return same instance when value unchanged");
    }

    @Test
    void withObjectEntrySeparator_whenNewValue_returnsNewInstanceWithUpdatedSeparator() {
        Separators original = new Separators('5', '5', '5');
        Separators updated = original.withObjectEntrySeparator('!');

        assertNotSame(original, updated, "Should return new instance when value changes");
        assertEquals('!', updated.getObjectEntrySeparator(), "Object entry separator should be updated");
        assertEquals('5', updated.getObjectFieldValueSeparator(), "Object field separator should remain unchanged");
        assertEquals('5', updated.getArrayValueSeparator(), "Array separator should remain unchanged");
    }

    // Tests for withObjectFieldValueSeparator()
    
    @Test
    void withObjectFieldValueSeparator_whenSameValue_returnsSameInstance() {
        Separators original = new Separators('5', '5', '5');
        Separators updated = original.withObjectFieldValueSeparator('5');

        assertSame(original, updated, "Should return same instance when value unchanged");
    }

    @Test
    void withObjectFieldValueSeparator_whenNewValue_returnsNewInstanceWithUpdatedSeparator() {
        Separators original = new Separators('5', '5', '5');
        Separators updated = original.withObjectFieldValueSeparator('6');

        assertNotSame(original, updated, "Should return new instance when value changes");
        assertEquals('6', updated.getObjectFieldValueSeparator(), "Object field separator should be updated");
        assertEquals('5', updated.getObjectEntrySeparator(), "Object entry separator should remain unchanged");
        assertEquals('5', updated.getArrayValueSeparator(), "Array separator should remain unchanged");
    }
}
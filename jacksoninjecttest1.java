package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link JacksonInject.Value} class, focusing on the behavior
 * of its "empty" instance.
 */
@DisplayName("JacksonInject.Value")
class JacksonInjectValueTest {

    private final JacksonInject.Value EMPTY = JacksonInject.Value.empty();

    @Test
    @DisplayName("The EMPTY singleton should have null properties")
    void emptyValueShouldHaveNullProperties() {
        // The EMPTY instance represents a default, non-configured state.
        // Therefore, its specific properties should be null.
        assertNull(EMPTY.getId(), "ID should be null for the empty value.");
        assertNull(EMPTY.getUseInput(), "'useInput' should be null for the empty value.");
        assertNull(EMPTY.getOptional(), "'optional' should be null for the empty value.");
    }

    @Test
    @DisplayName("The EMPTY singleton's willUseInput() should reflect the provided default")
    void emptyValueShouldUseProvidedDefaultForWillUseInput() {
        // When no 'useInput' value is configured (as in the EMPTY instance),
        // the willUseInput() method should fall back to the provided default setting.
        assertTrue(EMPTY.willUseInput(true), "Should return true when the default is true.");
        assertFalse(EMPTY.willUseInput(false), "Should return false when the default is false.");
    }

    @Test
    @DisplayName("The construct() factory method should return the EMPTY singleton for empty inputs")
    void constructShouldReturnEmptySingletonForEmptyInputs() {
        // The factory method should be optimized to return the singleton EMPTY
        // instance when constructed with values that signify "empty".

        // Case 1: All properties are null.
        assertSame(EMPTY, JacksonInject.Value.construct(null, null, null),
                "Constructing with all nulls should return the EMPTY singleton.");

        // Case 2: The ID is an empty string, which is treated as null.
        assertSame(EMPTY, JacksonInject.Value.construct("", null, null),
                "Constructing with an empty string ID should also return the EMPTY singleton.");
    }
}
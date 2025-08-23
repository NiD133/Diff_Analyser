package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for the factory method {@link CharSet#getInstance(String...)}.
 * This class improves upon the original by splitting a single, multi-assertion
 * test into several focused and descriptively named tests.
 */
class CharSetGetInstanceTest extends AbstractLangTest {

    @Test
    void getInstance_withNullArray_shouldReturnNull() {
        // The factory method contract specifies returning null for a null array input.
        assertNull(CharSet.getInstance((String[]) null),
            "A null array input should result in a null CharSet object.");
    }

    @Test
    void getInstance_withNoArguments_shouldReturnEmptySet() {
        // Calling with no arguments is equivalent to providing an empty array,
        // which should result in a new, empty CharSet instance.
        final CharSet emptySet = CharSet.getInstance();

        assertEquals("[]", emptySet.toString(),
            "An empty CharSet should have a '[]' string representation.");
    }

    @Test
    void getInstance_withSingleNullString_shouldReturnEmptyConstant() {
        // The factory method is optimized to return the CharSet.EMPTY singleton
        // when called with a single null argument. Asserting for identity is a
        // stronger and more accurate check than just verifying the string representation.
        final CharSet fromNull = CharSet.getInstance((String) null);

        assertSame(CharSet.EMPTY, fromNull,
            "A single null string argument should return the singleton CharSet.EMPTY.");
    }

    @Test
    void getInstance_withSimpleRange_shouldCreateCorrectSet() {
        // This tests the common case of creating a CharSet from a single definition string.
        final String rangeDefinition = "a-e";
        final CharSet charSet = CharSet.getInstance(rangeDefinition);
        final String expectedToString = "[a-e]";

        assertEquals(expectedToString, charSet.toString(),
            "The string representation should reflect the range used for creation.");
    }
}
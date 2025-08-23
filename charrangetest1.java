package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharRange}.
 */
// Renamed from CharRangeTestTest1 to follow standard naming conventions.
class CharRangeTest extends AbstractLangTest {

    /**
     * As of commons-lang 3.0, the {@link CharRange} class is intended for internal use.
     * This test verifies that its class modifiers reflect this design: it should not be
     * public and should be final to ensure its immutability.
     */
    @Test
    @DisplayName("CharRange class should be final and not public")
    void testClassModifiers() {
        final int classModifiers = CharRange.class.getModifiers();

        assertFalse(Modifier.isPublic(classModifiers),
            "CharRange is an internal helper class and should not be public.");
        assertTrue(Modifier.isFinal(classModifiers),
            "CharRange is immutable and should be declared final.");
    }
}
package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the basic class properties of {@link CharSet}.
 */
public class CharSetTestTest1 extends AbstractLangTest {

    /**
     * The CharSet class is part of the public API and is designed for extension.
     * This test verifies that its class modifiers reflect this design.
     */
    @Test
    @DisplayName("CharSet class should be public and non-final")
    void charSetClass_shouldBePublicAndNotFinal() {
        // Arrange: Get the class definition for CharSet
        final Class<CharSet> clazz = CharSet.class;

        // Act & Assert
        // It must be public to be part of the public API.
        assertTrue(Modifier.isPublic(clazz.getModifiers()), "CharSet should be a public class.");

        // It must not be final to allow for subclassing, as mentioned in the class Javadoc.
        assertFalse(Modifier.isFinal(clazz.getModifiers()), "CharSet should not be final to allow extension.");
    }
}
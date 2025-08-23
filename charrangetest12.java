package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CharRange}.
 *
 * Note: The original class name "CharRangeTestTest12" was redundant and uninformative.
 * Renaming it to "CharRangeTest" follows standard Java testing conventions.
 */
public class CharRangeTest extends AbstractLangTest {

    /**
     * Tests that contains(CharRange) throws a NullPointerException when passed a null argument,
     * as specified by the method's contract documented in the SUT.
     */
    @Test
    void testContains_withNullRange_throwsNullPointerException() {
        // Arrange: Create an arbitrary CharRange instance to test the method.
        final CharRange range = CharRange.is('a');

        // Act & Assert: Verify that calling contains() with a null argument
        // throws a NullPointerException.
        final NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> range.contains(null)
        );

        // Assert that the exception message is as expected. This provides a more
        // specific check and can help diagnose failures. The expected message "range"
        // corresponds to the parameter name in the method signature.
        assertEquals("range", exception.getMessage());
    }
}
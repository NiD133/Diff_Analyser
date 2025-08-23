package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link CharSequenceUtils} class.
 * This class demonstrates improvements over an auto-generated test for clarity and maintainability.
 */
// The original test class name and inheritance are preserved to maintain structural consistency.
public class CharSequenceUtils_ESTestTest4 extends CharSequenceUtils_ESTest_scaffolding {

    /**
     * A constant representing the "not found" index value, which is a common return value
     * for search methods in Java.
     */
    private static final int NOT_FOUND = -1;

    /**
     * Tests that {@link CharSequenceUtils#lastIndexOf(CharSequence, int, int)}
     * correctly returns -1 when searching within an empty CharSequence.
     *
     * <p>This test verifies the fundamental behavior that a search in an empty sequence
     * should never find a match, even when using edge-case inputs like the maximum
     * possible Unicode code point and a large start index.</p>
     */
    @Test
    public void lastIndexOf_onEmptyCharSequence_shouldReturnNotFound() {
        // Arrange: Define the inputs for the test case.
        // An empty CharSequence is the primary subject of this test.
        final CharSequence emptySequence = new StringBuilder();

        // Use the maximum Unicode code point to test an edge case for the search character.
        // This is more descriptive than the magic number 1114111.
        final int searchChar = Character.MAX_CODE_POINT;
        final int startIndex = Character.MAX_CODE_POINT;

        // Act: Execute the method being tested.
        final int actualIndex = CharSequenceUtils.lastIndexOf(emptySequence, searchChar, startIndex);

        // Assert: Verify that the result is NOT_FOUND.
        assertEquals("lastIndexOf on an empty sequence should always return NOT_FOUND (-1)",
                NOT_FOUND, actualIndex);
    }
}
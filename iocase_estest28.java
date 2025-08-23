package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class verifies the behavior of the {@link IOCase} enum.
 * It focuses on providing clear, self-documenting tests for its methods.
 */
public class IOCaseTest {

    /**
     * Tests that {@link IOCase#checkIndexOf(String, int, String)} returns -1
     * when the string to search for is null.
     * <p>
     * The documentation for {@code checkIndexOf} specifies that it should return -1
     * for a null search string input. This test verifies that contract.
     * The behavior is independent of the case-sensitivity rule.
     * </p>
     */
    @Test
    public void checkIndexOfShouldReturnNegativeOneForNullSearchString() {
        // Arrange
        // The case-sensitivity rule (SENSITIVE, INSENSITIVE, or SYSTEM) does not
        // affect the outcome when the search string is null. We use SENSITIVE as a representative case.
        final IOCase ioCase = IOCase.SENSITIVE;
        final String text = "Any non-null string will suffice here.";
        final int startIndex = 0; // The start index is also irrelevant for this specific test case.

        // Act
        final int result = ioCase.checkIndexOf(text, startIndex, null);

        // Assert
        assertEquals("checkIndexOf should return -1 for a null search string.", -1, result);
    }
}
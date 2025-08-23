package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that {@link IOCase#checkIndexOf(String, int, String)} treats a negative
     * start index as zero, which is consistent with the behavior of
     * {@link String#indexOf(String, int)}.
     */
    @Test
    public void checkIndexOfWithNegativeStartIndexShouldBeTreatedAsZero() {
        // Arrange
        final String text = "\"93F*1y";
        final String search = "\"93F*1y";
        final int negativeStartIndex = -794;
        final int expectedIndex = 0; // The search should start from index 0

        // Act
        final int actualIndex = IOCase.INSENSITIVE.checkIndexOf(text, negativeStartIndex, search);

        // Assert
        assertEquals("A negative start index should be treated as 0, finding the match at the beginning.",
                expectedIndex, actualIndex);
    }
}
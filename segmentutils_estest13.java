package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class provides an improved, more understandable test for the {@link SegmentUtils} class.
 * The original test was auto-generated and lacked clarity.
 */
public class SegmentUtilsTest {

    /**
     * Tests that countArgs correctly calculates the size of arguments for a descriptor
     * that contains a nested parenthesized group and non-standard characters.
     * This test also verifies the handling of a custom width for double/long types.
     */
    @Test
    public void countArgsShouldCorrectlyCalculateSizeForDescriptorWithNestedGroupAndCustomWidth() {
        // Arrange
        // The descriptor contains a nested group "(aDZ!yS3=O)". The countArgs method
        // is expected to parse the content of the outer parentheses.
        final String descriptorWithNesting = "_l((aDZ!yS3=O)=z/R2Z";
        final int widthOfLongsAndDoubles = 10;

        // The expected size is calculated from the characters inside the outer parentheses
        // until its matching closing parenthesis is found. The content to parse is "(aDZ!yS3=O)".
        //
        // Calculation breakdown:
        // '(' -> 1 (treated as a single-char argument)
        // 'a' -> 1 (non-standard, but counted as 1)
        // 'D' -> 10 (custom width for double)
        // 'Z' -> 1 (boolean)
        // '!' -> 1 (non-standard)
        // 'y' -> 1 (non-standard)
        // 'S' -> 1 (short)
        // '3' -> 1 (non-standard)
        // '=' -> 1 (non-standard)
        // 'O' -> 1 (non-standard)
        // ---
        // Total = 1 + 1 + 10 + 1 + 1 + 1 + 1 + 1 + 1 + 1 = 19
        final int expectedSize = 19;

        // Act
        final int actualSize = SegmentUtils.countArgs(descriptorWithNesting, widthOfLongsAndDoubles);

        // Assert
        assertEquals("The calculated argument size is incorrect.", expectedSize, actualSize);
    }
}
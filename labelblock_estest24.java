package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.awt.Color;
import java.awt.Font;

/**
 * A test suite for the equals() method in the {@link LabelBlock} class.
 */
public class LabelBlockEqualsTest {

    /**
     * Verifies that the equals() method returns false when a LabelBlock
     * is compared to an object of a different type.
     */
    @Test
    public void equals_whenComparedWithDifferentType_shouldReturnFalse() {
        // Arrange: Create a LabelBlock instance and an object of a different type.
        Font font = new Font("SansSerif", Font.PLAIN, 12);
        LabelBlock labelBlock = new LabelBlock("Test Label", font, Color.BLACK);
        Object objectOfDifferentType = Color.CYAN;

        // Act: Compare the LabelBlock with the other object.
        boolean isEqual = labelBlock.equals(objectOfDifferentType);

        // Assert: The result should be false, as the types are incompatible.
        assertFalse(isEqual);
    }
}
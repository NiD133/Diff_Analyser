package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that setContentAlignmentPoint() throws an IllegalArgumentException
     * when a null anchor is provided. The method contract requires a non-null argument.
     */
    @Test
    public void setContentAlignmentPoint_withNullAnchor_shouldThrowIllegalArgumentException() {
        // Arrange: Create a LabelBlock instance.
        LabelBlock labelBlock = new LabelBlock("Test Label");

        // Act & Assert: Call the method with a null argument and verify the exception.
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> labelBlock.setContentAlignmentPoint(null)
        );

        // Verify that the exception message is as expected.
        assertEquals("Null 'anchor' argument.", exception.getMessage());
    }
}
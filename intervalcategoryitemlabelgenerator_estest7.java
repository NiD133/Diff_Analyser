package org.jfree.chart.labels;

import org.junit.Test;
import java.text.DateFormat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the constructor of the {@link IntervalCategoryItemLabelGenerator} class.
 */
public class IntervalCategoryItemLabelGeneratorTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException
     * when initialized with a null DateFormat formatter. The constructor must
     * reject null arguments to ensure the generator is always in a valid state.
     */
    @Test
    public void constructor_WithNullDateFormat_ShouldThrowIllegalArgumentException() {
        // Arrange: Define a label format string. The content is not important for this test.
        String labelFormat = "{2}";
        
        // Act & Assert
        try {
            new IntervalCategoryItemLabelGenerator(labelFormat, (DateFormat) null);
            fail("Expected an IllegalArgumentException to be thrown for a null formatter.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the correct exception was thrown with the expected message.
            assertEquals("Null 'formatter' argument.", e.getMessage());
        }
    }
}
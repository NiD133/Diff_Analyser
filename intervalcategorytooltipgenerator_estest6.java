package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.DateFormat;

/**
 * Unit tests for the constructor of the {@link IntervalCategoryToolTipGenerator} class.
 */
public class IntervalCategoryToolTipGeneratorConstructorTest {

    /**
     * Verifies that the constructor correctly throws an IllegalArgumentException
     * when a null DateFormat is provided. A non-null formatter is a mandatory
     * dependency for the generator.
     */
    @Test
    public void constructor_WithNullDateFormat_ShouldThrowIllegalArgumentException() {
        // Arrange: Define the arguments for the constructor, with a null formatter.
        String labelFormat = "({0}, {1}) = {2}";
        DateFormat nullDateFormatter = null;
        String expectedExceptionMessage = "Null 'formatter' argument.";

        // Act & Assert: Attempt to create the object and verify the exception.
        try {
            new IntervalCategoryToolTipGenerator(labelFormat, nullDateFormatter);
            fail("Expected an IllegalArgumentException to be thrown for a null date formatter.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }
}
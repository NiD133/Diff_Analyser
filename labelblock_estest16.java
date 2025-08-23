package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Font;

/**
 * Unit tests for the constructor of the {@link LabelBlock} class.
 */
public class LabelBlockConstructorTest {

    /**
     * Verifies that the LabelBlock constructor throws an IllegalArgumentException
     * when the provided font is null. A null font is not a permitted argument.
     */
    @Test
    public void constructorShouldThrowIllegalArgumentExceptionForNullFont() {
        // Arrange: Define the expected exception message.
        final String expectedMessage = "Null 'font' argument.";

        // Act & Assert: Attempt to create a LabelBlock with a null font and
        // verify that the correct exception is thrown.
        try {
            new LabelBlock("Test Label", (Font) null);
            fail("Expected an IllegalArgumentException to be thrown for a null font, but none was.");
        } catch (IllegalArgumentException e) {
            // Verify that the thrown exception has the expected message.
            assertEquals("The exception message did not match the expected value.",
                         expectedMessage, e.getMessage());
        }
    }
}
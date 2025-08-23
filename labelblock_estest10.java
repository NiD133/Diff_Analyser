package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Font;

// Note: The original class name and inheritance are preserved to maintain
// the integrity of the existing test suite.
public class LabelBlock_ESTestTest10 extends LabelBlock_ESTest_scaffolding {

    /**
     * Verifies that the setFont() method throws an IllegalArgumentException
     * when a null font is provided. A label block must always have a
     * non-null font.
     */
    @Test
    public void setFontShouldThrowIllegalArgumentExceptionForNullFont() {
        // Arrange: Create a LabelBlock instance with a clear, readable label.
        LabelBlock labelBlock = new LabelBlock("Test Label");

        // Act & Assert: Attempt to set a null font and verify the resulting exception.
        try {
            labelBlock.setFont(null);
            fail("Expected an IllegalArgumentException to be thrown, but no exception occurred.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message correctly identifies the cause of the error.
            assertEquals("The exception message should indicate a null 'font' argument.",
                    "Null 'font' argument.", e.getMessage());
        }
    }
}
package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that the URL text set on a LabelBlock can be retrieved correctly.
     * This test covers the basic functionality of the setURLText() and getURLText() methods.
     */
    @Test
    public void testSetAndGetURLText() {
        // Arrange: Create a LabelBlock instance and define the expected URL.
        LabelBlock labelBlock = new LabelBlock("A sample label");
        String expectedUrl = "https://www.jfree.org/jfreechart/";

        // Act: Set the URL text and then retrieve it.
        labelBlock.setURLText(expectedUrl);
        String actualUrl = labelBlock.getURLText();

        // Assert: Verify that the retrieved URL matches the expected URL.
        assertEquals("The retrieved URL should match the one that was set.",
                expectedUrl, actualUrl);
    }
}
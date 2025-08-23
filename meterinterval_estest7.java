package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link MeterInterval} class, focusing on the getLabel() method.
 */
public class MeterInterval_ESTestTest7 {

    /**
     * Verifies that getLabel() returns the exact string that was provided
     * to the constructor, including an empty string.
     */
    @Test
    public void getLabelShouldReturnLabelSetInConstructor() {
        // Arrange: Create a MeterInterval with a specific label.
        // The range value is not relevant for this test, so a simple one is used.
        String expectedLabel = "";
        Range dummyRange = new Range(0.0, 100.0);
        MeterInterval interval = new MeterInterval(expectedLabel, dummyRange);

        // Act: Retrieve the label from the MeterInterval instance.
        String actualLabel = interval.getLabel();

        // Assert: Verify that the retrieved label matches the one set at construction.
        assertEquals(expectedLabel, actualLabel);
    }
}
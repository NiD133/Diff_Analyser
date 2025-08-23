package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Verifies that the getLabel() method correctly returns the label
     * that was provided in the constructor.
     */
    @Test
    public void getLabel_shouldReturnTheLabelProvidedInTheConstructor() {
        // Arrange: Create a MeterInterval with a specific label.
        // The other parameters (range, paints, stroke) are not relevant to this test,
        // so we can use simple or null values.
        String expectedLabel = "Normal";
        Range dummyRange = new Range(50.0, 75.0);
        MeterInterval interval = new MeterInterval(expectedLabel, dummyRange, null, null, null);

        // Act: Retrieve the label from the interval.
        String actualLabel = interval.getLabel();

        // Assert: Check if the retrieved label matches the expected one.
        assertEquals(expectedLabel, actualLabel);
    }
}
package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Verifies that the getRange() method correctly returns the Range object
     * that was provided to the constructor.
     */
    @Test
    public void getRange_shouldReturnTheRangeSetInConstructor() {
        // Arrange: Create a Range and a MeterInterval with that range.
        Range expectedRange = new Range(50.0, 75.0);
        MeterInterval meterInterval = new MeterInterval("Normal", expectedRange);

        // Act: Retrieve the range from the MeterInterval.
        Range actualRange = meterInterval.getRange();

        // Assert: The retrieved range should be the same instance as the one
        // used during construction, as MeterInterval is expected to store the reference.
        assertSame("The returned range should be the same object instance provided to the constructor.",
                expectedRange, actualRange);
    }
}
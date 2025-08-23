package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for the {@link MeterInterval} class.
 */
@DisplayName("MeterInterval")
class MeterIntervalTest {

    @Test
    @DisplayName("should not be cloneable because it is immutable")
    void meterIntervalShouldNotImplementCloneable() {
        // The MeterInterval class is designed to be immutable, so it should not
        // implement the Cloneable interface. This test verifies that design choice.
        MeterInterval interval = new MeterInterval("Test Interval", new Range(1.0, 2.0));

        assertFalse(interval instanceof Cloneable,
                "As an immutable class, MeterInterval should not implement Cloneable.");
    }
}
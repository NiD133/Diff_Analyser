package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Verifies that the equals() method returns false when a MeterInterval
     * is compared with an object of a different class, even if the object's
     * string representation might seem related.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparingWithDifferentObjectType() {
        // Arrange
        // Create a standard MeterInterval instance.
        Range range = new Range(0.0, 50.0);
        MeterInterval meterInterval = new MeterInterval("Normal", range);

        // Create an object of a different type (String) to compare against.
        Object objectOfDifferentType = "Normal";

        // Act
        // Compare the MeterInterval with the String object.
        boolean isEqual = meterInterval.equals(objectOfDifferentType);

        // Assert
        // The result must be false because the types are different.
        assertFalse("A MeterInterval instance should not be equal to a String instance.", isEqual);
    }
}
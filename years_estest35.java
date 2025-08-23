package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void minus_whenArgumentIsNull_shouldReturnSameInstance() {
        // The Javadoc for minus(Years) states that a null argument is treated as zero.
        // This test verifies that subtracting null does not change the value and,
        // as an optimization for immutable objects, returns the same instance.

        // Arrange
        Years twoYears = Years.TWO;

        // Act
        Years result = twoYears.minus((Years) null);

        // Assert
        assertSame("Subtracting a null Years object should return the original instance.", twoYears, result);
    }
}
package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that adding a null Years object is a no-op, as specified
     * by the Javadoc for the plus() method.
     */
    @Test
    public void plus_whenAddingNull_isANoOp() {
        // Arrange
        final Years initialYears = Years.ZERO;

        // Act
        final Years result = initialYears.plus((Years) null);

        // Assert
        // The result should be an object equal to the original.
        assertEquals(initialYears, result);
    }
}
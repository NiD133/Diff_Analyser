package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that the constructor correctly handles a negative threshold value by treating it as zero.
     */
    @Test
    public void testConstructorWithNegativeThresholdShouldSetThresholdToZero() {
        // Arrange
        final int negativeThreshold = -2596;

        // Act
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(negativeThreshold);

        // Assert
        // A negative threshold is documented to be treated as 0.
        assertEquals("The threshold should be corrected to 0 when a negative value is provided.", 0, stream.getThreshold());
        
        // The threshold should not be considered exceeded upon creation.
        assertFalse("The threshold should not be exceeded initially.", stream.isThresholdExceeded());
    }
}
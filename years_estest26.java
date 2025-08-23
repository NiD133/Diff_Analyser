package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class Years_ESTestTest26 extends Years_ESTest_scaffolding {

    /**
     * Tests that isLessThan() correctly handles a null comparison value.
     * The Javadoc states that a null 'other' period is treated as zero.
     * Therefore, MIN_VALUE should be less than null.
     */
    @Test
    public void isLessThan_shouldReturnTrue_whenComparingMinValueToNull() {
        // Arrange
        // According to the Javadoc, a null comparison value is treated as zero.
        Years minValueYears = Years.MIN_VALUE;

        // Act
        boolean isLessThan = minValueYears.isLessThan(null);

        // Assert
        assertTrue("Years.MIN_VALUE should be considered less than null (which is treated as zero)", isLessThan);
    }
}
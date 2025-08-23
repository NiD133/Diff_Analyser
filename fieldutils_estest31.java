package org.joda.time.field;

import org.junit.Test;
import java.math.RoundingMode;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeDivide() throws a NullPointerException when the provided RoundingMode is null.
     */
    @Test(expected = NullPointerException.class)
    public void safeDivide_whenRoundingModeIsNull_throwsNullPointerException() {
        // The safeDivide method with a RoundingMode parameter is expected to fail fast
        // if the rounding mode is null, as this is an invalid argument.
        
        // The specific dividend and divisor values are not relevant to triggering this exception.
        final long dividend = Long.MIN_VALUE;
        final long divisor = Long.MIN_VALUE;

        FieldUtils.safeDivide(dividend, divisor, (RoundingMode) null);
    }
}
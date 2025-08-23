package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.joda.time.ReadWritablePeriod;
import org.joda.time.MutablePeriod;

/**
 * This test class contains tests for the PeriodFormatter class.
 * This specific test case focuses on the behavior of the parseInto method.
 */
// The original test class name and hierarchy are preserved.
public class PeriodFormatter_ESTestTest29 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that parseInto() throws an IllegalArgumentException when the
     * provided ReadWritablePeriod object is null.
     */
    @Test
    public void parseInto_withNullPeriod_throwsIllegalArgumentException() {
        // Arrange: Create a basic formatter. The internal printer/parser logic is not
        // relevant for this test, as the null check on the period argument happens first.
        PeriodFormatter formatter = new PeriodFormatter(
            PeriodFormatterBuilder.Literal.EMPTY,
            PeriodFormatterBuilder.Literal.EMPTY
        );
        String anyText = "P1Y2M3D";
        int anyPosition = 0;

        // Act & Assert: Attempt to parse into a null period and verify the exception.
        try {
            formatter.parseInto((ReadWritablePeriod) null, anyText, anyPosition);
            fail("Expected an IllegalArgumentException to be thrown for a null period.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals("Period must not be null", e.getMessage());
        }
    }
}
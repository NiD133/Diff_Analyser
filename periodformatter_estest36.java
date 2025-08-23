package org.joda.time.format;

import org.joda.time.ReadablePeriod;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PeriodFormatter_ESTestTest36 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that calling printTo() with a null period throws an IllegalArgumentException,
     * as the period argument is non-nullable.
     */
    @Test(timeout = 4000)
    public void printTo_withNullPeriod_throwsIllegalArgumentException() {
        // Arrange: Create a basic formatter. The specific implementation of the
        // printer/parser doesn't matter for this test, as we are only verifying
        // the null check on the period argument.
        PeriodFormatterBuilder.Literal dummyComponent = new PeriodFormatterBuilder.Literal("");
        PeriodFormatter formatter = new PeriodFormatter(dummyComponent, dummyComponent);

        StringBuffer buffer = new StringBuffer();
        ReadablePeriod nullPeriod = null;

        // Act & Assert
        try {
            formatter.printTo(buffer, nullPeriod);
            fail("Expected an IllegalArgumentException to be thrown for a null period.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception and its message are correct.
            assertEquals("Period must not be null", e.getMessage());
        }
    }
}
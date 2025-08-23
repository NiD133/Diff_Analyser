package org.joda.time.format;

import org.joda.time.PeriodType;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * This test class contains tests for the PeriodFormatter.
 * This particular test was refactored for clarity from an auto-generated state.
 */
public class PeriodFormatter_ESTestTest12 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Verifies that getParseType() returns the exact PeriodType instance
     * that was provided to the PeriodFormatter's constructor.
     */
    @Test
    public void getParseType_shouldReturnTheTypeSetInTheConstructor() {
        // Arrange: Create a PeriodFormatter with a specific parse type.
        // A dummy printer/parser is required for the constructor but is not relevant to this test.
        PeriodFormatterBuilder.Literal dummyPrinterParser = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodType expectedParseType = PeriodType.years();

        PeriodFormatter formatter = new PeriodFormatter(
                dummyPrinterParser,
                dummyPrinterParser,
                null, // locale is not relevant for this test
                expectedParseType
        );

        // Act: Retrieve the parse type from the formatter.
        PeriodType actualParseType = formatter.getParseType();

        // Assert: The retrieved type should be the same instance as the one provided.
        assertSame("The getParseType() method should return the same instance provided to the constructor.",
                expectedParseType, actualParseType);
    }
}
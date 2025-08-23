package org.joda.time.format;

import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.junit.Test;

import java.util.Locale;

/**
 * Contains tests for the PeriodFormatter class, focusing on parsing behavior.
 */
public class PeriodFormatterTest {

    /**
     * Verifies that parseInto() throws a StringIndexOutOfBoundsException
     * when called with a negative position index, as this is an invalid argument.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void parseInto_shouldThrowException_whenPositionIsNegative() {
        // Arrange: Create a basic formatter. The specific configuration is not the focus
        // of this test; we only need a valid instance to call the method.
        PeriodParser parser = new PeriodFormatterBuilder()
                .appendYears()
                .toParser();
        PeriodFormatter formatter = new PeriodFormatter(null, parser, Locale.ENGLISH, PeriodType.standard());

        MutablePeriod period = new MutablePeriod();
        String anyText = "some text to parse";
        int invalidPosition = -1;

        // Act: Attempt to parse from an invalid negative position.
        // This action is expected to throw the exception.
        formatter.parseInto(period, anyText, invalidPosition);

        // Assert: The test will pass if the expected StringIndexOutOfBoundsException is thrown.
        // This is handled by the @Test(expected = ...) annotation.
    }
}
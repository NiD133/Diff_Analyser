package org.joda.time.format;

import static org.junit.Assert.assertEquals;

import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Test;

/**
 * Unit tests for {@link PeriodFormatter} focusing on parsing with a specific {@link PeriodType}.
 *
 * This test class verifies that when a PeriodFormatter is configured with a specific
 * parse type, it correctly parses strings that conform to that type and rejects
 * strings that contain fields outside of that type.
 */
public class PeriodFormatterParseTypeTest {

    private final PeriodFormatter formatter = ISOPeriodFormat.standard();

    @Test
    public void parsePeriod_withDayTimeType_parsesStringWithAllowedFields() {
        // Arrange
        // Create a parser that is restricted to the dayTime period type (no years, months, or weeks).
        PeriodFormatter dayTimeParser = formatter.withParseType(PeriodType.dayTime());
        String periodString = "P4DT5H6M7.008S";

        // The expected period should match the parsed fields: 4 days, 5 hours, 6 minutes, 7 seconds, 8 millis.
        // It should also have the restricted period type.
        Period expectedPeriod = new Period(0, 0, 0, 4, 5, 6, 7, 8, PeriodType.dayTime());

        // Act
        Period actualPeriod = dayTimeParser.parsePeriod(periodString);

        // Assert
        assertEquals(expectedPeriod, actualPeriod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsePeriod_withDayTimeType_throwsExceptionForDisallowedField() {
        // Arrange
        // Create a parser that is restricted to the dayTime period type.
        PeriodFormatter dayTimeParser = formatter.withParseType(PeriodType.dayTime());
        // This input string contains 'W' (weeks), which is not part of the dayTime type.
        String periodStringWithDisallowedField = "P3W4DT5H6M7.008S";

        // Act: This call is expected to throw an IllegalArgumentException.
        dayTimeParser.parsePeriod(periodStringWithDisallowedField);

        // Assert: The test passes if the expected exception is thrown.
    }
}
package org.joda.time.format;

import junit.framework.TestCase;
import org.joda.time.PeriodType;

/**
 * Tests the withParseType() and getParseType() methods of PeriodFormatter.
 *
 * <p>The original test class contained extensive setup for timezones, locales, and a fixed "now"
 * time. This was removed as it is not relevant to testing the withParseType() method, which
 * operates independently of that context. This refactoring makes the test more focused and easier
 * to understand.
 */
public class PeriodFormatterParseTypeTest extends TestCase {

    /**
     * Verifies that withParseType() returns a new formatter instance with the specified type.
     */
    public void testWithParseType_createsNewFormatterWithCorrectType() {
        // Arrange: Create a standard formatter, which has a null parse type by default.
        PeriodFormatter initialFormatter = ISOPeriodFormat.standard();
        assertNull("Initial formatter's parse type should be null", initialFormatter.getParseType());

        // Act: Create a new formatter with a specific parse type.
        PeriodType dayTimeType = PeriodType.dayTime();
        PeriodFormatter formatterWithDayTime = initialFormatter.withParseType(dayTimeType);

        // Assert: The new formatter has the correct type and is a new instance.
        assertEquals("Formatter should have the new parse type", dayTimeType, formatterWithDayTime.getParseType());
        assertNotSame("A new formatter instance should be returned when the type changes", initialFormatter, formatterWithDayTime);

        // Act: Create another formatter, setting the parse type back to null.
        PeriodFormatter formatterWithNull = formatterWithDayTime.withParseType(null);

        // Assert: The resulting formatter has a null parse type and is another new instance.
        assertNull("Formatter's parse type should now be null", formatterWithNull.getParseType());
        assertNotSame("A new formatter instance should be returned when changing to null", formatterWithDayTime, formatterWithNull);
    }

    /**
     * Verifies the immutability optimization: if the parse type is not changed,
     * the same formatter instance is returned.
     */
    public void testWithParseType_whenTypeIsUnchanged_returnsSameInstance() {
        // Arrange: Create a formatter with a specific, non-null parse type.
        PeriodType dayTimeType = PeriodType.dayTime();
        PeriodFormatter formatterWithDayTime = ISOPeriodFormat.standard().withParseType(dayTimeType);

        // Act: Call withParseType with the exact same type.
        PeriodFormatter sameFormatter = formatterWithDayTime.withParseType(dayTimeType);

        // Assert: The same instance is returned.
        assertSame("Should return the same instance if the type is unchanged", formatterWithDayTime, sameFormatter);

        // Arrange: Create a formatter with a null parse type.
        PeriodFormatter formatterWithNull = ISOPeriodFormat.standard().withParseType(null);

        // Act: Call withParseType with null again.
        PeriodFormatter sameFormatterWithNull = formatterWithNull.withParseType(null);

        // Assert: The same instance is returned.
        assertSame("Should return the same instance if the null type is unchanged", formatterWithNull, sameFormatterWithNull);
    }
}
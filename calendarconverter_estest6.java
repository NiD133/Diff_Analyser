package org.joda.time.convert;

import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Unit tests for {@link CalendarConverter}.
 */
public class CalendarConverterTest {

    private final CalendarConverter converter = CalendarConverter.INSTANCE;

    /**
     * Tests that getChronology() throws a NullPointerException if the object to be converted is null.
     * The Javadoc for the method explicitly states this behavior.
     */
    @Test(expected = NullPointerException.class)
    public void getChronology_whenObjectIsNull_throwsNullPointerException() {
        // Arrange: The object to convert is null.
        // The second argument (DateTimeZone) is also null, but the check for the
        // first argument's nullity should take precedence.
        Object nullCalendar = null;

        // Act & Assert: Call the method and expect a NullPointerException.
        // The cast to DateTimeZone is needed to resolve ambiguity between the two
        // overloaded getChronology methods.
        converter.getChronology(nullCalendar, (DateTimeZone) null);
    }
}
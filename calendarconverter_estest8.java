package org.joda.time.convert;

import org.joda.time.Chronology;
import org.junit.Test;

/**
 * This test suite focuses on the behavior of the CalendarConverter class,
 * specifically its handling of invalid input types.
 */
public class CalendarConverter_ESTestTest8 extends CalendarConverter_ESTest_scaffolding {

    /**
     * Verifies that getChronology() throws a ClassCastException when passed an object
     * that is not an instance of java.util.Calendar. The method contract requires
     * a Calendar object, and this test ensures it fails correctly for other types.
     */
    @Test(expected = ClassCastException.class)
    public void getChronology_whenObjectIsNotCalendar_shouldThrowClassCastException() {
        // Arrange: Create a converter instance and an object of an invalid type.
        // The method under test expects a java.util.Calendar.
        CalendarConverter converter = new CalendarConverter();
        Object invalidInput = new Object();

        // Act & Assert: Call getChronology with the invalid object.
        // The @Test(expected) annotation asserts that a ClassCastException is thrown.
        converter.getChronology(invalidInput, (Chronology) null);
    }
}
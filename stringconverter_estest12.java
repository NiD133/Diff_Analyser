package org.joda.time.convert;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StringConverter_ESTestTest12 extends StringConverter_ESTest_scaffolding {

    /**
     * Tests that getPartialValues() throws an IllegalArgumentException
     * when the input string cannot be parsed by the provided formatter.
     */
    @Test
    public void getPartialValues_whenStringIsInvalidFormat_throwsIllegalArgumentException() {
        // Arrange
        StringConverter converter = new StringConverter();
        String invalidDateString = ";Xb=I|6d!0*'jzM0/";

        // A Partial is needed to define the structure of the expected date-time fields.
        // The formatter derived from this Partial will expect only a year.
        Partial yearOnlyPartial = new Partial(DateTimeFieldType.year(), 2023);
        DateTimeFormatter formatter = yearOnlyPartial.getFormatter();

        // Act & Assert
        try {
            converter.getPartialValues(yearOnlyPartial, invalidDateString, null, formatter);
            fail("Expected an IllegalArgumentException to be thrown for an invalid format.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message indicates an invalid format.
            String expectedMessage = "Invalid format: \"" + invalidDateString + "\"";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
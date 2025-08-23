package org.apache.commons.cli;

import org.junit.Test;
import java.util.Date;

/**
 * Tests for {@link TypeHandler#createValue(String, Class)}.
 */
public class TypeHandler_ESTestTest13 extends TypeHandler_ESTest_scaffolding {

    /**
     * Verifies that attempting to create a Date from an empty string
     * correctly throws a ParseException, as an empty string is not a valid date.
     */
    @Test(expected = ParseException.class)
    public void createValue_withEmptyStringForDateType_shouldThrowParseException() throws ParseException {
        // Act: Attempt to create a Date from an invalid (empty) string.
        // The @Test(expected) annotation handles the assertion.
        TypeHandler.createValue("", Date.class);
    }
}
package org.joda.time.format;

import org.joda.time.PeriodType;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link PeriodFormatter}.
 */
public class PeriodFormatterTest {

    /**
     * Tests that getParseType() returns null when the formatter is created
     * using the basic constructor, which does not set a default parse type.
     */
    @Test
    public void getParseType_shouldReturnNull_whenCreatedWithBasicConstructor() {
        // Arrange: Create a formatter with a null printer and parser.
        // This constructor sets the parse type to null by default.
        PeriodFormatter formatter = new PeriodFormatter(null, null);

        // Act: Get the parse type from the formatter.
        PeriodType parseType = formatter.getParseType();

        // Assert: The parse type should be null as it was not explicitly configured.
        assertNull("The default parse type should be null for the basic constructor", parseType);
    }
}
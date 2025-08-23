package org.joda.time.format;

import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link PeriodFormatter}.
 */
public class PeriodFormatterTest {

    /**
     * Tests that getParser() returns null if the formatter was created
     * with a null parser, indicating it's incapable of parsing.
     */
    @Test
    public void getParser_shouldReturnNull_whenFormatterIsConstructedWithoutAParser() {
        // Arrange: Create a formatter with a dummy printer and a null parser.
        // A PeriodFormatterBuilder.Composite can serve as a valid, non-null printer instance.
        PeriodPrinter dummyPrinter = new PeriodFormatterBuilder.Composite(Collections.emptyList());
        PeriodFormatter formatter = new PeriodFormatter(dummyPrinter, null);

        // Act: Retrieve the parser from the formatter.
        PeriodParser parser = formatter.getParser();

        // Assert: The parser should be null, as it was not provided during construction.
        assertNull("Expected getParser() to return null for a formatter incapable of parsing.", parser);
    }
}
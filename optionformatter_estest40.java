package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    /**
     * Verifies that getSince() throws a NullPointerException if the formatter
     * was created with a null Option. This is expected because any attempt to
     * access properties of the null underlying Option will fail.
     */
    @Test(expected = NullPointerException.class)
    public void getSinceShouldThrowNullPointerExceptionWhenCreatedWithNullOption() {
        // Arrange: Create a formatter with a null Option.
        // The cast to (Option) is necessary to resolve the correct method overload.
        OptionFormatter formatter = OptionFormatter.from((Option) null);

        // Act: Calling getSince() should trigger the expected exception.
        formatter.getSince();
    }
}
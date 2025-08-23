package org.jfree.chart.title;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that the constructor throws an {@link IllegalArgumentException}
     * when the provided text is null. The class contract explicitly forbids
     * a null text argument.
     */
    @Test
    public void constructor_withNullText_shouldThrowIllegalArgumentException() {
        // Action & Assertion: Attempt to create a title with null text and
        // verify that the correct exception is thrown.
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> new ShortTextTitle(null)
        );

        // Assertion: Verify the exception message is as expected, ensuring
        // the correct validation is triggered.
        assertEquals("Null 'text' argument.", thrown.getMessage());
    }
}
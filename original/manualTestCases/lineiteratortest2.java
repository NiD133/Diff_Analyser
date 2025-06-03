package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link LineIterator} class.
 *
 * This test focuses on the constructor of the LineIterator class.
 */
public class LineIteratorConstructorTest {

    /**
     * Tests that the LineIterator constructor throws a NullPointerException when given a null Reader.
     *
     * This is important because LineIterator relies on a valid Reader to function correctly.
     * Passing a null Reader would lead to unexpected behavior and potential errors later on.
     */
    @Test
    public void testConstructorThrowsNullPointerExceptionWhenReaderIsNull() {
        // Verify that providing a null Reader to the LineIterator constructor results in a NullPointerException.
        assertThrows(NullPointerException.class, () -> new LineIterator(null),
                "Expected NullPointerException when constructing LineIterator with a null Reader.");
    }
}
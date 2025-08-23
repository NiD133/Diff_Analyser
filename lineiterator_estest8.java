package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.Reader;
import org.junit.Test;

/**
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    @Test
    public void constructorShouldThrowNullPointerExceptionForNullReader() {
        // The LineIterator constructor is documented to throw a NullPointerException
        // if the provided Reader is null. This test verifies that behavior.
        final NullPointerException thrown = assertThrows(
            NullPointerException.class,
            () -> new LineIterator((Reader) null)
        );

        // Additionally, we verify the exception message to ensure it originates
        // from the expected validation check.
        assertEquals("reader", thrown.getMessage());
    }
}
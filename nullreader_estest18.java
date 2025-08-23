package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Contains tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Verifies that calling reset() on a NullReader configured without
     * mark/reset support throws an UnsupportedOperationException.
     */
    @Test
    public void resetShouldThrowExceptionWhenMarkNotSupported() {
        // Arrange: Create a NullReader that is explicitly configured to not support mark/reset.
        // The size and EOF behavior are not relevant for this test.
        final boolean markSupported = false;
        final NullReader reader = new NullReader(10L, markSupported, false);

        // Act & Assert: Verify that calling reset() throws the expected exception.
        final UnsupportedOperationException thrown = assertThrows(
            UnsupportedOperationException.class,
            reader::reset
        );

        // Assert on the exception message for a more precise test.
        assertEquals("mark/reset not supported", thrown.getMessage());
    }
}
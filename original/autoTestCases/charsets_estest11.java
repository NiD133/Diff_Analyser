package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;

// The EvoSuite annotations are removed as they are specific to its execution environment
// and don't contribute to understanding the core logic being tested.

public class CharsetsTest { // Renamed class for clarity

    @Test
    public void testToCharset_NullCharset_ReturnsNull() {
        // Arrange:  Setting up the input
        Charset charset = null;
        Charset defaultCharset = null;

        // Act: Performing the action being tested (converting to Charset).
        Charset result = Charsets.toCharset(charset, defaultCharset);

        // Assert: Verifying the expected outcome.  If both input charsets are null, the result should be null.
        assertNull("Should return null if the input charset is null.", result);
    }
}
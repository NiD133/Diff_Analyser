package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test case to verify the functionality of the {@link Charsets} utility class,
 * specifically its US-ASCII charset constant.
 */
public class CharsetsUsAsciiTest { // Renamed the class for better clarity

    @Test
    public void testUsAsciiConstant() {
        // Arrange:  We are using predefined constants.  No explicit setup needed.

        // Act: Retrieve the name of the US-ASCII charset from both classes.
        String expectedCharsetName = StandardCharsets.US_ASCII.name();
        String actualCharsetName = Charsets.US_ASCII.name();

        // Assert: Verify that the names of the charsets are equal.
        assertEquals(expectedCharsetName, actualCharsetName,
                "The Charsets.US_ASCII constant should match the StandardCharsets.US_ASCII constant.");
    }
}
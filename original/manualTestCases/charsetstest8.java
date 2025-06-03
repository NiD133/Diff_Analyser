package org.apache.commons.io;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test case verifies the correctness of the {@link Charsets} class
 * in the Apache Commons IO library, specifically focusing on the UTF-16LE character set.
 */
public class CharsetsTest {  // Renamed class for better clarity

    /**
     * Tests that the {@link Charsets#UTF_16LE} constant is equivalent to
     * {@link StandardCharsets#UTF_16LE} from the standard Java library.
     * This ensures that Commons IO provides the same UTF-16LE charset as the standard library.
     */
    @Test
    public void testUtf16LeEquivalence() {
        // Act: Get the names of the UTF-16LE charsets from both Commons IO and Standard Java
        String commonsIoUtf16LeName = Charsets.UTF_16LE.name();
        String standardJavaUtf16LeName = StandardCharsets.UTF_16LE.name();

        // Assert: Verify that the names are equal, confirming the charsets are the same.
        assertEquals(standardJavaUtf16LeName, commonsIoUtf16LeName,
                "The UTF-16LE charset name from Commons IO should be the same as the standard Java charset name.");
    }
}
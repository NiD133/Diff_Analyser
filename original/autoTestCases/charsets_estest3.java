package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;

// Improved understandability:
// 1. Renamed class to describe the test's purpose.
// 2. Replaced cryptic "uS_ASCII0" with a more descriptive name.
// 3. Removed unnecessary imports related to exception handling as this test doesn't throw them.
// 4. Removed EvoSuite specific annotations and classes as they are not needed for simple test.
// 5. Used JUnit 5 annotations (e.g., @Test)
// 6. Added comments to explain the test's objective.
// 7. Simplified the assertion to directly compare the expected and actual charset names.

public class Charsets_ToCharset_SameCharsetTest {

    @Test
    public void testToCharset_SameCharset_ReturnsSameCharset() {
        // Arrange: Get the US-ASCII charset.
        Charset asciiCharset = Charsets.US_ASCII;

        // Act:  Call the toCharset method with the same charset as input.
        Charset resultCharset = Charsets.toCharset(asciiCharset, asciiCharset);

        // Assert: Verify that the returned charset is indeed US-ASCII.  We compare names instead of object equality
        // because the implementation might return a different instance but representing the same charset.
        assertEquals("US-ASCII", resultCharset.name());
    }
}
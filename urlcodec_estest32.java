package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import java.util.BitSet;

/**
 * Contains tests for the static methods of the {@link URLCodec} class.
 */
public class URLCodecStaticMethodsTest {

    /**
     * Tests that the static {@code encodeUrl} method returns null when the
     * input byte array is null. This is the expected behavior for handling
     * null inputs in many utility methods.
     */
    @Test
    public void encodeUrlWithNullByteArrayShouldReturnNull() {
        // Arrange: Define the set of "safe" characters for URL encoding.
        // The byte array to be encoded is explicitly set to null to test this edge case.
        final BitSet safeCharacters = URLCodec.WWW_FORM_URL;
        final byte[] inputBytes = null;

        // Act: Call the method under test with the null input.
        final byte[] encodedBytes = URLCodec.encodeUrl(safeCharacters, inputBytes);

        // Assert: Verify that the output is null, as expected.
        assertNull("Encoding a null byte array should result in a null array.", encodedBytes);
    }
}
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A focused test suite for the {@link Entities.CoreCharset} inner enum.
 */
public class EntitiesCoreCharsetTest {

    /**
     * Verifies that the byName() method correctly returns the 'fallback'
     * constant when provided with an unrecognized or invalid charset name.
     */
    @Test
    public void byNameReturnsFallbackForUnrecognizedCharsetName() {
        // Arrange: Define an input string that does not match any known core charset.
        // The original test used this specific, randomly-generated string.
        final String unrecognizedCharset = "\"PvE5H.,d+SC ,Q,}'xM";

        // Act: Call the method under test.
        Entities.CoreCharset result = Entities.CoreCharset.byName(unrecognizedCharset);

        // Assert: The method should return the 'fallback' enum constant, not throw an exception.
        assertEquals(Entities.CoreCharset.fallback, result);
    }
}
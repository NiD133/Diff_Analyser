package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Soundex} class.
 */
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Verifies that a {@link Soundex} instance created with the constructor
     * that accepts a mapping string correctly encodes a given name.
     */
    @Test
    @DisplayName("A Soundex instance created with a mapping string should encode correctly")
    void testInstanceCreatedWithMappingString() {
        // This test case confirms that the Soundex(String) constructor works as expected
        // by using the standard US English mapping. The original test name was "testNewInstance3".

        // Given: A Soundex encoder initialized with the standard US English mapping string.
        final Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING_STRING);
        final String nameToEncode = "Williams";
        final String expectedCode = "W452";

        // When: The name is encoded.
        final String actualCode = soundex.soundex(nameToEncode);

        // Then: The resulting code should match the expected value.
        assertEquals(expectedCode, actualCode,
            "The encoder should produce the correct Soundex code for 'Williams'.");
    }
}
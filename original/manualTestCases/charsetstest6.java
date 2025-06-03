package org.apache.commons.io;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test case verifies the equivalence between the Charsets defined in
 * `java.nio.charset.StandardCharsets` and the constants provided by
 * `org.apache.commons.io.Charsets`.  Specifically, it checks that the
 * `UTF_16` charset is consistent between the two libraries.
 */
public class CharsetsTest {

    @Test
    public void testUtf16_isEquivalent() {
        // Given: Nothing needs to be prepared. We are comparing constants.

        // When: We compare the names of the UTF-16 charsets from both libraries.
        String standardCharsetsName = StandardCharsets.UTF_16.name();
        String commonsIoCharsetsName = Charsets.UTF_16.name();

        // Then: The charset names should be equal, indicating they represent the same character encoding.
        assertEquals(standardCharsetsName, commonsIoCharsetsName,
                "The UTF-16 charset name from StandardCharsets should match the one from Charsets.");
    }
}
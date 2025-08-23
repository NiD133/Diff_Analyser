package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Soundex} class, focusing on its core encoding functionality.
 */
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * This test case serves as a basic sanity check for the default Soundex encoding.
     * It was added in relation to JIRA issues CODEC-54 and CODEC-56, which addressed
     * thread-safety and mutability problems in earlier versions of the class.
     *
     * @see <a href="https://issues.apache.org/jira/browse/CODEC-54">CODEC-54 - Soundex.soundex(String) is not thread-safe</a>
     * @see <a href="https://issues.apache.org/jira/browse/CODEC-56">CODEC-56 - Soundex has a mutable maxLength field</a>
     */
    @Test
    @DisplayName("Should encode 'Williams' to 'W452' using the default US English mapping")
    void shouldEncodeWilliamsToW452() {
        // Arrange
        final Soundex soundex = new Soundex();
        final String nameToEncode = "Williams";
        final String expectedEncoding = "W452";

        // Act
        final String actualEncoding = soundex.soundex(nameToEncode);

        // Assert
        assertEquals(expectedEncoding, actualEncoding);
    }
}
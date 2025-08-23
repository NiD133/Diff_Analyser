package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Metaphone} class, focusing on specific encoding rules.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone algorithm correctly handles a word with 'X'
     * at both the beginning and the end.
     *
     * <p>According to the Metaphone rules implemented in this class:</p>
     * <ul>
     *   <li>Initial 'X' is encoded as 'S'.</li>
     *   <li>'B' is silent when it follows 'M' at the end of a word.</li>
     *   <li>A non-initial 'X' is encoded as 'KS'.</li>
     * </ul>
     * <p>Therefore, "XMBX" should be encoded as "SMKS".</p>
     */
    @Test
    public void metaphone_shouldCorrectlyEncodeWordWithInitialAndFinalX() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        String input = "XMBX";
        String expectedEncoding = "SMKS";

        // Act
        String actualEncoding = metaphone.metaphone(input);

        // Assert
        assertEquals("The encoding for 'XMBX' was incorrect.", expectedEncoding, actualEncoding);
    }
}
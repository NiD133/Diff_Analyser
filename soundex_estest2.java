package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Soundex} class.
 */
public class SoundexTest {

    /**
     * Tests the behavior of the deprecated setMaxLength/getMaxLength methods.
     * The test confirms that the property can be set to a negative value,
     * which reflects the current behavior of this deprecated feature.
     */
    @Test
    @SuppressWarnings("deprecation") // Intentionally testing deprecated methods
    public void shouldSetAndGetNegativeValueForDeprecatedMaxLength() {
        // Arrange
        final Soundex soundex = new Soundex();
        final int negativeLength = -2841;

        // Act
        soundex.setMaxLength(negativeLength);
        final int actualLength = soundex.getMaxLength();

        // Assert
        assertEquals("The retrieved max length should match the set value.", negativeLength, actualLength);
    }
}
package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Unit tests for {@link org.apache.commons.lang3.text.translate.LookupTranslator}.
 */
public class LookupTranslatorTest {

    @Test
    public void shouldTranslateKnownSequence() {
        // Arrange
        final CharSequence[][] lookup = {
            {"alpha", "α"},
            {"beta", "β"}
        };
        final LookupTranslator translator = new LookupTranslator(lookup);
        final String input = "alpha beta";

        // Act
        final String result = translator.translate(input);

        // Assert
        assertEquals("The translated string is incorrect.", "α β", result);
    }

    @Test
    public void shouldNotTranslateUnknownSequence() {
        // Arrange
        final CharSequence[][] lookup = {{"one", "1"}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        final String input = "two three four";

        // Act
        final String result = translator.translate(input);

        // Assert
        assertEquals("Should not change the input string if no keys match.", input, result);
    }

    @Test
    public void shouldPreferLongestMatch() {
        // Arrange
        final CharSequence[][] lookup = {
            {"a", "X"},
            {"ab", "Y"}
        };
        final LookupTranslator translator = new LookupTranslator(lookup);
        final String input = "abc";

        // Act
        final String result = translator.translate(input);

        // Assert
        assertEquals("Should have matched 'ab', the longest key.", "Yc", result);
    }

    @Test
    public void shouldTranslateFirstMatchUsingWriter() throws IOException {
        // Arrange
        final CharSequence[][] lookup = {{"cat", "animal"}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        final StringWriter writer = new StringWriter();
        // The translate(input, index, writer) method only attempts one translation at the given index.
        final String input = "a cat sat on the mat";

        // Act
        final int charsConsumed = translator.translate(input, 2, writer);

        // Assert
        assertEquals("The translated value is incorrect.", "animal", writer.toString());
        assertEquals("The number of characters consumed should be the length of the key.", 3, charsConsumed);
    }

    @Test
    public void shouldHandleNullLookupTableGracefully() {
        // Arrange
        final LookupTranslator translator = new LookupTranslator((CharSequence[][]) null);
        final String input = "test string";

        // Act
        final String result = translator.translate(input);

        // Assert
        assertEquals("Translator with a null lookup table should not change the input.", input, result);
    }

    @Test
    public void constructorShouldThrowExceptionForNullValueInLookup() {
        // Arrange
        final CharSequence[][] lookupWithNullValue = {{"key", null}};

        // Act & Assert
        try {
            new LookupTranslator(lookupWithNullValue);
            fail("Expected a NullPointerException for a null value in the lookup table.");
        } catch (final NullPointerException e) {
            // This is the expected behavior.
        }
    }

    @Test
    public void constructorShouldThrowExceptionForTooShortLookupArray() {
        // Arrange
        // Each lookup pair must have a key and a value.
        final CharSequence[][] lookupWithShortArray = {{"key"}};

        // Act & Assert
        try {
            new LookupTranslator(lookupWithShortArray);
            fail("Expected an ArrayIndexOutOfBoundsException for a lookup array with less than 2 elements.");
        } catch (final ArrayIndexOutOfBoundsException e) {
            // This is the expected behavior.
        }
    }

    @Test
    public void constructorShouldThrowExceptionForEmptyKey() {
        // Arrange
        final CharSequence[][] lookupWithEmptyKey = {{"", "value"}};

        // Act & Assert
        try {
            new LookupTranslator(lookupWithEmptyKey);
            fail("Expected an IndexOutOfBoundsException for an empty key.");
        } catch (final IndexOutOfBoundsException e) {
            // Expected, as the constructor tries to access the first character for its prefix set.
        }
    }

    @Test
    public void translateShouldThrowExceptionForInvalidIndex() {
        // Arrange
        final LookupTranslator translator = new LookupTranslator(new CharSequence[0][0]); // An empty translator
        final StringWriter writer = new StringWriter();

        // Act & Assert
        try {
            translator.translate("abc", 10, writer);
            fail("Expected a StringIndexOutOfBoundsException for an out-of-bounds index.");
        } catch (final StringIndexOutOfBoundsException e) {
            // This is the expected behavior.
        } catch (final IOException e) {
            fail("Caught an unexpected IOException: " + e.getMessage());
        }
    }

    @Test
    public void translateShouldThrowExceptionForNullInput() {
        // Arrange
        final LookupTranslator translator = new LookupTranslator(new CharSequence[0][0]); // An empty translator
        final StringWriter writer = new StringWriter();

        // Act & Assert
        try {
            translator.translate(null, 0, writer);
            fail("Expected a NullPointerException for a null input CharSequence.");
        } catch (final NullPointerException e) {
            // This is the expected behavior.
        } catch (final IOException e) {
            fail("Caught an unexpected IOException: " + e.getMessage());
        }
    }
}